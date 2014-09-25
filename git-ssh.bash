#!/bin/bash -e

if [ ! -f "$GIT_SSH_IDENTITY_PATH" ]
then
	echo "Expected GIT_SSH_IDENTITY_PATH environment variable to point to the full path of a file suitable for ssh's -i option."
	exit 1
fi

exec /usr/bin/ssh -o StrictHostKeyChecking=no -i "$GIT_SSH_IDENTITY_PATH" "$@"

