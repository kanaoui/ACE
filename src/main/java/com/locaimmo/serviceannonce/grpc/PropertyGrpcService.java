package com.locaimmo.serviceannonce.grpc;

import com.locaimmo.serviceannonce.domain.entity.Property;
import com.locaimmo.serviceannonce.service.IServiceProperty;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import serviceannonce.PropertyServiceGrpc;

import java.util.stream.Collectors;

@GrpcService
public class PropertyGrpcService
        extends PropertyServiceGrpc.PropertyServiceImplBase {

    private final IServiceProperty propertyService;

    @Autowired
    public PropertyGrpcService(IServiceProperty propertyService) {
        this.propertyService = propertyService;
    }

    @Override
    @Transactional(readOnly = true)
    public void getAll(
            serviceannonce.Property.Empty request,
            StreamObserver<serviceannonce.Property.PropertyListResponse> responseObserver) {
        try {
            java.util.List<Property> properties = propertyService.findAll();
            
            // Convert to messages within the transaction to avoid lazy loading issues
            serviceannonce.Property.PropertyListResponse response = serviceannonce.Property.PropertyListResponse.newBuilder()
                    .addAllProperties(properties.stream()
                            .map(this::convertToPropertyMessage)
                            .collect(Collectors.toList()))
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(
                    Status.INTERNAL
                            .withDescription("Error retrieving properties: " + e.getMessage())
                            .withCause(e)
                            .asRuntimeException());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public void getById(
            serviceannonce.Property.PropertyIdRequest request,
            StreamObserver<serviceannonce.Property.PropertyMessage> responseObserver) {
        try {
            Property property = propertyService.findById(request.getId());
            serviceannonce.Property.PropertyMessage response = convertToPropertyMessage(property);
            
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (RuntimeException e) {
            responseObserver.onError(
                    Status.NOT_FOUND
                            .withDescription(e.getMessage())
                            .asRuntimeException());
        } catch (Exception e) {
            responseObserver.onError(
                    Status.INTERNAL
                            .withDescription("Error retrieving property: " + e.getMessage())
                            .withCause(e)
                            .asRuntimeException());
        }
    }

    @Override
    public void create(
            serviceannonce.Property.CreatePropertyRequest request,
            StreamObserver<serviceannonce.Property.PropertyMessage> responseObserver) {
        try {
            Property property = new Property();
            property.setAdresse(request.getAdresse());
            property.setVille(request.getVille());
            property.setLatitude(request.getLatitude());
            property.setLongitude(request.getLongitude());
            property.setDescription(request.getDescription());
            property.setRules(request.getRules());
            property.setProprietaireId(request.getProprietaireId());

            Property savedProperty = propertyService.create(property);
            serviceannonce.Property.PropertyMessage response = convertToPropertyMessage(savedProperty);

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(
                    Status.INTERNAL
                            .withDescription("Error creating property: " + e.getMessage())
                            .withCause(e)
                            .asRuntimeException());
        }
    }

    @Override
    public void update(
            serviceannonce.Property.UpdatePropertyRequest request,
            StreamObserver<serviceannonce.Property.PropertyMessage> responseObserver) {
        try {
            Property property = new Property();
            property.setAdresse(request.getAdresse());
            property.setVille(request.getVille());
            property.setLatitude(request.getLatitude());
            property.setLongitude(request.getLongitude());
            property.setDescription(request.getDescription());
            property.setRules(request.getRules());
            property.setProprietaireId(request.getProprietaireId());

            Property updatedProperty = propertyService.update(request.getId(), property);
            serviceannonce.Property.PropertyMessage response = convertToPropertyMessage(updatedProperty);

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (RuntimeException e) {
            responseObserver.onError(
                    Status.NOT_FOUND
                            .withDescription(e.getMessage())
                            .asRuntimeException());
        } catch (Exception e) {
            responseObserver.onError(
                    Status.INTERNAL
                            .withDescription("Error updating property: " + e.getMessage())
                            .withCause(e)
                            .asRuntimeException());
        }
    }

    @Override
    public void delete(
            serviceannonce.Property.PropertyIdRequest request,
            StreamObserver<serviceannonce.Property.Empty> responseObserver) {
        try {
            propertyService.delete(request.getId());
            responseObserver.onNext(serviceannonce.Property.Empty.newBuilder().build());
            responseObserver.onCompleted();
        } catch (RuntimeException e) {
            responseObserver.onError(
                    Status.NOT_FOUND
                            .withDescription(e.getMessage())
                            .asRuntimeException());
        } catch (Exception e) {
            responseObserver.onError(
                    Status.INTERNAL
                            .withDescription("Error deleting property: " + e.getMessage())
                            .withCause(e)
                            .asRuntimeException());
        }
    }

    private serviceannonce.Property.PropertyMessage convertToPropertyMessage(Property property) {
        serviceannonce.Property.PropertyMessage.Builder builder = serviceannonce.Property.PropertyMessage.newBuilder()
                .setId(property.getId())
                .setAdresse(property.getAdresse())
                .setVille(property.getVille())
                .setLatitude(property.getLatitude())
                .setLongitude(property.getLongitude())
                .setDescription(property.getDescription())
                .setRules(property.getRules())
                .setProprietaireId(property.getProprietaireId());

        // Add rooms if they exist - access within transaction to avoid lazy loading issues
        try {
            if (property.getRooms() != null && !property.getRooms().isEmpty()) {
                property.getRooms().forEach(room -> {
                    serviceannonce.Property.RoomMessage roomMessage = serviceannonce.Property.RoomMessage.newBuilder()
                            .setId(room.getId())
                            .setName("Room " + room.getId()) // Default name since entity doesn't have name
                            .setCapacity(1) // Default capacity since entity doesn't have capacity
                            .setPrice(room.getPrix() != null ? room.getPrix() : 0.0)
                            .build();
                    builder.addRooms(roomMessage);
                });
            }
        } catch (Exception e) {
            // If rooms can't be loaded, just skip them (they're optional)
            // This prevents lazy loading errors
        }

        // Photos would need to be loaded from Ad entity if needed
        // For now, leaving photos empty as they're linked to Ad, not directly to Property

        return builder.build();
    }
}
