package org.naukma.zlagoda.abstraction.repository;

import java.sql.ResultSet;
import java.util.Optional;

public interface IRepository<E, I> {
    I save(E entity);

    Boolean update(E entity);

    Boolean delete(I id);

    Optional<E> findById(I id);

    void setMainFields(E entity);

    E parseSetToEntity(ResultSet set);
}
