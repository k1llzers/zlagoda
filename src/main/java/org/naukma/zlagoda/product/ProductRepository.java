package org.naukma.zlagoda.product;

import org.naukma.zlagoda.abstraction.repository.BaseRepository;
import org.naukma.zlagoda.category.CategoryRepository;
import org.naukma.zlagoda.exception.NoSuchEntityException;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ProductRepository extends BaseRepository<ProductEntity, Integer> {
    private final CategoryRepository categoryRepository;

    public ProductRepository(CategoryRepository categoryRepository) {
        super("product",
                "INSERT INTO product (category_number, product_name, characteristics) " +
                        "VALUES (?, ?, ?)",
                "UPDATE product SET category_number=?, product_name=?, characteristics=? " +
                        "WHERE id_product=?",
                "DELETE FROM product WHERE id_product=?",
                "SELECT * FROM product WHERE id_product=?",
                "product_name");
        this.categoryRepository = categoryRepository;
    }

    public List<ProductEntity> findAllProductsFromCategoryOrderByName(Integer id) {
        String query = String.format("SELECT * FROM product WHERE category_number=%s ORDER BY product_name", id);
        return findAllByCustomQuery(query);
    }

    public List<ProductEntity> findAllProductsNameLike(String name) {
        List<ProductEntity> entities = new ArrayList<>();
        try(PreparedStatement findAllStatement = connection.prepareStatement("SELECT * FROM product " +
                "WHERE LOWER(product_name) LIKE ?")) {
            findAllStatement.setString(1, "%" + name.toLowerCase() + "%");
            ResultSet resultSet = findAllStatement.executeQuery();
            while (resultSet.next()){
                entities.add(parseSetToEntity(resultSet));
            }
            return entities;
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void setMainFields(PreparedStatement statement, ProductEntity entity) throws SQLException {
        statement.setInt(1, entity.getCategory().getId());
        statement.setString(2, entity.getName());
        statement.setString(3, entity.getCharacteristics());
    }

    @Override
    protected ProductEntity parseSetToEntity(ResultSet set) throws SQLException {
        Integer categoryNumber =  set.getInt("category_number");
        return ProductEntity.builder()
                .id(set.getInt("id_product"))
                .category(categoryRepository.findById(categoryNumber)
                        .orElseThrow(
                                () -> new NoSuchEntityException("Can`t find category by id: " + categoryNumber))
                )
                .name(set.getString("product_name"))
                .characteristics(set.getString("characteristics"))
                .build();
    }

    @Override
    protected void setIdToEntity(ProductEntity entity, ResultSet set) throws SQLException {
        entity.setId(set.getInt(1));
    }

    @Override
    protected void setIdToFindDeleteStatement(PreparedStatement statement, Integer id) throws SQLException {
        statement.setInt(1, id);
    }

    @Override
    protected void setIdToUpdateStatement(PreparedStatement statement, Integer id) throws SQLException {
        statement.setInt(4, id);
    }
}
