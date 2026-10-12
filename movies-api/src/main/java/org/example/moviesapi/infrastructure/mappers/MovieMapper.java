package org.example.moviesapi.infrastructure.mappers;

import org.example.moviesapi.domain.models.MovieDto;
import org.example.moviesapi.domain.models.MovieInfoDto;
import org.example.moviesapi.infrastructure.persistence.MovieEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface MovieMapper {

    MovieInfoDto toInfoDto(MovieEntity entity);

    MovieDto toDetailsDto(MovieEntity entity);

    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    MovieEntity toEntity(MovieDto movieDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    MovieEntity updateEntityFromDto(MovieDto movieDto, @MappingTarget MovieEntity entity);
}
