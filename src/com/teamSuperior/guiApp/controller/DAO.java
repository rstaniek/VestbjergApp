package com.teamSuperior.guiApp.controller;

import java.io.Serializable;
import java.util.List;

/**
 * Data Access Object interface
 * <p>
 * This interface defines the functions required to communicate with the database.
 * We use generics as we do not want create the same interface implementation for every model.
 */
public interface DAO<T, Id extends Serializable> {
    void persist(T t);

    T getById(Id id);

    List<T> getAll();

    void update(T t);

    void delete(T t);

    void deleteAll();
}
