package org.naukma.zlagoda.abstraction.repository;

import java.util.Optional;

public interface IRepository<E, I> {
    I save(E entity);

    Boolean update(E entity);

    Boolean delete(I id);

    Optional<E> findById(I id);
}
