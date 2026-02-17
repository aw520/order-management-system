#!/bin/sh
#create the directory
mkdir -p /run/secrets

#Let AWS inject these as strings into the container
if [ ! -z "$JWT_PRIVATE_KEY_CONTENT" ]; then
  echo "$JWT_PRIVATE_KEY_CONTENT" > /run/secrets/jwt-private.key
fi

if [ ! -z "$JWT_PUBLIC_KEY_CONTENT" ]; then
  echo "$JWT_PUBLIC_KEY_CONTENT" > /run/secrets/jwt-public.key
fi

#hand over control to the Java application
exec java -jar app.jar