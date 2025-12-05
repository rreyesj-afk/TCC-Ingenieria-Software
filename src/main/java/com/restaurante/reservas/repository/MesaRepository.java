package com.restaurante.reservas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.restaurante.reservas.model.Mesa;

@Repository
public interface MesaRepository extends JpaRepository<Mesa, Long> {
}