package com.proyecto.papalotes.escolar.proyecto_escolar.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.proyecto.papalotes.escolar.proyecto_escolar.entities.MateriaGrupo;

public interface MateriaGrupoRepository extends JpaRepository<MateriaGrupo, Long> {
    List<MateriaGrupo> findByGrupoId(Long id);
    boolean existsByGrupoIdAndMateriaId(Long grupoId, Long materiaId);
    
}
