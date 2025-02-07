package com.alfredo.gestionreservas.controller;

import com.alfredo.gestionreservas.DTO.LoginRequestDTO;
import com.alfredo.gestionreservas.DTO.LoginResponseDTO;
import com.alfredo.gestionreservas.DTO.UserRegisterDTO;
import com.alfredo.gestionreservas.config.JwtTokenProvider;
import com.alfredo.gestionreservas.entity.UserEntity;
import com.alfredo.gestionreservas.repository.UserEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
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

    @PostMapping("/auth/register")
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
    }

    @PostMapping("/auth/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO loginDTO) {
        try {

            //Validamos al usuario en Spring (hacemos login manualmente)
            UsernamePasswordAuthenticationToken userPassAuthToken = new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword());
            Authentication auth = authenticationManager.authenticate(userPassAuthToken);    //valida el usuario y devuelve un objeto Authentication con sus datos
            //Obtenemos el UserEntity del usuario logueado
            UserEntity user = (UserEntity) auth.getPrincipal();

            //Generamos un token con los datos del usuario (la clase tokenProvider ha hemos creado nosotros para no poner aquí todo el código
            String token = this.tokenProvider.generateToken(auth);

            //Devolvemos un código 200 con el username y token JWT
            return ResponseEntity.ok(new LoginResponseDTO(user.getUsername(), token));
        }catch (Exception e) {  //Si el usuario no es válido, salta una excepción BadCredentialsException
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    Map.of(
                            "path", "/auth/login",
                            "message", "Credenciales erróneas",
                            "timestamp", new Date()
                    )
            );
        }
    }
}
