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

import com.restaurante.reservas.model.Mesa;
import com.restaurante.reservas.model.Mesa.Estado;
import com.restaurante.reservas.model.Reserva;
import com.restaurante.reservas.repository.MesaRepository;
import com.restaurante.reservas.repository.ReservaRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests Unitarios - MesaService")
class MesaServiceTest {

    @Mock
    private MesaRepository mesaRepository;

    @Mock
    private ReservaRepository reservaRepository;

    @InjectMocks
    private MesaService mesaService;

    private Mesa mesa;
    private Mesa mesaActualizada;

    @BeforeEach
    void setUp() {
        mesa = new Mesa(1, 4, Estado.DISPONIBLE);
        mesa.setIdMesa(1L);

        mesaActualizada = new Mesa(1, 6, Estado.OCUPADA);
    }

    @Test
    @DisplayName("RF2: Debe crear una mesa exitosamente")
    void testCrearMesa() {
        // Given
        when(mesaRepository.save(any(Mesa.class))).thenReturn(mesa);

        // When
        Mesa resultado = mesaService.crearMesa(mesa);

        // Then
        assertNotNull(resultado);
        assertEquals(1, resultado.getNumeroMesa());
        assertEquals(4, resultado.getCapacidad());
        assertEquals(Estado.DISPONIBLE, resultado.getEstado());
        verify(mesaRepository, times(1)).save(mesa);
    }

    @Test
    @DisplayName("RF4: Debe listar todas las mesas")
    void testListarMesas() {
        // Given
        Mesa mesa2 = new Mesa(2, 6, Estado.DISPONIBLE);
        List<Mesa> mesas = Arrays.asList(mesa, mesa2);
        when(mesaRepository.findAll()).thenReturn(mesas);

        // When
        List<Mesa> resultado = mesaService.listarMesas();

        // Then
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(mesaRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("RF4: Debe obtener una mesa por ID")
    void testObtenerMesaPorId() {
        // Given
        when(mesaRepository.findById(1L)).thenReturn(Optional.of(mesa));

        // When
        Optional<Mesa> resultado = mesaService.obtenerMesaPorId(1L);

        // Then
        assertTrue(resultado.isPresent());
        assertEquals(1, resultado.get().getNumeroMesa());
        verify(mesaRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("RF5: Debe actualizar una mesa existente")
    void testActualizarMesa() {
        // Given
        when(mesaRepository.findById(1L)).thenReturn(Optional.of(mesa));
        when(mesaRepository.save(any(Mesa.class))).thenReturn(mesa);

        // When
        Mesa resultado = mesaService.actualizarMesa(1L, mesaActualizada);

        // Then
        assertNotNull(resultado);
        verify(mesaRepository, times(1)).findById(1L);
        verify(mesaRepository, times(1)).save(mesa);
    }

    @Test
    @DisplayName("RF5: Debe eliminar una mesa sin reservas activas")
    void testEliminarMesaSinReservas() {
        // Given
        when(mesaRepository.existsById(1L)).thenReturn(true);
        when(reservaRepository.existsByMesaIdMesaAndEstado(1L, Reserva.Estado.ACTIVA))
                .thenReturn(false);

        // When
        mesaService.eliminarMesa(1L);

        // Then
        verify(mesaRepository, times(1)).existsById(1L);
        verify(reservaRepository, times(1))
                .existsByMesaIdMesaAndEstado(1L, Reserva.Estado.ACTIVA);
        verify(mesaRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("RF5: Debe bloquear eliminación si tiene reservas activas")
    void testEliminarMesaConReservasActivas() {
        // Given
        when(mesaRepository.existsById(1L)).thenReturn(true);
        when(reservaRepository.existsByMesaIdMesaAndEstado(1L, Reserva.Estado.ACTIVA))
                .thenReturn(true);

        // When & Then
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            mesaService.eliminarMesa(1L);
        });

        assertEquals("No se puede eliminar la mesa porque tiene reservas activas",
                exception.getMessage());
        verify(mesaRepository, never()).deleteById(any());
    }
}



