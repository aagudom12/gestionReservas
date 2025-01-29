package com.alfredo.gestionreservas.controller;

import com.alfredo.gestionreservas.entity.Mesa;
import com.alfredo.gestionreservas.entity.Reserva;
import com.alfredo.gestionreservas.repository.MesaRepository;
import com.alfredo.gestionreservas.repository.ReservaRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class MesaController {

    @Autowired
    private MesaRepository mesaRepository;

    @Autowired
    private ReservaRepository reservaRepository;

    @GetMapping("/mesas")
    public ResponseEntity<List<Mesa>> obtenerMesas(){
        var mesas = mesaRepository.findAll();
        return ResponseEntity.ok(mesas);
    }

    @PostMapping("/mesas")
    public ResponseEntity<Mesa> crearMesa(@RequestBody @Valid Mesa mesa ){
        var mesaGuardada = mesaRepository.save(mesa);
        return ResponseEntity.status(HttpStatus.CREATED).body(mesaGuardada);
    }

    @PutMapping("/mesas/{id}")
    public ResponseEntity<Mesa> editarMesa(@RequestBody @Valid Mesa nuevaMesa, @PathVariable Long id){
        return mesaRepository.findById(id)
                .map(mesa -> {
                    mesa.setNumeroMesa(nuevaMesa.getNumeroMesa());
                    mesa.setDescripcion(nuevaMesa.getDescripcion());
                    return ResponseEntity.ok(mesaRepository.save(mesa));
                })
                .orElseGet(() -> {
                    return ResponseEntity.notFound().build();
                });
    }

    @DeleteMapping("/mesas/{id}")
    public ResponseEntity<?> eliminarMesa(@PathVariable Long id){
        return mesaRepository.findById(id)
                .map(mesa -> {
                    mesaRepository.delete(mesa);
                    return ResponseEntity.noContent().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

    //PARA AÑADIR UNA RESERVA A UNA MESA
    @PostMapping("/mesas/{id}/reservas")
    public ResponseEntity<Mesa> hacerReserva(@PathVariable Long id, @RequestBody Reserva reserva){
        Optional<Mesa> mesa = mesaRepository.findById(id);
        Optional<Reserva> reservaBD = reservaRepository.findById(reserva.getId());
        if(mesa.isPresent() && reservaBD.isPresent()) {
            mesa.get().getReservas().add(reservaBD.get());
            mesaRepository.save(mesa.get());
            return ResponseEntity.ok(mesa.get());
        }
        return ResponseEntity.notFound().build();
    }
}
