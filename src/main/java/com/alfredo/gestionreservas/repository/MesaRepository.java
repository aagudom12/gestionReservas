package com.alfredo.gestionreservas.repository;

import com.alfredo.gestionreservas.entity.Mesa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MesaRepository extends JpaRepository<Mesa, Long> {
    @Query("SELECT m FROM Mesa m WHERE m.id NOT IN (SELECT r.mesa.id FROM Reserva r WHERE r.fecha = :fecha AND r.hora = :hora)")
    List<Mesa> findMesasDisponibles(@Param("fecha") LocalDate fecha, @Param("hora") String hora);
}
