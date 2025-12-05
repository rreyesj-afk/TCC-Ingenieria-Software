package com.restaurante.reservas.controller;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.restaurante.reservas.model.Reserva;
import com.restaurante.reservas.service.ReservaService;
/**
 *
 * @author STEVEN AF
 */
@RestController
@RequestMapping("/api/reservas")
public class ReservaController {

	@Autowired
	private ReservaService reservaService;

	// RF3: Registrar Reserva
	@PostMapping
	public ResponseEntity<Reserva> crearReserva(@RequestBody Reserva reserva) {
		Reserva nuevaReserva = reservaService.crearReserva(reserva);
		return new ResponseEntity<>(nuevaReserva, HttpStatus.CREATED);
	}

	// RF4: Listar Reservas
	@GetMapping
	public ResponseEntity<List<Reserva>> listarReservas() {
		List<Reserva> reservas = reservaService.listarReservas();
		return ResponseEntity.ok(reservas);
	}

	// RF4: Obtener Reserva por ID
	@GetMapping("/{id}")
	public ResponseEntity<Reserva> obtenerReserva(@PathVariable Long id) {
		return reservaService.obtenerReservaPorId(id)
				.map(reserva -> ResponseEntity.ok(reserva))
				.orElse(ResponseEntity.notFound().build());
	}

	// RF5: Actualizar Reserva
	@PutMapping("/{id}")
	public ResponseEntity<Reserva> actualizarReserva(@PathVariable Long id, @RequestBody Reserva reserva) {
		Reserva reservaActualizada = reservaService.actualizarReserva(id, reserva);
		return ResponseEntity.ok(reservaActualizada);
	}

	// RF5: Eliminar Reserva
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> eliminarReserva(@PathVariable Long id) {
		reservaService.eliminarReserva(id);
		return ResponseEntity.noContent().build();
	}

	// RF6: Consultar disponibilidad básica de mesas
	@GetMapping("/disponibilidad")
	public ResponseEntity<Map<String, Object>> verificarDisponibilidad(
			@RequestParam Long idMesa,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime hora) {
		
		boolean disponible = reservaService.verificarDisponibilidad(idMesa, fecha, hora);
		
		Map<String, Object> respuesta = new HashMap<>();
		respuesta.put("idMesa", idMesa);
		respuesta.put("fecha", fecha);
		respuesta.put("hora", hora);
		respuesta.put("disponible", disponible);
		
		return ResponseEntity.ok(respuesta);
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<Map<String, String>> manejarBadRequest(IllegalArgumentException ex) {
		Map<String, String> body = new HashMap<>();
		body.put("mensaje", ex.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
	}

	@ExceptionHandler(IllegalStateException.class)
	public ResponseEntity<Map<String, String>> manejarConflicto(IllegalStateException ex) {
		Map<String, String> body = new HashMap<>();
		body.put("mensaje", ex.getMessage());
		return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
	}

	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<Map<String, String>> manejarNoEncontrado(RuntimeException ex) {
		Map<String, String> body = new HashMap<>();
		body.put("mensaje", ex.getMessage());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
	}
}


