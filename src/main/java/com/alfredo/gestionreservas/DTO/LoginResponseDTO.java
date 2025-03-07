package com.alfredo.gestionreservas.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
public class LoginResponseDTO {
    private String username;
    private String token;
    private Long clienteId;
    private String clienteNombre;  // Agregamos el nombre del cliente

    public LoginResponseDTO() {
    }

    public LoginResponseDTO(String username, String token) {
        this.username = username;
        this.token = token;
    }

    public LoginResponseDTO(String username, String token, Long clienteId, String clienteNombre) {
        this.username = username;
        this.token = token;
        this.clienteId = clienteId;
        this.clienteNombre = clienteNombre;  // Inicializamos el nombre del cliente
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}

