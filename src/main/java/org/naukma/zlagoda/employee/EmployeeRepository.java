package org.naukma.zlagoda.employee;

import org.naukma.zlagoda.abstraction.repository.BaseRepository;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.Optional;

@Repository
public class EmployeeRepository extends BaseRepository<EmployeeEntity, Integer> {

    private String createQuery = "INSERT INTO `employee` (empl_surname, empl_name, empl_patronymic, " +
            "empl_role, salary, date_of_birth, date_of_start, phone_number, city, street, zip_code) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private String updateQuery = "UPDATE `employee` SET empl_surname=?, empl_name=?, empl_patronymic=?, " +
            "empl_role=?, salary=?, date_of_birth=?, date_of_start=?, phone_number=?, city=?, street=?, zip_code=?" +
            "WHERE id_employee=?";
    private String deleteQuery = "DELETE FROM `employee` WHERE id_employee=?";
    private String findByIdQuery = "SELECT * FROM `employee` WHERE id_employee=?";

    @Override
    public Integer save(EmployeeEntity entity) {
        try(PreparedStatement createStatement = connection.prepareStatement(createQuery, Statement.RETURN_GENERATED_KEYS)) {
            setMainFields(createStatement, entity);
            createStatement.executeUpdate();
            ResultSet generatedKeys = createStatement.getGeneratedKeys();
            if(generatedKeys.next()){
                entity.setId(generatedKeys.getInt(1));
            }
            return entity.getId();
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Boolean update(EmployeeEntity entity) {
        try(PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
            setMainFields(updateStatement, entity);
            updateStatement.setInt(12, entity.getId());
            return updateStatement.executeUpdate() > 0;
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Boolean delete(Integer id) {
        try(PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery)) {
            deleteStatement.setInt(1, id);
            return deleteStatement.executeUpdate() > 0;
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<EmployeeEntity> findById(Integer id) {
        try(PreparedStatement findByIdStatement = connection.prepareStatement(findByIdQuery)) {
            findByIdStatement.setInt(1, id);
            ResultSet resultSet = findByIdStatement.executeQuery();
            EmployeeEntity result = null;
            if(resultSet.next()){
                result = parseSetToEntity(resultSet);
            }
            return Optional.ofNullable(result);
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void setMainFields(PreparedStatement statement, EmployeeEntity entity) throws SQLException {
        statement.setString(1, entity.getSurname());
        statement.setString(2, entity.getName());
        statement.setString(3, entity.getPatronymic());
        statement.setString(4, entity.getRole().name());
        statement.setBigDecimal(5, entity.getSalary());
        statement.setDate(6, Date.valueOf(entity.getDateOfBirth()));
        statement.setDate(7, Date.valueOf(entity.getDateOfStart()));
        statement.setString(8, entity.getPhoneNumber());
        statement.setString(9, entity.getCity());
        statement.setString(10, entity.getStreet());
        statement.setString(11, entity.getZipCode());
    }

    @Override
    public EmployeeEntity parseSetToEntity(ResultSet set) throws SQLException {
        return EmployeeEntity.builder()
                .id(set.getInt("id_employee"))
                .surname(set.getString("empl_surname"))
                .name(set.getString("empl_name"))
                .patronymic(set.getString("empl_patronymic"))
                .role(Role.valueOf(set.getString("empl_role")))
                .salary(set.getBigDecimal("salary"))
                .dateOfBirth(set.getDate("date_of_birth").toLocalDate())
                .dateOfStart(set.getDate("date_of_start").toLocalDate())
                .phoneNumber(set.getString("phone_number"))
                .city(set.getString("city"))
                .street(set.getString("street"))
                .zipCode(set.getString("zip_code"))
                .build();
    }
}
