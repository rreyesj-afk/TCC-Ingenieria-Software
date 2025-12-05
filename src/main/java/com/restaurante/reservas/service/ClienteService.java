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

import com.restaurante.reservas.model.Cliente;
import com.restaurante.reservas.model.Reserva;
import com.restaurante.reservas.repository.ClienteRepository;
import com.restaurante.reservas.repository.ReservaRepository;

@Service
@Transactional
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

	@Autowired
	private ReservaRepository reservaRepository;

    // RF1: Registrar Cliente
    public Cliente crearCliente(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    // RF4: Listar Clientes
    public List<Cliente> listarClientes() {
        return clienteRepository.findAll();
    }

    // RF4: Obtener Cliente por ID
    public Optional<Cliente> obtenerClientePorId(Long id) {
        return clienteRepository.findById(id);
    }

    // RF5: Actualizar Cliente
    public Cliente actualizarCliente(Long id, Cliente clienteActualizado) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con id: " + id));
        cliente.setNombre(clienteActualizado.getNombre());
        cliente.setTelefono(clienteActualizado.getTelefono());
        return clienteRepository.save(cliente);
    }

    // RF5: Eliminar Cliente
    public void eliminarCliente(Long id) {
		if (!clienteRepository.existsById(id)) {
			throw new RuntimeException("Cliente no encontrado con id: " + id);
		}
		// Bloquear eliminación si tiene reservas activas
		boolean tieneReservas = reservaRepository.existsByClienteIdClienteAndEstado(id, Reserva.Estado.ACTIVA);
		if (tieneReservas) {
			throw new IllegalStateException("No se puede eliminar el cliente porque tiene reservas activas");
		}
		clienteRepository.deleteById(id);
    }
}