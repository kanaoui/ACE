
package com.locaimmo.serviceannonce.mapper;

import com.locaimmo.serviceannonce.domain.dto.PropertyDto;
import com.locaimmo.serviceannonce.domain.entity.Property;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PropertyMapper {

    //@Mapping(source = "property.id", target = "proprietaireId")
    PropertyDto toDto(Property property);

    //@Mapping(target = "property", ignore = true)
    @Mapping(target = "rooms", ignore = true)
    Property toEntity(PropertyDto dto);

    List<PropertyDto> toDtoList(List<Property> properties);
}
