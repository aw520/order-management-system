INSERT INTO products (
    product_id, 
    product_name,
    product_price,
    product_description,
    image_url,
    quantity
)
SELECT UUID_TO_BIN(UUID()),
    'MacBook Pro 14"',
    2499.99,
    'Apple MacBook Pro 14-inch with M3 chip',
    'https://example.com/images/macbook-pro-14.jpg',
    10
    WHERE NOT EXISTS (
    SELECT 1 FROM products WHERE product_name = 'MacBook Pro 14"'
);

INSERT INTO products (
    product_id,
    product_name,
    product_price,
    product_description,
    image_url,
    quantity
)
SELECT UUID_TO_BIN(UUID()),
    'iPhone 15 Pro',
    999.99,
    'Apple iPhone 15 Pro with A17 chip',
    'https://example.com/images/iphone-15-pro.jpg',
    25
    WHERE NOT EXISTS (
    SELECT 1 FROM products WHERE product_name = 'iPhone 15 Pro'
);

INSERT INTO products (
    product_id,
    product_name,
    product_price,
    product_description,
    image_url,
    quantity
)
SELECT UUID_TO_BIN(UUID()),
    'AirPods Pro',
    249.99,
    'Apple AirPods Pro (2nd generation)',
    'https://example.com/images/airpods-pro.jpg',
    40
    WHERE NOT EXISTS (
    SELECT 1 FROM products WHERE product_name = 'AirPods Pro'
);

INSERT INTO products (
    product_id,
    product_name,
    product_price,
    product_description,
    image_url,
    quantity
)
SELECT UUID_TO_BIN(UUID()),
    'Apple Watch Series 9',
    399.99,
    'Apple Watch Series 9 with health tracking',
    'https://example.com/images/apple-watch-9.jpg',
    15
    WHERE NOT EXISTS (
    SELECT 1 FROM products WHERE product_name = 'Apple Watch Series 9'
);

INSERT INTO products (
    product_id,
    product_name,
    product_price,
    product_description,
    image_url,
    quantity
)
SELECT UUID_TO_BIN(UUID()),
    'iPad Pro 11"',
    799.99,
    'Apple iPad Pro 11-inch with M2 chip',
    'https://example.com/images/ipad-pro-11.jpg',
    20
    WHERE NOT EXISTS (
    SELECT 1 FROM products WHERE product_name = 'iPad Pro 11"'
);

INSERT INTO products (product_id, product_name, product_price, product_description, image_url, quantity)
SELECT UUID_TO_BIN(UUID()),  'Dell XPS 15', 1899.99,
       'Dell XPS 15 with Intel Core i9 and OLED display',
       'https://example.com/images/dell-xps-15.jpg',
       12
    WHERE NOT EXISTS (SELECT 1 FROM products WHERE product_name = 'Dell XPS 15');

INSERT INTO products (product_id, product_name, product_price, product_description, image_url, quantity)
SELECT UUID_TO_BIN(UUID()),  'Lenovo ThinkPad X1 Carbon', 1749.99,
       'Lenovo ThinkPad X1 Carbon Gen 11 business laptop',
       'https://example.com/images/thinkpad-x1-carbon.jpg',
       15
    WHERE NOT EXISTS (SELECT 1 FROM products WHERE product_name = 'Lenovo ThinkPad X1 Carbon');

INSERT INTO products (product_id, product_name, product_price, product_description, image_url, quantity)
SELECT UUID_TO_BIN(UUID()),  'ASUS ROG Zephyrus G14', 1599.99,
       'ASUS ROG Zephyrus G14 gaming laptop with Ryzen CPU',
       'https://example.com/images/rog-g14.jpg',
       8
    WHERE NOT EXISTS (SELECT 1 FROM products WHERE product_name = 'ASUS ROG Zephyrus G14');

