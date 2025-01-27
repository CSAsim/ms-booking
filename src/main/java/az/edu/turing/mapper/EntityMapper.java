package az.edu.turing.mapper;

import java.util.List;

public interface EntityMapper<D, E> {

    E toEntity(D d);

    List<E> toEntityList(List<D> d);

    D toDto(E e);

    List<D> toDtoList(List<E> e);
}
