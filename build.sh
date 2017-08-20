#!/bin/bash
cd event-store-srv
./mvnw clean package docker:build
STATUS=$?
if [ $STATUS -eq 0 ]; then
echo "event-store-srv => docker image creation success"
else
echo "event-store-srv => docker image creation failed"
fi

cd ..
cd property-mgmt-srv
./mvnw clean package docker:build
STATUS=$?
if [ $STATUS -eq 0 ]; then
echo "property-mgmt-srv => docker image creation success"
else
echo "property-mgmt-srv => docker image creation failed"
fi

cd ..
cd scoring-mgmt-srv
./mvnw clean package docker:build
STATUS=$?
if [ $STATUS -eq 0 ]; then
echo "scoring-mgmt-srv => docker image creation success"
else
echo "scoring-mgmt-srv => docker image creation failed"
fi
