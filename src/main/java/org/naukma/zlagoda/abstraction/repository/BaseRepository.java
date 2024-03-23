package org.naukma.zlagoda.abstraction.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.sql.Connection;

@Repository
@RequiredArgsConstructor
public abstract class BaseRepository<T, I> implements IRepository<T, I> {
    private Connection connection;
}
