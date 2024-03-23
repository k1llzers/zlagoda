package org.naukma.zlagoda.abstraction.service;

import org.naukma.zlagoda.abstraction.repository.GettableById;

import java.util.Optional;

public interface IService<D, E extends GettableById<I>, I> {
    I save(D dto);

    Boolean update(D dto);

    Boolean delete(I id);

    E getById(I id);
}
