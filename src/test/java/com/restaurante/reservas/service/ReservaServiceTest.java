package com.restaurante.reservas.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.restaurante.reservas.model.Cliente;
import com.restaurante.reservas.model.Mesa;
import com.restaurante.reservas.model.Reserva;
import com.restaurante.reservas.repository.ClienteRepository;
import com.restaurante.reservas.repository.MesaRepository;
import com.restaurante.reservas.repository.ReservaRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests Unitarios - ReservaService")
class ReservaServiceTest {

    @Mock
    private ReservaRepository reservaRepository;

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private MesaRepository mesaRepository;

    @InjectMocks
    private ReservaService reservaService;

    private Cliente cliente;
    private Mesa mesa;
    private Reserva reserva;

    @BeforeEach
    void setUp() {
        cliente = new Cliente("Juan Pérez", "555-1234");
        cliente.setIdCliente(1L);

        mesa = new Mesa(1, 4, Mesa.Estado.DISPONIBLE);
        mesa.setIdMesa(1L);

        reserva = new Reserva(
                LocalDate.of(2025, 12, 10),
                LocalTime.of(19, 0),
                cliente,
                mesa,
                Reserva.Estado.ACTIVA
        );
        reserva.setIdReserva(1L);
    }

    @Test
    @DisplayName("RF3: Debe crear una reserva exitosamente")
    void testCrearReserva() {
        // Given
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(mesaRepository.findById(1L)).thenReturn(Optional.of(mesa));
        when(reservaRepository.existsByMesaIdMesaAndFechaAndHoraAndEstado(
                eq(1L), any(LocalDate.class), any(LocalTime.class), eq(Reserva.Estado.ACTIVA)))
                .thenReturn(false);
        when(reservaRepository.save(any(Reserva.class))).thenReturn(reserva);

        // When
        Reserva resultado = reservaService.crearReserva(reserva);

        // Then
        assertNotNull(resultado);
        verify(clienteRepository, times(1)).findById(1L);
        verify(mesaRepository, times(1)).findById(1L);
        verify(reservaRepository, times(1)).save(reserva);
    }

    @Test
    @DisplayName("RF3: Debe lanzar excepción si el cliente no existe")
    void testCrearReservaClienteNoExiste() {
        // Given
        when(clienteRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            reserva.getCliente().setIdCliente(999L);
            reservaService.crearReserva(reserva);
        });

        assertEquals("Cliente no encontrado", exception.getMessage());
        verify(reservaRepository, never()).save(any());
    }

    @Test
    @DisplayName("RF3: Debe lanzar excepción si la mesa no existe")
    void testCrearReservaMesaNoExiste() {
        // Given
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(mesaRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            reserva.getMesa().setIdMesa(999L);
            reservaService.crearReserva(reserva);
        });

        assertEquals("Mesa no encontrada", exception.getMessage());
        verify(reservaRepository, never()).save(any());
    }

    @Test
    @DisplayName("RF3: Debe lanzar excepción si la mesa ya está reservada")
    void testCrearReservaMesaOcupada() {
        // Given
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(mesaRepository.findById(1L)).thenReturn(Optional.of(mesa));
        when(reservaRepository.existsByMesaIdMesaAndFechaAndHoraAndEstado(
                eq(1L), any(LocalDate.class), any(LocalTime.class), eq(Reserva.Estado.ACTIVA)))
                .thenReturn(true);

        // When & Then
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            reservaService.crearReserva(reserva);
        });

        assertEquals("La mesa ya está reservada en esa fecha y hora", exception.getMessage());
        verify(reservaRepository, never()).save(any());
    }

    @Test
    @DisplayName("RF6: Debe verificar disponibilidad correctamente")
    void testVerificarDisponibilidad() {
        // Given
        LocalDate fecha = LocalDate.of(2025, 12, 10);
        LocalTime hora = LocalTime.of(19, 0);
        when(reservaRepository.existsByMesaIdMesaAndFechaAndHoraAndEstado(
                1L, fecha, hora, Reserva.Estado.ACTIVA)).thenReturn(false);

        // When
        boolean disponible = reservaService.verificarDisponibilidad(1L, fecha, hora);

        // Then
        assertTrue(disponible);
        verify(reservaRepository, times(1))
                .existsByMesaIdMesaAndFechaAndHoraAndEstado(1L, fecha, hora, Reserva.Estado.ACTIVA);
    }

    @Test
    @DisplayName("RF6: Debe retornar false si la mesa no está disponible")
    void testVerificarDisponibilidadNoDisponible() {
        // Given
        LocalDate fecha = LocalDate.of(2025, 12, 10);
        LocalTime hora = LocalTime.of(19, 0);
        when(reservaRepository.existsByMesaIdMesaAndFechaAndHoraAndEstado(
                1L, fecha, hora, Reserva.Estado.ACTIVA)).thenReturn(true);

        // When
        boolean disponible = reservaService.verificarDisponibilidad(1L, fecha, hora);

        // Then
        assertFalse(disponible);
    }
}

