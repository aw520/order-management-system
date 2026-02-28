echo "ENTRYPOINT.SH ACTUALLY RAN"
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

echo "=== DEBUG: File check ==="
ls -l /run/secrets || true

echo "=== DEBUG: First lines of public key ==="
head -n 3 /run/secrets/jwt-public.key || true

echo "=== DEBUG: Literal \\n check ==="
echo "$JWT_PUBLIC_KEY_CONTENT" | grep -o '\\n' | head -n 3 || echo "No literal \\n found"

echo "=== DEBUG END ==="

#hand over control to the Java application
exec java -jar app.jar