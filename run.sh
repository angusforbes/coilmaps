#!/bin/bash

java -Djava.library.path="natives/" -Xms1024m -Xmx1024m -XX:+UseConcMarkSweepGC -jar CEB-CoilMap.jar
