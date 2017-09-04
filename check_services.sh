red=$'\e[1;31m'
grn=$'\e[1;32m'
yel=$'\e[1;33m'
blu=$'\e[1;34m'
mag=$'\e[1;35m'
cyn=$'\e[1;36m'
end=$'\e[0m'

echo "${blu}*** EVENT STORE SRV => 192.168.99.100:8090/health${end}"
event_store_srv=$(curl 192.168.99.100:8090/health)
if [ "$event_store_srv" == "{\"status\":\"UP\"}" ]; then
  echo "${grn}$event_store_srv${end}"
else
  echo "${red}$event_store_srv${end}"
fi
echo

echo "${blu}*** PROPERTY MGMT SRV => 192.168.99.100:8091/health${end}"
property_mgmt_srv=$(curl 192.168.99.100:8091/health)
if [ "$property_mgmt_srv" == "{\"status\":\"UP\"}" ]; then
  echo "${grn}$property_mgmt_srv${end}"
else
  echo "${red}$property_mgmt_srv${end}"
fi
echo

echo "${blu}*** SCORING MGMT SRV => 192.168.99.100:8092/health${end}"
scoring_mgmt_srv=$(curl 192.168.99.100:8092/health)
if [ "$scoring_mgmt_srv" == "{\"status\":\"UP\"}" ]; then
  echo "${grn}$scoring_mgmt_srv${end}"
else
  echo "${red}$scoring_mgmt_srv${end}"
fi
echo

echo "${blu}*** ALERT MGMT SRV => 192.168.99.100:8093/health${end}"
scoring_mgmt_srv=$(curl 192.168.99.100:8093/health)
if [ "$alert_mgmt_srv" == "{\"status\":\"UP\"}" ]; then
  echo "${grn}$alert_mgmt_srv${end}"
else
  echo "${red}$alert_mgmt_srv${end}"
fi
echo

echo "${blu}*** PROPERTY MGMT SRV - NGINX => localhost:8888/property/health${end}"
property_mgmt_srv_nginx=$(curl localhost:8888/property/health)
if [ "$property_mgmt_srv_nginx" == "{\"status\":\"UP\"}" ]; then
  echo "${grn}$property_mgmt_srv_nginx${end}"
else
  echo "${red}$property_mgmt_srv_nginx${end}"
fi
echo

echo "${blu}*** SCORING MGMT SRV - NGINX => localhost:8888/scoring/health${end}"
scoring_mgmt_srv_nginx=$(curl localhost:8888/scoring/health)
if [ "$scoring_mgmt_srv_nginx" == "{\"status\":\"UP\"}" ]; then
  echo "${grn}$scoring_mgmt_srv_nginx${end}"
else
  echo "${red}$scoring_mgmt_srv_nginx${end}"
fi

echo "${blu}*** ALERT MGMT SRV - NGINX => localhost:8888/alert/health${end}"
alert_mgmt_srv_nginx=$(curl localhost:8888/alert/health)
if [ "$alert_mgmt_srv_nginx" == "{\"status\":\"UP\"}" ]; then
  echo "${grn}$alert_mgmt_srv_nginx${end}"
else
  echo "${red}$alert_mgmt_srv_nginx${end}"
fi
