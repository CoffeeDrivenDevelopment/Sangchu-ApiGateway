#!/bin/sh

##########
# BootJar
##########

cd ..

echo '\n🗄️Start Create Boot Jar'

chmod 755 gradlew
./gradlew clean bootJar