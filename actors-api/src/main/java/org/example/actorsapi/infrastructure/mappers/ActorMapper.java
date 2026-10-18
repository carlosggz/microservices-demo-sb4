package org.example.actorsapi.infrastructure.mappers;

import org.example.actorsapi.domain.models.ActorDto;
import org.example.actorsapi.domain.models.ActorInfoDto;
import org.example.actorsapi.infrastructure.persistence.ActorEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ActorMapper {

    ActorInfoDto toInfoDto(ActorEntity entity);

    ActorDto toDetailsDto(ActorEntity entity);

    ActorEntity toEntity(ActorDto actorDto);
}
