package com.proyecto.papalotes.escolar.proyecto_escolar.controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.proyecto.papalotes.escolar.proyecto_escolar.entities.Alumno;
import com.proyecto.papalotes.escolar.proyecto_escolar.entities.AlumnoBoleta;
import com.proyecto.papalotes.escolar.proyecto_escolar.entities.AlumnoGrupo;
import com.proyecto.papalotes.escolar.proyecto_escolar.entities.Boleta;
import com.proyecto.papalotes.escolar.proyecto_escolar.entities.Grupo;
import com.proyecto.papalotes.escolar.proyecto_escolar.entities.Materia;
import com.proyecto.papalotes.escolar.proyecto_escolar.entities.MateriaGrupo;
import com.proyecto.papalotes.escolar.proyecto_escolar.entities.Profesor;
import com.proyecto.papalotes.escolar.proyecto_escolar.repositories.AlumnoBoletaRepository;
import com.proyecto.papalotes.escolar.proyecto_escolar.repositories.AlumnoGrupoRepository;
import com.proyecto.papalotes.escolar.proyecto_escolar.repositories.AlumnoRepository;
import com.proyecto.papalotes.escolar.proyecto_escolar.repositories.BoletaRepository;
import com.proyecto.papalotes.escolar.proyecto_escolar.repositories.GrupoRepository;
import com.proyecto.papalotes.escolar.proyecto_escolar.repositories.MateriaGrupoRepository;
import com.proyecto.papalotes.escolar.proyecto_escolar.repositories.MateriaRepository;
import com.proyecto.papalotes.escolar.proyecto_escolar.repositories.ProfesorRepository;

@Controller
@RequestMapping("/profesor")
public class ProfesorController {

    @Autowired
    BoletaRepository boletaRepository;
    @Autowired
    AlumnoGrupoRepository alumnoGrupoRepository;

    @Autowired
    MateriaRepository materiaRepository;
    
    @Autowired
    private ProfesorRepository profesorRepository;
    
    @Autowired
    private GrupoRepository grupoRepository;
    
    @Autowired
    private AlumnoRepository alumnoRepository;
    
    @Autowired
    private MateriaGrupoRepository materiaGrupoRepository;
    
    @Autowired
    private AlumnoBoletaRepository alumnoBoletaRepository;

    @GetMapping("/dashboard/{id}")
    public String dashboard(@PathVariable Long id, Model model) {
        // Obtener el profesor
        Profesor profesor = profesorRepository.findById(id).orElseThrow();

        // Obtener los grupos asignados al profesor
        Optional<Grupo> grupo = grupoRepository.findByProfesorId(profesor.getId());
        List<Grupo> grupos= Arrays.asList(grupo.orElseThrow());
        model.addAttribute("profesor", profesor);
        model.addAttribute("grupos", grupos);

        return "profesor";
    }

    @GetMapping("/calificaciones/{grupoId}")
    public String calificarAlumnos(@PathVariable Long grupoId, Model model) {
        
        List<AlumnoGrupo> alumnoGrupos = alumnoGrupoRepository.findByGrupoId(grupoId);
        List<Alumno> alumnos = alumnoGrupos.stream()
                       .map(AlumnoGrupo::getAlumno)
                       .collect(Collectors.toList());
        model.addAttribute("alumnos", alumnos);

        // Obtener las materias del grupo
        List<MateriaGrupo> materiasAll = materiaGrupoRepository.findByGrupoId(grupoId);
        List<Materia> materias =materiasAll.stream().map(MateriaGrupo::getMateria).collect(Collectors.toList());
        model.addAttribute("materias", materias);

        return "calificaciones";
    }

    @PostMapping("/calificar")
    public String calificar(@RequestParam Long alumnoId, @RequestParam Long materiaId, @RequestParam Double calificacion) {
        Materia materia=materiaRepository.getById(materiaId);
        if(boletaRepository.findByAlumnoId(alumnoId).isEmpty()){
            Boleta boleta=new Boleta();
            boleta.setAlumno(alumnoRepository.getById(alumnoId));
            boletaRepository.save(boleta);
        }
        boletaRepository.findByAlumnoId(alumnoId).ifPresent(boleta ->{
            
                AlumnoBoleta calificacionBoleta = new AlumnoBoleta();
                calificacionBoleta.setBoleta(boleta);
                calificacionBoleta.setMateria(materia);
                calificacionBoleta.setCalificacion(calificacion);
                alumnoBoletaRepository.save(calificacionBoleta);
            
        });
        

        return "redirect:/profesor/dashboard/" + 1;
    }
}
