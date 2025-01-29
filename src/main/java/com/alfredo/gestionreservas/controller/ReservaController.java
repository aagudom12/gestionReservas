package com.alfredo.gestionreservas.controller;

import com.alfredo.gestionreservas.entity.Cliente;
import com.alfredo.gestionreservas.entity.Mesa;
import com.alfredo.gestionreservas.entity.Reserva;
import com.alfredo.gestionreservas.repository.ClienteRepository;
import com.alfredo.gestionreservas.repository.MesaRepository;
import com.alfredo.gestionreservas.repository.ReservaRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ReservaController {

    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private MesaRepository mesaRepository;

    @GetMapping("/reservas")
    public ResponseEntity<List<Reserva>> obtenerReservas(){
        var reservas = reservaRepository.findAll();
        return ResponseEntity.ok(reservas);
    }

    @PostMapping("/reservas")
    public ResponseEntity<Reserva> crearReserva(@RequestBody @Valid Reserva reserva ){
        // Verificar que el cliente existe
        Cliente cliente = clienteRepository.findById(reserva.getCliente().getId())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        // Verificar que la mesa existe
        Mesa mesa = mesaRepository.findById(reserva.getMesa().getId())
                .orElseThrow(() -> new RuntimeException("Mesa no encontrada"));

        // Asignar los objetos reales a la reserva
        reserva.setCliente(cliente);
        reserva.setMesa(mesa);

        // Guardar reserva
        Reserva nuevaReserva = reservaRepository.save(reserva);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaReserva);
    }

    @PutMapping("/reservas/{id}")
    public ResponseEntity<Reserva> editarReserva(@RequestBody @Valid Reserva nuevaReserva, @PathVariable Long id){
        return reservaRepository.findById(id)
                .map(reserva -> {
                    reserva.setFecha(nuevaReserva.getFecha());
                    reserva.setHora(nuevaReserva.getHora());
                    reserva.setNumeroPersonas(nuevaReserva.getNumeroPersonas());
                    return ResponseEntity.ok(reservaRepository.save(reserva));
                })
                .orElseGet(() -> {
                    return ResponseEntity.notFound().build();
                });
    }

    @DeleteMapping("/reservas/{id}")
    public ResponseEntity<?> eliminarReserva(@PathVariable Long id){
        return reservaRepository.findById(id)
                .map(reserva -> {
                    reservaRepository.delete(reserva);
                    return ResponseEntity.noContent().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

}
