package org.naukma.zlagoda.storeproduct;

import org.naukma.zlagoda.abstraction.repository.BaseRepository;
import org.naukma.zlagoda.exception.NoSuchEntityException;
import org.naukma.zlagoda.product.ProductRepository;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class StoreProductRepository extends BaseRepository<StoreProductEntity, Integer> {
    private final ProductRepository productRepository;

    public StoreProductRepository(ProductRepository productRepository) {
        super("store_product",
                "INSERT INTO store_product (upc_prom, id_product, selling_price, products_number, " +
                        "promotional_product) VALUES (?, ?, ?, ?, ?)",
                "UPDATE store_product SET upc_prom=?, id_product=?, selling_price=?, products_number=?, " +
                        "promotional_product=? WHERE upc=?",
                "DELETE FROM store_product WHERE upc=?",
                "SELECT * FROM store_product WHERE upc=?",
                "products_number");
        this.productRepository = productRepository;
    }

    public List<StoreProductEntity> findAllPromotionalOrderByNumberName(boolean promotional) {
        String query = String.format("SELECT * FROM store_product " +
                "INNER JOIN product ON store_product.id_product=product.id_product " +
                "WHERE promotional_product=%s ORDER BY products_number, product_name", promotional ? "TRUE" : "FALSE");
        return findAllByCustomQuery(query);
    }

    public List<StoreProductEntity> findAllOrderByName() {
        return findAllByCustomQuery("SELECT * FROM store_product " +
                "INNER JOIN product ON store_product.id_product=product.id_product " +
                "ORDER BY product_name");
    }

    public List<StoreProductEntity> findAllByProductNameLike(String productName) {
        List<StoreProductEntity> entities = new ArrayList<>();
        try(PreparedStatement findAllStatement = connection.prepareStatement("SELECT * FROM store_product " +
                "INNER JOIN product ON store_product.id_product=product.id_product " +
                "WHERE LOWER(product_name) LIKE ?")) {
            findAllStatement.setString(1, "%" + productName.toLowerCase() + "%");
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

    public Optional<StoreProductEntity> findByPromotionId(Integer id) {
        try(PreparedStatement findByPromotionIdStatement = connection.prepareStatement("SELECT * FROM store_product " +
                "WHERE store_product.upc_prom = ?")) {
            findByPromotionIdStatement.setInt(1, id);
            ResultSet resultSet = findByPromotionIdStatement.executeQuery();
            StoreProductEntity result = null;
            if(resultSet.next()){
                result = parseSetToEntity(resultSet);
            }
            return Optional.ofNullable(result);
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<StoreProductEntity> findAllByUpcLike(Integer upc) {
        List<StoreProductEntity> entities = new ArrayList<>();
        try(PreparedStatement findAllStatement = connection.prepareStatement("SELECT * FROM store_product " +
                "WHERE CAST(upc AS TEXT)LIKE ?")) {
            findAllStatement.setString(1, "%" + upc + "%");
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
    public List<StoreProductEntity> findAll() {
        return findAllByCustomQuery(String.format("SELECT * FROM %s WHERE products_number != 0", tableName));
    }

    @Override
    protected void setMainFields(PreparedStatement statement, StoreProductEntity entity) throws SQLException {
        if(entity.getPromotion() != null)
            statement.setInt(1, entity.getPromotion().getId());
        else
            statement.setNull(1, Types.INTEGER);
        statement.setInt(2, entity.getProduct().getId());
        statement.setBigDecimal(3, entity.getSellingPrice());
        statement.setInt(4, entity.getProductsNumber());
        statement.setBoolean(5, entity.isPromotional());
    }

    @Override
    protected StoreProductEntity parseSetToEntity(ResultSet set) throws SQLException {
        Integer productId = set.getInt("id_product");
        Integer promotionalId = set.getInt("upc_prom");
        StoreProductEntity.StoreProductEntityBuilder builder = StoreProductEntity.builder()
                .id(set.getInt("upc"))
                .product(productRepository.findById(productId)
                        .orElseThrow(
                                () -> new NoSuchEntityException("Can`t find product by id: " + productId)
                        )
                )
                .sellingPrice(set.getBigDecimal("selling_price"))
                .productsNumber(set.getInt("products_number"))
                .promotional(set.getBoolean("promotional_product"));
        if (promotionalId != 0)
            builder.promotion(findById(promotionalId)
                            .orElseThrow(
                                    () -> new NoSuchEntityException("Can`t find store product by id: " + promotionalId)
                            )
                    );
        return builder.build();
    }

    @Override
    protected void setIdToEntity(StoreProductEntity entity, ResultSet set) throws SQLException {
        entity.setId(set.getInt(1));
    }

    @Override
    protected void setIdToFindDeleteStatement(PreparedStatement statement, Integer id) throws SQLException {
        statement.setInt(1, id);
    }

    @Override
    protected void setIdToUpdateStatement(PreparedStatement statement, Integer id) throws SQLException {
        statement.setInt(6, id);
    }
}
