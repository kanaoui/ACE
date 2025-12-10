package com.locaimmo.serviceannonce.service.impl;

import com.locaimmo.serviceannonce.domain.dto.PropertyDto;
import com.locaimmo.serviceannonce.domain.entity.Property;
import com.locaimmo.serviceannonce.repository.PropertyRepository;
import com.locaimmo.serviceannonce.service.IServiceProperty;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ServicePropertyImpl implements IServiceProperty {

    private final PropertyRepository propertyRepository;

    public ServicePropertyImpl(PropertyRepository propertyRepository) {
        this.propertyRepository = propertyRepository;
    }

    public PropertyDto getPropertyWithUser(Long propertyId) {
        Property property = findById(propertyId);
        // User service not available in standalone mode
        return new PropertyDto(property.getId(), property.getAdresse(), property.getVille(),
                property.getLatitude(), property.getLongitude(),
                property.getDescription(), property.getRules(), null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Property> findAll() {
        return propertyRepository.findAllWithRooms();
    }

    @Override
    @Transactional(readOnly = true)
    public Property findById(Long id) {
        return propertyRepository.findByIdWithRooms(id)
                .orElseThrow(() -> new RuntimeException("PropertyRepository not found with id " + id));
    }

    @Override
    public Property create(Property property) {
        return propertyRepository.save(property);
    }

    @Override
    public Property update(Long id, Property property) {
        Property existing = findById(id);
        existing.setAdresse(property.getAdresse());
        existing.setVille(property.getVille());
        existing.setLatitude(property.getLatitude());
        existing.setLongitude(property.getLongitude());
        existing.setDescription(property.getDescription());
        existing.setRules(property.getRules());
        return propertyRepository.save(existing);
    }

    @Override
    public void delete(Long id) {
        propertyRepository.deleteById(id);
    }
}
