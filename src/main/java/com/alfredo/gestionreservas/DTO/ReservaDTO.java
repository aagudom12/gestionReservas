package com.alfredo.gestionreservas.DTO;

import com.alfredo.gestionreservas.entity.Reserva;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
public class ReservaDTO {
    private String nombreCliente;
    private String emailCliente;
    private LocalDate fechaReserva;
    private String horaReserva;
    private Long numeroMesa;
    private Integer numeroPersonas;

    public ReservaDTO() {}

    public ReservaDTO(String nombreCliente, String emailCliente, LocalDate fechaReserva, String horaReserva, Long numeroMesa, Integer numeroPersonas) {
        this.nombreCliente = nombreCliente;
        this.emailCliente = emailCliente;
        this.fechaReserva = fechaReserva;
        this.horaReserva = horaReserva;
        this.numeroMesa = numeroMesa;
        this.numeroPersonas = numeroPersonas;
    }

    public ReservaDTO(Reserva reserva) {
        this.nombreCliente = reserva.getCliente().getNombre();
        this.emailCliente = reserva.getCliente().getEmail();
        this.fechaReserva = reserva.getFecha();
        this.horaReserva = reserva.getHora().toString();  // Convertimos LocalTime a String
        this.numeroMesa = reserva.getMesa().getNumeroMesa();
        this.numeroPersonas = reserva.getNumeroPersonas();
    }

    public String getNombreCliente() { return nombreCliente; }
    public void setNombreCliente(String nombreCliente) { this.nombreCliente = nombreCliente; }

    public String getEmailCliente() { return emailCliente; }
    public void setEmailCliente(String emailCliente) { this.emailCliente = emailCliente; }

    public LocalDate getFechaReserva() { return fechaReserva; }
    public void setFechaReserva(LocalDate fechaReserva) { this.fechaReserva = fechaReserva; }

    public String getHoraReserva() { return horaReserva; }
    public void setHoraReserva(String horaReserva) { this.horaReserva = horaReserva; }

    public Long getNumeroMesa() { return numeroMesa; }
    public void setNumeroMesa(Long numeroMesa) { this.numeroMesa = numeroMesa; }

    public Integer getNumeroPersonas() { return numeroPersonas; }
    public void setNumeroPersonas(Integer numeroPersonas) { this.numeroPersonas = numeroPersonas; }
}
