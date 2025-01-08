#!/bin/bash
# wait-for-mysql.sh

set -e

host="localhost"
user="user"
password="A19a17g27n06k18!!"

until mysql -h "$host" -u "$user" -p"$password" -e "SELECT 1" &> /dev/null; do
  >&2 echo "MySQL is unavailable - sleeping"
  sleep 1
done

>&2 echo "MySQL is up - executing command"
exec "$@"