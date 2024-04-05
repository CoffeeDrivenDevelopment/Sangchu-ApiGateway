#!/bin/sh

##########
# ENV
##########

export API_GATEWAY_IMAGE_NAME="cdd/api-gateway"
export API_GATEWAY_TAG_NAME="0.0.1"

##########
# Build Api Gateway Image
##########

echo "\n🗑 Start Delete Docker Files"

if docker image inspect $API_GATEWAY_IMAGE_NAME:$API_GATEWAY_TAG_NAME &> /dev/null; then
    docker image rm -f $API_GATEWAY_IMAGE_NAME:$API_GATEWAY_TAG_NAME
fi

echo "\n🔨 Start Build Docker Image"

docker build \
-t $API_GATEWAY_IMAGE_NAME:$API_GATEWAY_TAG_NAME .

##########
# Api Gateway Container Start
##########

if [ "$(docker ps -aq -f name=$API_GATEWAY_NAME)" ]; then
    echo "🚫 Stop Docker Container : "
    docker rm -f $API_GATEWAY_NAME
else
    echo "🚫 There is no running container named $API_GATEWAY_NAME"
fi

echo "🚀 Docker $API_GATEWAY_NAME Container Start! : "
docker run -d \
--name $API_GATEWAY_NAME \
-p $API_GATEWAY_PORT:$API_GATEWAY_PORT \
-e PROFILE=$API_GATEWAY_PROFILE \
$API_GATEWAY_IMAGE_NAME:$API_GATEWAY_TAG_NAME