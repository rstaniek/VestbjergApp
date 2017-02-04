package com.teamSuperior.guiApp.controller;

import java.io.Serializable;
import java.util.List;

/**
 * Data Access Object interface
 *
 * This interface defines the functions required to communicate with the database.
 * We use generics as we do not want create the same interface implementation for every model.
 */
public interface DAO<T, Id extends Serializable> {
    public void persist(T t);

    public T getById(Id id);

    public List<T> getAll();

    public void update(T t);

    public void delete(T t);

    public void deleteAll();
}
