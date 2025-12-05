package com.restaurante.reservas.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.restaurante.reservas.model.Cliente;
import com.restaurante.reservas.service.ClienteService;
import jakarta.validation.Valid;
/**
 *
 * @author STEVEN AF
 */
@RestController
@RequestMapping("/api/clientes")
public class ClienteController {
        @Autowired
	private ClienteService clienteService;

	// RF1: Registrar Cliente
	@PostMapping
	public ResponseEntity<Cliente> crearCliente(@Valid @RequestBody Cliente cliente) {
		Cliente nuevoCliente = clienteService.crearCliente(cliente);
		return new ResponseEntity<>(nuevoCliente, HttpStatus.CREATED);
	}

	// RF4: Listar Clientes
	@GetMapping
	public ResponseEntity<List<Cliente>> listarClientes() {
		List<Cliente> clientes = clienteService.listarClientes();
		return ResponseEntity.ok(clientes);
	}

	// RF4: Obtener Cliente por ID
	@GetMapping("/{id}")
	public ResponseEntity<Cliente> obtenerCliente(@PathVariable Long id) {
		return clienteService.obtenerClientePorId(id)
				.map(cliente -> ResponseEntity.ok(cliente))
				.orElse(ResponseEntity.notFound().build());
	}

	// RF5: Actualizar Cliente
	@PutMapping("/{id}")
	public ResponseEntity<Cliente> actualizarCliente(@PathVariable Long id, @Valid @RequestBody Cliente cliente) {
		Cliente clienteActualizado = clienteService.actualizarCliente(id, cliente);
		return ResponseEntity.ok(clienteActualizado);
	}

	// RF5: Eliminar Cliente
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> eliminarCliente(@PathVariable Long id) {
		clienteService.eliminarCliente(id);
		return ResponseEntity.noContent().build();
	}

	@ExceptionHandler(IllegalStateException.class)
	public ResponseEntity<String> manejarConflicto(IllegalStateException ex) {
		return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
	}

	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<String> manejarNoEncontrado(RuntimeException ex) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
	}
}

