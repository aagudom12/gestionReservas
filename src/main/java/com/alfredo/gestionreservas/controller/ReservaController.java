package com.alfredo.gestionreservas.controller;

import com.alfredo.gestionreservas.DTO.ReservaDTO;
import com.alfredo.gestionreservas.entity.Cliente;
import com.alfredo.gestionreservas.entity.Mesa;
import com.alfredo.gestionreservas.entity.Reserva;
import com.alfredo.gestionreservas.entity.UserEntity;
import com.alfredo.gestionreservas.repository.ClienteRepository;
import com.alfredo.gestionreservas.repository.MesaRepository;
import com.alfredo.gestionreservas.repository.ReservaRepository;
import com.alfredo.gestionreservas.repository.UserEntityRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
public class ReservaController {

    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private UserEntityRepository userRepository;

    @Autowired
    private MesaRepository mesaRepository;

    @GetMapping("/reservas")
    public ResponseEntity<List<Reserva>> obtenerReservas(){
        var reservas = reservaRepository.findAll();
        return ResponseEntity.ok(reservas);
    }

    @PostMapping("/reservas")
    public ResponseEntity<?> crearReserva(@RequestBody @Valid Reserva reserva ){
        // Verificar que el cliente existe
        Cliente cliente = clienteRepository.findById(reserva.getCliente().getId())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        // Verificar que el cliente existe
        UserEntity usuario = userRepository.findById(reserva.getUsuario().getId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Verificar que la mesa existe
        Mesa mesa = mesaRepository.findById(reserva.getMesa().getId())
                .orElseThrow(() -> new RuntimeException("Mesa no encontrada"));

        boolean reservaExiste = reservaRepository.existsByMesaAndFechaAndHora(mesa, reserva.getFecha(), reserva.getHora());

        if (reservaExiste) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("La mesa ya está reservada en esa fecha y hora. Elige otro horario.");
        }

        // Asignar los objetos reales a la reserva
        reserva.setCliente(cliente);
        reserva.setMesa(mesa);
        reserva.setUsuario(usuario);

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

    @GetMapping("/reservas/{fecha}")
    public ResponseEntity<List<ReservaDTO>> obtenerReservasDeUnaFecha(@PathVariable String fecha){
        List<ReservaDTO> reservasDTO = new ArrayList<>();

        LocalDate fechaReserva = LocalDate.parse(fecha);

        reservaRepository.findByFecha(fechaReserva).forEach(reserva -> {
            reservasDTO.add(new ReservaDTO(reserva));
        });

        return ResponseEntity.ok(reservasDTO);
    }

    @GetMapping("/mis-reservas")
    public ResponseEntity<List<Reserva>> obtenerMisReservas(Authentication authentication) {
        String username = authentication.getName(); // Obtiene el username del usuario autenticado
        UserEntity usuario = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        System.out.println("Usuario encontrado en la BD: " + usuario.getUsername());

        List<Reserva> misReservas = reservaRepository.findByUsuario(usuario);
        System.out.println("Número de reservas encontradas: " + misReservas.size());
        return ResponseEntity.ok(misReservas);
    }

}
