#!/bin/bash

# Configuration variables
DB_CONTAINER="citus-coordinator" # Docker container name for Citus coordinator
DB_PORT="5442"               # Citus coordinator port
DB_NAME="postgres"           # Database name
DB_USER="postgres"           # Database user

# SQL commands

SQL_COMMANDS="
CREATE TABLE orders (
    order_id BIGINT PRIMARY KEY,
    user_id BIGINT,
    total_amount DECIMAL(10,2)
);

SELECT create_distributed_table('orders', 'order_id');
"
CREATE TABLE order_items (
     item_id BIGINT PRIMARY KEY,
     order_id BIGINT NOT NULL REFERENCES orders(order_id),
     product_name VARCHAR(255),
     price DECIMAL(10,2)
 );
 #
 #SELECT create_distributed_table('order_items', 'item_id');

# Execute the SQL commands inside the Docker container
docker exec -i "$DB_CONTAINER" psql -d "$DB_NAME" -U "$DB_USER" -c "$SQL_COMMANDS"

if [ $? -eq 0 ]; then
    echo "Tables created and sharded successfully inside Docker container."
else
    echo "Failed to execute SQL commands in Docker container. Check your configuration and try again."
fi
