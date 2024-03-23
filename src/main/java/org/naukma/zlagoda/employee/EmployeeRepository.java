package org.naukma.zlagoda.employee;

import org.naukma.zlagoda.abstraction.repository.BaseRepository;
import org.springframework.stereotype.Repository;

import java.sql.*;

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
            createStatement.setString(1, entity.getSurname());
            createStatement.setString(2, entity.getName());
            createStatement.setString(3, entity.getPatronymic());
            createStatement.setString(4, entity.getRole().name());
            createStatement.setBigDecimal(5, entity.getSalary());
            createStatement.setDate(6, Date.valueOf(entity.getDateOfBirth()));
            createStatement.setDate(7, Date.valueOf(entity.getDateOfStart()));
            createStatement.setString(8, entity.getPhoneNumber());
            createStatement.setString(9, entity.getCity());
            createStatement.setString(10, entity.getStreet());
            createStatement.setString(11, entity.getZipCode());
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
            updateStatement.setString(1, entity.getSurname());
            updateStatement.setString(2, entity.getName());
            updateStatement.setString(3, entity.getPatronymic());
            updateStatement.setString(4, entity.getRole().name());
            updateStatement.setBigDecimal(5, entity.getSalary());
            updateStatement.setDate(6, Date.valueOf(entity.getDateOfBirth()));
            updateStatement.setDate(7, Date.valueOf(entity.getDateOfStart()));
            updateStatement.setString(8, entity.getPhoneNumber());
            updateStatement.setString(9, entity.getCity());
            updateStatement.setString(10, entity.getStreet());
            updateStatement.setString(11, entity.getZipCode());
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
    public EmployeeEntity findById(Integer id) {
        try(PreparedStatement findByIdStatement = connection.prepareStatement(findByIdQuery)) {
            findByIdStatement.setInt(1, id);
            ResultSet resultSet = findByIdStatement.executeQuery();
            return parseEmployee(resultSet);
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private EmployeeEntity parseEmployee(ResultSet set) throws SQLException {
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
