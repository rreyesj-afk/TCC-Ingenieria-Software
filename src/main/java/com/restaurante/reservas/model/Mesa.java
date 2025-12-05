package com.restaurante.reservas.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "mesas")
public class Mesa {

	public enum Estado {
		DISPONIBLE,
		OCUPADA
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_mesa")
	private Long idMesa;

	@NotNull
	@Column(name = "numero_mesa", nullable = false, unique = true)
	private Integer numeroMesa;

	@NotNull
	@Min(1)
	@Max(20)
	@Column(nullable = false)
	private Integer capacidad;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 15)
	private Estado estado = Estado.DISPONIBLE;

	public Mesa() {
	}

	public Mesa(Integer numeroMesa, Integer capacidad, Estado estado) {
		this.numeroMesa = numeroMesa;
		this.capacidad = capacidad;
		this.estado = estado;
	}

	public Long getIdMesa() {
		return idMesa;
	}

	public void setIdMesa(Long idMesa) {
		this.idMesa = idMesa;
	}

	public Integer getNumeroMesa() {
		return numeroMesa;
	}

	public void setNumeroMesa(Integer numeroMesa) {
		this.numeroMesa = numeroMesa;
	}

	public Integer getCapacidad() {
		return capacidad;
	}

	public void setCapacidad(Integer capacidad) {
		this.capacidad = capacidad;
	}

	public Estado getEstado() {
		return estado;
	}

	public void setEstado(Estado estado) {
		this.estado = estado;
	}
}
