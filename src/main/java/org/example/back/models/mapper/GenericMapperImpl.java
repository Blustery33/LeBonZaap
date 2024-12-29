package org.example.back.models.mapper;

import org.example.back.models.UserModel;
import org.example.back.models.dto.UserDTO;
import org.modelmapper.ModelMapper;

import org.springframework.beans.factory.annotation.Autowired;

public abstract class GenericMapperImpl<E, T> implements CommonMapper<E, T> {

    @Autowired
    protected ModelMapper modelMapper;


    private final Class<E> entityClass;
    private final Class<T> dtoClass;

    public GenericMapperImpl(Class<E> entityClass, Class<T> dtoClass) {
        this.entityClass = entityClass;
        this.dtoClass = dtoClass;
    }

    @Override
    public T toDto(E entity) {
        return modelMapper.map(entity, dtoClass);
    }

    @Override
    public E toEntity(T dto) {
        return modelMapper.map(dto, entityClass);
    }


}
