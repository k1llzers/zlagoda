package org.naukma.zlagoda.abstraction.service;

import org.naukma.zlagoda.abstraction.repository.GettableById;

public interface IService<D extends GettableById<I>, E extends GettableById<I>, I> {
    I save(D dto);

    Boolean update(D dto);

    Boolean delete(I id);

    E getById(I id);
}
