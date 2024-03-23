package org.naukma.zlagoda.abstraction.repository;

import org.springframework.beans.factory.annotation.Autowired;

import java.sql.*;

public abstract class BaseRepository<E, I> implements IRepository<E, I> {
    @Autowired
    protected Connection connection;

    abstract protected void setMainFields(PreparedStatement statement, E entity) throws SQLException;

    abstract protected E parseSetToEntity(ResultSet set) throws SQLException;
}
