package org.naukma.zlagoda.abstraction.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Connection;

public abstract class BaseRepository<T, I> implements IRepository<T, I> {
    @Autowired
    protected Connection connection;
}
