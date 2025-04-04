package com.proyecto.papalotes.escolar.proyecto_escolar.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.proyecto.papalotes.escolar.proyecto_escolar.entities.AlumnoBoleta;

public interface AlumnoBoletaRepository extends JpaRepository<AlumnoBoleta, Long>  {

    List<AlumnoBoleta> findByBoletaId(Long id);

}
