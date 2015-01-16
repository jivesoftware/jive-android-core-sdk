#!/bin/bash -e

if [ -z "${WORKSPACE}" ]; then
    echo "ERROR: WORKSPACE environment variable not set.  Are you running under Jenkins?"
    exit 1
fi

export JAVA_HOME=/opt/tools/java/jdk1.6.0_32

export CODE_DIR="$(pwd)"
export PATH="${JAVA_HOME}/bin:${PATH}:${CODE_DIR}/bin/ci/lib"

# Configure our gradle environment and base options:
export GRADLE="${CODE_DIR}/gradlew --no-color --continue"
export GRADLE_OPTS="-Dorg.gradle.java.home=${JAVA_HOME}"

# Redirect temp files into our workspace for jenkins-strict compliance
export TMPDIR="${WORKSPACE}/tmp"
rm -fr "${TMPDIR}"
mkdir -p "${TMPDIR}"
