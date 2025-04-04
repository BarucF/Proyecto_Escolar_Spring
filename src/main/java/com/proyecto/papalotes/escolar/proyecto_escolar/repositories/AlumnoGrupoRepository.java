package com.proyecto.papalotes.escolar.proyecto_escolar.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.proyecto.papalotes.escolar.proyecto_escolar.entities.AlumnoGrupo;

public interface AlumnoGrupoRepository extends JpaRepository<AlumnoGrupo, Long>  {
    List<AlumnoGrupo> findByGrupoId(Long id);
    Optional<AlumnoGrupo> findByAlumnoId(Long alumnoId);

}
