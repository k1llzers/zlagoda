package org.naukma.zlagoda.abstraction.service;

import lombok.RequiredArgsConstructor;
import org.naukma.zlagoda.abstraction.repository.GettableById;
import org.naukma.zlagoda.abstraction.repository.IRepository;
import org.naukma.zlagoda.employee.EmployeeEntity;
import org.naukma.zlagoda.exception.NoSuchEntityException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.function.Supplier;

@RequiredArgsConstructor
public abstract class BaseService<D, E extends GettableById<I>, I> implements IService<D, E, I> {
    @Autowired
    protected IRepository<E, I> repository;
    protected final Supplier<E> entitySupplier;
    protected final Class<?> clazz;

    @Override
    public I save(D dto) {
        E entity = entitySupplier.get();
        mergeEntity(entity, dto);
        return repository.save(entity);
    }

    @Override
    public Boolean update(D dto) {
        E entity = entitySupplier.get();
        mergeEntity(entity, dto);
        return repository.update(entity);
    }

    @Override
    public Boolean delete(I id) {
        return repository.delete(id);
    }

    @Override
    public E getById(I id) {
        return repository.findById(id).orElseThrow(
                () -> new NoSuchEntityException("Can`t find " + clazz.getName() + " by id: " + id)
        );
    }

    protected abstract void mergeEntity(E entity, D dto);
}
