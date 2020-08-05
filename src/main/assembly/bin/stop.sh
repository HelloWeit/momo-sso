#!/bin/bash

echo "stop common-sso .."
ps -ef | grep common-sso | grep -v 'grep' | awk '{print $2}' | xargs kill -9
echo "stop common-sso successfully."

exit 0

