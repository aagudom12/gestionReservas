package com.alfredo.gestionreservas.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "reservas")
public class Reserva {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @FutureOrPresent(message = "La fecha de la reserva no puede ser en el pasado")
    private LocalDate fecha;

    @NotBlank(message = "La hora no puede estar vacía")
    private String hora;

    @OneToMany(targetEntity = Cliente.class, cascade = CascadeType.ALL, mappedBy = "reserva")
    private List<Cliente> clientes = new ArrayList<>();

    @ManyToOne
    private Mesa mesa;

}
