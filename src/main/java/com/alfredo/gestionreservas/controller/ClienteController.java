package com.alfredo.gestionreservas.controller;

import com.alfredo.gestionreservas.entity.Cliente;
import com.alfredo.gestionreservas.entity.UserEntity;
import com.alfredo.gestionreservas.repository.ClienteRepository;
import com.alfredo.gestionreservas.repository.UserEntityRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ClienteController {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private UserEntityRepository userEntityRepository;

    @GetMapping("/clientes")
    public ResponseEntity<List<Cliente>> obtenerClientes(){
        var clientes = clienteRepository.findAll();
        return ResponseEntity.ok(clientes);
    }

    @PostMapping("/clientes")
    public ResponseEntity<Cliente> crearCliente(@RequestBody @Valid Cliente cliente){
        var clienteGuardado = clienteRepository.save(cliente);
        return ResponseEntity.status(HttpStatus.CREATED).body(clienteGuardado);
    }

    @PutMapping("/clientes/{id}")
    public ResponseEntity<Cliente> actualizarCliente(@PathVariable Long id, @RequestBody @Valid Cliente nuevoCliente){
        return clienteRepository.findById(id)
                .map(cliente -> {
                    cliente.setNombre(nuevoCliente.getNombre());
                    cliente.setEmail(nuevoCliente.getEmail());
                    cliente.setTelefono(nuevoCliente.getTelefono());
                    return ResponseEntity.ok(clienteRepository.save(cliente));
                })
                .orElseGet(() -> {

                    return ResponseEntity.notFound().build();
                });
    }

    @DeleteMapping("/clientes/{id}")
    public ResponseEntity<?> eliminarCliente(@PathVariable Long id){
        return clienteRepository.findById(id)
                .map(cliente -> {
                    clienteRepository.delete(cliente);
                    return ResponseEntity.noContent().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/clientes/mi-cliente")
    public ResponseEntity<?> obtenerClienteId(@AuthenticationPrincipal UserEntity usuario) {
        if (usuario == null) {
            System.out.println("🔴 Usuario autenticado es NULL");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario no autenticado");
        }

        System.out.println("🟢 Usuario autenticado: " + usuario.getUsername());

        if (usuario.getCliente() == null) {
            System.out.println("🔴 Usuario no tiene un cliente asociado");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("El usuario no tiene un cliente asociado");
        }

        System.out.println("🟢 ID del cliente: " + usuario.getCliente().getId());
        return ResponseEntity.ok(usuario.getCliente().getId());
    }


}
