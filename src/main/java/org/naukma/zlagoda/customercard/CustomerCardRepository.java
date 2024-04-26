package org.naukma.zlagoda.customercard;

import org.naukma.zlagoda.abstraction.repository.BaseRepository;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CustomerCardRepository extends BaseRepository<CustomerCardEntity, Integer> {
    private static final String CUSTOMER_WHO_BUY_ALL_PRODUCT_QUERY = "SELECT * " +
            "FROM customer_card WHERE NOT EXISTS(SELECT * " +
            "FROM store_product WHERE NOT EXISTS(" +
            "SELECT * FROM sale WHERE sale.upc = store_product.upc " +
            "AND sale.check_number IN (SELECT check_number FROM customer_check " +
            "WHERE customer_check.card_number = customer_card.card_number)))" +
            "AND EXISTS(SELECT * FROM store_product)";

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

    public List<CustomerCardEntity> findAllCustomerWithPercentOrderBySurname(Integer percent) {
        String query = String.format("SELECT * FROM customer_card WHERE percent=%s ORDER BY cust_surname", percent);
        return findAllByCustomQuery(query);
    }

    public List<CustomerCardEntity> findCustomerWhoBuyAllProducts() {
        List<CustomerCardEntity> entities = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(CUSTOMER_WHO_BUY_ALL_PRODUCT_QUERY)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                entities.add(parseSetToEntity(resultSet));
            }
            return entities;
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<CustomerCardEntity> findAllBySurname(String surname) {
        List<CustomerCardEntity> entities = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM customer_card " +
                "WHERE LOWER(cust_surname) LIKE ?")) {
            statement.setString(1, "%" + surname.toLowerCase() + "%");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                entities.add(parseSetToEntity(resultSet));
            }
            return entities;
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
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
