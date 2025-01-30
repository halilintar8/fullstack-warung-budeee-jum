#!/bin/sh
echo "Waiting for PostgreSQL to start..."

while ! nc -z db 5432; do
  sleep 1
done

echo "PostgreSQL started, launching backend..."
exec java -jar target/your-backend-app.jar

