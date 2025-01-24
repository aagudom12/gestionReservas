package com.alfredo.gestionreservas.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "mesas")
public class Mesa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private Long numeroMesa;

    @NotBlank(message = "La descripción no puede estar en blanco")
    private String descripcion;

    @OneToMany(targetEntity = Reserva.class, cascade = CascadeType.ALL, mappedBy = "mesa")
    private List<Reserva> reservas = new ArrayList<>();

    public Long getNumeroMesa() {
        return numeroMesa;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setNumeroMesa(Long numeroMesa) {
        this.numeroMesa = numeroMesa;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
