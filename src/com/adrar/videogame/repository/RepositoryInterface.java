package com.adrar.videogame.repository;

import com.adrar.videogame.entity.Device;

import java.util.ArrayList;

public interface RepositoryInterface<T> {
    public T find(Integer id);
    public ArrayList<T> findAll();
    public T save(T entity);
    public T update(T entity);
    public void delete(Integer id);
}
