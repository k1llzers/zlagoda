CREATE TABLE check(
    check_number SERIAL PRIMARY KEY,
    id_employee INT NOT NULL,
    card_number INT,
    print_date TIMESTAMP NOT NULL,
    sum_total DECIMAL(13,4) NOT NULL,
    vat DECIMAL(13,4) NOT NULL,
    FOREIGN KEY (id_employee) REFERENCES employee(id_employee) ON UPDATE CASCADE ON DELETE NO ACTION,
    FOREIGN KEY (card_number) REFERENCES customer_card(card_number) ON UPDATE CASCADE ON DELETE NO ACTION
)