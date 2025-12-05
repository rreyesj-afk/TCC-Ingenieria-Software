package com.restaurante.reservas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.restaurante.reservas.model.Cliente;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
}