package org.naukma.zlagoda.sale;

import org.naukma.zlagoda.abstraction.repository.BaseRepository;
import org.naukma.zlagoda.check.CheckRepository;
import org.naukma.zlagoda.exception.NoSuchEntityException;
import org.naukma.zlagoda.storeproduct.StoreProductRepository;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class SaleRepository extends BaseRepository<SaleEntity, SaleId> {
    private final CheckRepository checkRepository;
    private final StoreProductRepository storeProductRepository;

    public SaleRepository(CheckRepository checkRepository, StoreProductRepository storeProductRepository) {
        super("sale",
                "INSERT INTO sale (upc, check_number, product_number, selling_price)" +
                "VALUES (?,?,?,?)",
                "UPDATE sale SET product_number=?, selling_price=?" +
                        "WHERE upc=? AND check_number=?",
                "DELETE FROM sale WHERE upc=? AND check_number=?",
                "SELECT * FROM sale WHERE upc=? AND check_number=?",
                null);
        this.checkRepository = checkRepository;
        this.storeProductRepository = storeProductRepository;
    }

    public List<SaleEntity> findAllByCheckNumber(Integer checkNumber) {
        List<SaleEntity> result = new ArrayList<>();
        String query = "SELECT * FROM sale WHERE check_number=?";
        try(PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, checkNumber);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next())
                result.add(parseSetToEntity(resultSet));
            return result;
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public SaleId save(SaleEntity entity) {
        try(PreparedStatement createStatement = connection.prepareStatement(createQuery)) {
            setMainFields(createStatement, entity);
            createStatement.executeUpdate();
            return entity.getId();
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Boolean update(SaleEntity entity) {
        try(PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
            updateStatement.setInt(1, entity.getProductNumber());
            updateStatement.setBigDecimal(2, entity.getSellingPrice());
            setIdToUpdateStatement(updateStatement, entity.getId());
            return updateStatement.executeUpdate() > 0;
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void setMainFields(PreparedStatement statement, SaleEntity entity) throws SQLException {
        statement.setInt(1, entity.getId().getStoreProduct().getId());
        statement.setInt(2, entity.getId().getCheckEntity().getId());
        statement.setInt(3, entity.getProductNumber());
        statement.setBigDecimal(4, entity.getSellingPrice());
    }

    @Override
    protected SaleEntity parseSetToEntity(ResultSet set) throws SQLException {
        Integer upc = set.getInt("upc");
        Integer checkNumber = set.getInt("check_number");
        SaleId id = SaleId.builder()
                .checkEntity(checkRepository.findById(checkNumber)
                        .orElseThrow(
                                () -> new NoSuchEntityException("Can't find check by id: " + checkNumber)
                        )
                )
                .storeProduct(storeProductRepository.findById(upc)
                        .orElseThrow(
                                () -> new NoSuchEntityException("Can`t find store product by id: " + upc)
                        )
                )
                .build();
        return SaleEntity.builder()
                .id(id)
                .productNumber(set.getInt("product_number"))
                .sellingPrice(set.getBigDecimal("selling_price"))
                .build();
    }

    @Override
    protected void setIdToEntity(SaleEntity entity, ResultSet set){
    }

    @Override
    protected void setIdToFindDeleteStatement(PreparedStatement statement, SaleId id) throws SQLException {
        statement.setInt(1, id.getStoreProduct().getId());
        statement.setInt(2, id.getCheckEntity().getId());
    }

    @Override
    protected void setIdToUpdateStatement(PreparedStatement statement, SaleId id) throws SQLException {
        statement.setInt(3, id.getStoreProduct().getId());
        statement.setInt(4, id.getCheckEntity().getId());
    }
}
