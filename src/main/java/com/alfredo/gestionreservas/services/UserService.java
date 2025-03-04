package com.alfredo.gestionreservas.services;

import com.alfredo.gestionreservas.DTO.UserRegisterDTO;
import com.alfredo.gestionreservas.entity.Cliente;
import com.alfredo.gestionreservas.entity.UserEntity;
import com.alfredo.gestionreservas.repository.ClienteRepository;
import com.alfredo.gestionreservas.repository.UserEntityRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserEntityRepository userRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserEntity registerUser(UserRegisterDTO userDTO) {
        // Verificamos si el nombre de usuario ya existe
        if (userRepository.existsByUsername(userDTO.getUsername())) {
            throw new RuntimeException("El nombre de usuario ya está en uso");
        }

        // Verificamos si el email ya está registrado
        if (userRepository.existsByEmail(userDTO.getEmail())) {
            throw new RuntimeException("El email ya está registrado");
        }

        // Crear Cliente
        Cliente cliente = new Cliente();
        cliente.setNombre(userDTO.getNombre());  // Asignamos el nombre del cliente
        cliente.setEmail(userDTO.getEmail());  // Asignamos el email del cliente
        cliente.setTelefono(userDTO.getTelefono() != null ? userDTO.getTelefono() : "No disponible");  // Si no hay teléfono, asignamos un valor por defecto

        // Guardamos el Cliente
        clienteRepository.save(cliente);

        // Crear Usuario y asociar al Cliente
        UserEntity user = new UserEntity();
        user.setUsername(userDTO.getUsername());  // Asignamos el username del usuario
        user.setEmail(userDTO.getEmail());  // Asignamos el email del usuario
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));  // Encriptamos la contraseña antes de guardarla
        user.setCliente(cliente);  // Asociamos el cliente al usuario
        user.setAuthorities(List.of("ROLE_USER"));  // Asignamos los roles al usuario

        // Guardamos el Usuario
        return userRepository.save(user);
    }
}


