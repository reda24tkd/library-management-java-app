package org.ensa.dao;

import org.ensa.model.Result;

import java.util.List;

public interface DAO<T> {
    Result<T> create(T obj);

    Result<T> findById(int id);

    Result<List<T>> findAll();

    Result<T> update(T obj);

    Result<Boolean> delete(int id);
}
