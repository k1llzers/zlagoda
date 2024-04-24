package org.naukma.zlagoda.category;

import org.naukma.zlagoda.abstraction.repository.BaseRepository;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CategoryRepository extends BaseRepository<CategoryEntity, Integer> {

    private static final String TOP_CATEGORY_BY_CUSTOMER_CARD_QUERY = "SELECT category.category_number, category.category_name " +
            "FROM customer_card INNER JOIN customer_check ON customer_card.card_number = customer_check.card_number " +
            "INNER JOIN sale ON customer_check.check_number = sale.check_number " +
            "INNER JOIN store_product ON sale.upc = store_product.upc " +
            "INNER JOIN product ON store_product.id_product = product.id_product " +
            "INNER JOIN category ON product.category_number = category.category_number " +
            "WHERE customer_card.card_number = ? " +
            "GROUP BY customer_card.card_number, customer_card.cust_surname, customer_card.cust_name, " +
            "category.category_number, category.category_name " +
            "HAVING COUNT(*) = (SELECT MAX(sub_table.count_per_category) " +
            "FROM (SELECT COUNT(*) AS count_per_category " +
            "FROM customer_check INNER JOIN sale ON customer_check.check_number = sale.check_number " +
            "INNER JOIN store_product ON sale.upc = store_product.upc " +
            "INNER JOIN product ON store_product.id_product = product.id_product " +
            "INNER JOIN category ON product.category_number = category.category_number " +
            "WHERE customer_card.card_number = customer_check.card_number " +
            "GROUP BY category.category_number) AS sub_table)";

    public CategoryRepository() {
        super("category",
                "INSERT INTO category (category_name) VALUES (?)",
                "UPDATE category SET category_name=? WHERE category_number=?",
                "DELETE FROM category WHERE category_number=?",
                "SELECT * FROM category WHERE category_number=?",
                "category_name");
    }

    public List<CategoryEntity> findMostPopularCategoryByCustomerCard(Integer cardNumber) {
        List<CategoryEntity> entities = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(TOP_CATEGORY_BY_CUSTOMER_CARD_QUERY)) {
            statement.setInt(1, cardNumber);
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()) {
                entities.add(parseSetToEntity(resultSet));
            }
            return entities;
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void setMainFields(PreparedStatement statement, CategoryEntity entity) throws SQLException {
        statement.setString(1, entity.getName());
    }

    @Override
    protected CategoryEntity parseSetToEntity(ResultSet set) throws SQLException {
        return CategoryEntity.builder()
                .id(set.getInt("category_number"))
                .name(set.getString("category_name"))
                .build();
    }

    @Override
    protected void setIdToEntity(CategoryEntity entity, ResultSet set) throws SQLException {
        entity.setId(set.getInt(1));
    }

    @Override
    protected void setIdToFindDeleteStatement(PreparedStatement statement, Integer id) throws SQLException {
        statement.setInt(1, id);
    }

    @Override
    protected void setIdToUpdateStatement(PreparedStatement statement, Integer id) throws SQLException {
        statement.setInt(2, id);
    }
}
