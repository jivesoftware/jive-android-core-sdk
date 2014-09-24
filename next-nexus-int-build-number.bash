#!/bin/bash -e

if [ ! -n "$2" ]
then
  echo "Usage: `basename $0` <repoId> <artifactId>"
  exit 1
fi

REPO_ID="$1"
ARTIFACT_ID="$2"

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

pushd "$DIR" > /dev/null

# xpath spams STDERR, so I redirect the output to /dev/null. Obviously, this makes debugging a bit difficult. Just remove the /dev/nulls if this breaks.

WORKING_DIR_PATH=`mktemp -d 2>/dev/null || mktemp -d -t latest-version` # For Mac/Linux compatibility: http://unix.stackexchange.com/a/84980

cp saxon9he.jar "$WORKING_DIR_PATH"
pushd "$WORKING_DIR_PATH" > /dev/null

set +e
echo "" | sed -r -n '' > /dev/null 2>&1
if [ $? -eq 0 ]
then
SED_MODERN_REGEX_OPTION="-r"
else
SED_MODERN_REGEX_OPTION="-E"
fi
set -e

saxon () {
  if [ ! -n "$2" ]
  then
    echo "FATAL: Invalid saxon call: " $*
    exit 999
  fi

  INPUT_PATH="$1"
  XPATH="$2"

  java -cp saxon9he.jar net.sf.saxon.Query -s:"$INPUT_PATH" -qs:"declare option saxon:output 'omit-xml-declaration=yes'; $XPATH"
}

curl -s -f http://nexus-int.eng.jiveland.com/service/local/repo_groups/"$REPO_ID"/"index_content"/com/jivesoftware/android/mobile/"$ARTIFACT_ID"/ > index_content.xml
echo "<versions>" > versions.xml
saxon index_content.xml '/indexBrowserTreeViewResponse/data/children/indexBrowserTreeNode/children/indexBrowserTreeNode/version' > versions.xmlfragment
cat versions.xmlfragment >> versions.xml
echo "</versions>" >> versions.xml

VERSION_COUNT=`saxon versions.xml 'count(/versions/version/text())'`

if [ -z "$VERSION_COUNT" -o "$VERSION_COUNT" -eq "0" ]
then
	# found no versions, start with 1.
	echo 1
else
	rm -rf build_list.txt
	for i in `seq 1 $VERSION_COUNT`
	do
		VERSION=`saxon versions.xml '/versions/version['$i']/text()'`
		BUILD=`echo "$VERSION" | sed -n $SED_MODERN_REGEX_OPTION 's/(.+)-([[:digit:]]+)-(.+)/\2/p'`
		if [ -n "$BUILD" ]
		then
			echo "$BUILD" >> build_list.txt
		fi
	done

	LATEST_BUILD=`cat build_list.txt | sort -n -r | head -n 1`
	NEXT_BUILD=$(( $LATEST_BUILD + 1 ))
	echo $NEXT_BUILD
fi

popd > /dev/null
rm -rf "$WORKING_DIR_PATH"
