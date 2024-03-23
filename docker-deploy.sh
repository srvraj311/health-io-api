#!/bin/bash

set -e  # Exit on errors

image_name=$1
image_tag=$2

# Update with your commands for stopping the old container (if needed)
docker stop health-io-api  # Example, replace with your container name

docker pull $image_name:$image_tag

# Update with your commands for running the new container
docker run -d -p 80:8080 $image_name:$image_tag --name health-io-api  # Example, replace with your container name and any additional options

echo "Deployed image: $image_name:$image_tag"