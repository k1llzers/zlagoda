package org.naukma.zlagoda.abstraction.repository;

import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public abstract class BaseRepository<E, I> implements IRepository<E, I> {
    @Autowired
    protected Connection connection;

    abstract protected void setMainFields(Statement statement, E entity);

    abstract protected E parseSetToEntity(ResultSet set);
}
