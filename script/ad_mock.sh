#!/bin/bash

for i in hadoop-001 hadoop-002
do
echo "========== $i =========="
        ssh $i "cd /opt/module/ad_mock ; java -jar /opt/module/ad_mock/NginxDataGenerator-1.0-SNAPSHOT-jar-with-dependencies.jar >/dev/null  2>&1 &"
done

