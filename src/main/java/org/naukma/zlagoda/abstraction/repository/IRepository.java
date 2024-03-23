package org.naukma.zlagoda.abstraction.repository;

public interface IRepository<T, I> {
    I save(T entity);

    Boolean update(T entity);

    Boolean delete(I id);

    T findById(I id);
}
