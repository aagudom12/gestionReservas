package com.alfredo.gestionreservas.services;

import com.alfredo.gestionreservas.repository.UserEntityRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class UserDetailsServiceImpl implements UserDetailsService {

    private UserEntityRepository userRepository;

    UserDetailsServiceImpl(UserEntityRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //Extrae el usuario de la BD
        return this.userRepository.findByUsername(username).orElseThrow(
                ()-> new UsernameNotFoundException(username+" no encontrado")
        );

    }
}
