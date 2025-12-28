CREATE DATABASE IF NOT EXISTS user_db;
CREATE DATABASE IF NOT EXISTS product_db;
CREATE DATABASE IF NOT EXISTS order_db;

CREATE USER IF NOT EXISTS 'user_user'@'%' IDENTIFIED BY 'product_pass';
CREATE USER IF NOT EXISTS 'product_user'@'%' IDENTIFIED BY 'product_pass';
CREATE USER IF NOT EXISTS 'order_user'@'%' IDENTIFIED BY 'product_pass';

GRANT ALL PRIVILEGES ON user_db.* TO 'user_user'@'%';
GRANT ALL PRIVILEGES ON product_db.* TO 'product_user'@'%';
GRANT ALL PRIVILEGES ON order_db.* TO 'order_user'@'%';

FLUSH PRIVILEGES;
