package com.restaurante.reservas.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.restaurante.reservas.model.Cliente;
import com.restaurante.reservas.model.Reserva;
import com.restaurante.reservas.repository.ClienteRepository;
import com.restaurante.reservas.repository.ReservaRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests Unitarios - ClienteService")
class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private ReservaRepository reservaRepository;

    @InjectMocks
    private ClienteService clienteService;

    private Cliente cliente;
    private Cliente clienteActualizado;

    @BeforeEach
    void setUp() {
        cliente = new Cliente("Juan Pérez", "555-1234");
        cliente.setIdCliente(1L);

        clienteActualizado = new Cliente("Juan Pérez Actualizado", "555-5678");
    }

    @Test
    @DisplayName("RF1: Debe crear un cliente exitosamente")
    void testCrearCliente() {
        // Given
        when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);

        // When
        Cliente resultado = clienteService.crearCliente(cliente);

        // Then
        assertNotNull(resultado);
        assertEquals("Juan Pérez", resultado.getNombre());
        assertEquals("555-1234", resultado.getTelefono());
        verify(clienteRepository, times(1)).save(cliente);
    }

    @Test
    @DisplayName("RF4: Debe listar todos los clientes")
    void testListarClientes() {
        // Given
        Cliente cliente2 = new Cliente("María García", "555-5678");
        List<Cliente> clientes = Arrays.asList(cliente, cliente2);
        when(clienteRepository.findAll()).thenReturn(clientes);

        // When
        List<Cliente> resultado = clienteService.listarClientes();

        // Then
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(clienteRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("RF4: Debe obtener un cliente por ID")
    void testObtenerClientePorId() {
        // Given
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));

        // When
        Optional<Cliente> resultado = clienteService.obtenerClientePorId(1L);

        // Then
        assertTrue(resultado.isPresent());
        assertEquals("Juan Pérez", resultado.get().getNombre());
        verify(clienteRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("RF4: Debe retornar Optional vacío cuando el cliente no existe")
    void testObtenerClientePorIdNoExiste() {
        // Given
        when(clienteRepository.findById(999L)).thenReturn(Optional.empty());

        // When
        Optional<Cliente> resultado = clienteService.obtenerClientePorId(999L);

        // Then
        assertFalse(resultado.isPresent());
        verify(clienteRepository, times(1)).findById(999L);
    }

    @Test
    @DisplayName("RF5: Debe actualizar un cliente existente")
    void testActualizarCliente() {
        // Given
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);

        // When
        Cliente resultado = clienteService.actualizarCliente(1L, clienteActualizado);

        // Then
        assertNotNull(resultado);
        verify(clienteRepository, times(1)).findById(1L);
        verify(clienteRepository, times(1)).save(cliente);
    }

    @Test
    @DisplayName("RF5: Debe lanzar excepción al actualizar cliente inexistente")
    void testActualizarClienteNoExiste() {
        // Given
        when(clienteRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            clienteService.actualizarCliente(999L, clienteActualizado);
        });

        assertEquals("Cliente no encontrado con id: 999", exception.getMessage());
        verify(clienteRepository, times(1)).findById(999L);
        verify(clienteRepository, never()).save(any());
    }

    @Test
    @DisplayName("RF5: Debe eliminar un cliente sin reservas activas")
    void testEliminarClienteSinReservas() {
        // Given
        when(clienteRepository.existsById(1L)).thenReturn(true);
        when(reservaRepository.existsByClienteIdClienteAndEstado(1L, Reserva.Estado.ACTIVA))
                .thenReturn(false);

        // When
        clienteService.eliminarCliente(1L);

        // Then
        verify(clienteRepository, times(1)).existsById(1L);
        verify(reservaRepository, times(1))
                .existsByClienteIdClienteAndEstado(1L, Reserva.Estado.ACTIVA);
        verify(clienteRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("RF5: Debe bloquear eliminación si tiene reservas activas")
    void testEliminarClienteConReservasActivas() {
        // Given
        when(clienteRepository.existsById(1L)).thenReturn(true);
        when(reservaRepository.existsByClienteIdClienteAndEstado(1L, Reserva.Estado.ACTIVA))
                .thenReturn(true);

        // When & Then
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            clienteService.eliminarCliente(1L);
        });

        assertEquals("No se puede eliminar el cliente porque tiene reservas activas",
                exception.getMessage());
        verify(clienteRepository, never()).deleteById(any());
    }

    @Test
    @DisplayName("RF5: Debe lanzar excepción al eliminar cliente inexistente")
    void testEliminarClienteNoExiste() {
        // Given
        when(clienteRepository.existsById(999L)).thenReturn(false);

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            clienteService.eliminarCliente(999L);
        });

        assertEquals("Cliente no encontrado con id: 999", exception.getMessage());
        verify(clienteRepository, never()).deleteById(any());
    }
}



