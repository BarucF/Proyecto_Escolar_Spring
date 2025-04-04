package com.proyecto.papalotes.escolar.proyecto_escolar.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.proyecto.papalotes.escolar.proyecto_escolar.entities.Boleta;

public interface BoletaRepository extends JpaRepository<Boleta, Long>   {
    Optional<Boleta> findByAlumnoId(Long id);
}
