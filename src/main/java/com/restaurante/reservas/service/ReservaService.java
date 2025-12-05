/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.restaurante.reservas.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.restaurante.reservas.model.Cliente;
import com.restaurante.reservas.model.Mesa;
import com.restaurante.reservas.model.Reserva;
import com.restaurante.reservas.model.Reserva.Estado;
import com.restaurante.reservas.repository.ClienteRepository;
import com.restaurante.reservas.repository.MesaRepository;
import com.restaurante.reservas.repository.ReservaRepository;

@Service
@Transactional
public class ReservaService {

	@Autowired
	private ReservaRepository reservaRepository;

	@Autowired
	private ClienteRepository clienteRepository;

	@Autowired
	private MesaRepository mesaRepository;

	// RF3: Registrar Reserva
	public Reserva crearReserva(Reserva reserva) {
		if (reserva.getCliente() == null || reserva.getCliente().getIdCliente() == null) {
			throw new IllegalArgumentException("Debe indicar el id del cliente");
		}
		if (reserva.getMesa() == null || reserva.getMesa().getIdMesa() == null) {
			throw new IllegalArgumentException("Debe indicar el id de la mesa");
		}
		if (reserva.getFecha() == null || reserva.getHora() == null) {
			throw new IllegalArgumentException("Debe indicar fecha y hora");
		}

		// Validar que el cliente existe
		Cliente cliente = clienteRepository.findById(reserva.getCliente().getIdCliente())
				.orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

		// Validar que la mesa existe
		Mesa mesa = mesaRepository.findById(reserva.getMesa().getIdMesa())
				.orElseThrow(() -> new RuntimeException("Mesa no encontrada"));

		// Validar disponibilidad
		boolean disponible = verificarDisponibilidad(mesa.getIdMesa(), reserva.getFecha(), reserva.getHora());
		if (!disponible) {
			throw new IllegalStateException("La mesa ya está reservada en esa fecha y hora");
		}

		reserva.setCliente(cliente);
		reserva.setMesa(mesa);
		return reservaRepository.save(reserva);
	}

	// RF4: Listar Reservas
	public List<Reserva> listarReservas() {
		return reservaRepository.findAll();
	}

	// RF4: Obtener Reserva por ID
	public Optional<Reserva> obtenerReservaPorId(Long id) {
		return reservaRepository.findById(id);
	}

	// RF5: Actualizar Reserva
	public Reserva actualizarReserva(Long id, Reserva reservaActualizada) {
		Reserva reserva = reservaRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Reserva no encontrada con id: " + id));

		if (reservaActualizada.getCliente() != null) {
			Cliente cliente = clienteRepository.findById(reservaActualizada.getCliente().getIdCliente())
					.orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
			reserva.setCliente(cliente);
		}

		if (reservaActualizada.getMesa() != null) {
			Mesa mesa = mesaRepository.findById(reservaActualizada.getMesa().getIdMesa())
					.orElseThrow(() -> new RuntimeException("Mesa no encontrada"));
			reserva.setMesa(mesa);
		}

		if (reservaActualizada.getFecha() != null) {
			reserva.setFecha(reservaActualizada.getFecha());
		}

		if (reservaActualizada.getHora() != null) {
			reserva.setHora(reservaActualizada.getHora());
		}

		// Si cambiaron mesa/fecha/hora, validar disponibilidad
		if (reserva.getMesa() != null && reserva.getFecha() != null && reserva.getHora() != null) {
			boolean disponible = verificarDisponibilidad(reserva.getMesa().getIdMesa(), reserva.getFecha(), reserva.getHora());
			if (!disponible) {
				throw new IllegalStateException("La mesa ya está reservada en esa fecha y hora");
			}
		}

		if (reservaActualizada.getEstado() != null) {
			reserva.setEstado(reservaActualizada.getEstado());
		}

		return reservaRepository.save(reserva);
	}

	// RF5: Eliminar Reserva
	public void eliminarReserva(Long id) {
		if (!reservaRepository.existsById(id)) {
			throw new RuntimeException("Reserva no encontrada con id: " + id);
		}
		reservaRepository.deleteById(id);
	}

	// RF6: Consultar disponibilidad básica de mesas
	public boolean verificarDisponibilidad(Long idMesa, LocalDate fecha, LocalTime hora) {
		// Verifica si existe una reserva ACTIVA para esa mesa en esa fecha/hora
		boolean tieneReserva = reservaRepository.existsByMesaIdMesaAndFechaAndHoraAndEstado(
				idMesa, fecha, hora, Estado.ACTIVA);
		return !tieneReserva; // Retorna true si está disponible (no tiene reserva)
	}
        
}