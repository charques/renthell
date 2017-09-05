#!/bin/bash
red=$'\e[1;31m'
grn=$'\e[1;32m'
yel=$'\e[1;33m'
blu=$'\e[1;34m'
mag=$'\e[1;35m'
cyn=$'\e[1;36m'
end=$'\e[0m'

cd event-store-srv
./mvnw clean package docker:build
STATUS_EVENT_STORE=$?

cd ..
cd property-mgmt-srv
./mvnw clean package docker:build
STATUS_PROPERTY_MGMT=$?

cd ..
cd scoring-mgmt-srv
./mvnw clean package docker:build
STATUS_SCORING_MGMT=$?

cd ..
cd alert-mgmt-srv
./mvnw clean package docker:build
STATUS_ALERT_MGMT=$?

cd ..
cd web-app
polymer build
STATUS_POLYMER=$?

echo
echo "[${blu}INFO${end}] ------------------------------------------------------------------------"
echo "[${blu}INFO${end}] ${mag}BUILD STATUS${end}"
echo "[${blu}INFO${end}] ------------------------------------------------------------------------${end}"
echo
if [ $STATUS_EVENT_STORE -eq 0 ]; then
echo "[${blu}INFO${end}] --- ${blu}event-store-srv${end}   => docker image creation ${grn}SUCCESS${end}"
else
echo "[${blu}INFO${end}] --- ${blu}event-store-srv${end}   => docker image creation ${red}FAILED${end}"
fi

if [ $STATUS_PROPERTY_MGMT -eq 0 ]; then
echo "[${blu}INFO${end}] --- ${blu}property-mgmt-srv${end} => docker image creation ${grn}SUCCESS${end}"
else
echo "[${blu}INFO${end}] --- ${blu}property-mgmt-srv${end} => docker image creation ${red}FAILED${end}"
fi

if [ $STATUS_SCORING_MGMT -eq 0 ]; then
echo "[${blu}INFO${end}] --- ${blu}scoring-mgmt-srv${end}  => docker image creation ${grn}SUCCESS${end}"
else
echo "[${blu}INFO${end}] --- ${blu}scoring-mgmt-srv${end}  => docker image creation ${red}FAILED${end}"
fi

if [ $STATUS_ALERT_MGMT -eq 0 ]; then
echo "[${blu}INFO${end}] --- ${blu}alert-mgmt-srv${end}    => docker image creation ${grn}SUCCESS${end}"
else
echo "[${blu}INFO${end}] --- ${blu}alert-mgmt-srv${end}    => docker image creation ${red}FAILED${end}"
fi

if [ $STATUS_POLYMER -eq 0 ]; then
echo "[${blu}INFO${end}] --- ${blu}web-app${end}           => polymer web app build ${grn}SUCCESS${end}"
else
echo "[${blu}INFO${end}] --- ${blu}web-app${end}           => polymer web app build ${red}FAILED${end}"
fi
echo
