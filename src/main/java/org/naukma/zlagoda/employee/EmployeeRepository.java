package org.naukma.zlagoda.employee;

import org.naukma.zlagoda.abstraction.repository.BaseRepository;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class EmployeeRepository extends BaseRepository<EmployeeEntity, Integer> {
    public EmployeeRepository() {
        super("employee",
                "INSERT INTO employee (empl_surname, empl_name, empl_patronymic, " +
                "empl_role, salary, date_of_birth, date_of_start, phone_number, city, street, zip_code, login, password) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                "UPDATE employee SET empl_surname=?, empl_name=?, empl_patronymic=?, " +
                 "empl_role=?, salary=?, date_of_birth=?, date_of_start=?, phone_number=?, city=?, street=?, zip_code=?, " +
                 "login=? WHERE id_employee=?",
                "DELETE FROM employee WHERE id_employee=?",
                "SELECT * FROM employee WHERE id_employee=?",
                "empl_surname");
    }

    public List<EmployeeEntity> findAllCashiersOrderedBySurname() {
        String query = "SELECT * FROM employee WHERE empl_role='CASHIER' ORDER BY empl_surname";
        return findAllByCustomQuery(query);
    }

    public List<EmployeeEntity> findPhoneNumberAndAddressBySurname(String surname) {
        String query = "SELECT id_employee, empl_surname, empl_name, empl_patronymic, phone_number, city, street, zip_code FROM employee WHERE empl_surname=?";
        List<EmployeeEntity> entities = new ArrayList<>();
        try(PreparedStatement findAllStatement = connection.prepareStatement(query)) {
            findAllStatement.setString(1, surname);
            ResultSet resultSet = findAllStatement.executeQuery();
            while (resultSet.next()){
                entities.add(parseSetWithAddressPhoneNumberAndNamToEntity(resultSet));
            }
            return entities;
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void setMainFields(PreparedStatement statement, EmployeeEntity entity) throws SQLException {
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
        statement.setString(12, entity.getLogin());
    }

    @Override
    public Integer save(EmployeeEntity entity) {
        try(PreparedStatement createStatement = connection.prepareStatement(createQuery, Statement.RETURN_GENERATED_KEYS)) {
            setMainFields(createStatement, entity);
            createStatement.setString(13, entity.getPassword());
            createStatement.executeUpdate();
            ResultSet generatedKeys = createStatement.getGeneratedKeys();
            if(generatedKeys.next()){
                setIdToEntity(entity, generatedKeys);
            }
            return entity.getId();
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected EmployeeEntity parseSetToEntity(ResultSet set) throws SQLException {
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
                .login(set.getString("login"))
                .build();
    }

    private EmployeeEntity parseSetWithAddressPhoneNumberAndNamToEntity(ResultSet set) throws SQLException {
        return EmployeeEntity.builder()
                .id(set.getInt("id_employee"))
                .surname(set.getString("empl_surname"))
                .name(set.getString("empl_name"))
                .patronymic(set.getString("empl_patronymic"))
                .phoneNumber(set.getString("phone_number"))
                .city(set.getString("city"))
                .street(set.getString("street"))
                .zipCode(set.getString("zip_code"))
                .build();
    }

    @Override
    protected void setIdToEntity(EmployeeEntity entity, ResultSet set) throws SQLException {
        entity.setId(set.getInt(1));
    }

    @Override
    protected void setIdToFindDeleteStatement(PreparedStatement statement, Integer id) throws SQLException {
        statement.setInt(1, id);
    }

    @Override
    protected void setIdToUpdateStatement(PreparedStatement statement, Integer id) throws SQLException {
        statement.setInt(13, id);
    }
}
