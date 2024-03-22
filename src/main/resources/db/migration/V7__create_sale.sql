CREATE TABLE sale(
    upc INT,
    check_number INT,
    product_number INT NOT NULL,
    selling_price DECIMAL(13,4) NOT NULL,
    FOREIGN KEY (upc) REFERENCES store_product(upc) ON UPDATE CASCADE ON DELETE NO ACTION,
    FOREIGN KEY (check_number) REFERENCES customer_check(check_number) ON UPDATE CASCADE ON DELETE CASCADE,
    PRIMARY KEY (upc, check_number)
)