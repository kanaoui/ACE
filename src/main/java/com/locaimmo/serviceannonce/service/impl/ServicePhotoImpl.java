package com.locaimmo.serviceannonce.service.impl;

import com.locaimmo.serviceannonce.domain.entity.Photo;
import com.locaimmo.serviceannonce.repository.PhotoRepository;
import com.locaimmo.serviceannonce.service.IServicePhoto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ServicePhotoImpl implements IServicePhoto {

    private final PhotoRepository photoRepository;

    public ServicePhotoImpl(PhotoRepository photoRepository) {
        this.photoRepository = photoRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Photo> findAll() {
        return photoRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Photo findById(Long id) {
        return photoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Photo not found with id " + id));
    }

    @Override
    public Photo create(Photo photo) {
        return photoRepository.save(photo);
    }

    @Override
    public Photo update(Long id, Photo photo) {
        Photo existing = findById(id);
        existing.setUrl(photo.getUrl());
        existing.setAd(photo.getAd());
        return photoRepository.save(existing);
    }

    @Override
    public void delete(Long id) {
        photoRepository.deleteById(id);
    }
}
