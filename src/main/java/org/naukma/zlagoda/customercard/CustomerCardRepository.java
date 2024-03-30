package org.naukma.zlagoda.customercard;

import org.naukma.zlagoda.abstraction.repository.BaseRepository;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class CustomerCardRepository extends BaseRepository<CustomerCardEntity, Integer> {
    public CustomerCardRepository() {
        super("customer_card",
                "INSERT INTO customer_card (cust_surname, cust_name, cust_patronymic, " +
                "phone_number, city, street, zip_code, percent) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
                "UPDATE customer_card SET cust_surname=?, cust_name=?, cust_patronymic=?, " +
                        "phone_number=?, city=?, street=?, zip_code=?, percent=? WHERE card_number=?",
                "DELETE FROM customer_card WHERE card_number=?",
                "SELECT * FROM customer_card WHERE card_number=?",
                "cust_surname");
    }

    @Override
    protected void setMainFields(PreparedStatement statement, CustomerCardEntity entity) throws SQLException {
        statement.setString(1, entity.getSurname());
        statement.setString(2, entity.getName());
        statement.setString(3, entity.getPatronymic());
        statement.setString(4, entity.getPhoneNumber());
        statement.setString(5, entity.getCity());
        statement.setString(6, entity.getStreet());
        statement.setString(7, entity.getZipCode());
        statement.setInt(8, entity.getPercent());
    }

    @Override
    protected CustomerCardEntity parseSetToEntity(ResultSet set) throws SQLException {
        return CustomerCardEntity.builder()
                .id(set.getInt("card_number"))
                .surname(set.getString("cust_surname"))
                .name(set.getString("cust_name"))
                .patronymic(set.getString("cust_patronymic"))
                .phoneNumber(set.getString("phone_number"))
                .city(set.getString("city"))
                .street(set.getString("street"))
                .zipCode(set.getString("zip_code"))
                .percent(set.getInt("percent"))
                .build();
    }

    @Override
    protected void setIdToEntity(CustomerCardEntity entity, ResultSet set) throws SQLException {
        entity.setId(set.getInt(1));
    }

    @Override
    protected void setIdToFindDeleteStatement(PreparedStatement statement, Integer id) throws SQLException {
        statement.setInt(1, id);
    }

    @Override
    protected void setIdToUpdateStatement(PreparedStatement statement, Integer id) throws SQLException {
        statement.setInt(9, id);
    }
}
