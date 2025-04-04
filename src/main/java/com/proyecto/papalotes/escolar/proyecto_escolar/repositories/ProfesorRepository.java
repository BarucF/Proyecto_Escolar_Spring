package com.proyecto.papalotes.escolar.proyecto_escolar.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.proyecto.papalotes.escolar.proyecto_escolar.entities.Profesor;

public interface ProfesorRepository extends JpaRepository<Profesor, Long> {
    Profesor findByEmailAndPassword(String email, String password);
}
