package com.locaimmo.serviceannonce.service;

import com.locaimmo.serviceannonce.domain.entity.Photo;

import java.util.List;

public interface IServicePhoto {

    List<Photo> findAll();

    Photo findById(Long id);

    Photo create(Photo photo);

    Photo update(Long id, Photo photo);

    void delete(Long id);
}
