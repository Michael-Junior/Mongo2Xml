#!/bin/bash

cd /home/oliveirmic/Projetos-dev/Mongo2Xml/scala/ || exit

sbt "runMain Mongo2Xml.Mongo2Xml $1 $2 $3 $4 $5 $6 $7"
ret="$?"

cd - || exit

exit $ret
