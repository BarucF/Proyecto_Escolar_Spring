package com.proyecto.papalotes.escolar.proyecto_escolar.repositories;
import org.springframework.data.jpa.repository.JpaRepository;

import com.proyecto.papalotes.escolar.proyecto_escolar.entities.Alumno;

public interface AlumnoRepository extends JpaRepository<Alumno, Long> {
    Alumno findByEmailAndPassword(String email, String password);
    //List<Alumno> findByGrupoId(Long id);
}
