ALTER TABLE employee
ADD UNIQUE (login);

ALTER TABLE product
ADD UNIQUE (product_name);

ALTER TABLE category
ADD UNIQUE (category_name);