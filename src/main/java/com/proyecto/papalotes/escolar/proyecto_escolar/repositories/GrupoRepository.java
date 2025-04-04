package com.proyecto.papalotes.escolar.proyecto_escolar.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.proyecto.papalotes.escolar.proyecto_escolar.entities.Grupo;

public interface GrupoRepository extends JpaRepository<Grupo, Long>  {
    Optional<Grupo> findByProfesorId(Long id);
    List<Grupo> findByProfesorIdIsNull();

}
