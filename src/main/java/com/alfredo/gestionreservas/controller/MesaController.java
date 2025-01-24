package com.alfredo.gestionreservas.controller;

import com.alfredo.gestionreservas.entity.Mesa;
import com.alfredo.gestionreservas.repository.MesaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class MesaController {

    @Autowired
    private MesaRepository mesaRepository;

    @GetMapping("/mesas")
    public ResponseEntity<List<Mesa>> obtenerMesas(){
        var mesas = mesaRepository.findAll();
        return ResponseEntity.ok(mesas);
    }

    @PostMapping("/mesas")
    public ResponseEntity<Mesa> crearMesa(@RequestBody Mesa mesa ){
        var mesaGuardada = mesaRepository.save(mesa);
        return ResponseEntity.status(HttpStatus.CREATED).body(mesaGuardada);
    }

    @PutMapping("/mesas/{id}")
    public ResponseEntity<Mesa> editarMesa(@RequestBody Mesa nuevaMesa, @PathVariable Long id){
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
}
