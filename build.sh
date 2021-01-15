#!/bin/bash
#title          :build.sh
#description    :This script builds the payment service
#author         :Lukas Amtoft Dahl
#==============================================================================

set -e

mvn clean package

docker build -t tokens .
docker run -d -p8080:8080 tokens

sleep 2s

mvn test