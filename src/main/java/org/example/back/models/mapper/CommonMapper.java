package org.example.back.models.mapper;

public interface CommonMapper<E,T> {
    T toDto(E entity);
    E toEntity(T dto);
}
