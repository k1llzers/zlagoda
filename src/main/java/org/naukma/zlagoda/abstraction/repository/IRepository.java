package org.naukma.zlagoda.abstraction.repository;

import java.util.List;
import java.util.Optional;

public interface IRepository<E extends GettableById<I>, I> {
    I save(E entity);

    Boolean update(E entity);

    Boolean delete(I id);

    Optional<E> findById(I id);

    List<E> findAll();

    List<E> findAllOrderByDefault();
}
