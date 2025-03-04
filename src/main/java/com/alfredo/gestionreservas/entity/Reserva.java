package com.alfredo.gestionreservas.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

    @NotNull(message = "Debe especificar el número de personas")
    @Min(value = 1, message = "Debe haber al menos una persona en la reserva")
    private Integer numeroPersonas;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false) // Asocia la reserva con el usuario
    private UserEntity usuario;

    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "mesa_id", nullable = false)
    private Mesa mesa;

    public Long getId() {
        return id;
    }

    public @FutureOrPresent(message = "La fecha de la reserva no puede ser en el pasado") LocalDate getFecha() {
        return fecha;
    }

    public @NotBlank(message = "La hora no puede estar vacía") String getHora() {
        return hora;
    }

    public @NotNull(message = "Debe especificar el número de personas") @Min(value = 1, message = "Debe haber al menos una persona en la reserva") Integer getNumeroPersonas() {
        return numeroPersonas;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setFecha(@FutureOrPresent(message = "La fecha de la reserva no puede ser en el pasado") LocalDate fecha) {
        this.fecha = fecha;
    }

    public void setHora(@NotBlank(message = "La hora no puede estar vacía") String hora) {
        this.hora = hora;
    }

    public void setNumeroPersonas(@NotNull(message = "Debe especificar el número de personas") @Min(value = 1, message = "Debe haber al menos una persona en la reserva") Integer numeroPersonas) {
        this.numeroPersonas = numeroPersonas;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public Mesa getMesa() {
        return mesa;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public void setMesa(Mesa mesa) {
        this.mesa = mesa;
    }

    public UserEntity getUsuario() {
        return usuario;
    }

    public void setUsuario(UserEntity usuario) {
        this.usuario = usuario;
    }
}
