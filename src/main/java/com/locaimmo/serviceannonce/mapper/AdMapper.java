package com.locaimmo.serviceannonce.mapper;

import com.locaimmo.serviceannonce.domain.dto.AdDto;
import com.locaimmo.serviceannonce.domain.entity.Ad;
import org.mapstruct.*;


import java.util.List;

@Mapper(componentModel = "spring")
public interface AdMapper {

    @Mapping(source = "room.id", target = "roomId")
    AdDto toDto(Ad ad);

    @Mapping(target = "room", ignore = true)
    @Mapping(target = "photos", ignore = true)
    Ad toEntity(AdDto dto);

    List<AdDto> toDtoList(List<Ad> ads);
}
