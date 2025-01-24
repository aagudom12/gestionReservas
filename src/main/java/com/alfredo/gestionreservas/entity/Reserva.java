package com.alfredo.gestionreservas.entity;

import jakarta.persistence.*;
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

    private LocalDate fecha;
    private String hora;

    @OneToMany(targetEntity = Cliente.class, cascade = CascadeType.ALL, mappedBy = "reserva")
    private List<Cliente> clientes = new ArrayList<>();

    @ManyToOne
    private Mesa mesa;

}
