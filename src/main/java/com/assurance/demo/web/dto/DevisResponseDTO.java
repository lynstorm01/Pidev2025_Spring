package com.assurance.demo.web.dto;


public class DevisResponseDTO {

    private Long id;  // Identifiant unique du devis
    private String message;  // Message de réponse pour l'action effectuée

    // Constructeur pour les cas où l'on souhaite retourner l'ID et un message
    public DevisResponseDTO(Long id, String message) {
        this.id = id;
        this.message = message;
    }

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
