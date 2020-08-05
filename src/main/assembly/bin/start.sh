#!/bin/bash

echo "start common-sso .."
cd `dirname $0`
cd ..
base_dir=`pwd`
echo "base dir: ${base_dir}"
nohup java -jar -Dspring.config.additional-location=file:./config/ ./lib/common-sso-1.0-SNAPSHOT.jar > ./common-sso.log 2>&1 &
echo "start common-sso successfully."

exit 0