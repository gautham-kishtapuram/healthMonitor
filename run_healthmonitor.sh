#!/bin/bash
#mvn clean install

echo "Building Docker image"
docker build -t healthmonitor .

echo "Running Docker container"
docker run -i healthmonitor