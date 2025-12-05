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

import com.restaurante.reservas.model.Cliente;
import com.restaurante.reservas.service.ClienteService;
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
	public ResponseEntity<Cliente> crearCliente(@RequestBody Cliente cliente) {
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
	public ResponseEntity<Cliente> actualizarCliente(@PathVariable Long id, @RequestBody Cliente cliente) {
		try {
			Cliente clienteActualizado = clienteService.actualizarCliente(id, cliente);
			return ResponseEntity.ok(clienteActualizado);
		} catch (RuntimeException e) {
			return ResponseEntity.notFound().build();
		}
	}

	// RF5: Eliminar Cliente
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> eliminarCliente(@PathVariable Long id) {
		try {
			clienteService.eliminarCliente(id);
			return ResponseEntity.noContent().build();
		} catch (RuntimeException e) {
			return ResponseEntity.notFound().build();
		}
	}
}

