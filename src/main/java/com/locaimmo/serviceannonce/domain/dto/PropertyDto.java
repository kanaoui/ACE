package com.locaimmo.serviceannonce.domain.dto;

import com.locaimmo.serviceannonce.feignrequests.UserDto;

public class PropertyDto {
    private Long id;
    private String adresse;
    private String ville;
    private Double latitude;
    private Double longitude;
    private String description;
    private String rules;
    private UserDto owner;
    private Long proprietaireId; // <-- ajoute ça



    public PropertyDto() {} // constructeur vide

    // constructeur complet
    public PropertyDto(Long id, String adresse, String ville, Double latitude, Double longitude,
                       String description, String rules, UserDto owner) {
        this.id = id;
        this.adresse = adresse;
        this.ville = ville;
        this.latitude = latitude;
        this.longitude = longitude;
        this.description = description;
        this.rules = rules;
        this.owner = owner;
    }

    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getAdresse() { return adresse; }
    public void setAdresse(String adresse) { this.adresse = adresse; }

    public String getVille() { return ville; }
    public void setVille(String ville) { this.ville = ville; }

    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }

    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getRules() { return rules; }
    public void setRules(String rules) { this.rules = rules; }

    public UserDto getOwner() { return owner; }
    public void setOwner(UserDto owner) { this.owner = owner; }

    public Long getProprietaireId() { return proprietaireId; }
    public void setProprietaireId(Long proprietaireId) {
        this.proprietaireId = proprietaireId;
    }

}
