package com.alfredo.gestionreservas.controller;

import com.alfredo.gestionreservas.DTO.LoginRequestDTO;
import com.alfredo.gestionreservas.DTO.LoginResponseDTO;
import com.alfredo.gestionreservas.DTO.UserRegisterDTO;
import com.alfredo.gestionreservas.config.JwtTokenProvider;
import com.alfredo.gestionreservas.entity.UserEntity;
import com.alfredo.gestionreservas.repository.UserEntityRepository;
import com.alfredo.gestionreservas.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
public class AuthController {
    @Autowired
    private UserEntityRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtTokenProvider tokenProvider;
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    /*@PostMapping("/auth/register")
    public ResponseEntity<Map<String, String>> save(@RequestBody UserRegisterDTO userDTO) {
        try {
            // Crear manualmente la entidad UserEntity
            UserEntity userEntity = new UserEntity();
            userEntity.setUsername(userDTO.getUsername());
            userEntity.setPassword(passwordEncoder.encode(userDTO.getPassword()));
            userEntity.setEmail(userDTO.getEmail());
            userEntity.setAuthorities(List.of("ROLE_USER", "ROLE_ADMIN"));

            // Guardar en la base de datos
            userEntity = this.userRepository.save(userEntity);

            // Responder con los datos del usuario creado
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    Map.of("email", userEntity.getEmail(),
                            "username", userEntity.getUsername())
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Email o username ya utilizado"));
        }
    }*/

    @PostMapping("/auth/register")
    public ResponseEntity<?> register(@RequestBody @Valid UserRegisterDTO userDTO) {
        try {
            UserEntity newUser = userService.registerUser(userDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    Map.of(
                            "message", "Usuario registrado con éxito",
                            "username", newUser.getUsername(),
                            "email", newUser.getEmail()
                    )
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/auth/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO loginDTO) {
        try {
            UsernamePasswordAuthenticationToken userPassAuthToken = new UsernamePasswordAuthenticationToken(
                    loginDTO.getUsername(), loginDTO.getPassword()
            );
            Authentication auth = authenticationManager.authenticate(userPassAuthToken);

            UserEntity user = (UserEntity) auth.getPrincipal();
            String token = this.tokenProvider.generateToken(auth);

            // Obtener el cliente ID y el nombre del cliente si el usuario tiene un cliente asociado
            Long clienteId = (user.getCliente() != null) ? user.getCliente().getId() : null;
            String clienteNombre = (user.getCliente() != null) ? user.getCliente().getNombre() : null; // Asumimos que tienes un método `getNombre` en Cliente

            // Devuelves también el clienteId y clienteNombre
            return ResponseEntity.ok(new LoginResponseDTO(user.getUsername(), token, clienteId, clienteNombre));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    Map.of(
                            "path", "/auth/login",
                            "message", "Credenciales erróneas",
                            "timestamp", new Date()
                    )
            );
        }
    }



    @GetMapping("/auth/user")
    public ResponseEntity<?> getUserInfo(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Usuario no autenticado"));
        }

        return ResponseEntity.ok(Map.of("username", authentication.getName()));
    }

}
