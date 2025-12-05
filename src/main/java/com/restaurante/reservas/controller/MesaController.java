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

import com.restaurante.reservas.model.Mesa;
import com.restaurante.reservas.service.MesaService;
/**
 *
 * @author STEVEN AF
 */
@RestController
@RequestMapping("/api/mesas")
public class MesaController {

	@Autowired
	private MesaService mesaService;

	// RF2: Registrar Mesa
	@PostMapping
	public ResponseEntity<Mesa> crearMesa(@RequestBody Mesa mesa) {
		Mesa nuevaMesa = mesaService.crearMesa(mesa);
		return new ResponseEntity<>(nuevaMesa, HttpStatus.CREATED);
	}

	// RF4: Listar Mesas
	@GetMapping
	public ResponseEntity<List<Mesa>> listarMesas() {
		List<Mesa> mesas = mesaService.listarMesas();
		return ResponseEntity.ok(mesas);
	}

	// RF4: Obtener Mesa por ID
	@GetMapping("/{id}")
	public ResponseEntity<Mesa> obtenerMesa(@PathVariable Long id) {
		return mesaService.obtenerMesaPorId(id)
				.map(mesa -> ResponseEntity.ok(mesa))
				.orElse(ResponseEntity.notFound().build());
	}

	// RF5: Actualizar Mesa
	@PutMapping("/{id}")
	public ResponseEntity<Mesa> actualizarMesa(@PathVariable Long id, @RequestBody Mesa mesa) {
		try {
			Mesa mesaActualizada = mesaService.actualizarMesa(id, mesa);
			return ResponseEntity.ok(mesaActualizada);
		} catch (RuntimeException e) {
			return ResponseEntity.notFound().build();
		}
	}

	// RF5: Eliminar Mesa
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> eliminarMesa(@PathVariable Long id) {
		try {
			mesaService.eliminarMesa(id);
			return ResponseEntity.noContent().build();
		} catch (RuntimeException e) {
			return ResponseEntity.notFound().build();
		}
	}
}
