package com.alfredo.gestionreservas.controller;

import com.alfredo.gestionreservas.repository.ReservaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReservaController {

    @Autowired
    private ReservaRepository reservaRepository;
}
