CREATE TABLE store_product(
    upc SERIAL PRIMARY KEY,
    upc_prom INT,
    id_product INT NOT NULL,
    selling_price DECIMAL(13,4) NOT NULL,
    products_number INT NOT NULL,
    promotional_product BOOLEAN NOT NULL,
    FOREIGN KEY (upc_prom) REFERENCES store_product(upc) ON UPDATE CASCADE ON DELETE SET NULL,
    FOREIGN KEY (id_product) REFERENCES product(id_product) ON UPDATE CASCADE ON UPDATE NO ACTION
)