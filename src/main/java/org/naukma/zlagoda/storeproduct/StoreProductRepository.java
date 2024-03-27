package org.naukma.zlagoda.storeproduct;

import org.naukma.zlagoda.abstraction.repository.BaseRepository;
import org.naukma.zlagoda.exception.NoSuchEntityException;
import org.naukma.zlagoda.product.ProductRepository;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class StoreProductRepository extends BaseRepository<StoreProductEntity, Integer> {
    private final ProductRepository productRepository;

    public StoreProductRepository(ProductRepository productRepository) {
        super("INSERT INTO store_products (upc_prom, id_product, selling_price, products_number, " +
                        "promotional_product VALUES (?, ?, ?, ?, ?)",
                "UPDATE store_products SET upc_prom=?, id_product=?, selling_price=?, products_number=?, " +
                        "promotional_product=? WHERE upc=?",
                "DELETE FROM store_products WHERE upc=?",
                "SELECT * FROM store_products WHERE upc=?");
        this.productRepository = productRepository;
    }

    @Override
    protected void setMainFields(PreparedStatement statement, StoreProductEntity entity) throws SQLException {
        statement.setInt(1, entity.getPromotion().getId());
        statement.setInt(2, entity.getProduct().getId());
        statement.setBigDecimal(3, entity.getSellingPrice());
        statement.setInt(4, entity.getProductsNumber());
        statement.setBoolean(5, entity.isPromotional());
    }

    @Override
    protected StoreProductEntity parseSetToEntity(ResultSet set) throws SQLException {
        Integer productId = set.getInt("id_product");
        Integer promotionalId = set.getInt("upc_prom");
        return StoreProductEntity.builder()
                .id(set.getInt("upc"))
                .promotion(findById(promotionalId)
                        .orElseThrow(
                                () -> new NoSuchEntityException("Can`t find store product by id: " + promotionalId)
                        )
                )
                .product(productRepository.findById(productId)
                        .orElseThrow(
                                () -> new NoSuchEntityException("Can`t find product by id: " + productId)
                        )
                )
                .sellingPrice(set.getBigDecimal("selling_price"))
                .productsNumber(set.getInt("products_number"))
                .promotional(set.getBoolean("promotional_product"))
                .build();
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
