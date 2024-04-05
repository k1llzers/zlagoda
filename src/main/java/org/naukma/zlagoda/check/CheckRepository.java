package org.naukma.zlagoda.check;

import org.naukma.zlagoda.abstraction.repository.BaseRepository;
import org.naukma.zlagoda.customercard.CustomerCardRepository;
import org.naukma.zlagoda.employee.EmployeeRepository;
import org.naukma.zlagoda.exception.NoSuchEntityException;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CheckRepository extends BaseRepository<CheckEntity, Integer> {

    private final EmployeeRepository employeeRepository;
    private final CustomerCardRepository customerCardRepository;

    public CheckRepository(EmployeeRepository employeeRepository, CustomerCardRepository customerCardRepository) {
        super("customer_check",
                "INSERT INTO customer_check (id_employee, card_number, print_date, sum_total, vat) " +
                        "VALUES (?, ?, ?, ?, ?)",
                "UPDATE customer_check SET id_employee=?, card_number=?, print_date=?, sum_total=?, vat=? " +
                        "WHERE check_number=?",
                "DELETE FROM customer_check WHERE check_number=?",
                "SELECT * FROM customer_check WHERE check_number=?",
                null);
        this.employeeRepository = employeeRepository;
        this.customerCardRepository = customerCardRepository;
    }

    public List<CheckEntity> findAllByCashierAndPrintDateBetween(Integer id, LocalDateTime from, LocalDateTime to) {
        List<CheckEntity> entities = new ArrayList<>();
        String queryWithEmpl = "SELECT * FROM customer_check WHERE print_date BETWEEN ? AND ? AND id_employee=?";
        String queryWithoutEmpl = "SELECT * FROM customer_check WHERE print_date BETWEEN ? AND ?";
        try(PreparedStatement statement = connection.prepareStatement(id == null ? queryWithoutEmpl : queryWithEmpl)) {
            statement.setTimestamp(1,Timestamp.valueOf(from));
            statement.setTimestamp(2, Timestamp.valueOf(to));
            if (id != null)
                statement.setInt(3, id);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                entities.add(parseSetToEntity(resultSet));
            }
            return entities;
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public BigDecimal findNumberOfProductsByCashierAndPrintDateBetween(Integer id, LocalDateTime from, LocalDateTime to) {
        String queryWithEmpl = "SELECT SUM(sum_total) FROM customer_check WHERE print_date BETWEEN ? AND ? AND id_employee=?";
        String queryWithoutEmpl = "SELECT SUM(sum_total) FROM customer_check WHERE print_date BETWEEN ? AND ?";
        try(PreparedStatement statement = connection.prepareStatement(id == null ? queryWithoutEmpl : queryWithEmpl)) {
            statement.setTimestamp(1, Timestamp.valueOf(from));
            statement.setTimestamp(2, Timestamp.valueOf(to));
            if (id != null)
                statement.setInt(3, id);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.getBigDecimal(1);
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }



    @Override
    protected void setMainFields(PreparedStatement statement, CheckEntity entity) throws SQLException {
        statement.setInt(1, entity.getEmployee().getId());
        statement.setInt(2, entity.getCustomerCard().getId());
        statement.setTimestamp(3, Timestamp.valueOf(entity.getPrintDate()));
        statement.setBigDecimal(4, entity.getSumTotal());
        statement.setBigDecimal(5, entity.getVat());
    }

    @Override
    protected CheckEntity parseSetToEntity(ResultSet set) throws SQLException {
        Integer employeeId = set.getInt("id_employee");
        Integer customerCardId = set.getInt("card_number");
        return CheckEntity.builder()
                .id(set.getInt("check_number"))
                .employee(employeeRepository.findById(employeeId)
                        .orElseThrow(
                                () -> new NoSuchEntityException("Can`t find employee by id: " + employeeId)
                        )
                )
                .customerCard(customerCardRepository.findById(customerCardId)
                        .orElseThrow(
                                () -> new NoSuchEntityException("Can`t find customer card by id: " + customerCardId)
                        )
                )
                .printDate(set.getTimestamp("print_date").toLocalDateTime())
                .sumTotal(set.getBigDecimal("sum_total"))
                .vat(set.getBigDecimal("vat"))
                .build();
    }

    @Override
    protected void setIdToEntity(CheckEntity entity, ResultSet set) throws SQLException {
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
