#!/bin/sh
set -e
VERSION=$(grep "^version" springboot-app/build.gradle | awk '{print $3}' | sed -e "s/'//g")
echo -e "Building API-Server-$VERSION"
./springboot-app/gradlew -p springboot-app build -x test
BUILD_DIR=./deployment/backend/target
rm -rf $BUILD_DIR && mkdir -p $BUILD_DIR && cd $BUILD_DIR
echo "Extracting project jar at: $PWD"
jar -xf "../../../springboot-app/build/libs/API-Server-${VERSION}.jar"
