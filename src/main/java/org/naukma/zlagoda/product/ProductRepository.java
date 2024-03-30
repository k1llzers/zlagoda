package org.naukma.zlagoda.product;

import org.naukma.zlagoda.abstraction.repository.BaseRepository;
import org.naukma.zlagoda.category.CategoryRepository;
import org.naukma.zlagoda.exception.NoSuchEntityException;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
