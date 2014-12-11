#!/bin/bash

echo 'Killing existing java processes on machine' $1
sudo killall java
sleep 2
rm -rf temp-* data-*

echo 'Starting server on machine' $1
let debugport=10000+$1
java -agentlib:jdwp=transport=dt_socket,address=$debugport,server=y,suspend=n -jar cs244b-final-project.jar server configuration$1.yml > log$1.txt&
