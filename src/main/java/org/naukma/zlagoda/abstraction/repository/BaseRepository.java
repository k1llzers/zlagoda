package org.naukma.zlagoda.abstraction.repository;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public abstract class BaseRepository<E extends GettableById<I>, I> implements IRepository<E, I> {
    @Autowired
    protected Connection connection;
    protected final String tableName;
    protected final String createQuery;
    protected final String updateQuery;
    protected final String deleteQuery;
    protected final String findByIdQuery;
    protected final String defaultOrderByColumn;
    protected String findAllQuery = "SELECT * FROM %a";
    protected String findAllQueryOrderBy = "SELECT * FROM %a ORDER BY %d";

    @Override
    public I save(@Valid E entity) {
        try(PreparedStatement createStatement = connection.prepareStatement(createQuery, Statement.RETURN_GENERATED_KEYS)) {
            setMainFields(createStatement, entity);
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
    public Boolean update(@Valid E entity) {
        try(PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
            setMainFields(updateStatement, entity);
            setIdToUpdateStatement(updateStatement, entity.getId());
            return updateStatement.executeUpdate() > 0;
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Boolean delete(I id) {
        try(PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery)) {
            setIdToFindDeleteStatement(deleteStatement, id);
            return deleteStatement.executeUpdate() > 0;
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<E> findById(I id) {
        try(PreparedStatement findByIdStatement = connection.prepareStatement(findByIdQuery)) {
            setIdToFindDeleteStatement(findByIdStatement, id);
            ResultSet resultSet = findByIdStatement.executeQuery();
            E result = null;
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
    public List<E> findAll() {
        List<E> entities = new ArrayList<>();
        try(PreparedStatement findAllStatement = connection.prepareStatement(String.format(findAllQuery, tableName))) {
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
    public List<E> findAllOrderBy() {
        List<E> entities = new ArrayList<>();
        try(PreparedStatement findAllStatement = connection.prepareStatement(String.format(findAllQueryOrderBy, tableName, defaultOrderByColumn))) {
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

    abstract protected void setMainFields(PreparedStatement statement, E entity) throws SQLException;

    abstract protected E parseSetToEntity(ResultSet set) throws SQLException;

    abstract protected void setIdToEntity(E entity, ResultSet set) throws SQLException;
    abstract protected void setIdToFindDeleteStatement(PreparedStatement statement, I id) throws SQLException;
    abstract protected void setIdToUpdateStatement(PreparedStatement statement, I id) throws SQLException;
}
