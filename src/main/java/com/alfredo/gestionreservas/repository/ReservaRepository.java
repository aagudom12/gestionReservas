package com.alfredo.gestionreservas.repository;

import com.alfredo.gestionreservas.entity.Mesa;
import com.alfredo.gestionreservas.entity.Reserva;
import com.alfredo.gestionreservas.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {
    boolean existsByMesaAndFechaAndHora(Mesa mesa, LocalDate fecha, String hora);

    List<Reserva> findByFecha(LocalDate fecha);
    List<Reserva> findByUsuario(UserEntity usuario);
}
