package com.restaurante.reservas.repository;

import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.restaurante.reservas.model.Reserva;
import com.restaurante.reservas.model.Reserva.Estado;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {
    
    // RF6: Verificar si una mesa tiene reserva en una fecha/hora específica
    boolean existsByMesaIdMesaAndFechaAndHoraAndEstado(Long idMesa, LocalDate fecha, LocalTime hora, Estado estado);
}