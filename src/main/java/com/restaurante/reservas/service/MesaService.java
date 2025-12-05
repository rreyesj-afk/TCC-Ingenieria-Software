/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.restaurante.reservas.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.restaurante.reservas.model.Mesa;
import com.restaurante.reservas.model.Reserva;
import com.restaurante.reservas.repository.MesaRepository;
import com.restaurante.reservas.repository.ReservaRepository;

@Service
@Transactional
public class MesaService {

    @Autowired
    private MesaRepository mesaRepository;

	@Autowired
	private ReservaRepository reservaRepository;

    // RF2: Registrar Mesa
    public Mesa crearMesa(Mesa mesa) {
        return mesaRepository.save(mesa);
    }

    // RF4: Listar Mesas
    public List<Mesa> listarMesas() {
        return mesaRepository.findAll();
    }

    // RF4: Obtener Mesa por ID
    public Optional<Mesa> obtenerMesaPorId(Long id) {
        return mesaRepository.findById(id);
    }

    // RF5: Actualizar Mesa
    public Mesa actualizarMesa(Long id, Mesa mesaActualizada) {
        Mesa mesa = mesaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Mesa no encontrada con id: " + id));
        mesa.setNumeroMesa(mesaActualizada.getNumeroMesa());
        mesa.setCapacidad(mesaActualizada.getCapacidad());
        mesa.setEstado(mesaActualizada.getEstado());
        return mesaRepository.save(mesa);
    }

    // RF5: Eliminar Mesa
    public void eliminarMesa(Long id) {
        if (!mesaRepository.existsById(id)) {
            throw new RuntimeException("Mesa no encontrada con id: " + id);
        }
		// Bloquear eliminación si tiene reservas activas
		boolean tieneReservas = reservaRepository.existsByMesaIdMesaAndEstado(id, Reserva.Estado.ACTIVA);
		if (tieneReservas) {
			throw new IllegalStateException("No se puede eliminar la mesa porque tiene reservas activas");
		}
        mesaRepository.deleteById(id);
    }
}