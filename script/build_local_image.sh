#!/bin/sh

##########
# ENV
##########

export IMAGE_NAME='cdd/api-gateway'
export TAG_NAME='0.0.1'

cd ..

echo '\n🗑 Start Delete Docker Files'

if docker image inspect $IMAGE_NAME:$TAG_NAME &> /dev/null; then
    docker image rm -f $IMAGE_NAME:$TAG_NAME
fi

echo '\n🔨 Start Build Docker Image'

docker build \
-t $IMAGE_NAME:$TAG_NAME .