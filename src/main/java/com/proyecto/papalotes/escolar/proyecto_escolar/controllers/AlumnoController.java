package com.proyecto.papalotes.escolar.proyecto_escolar.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.proyecto.papalotes.escolar.proyecto_escolar.entities.Alumno;
import com.proyecto.papalotes.escolar.proyecto_escolar.entities.AlumnoBoleta;
import com.proyecto.papalotes.escolar.proyecto_escolar.entities.Boleta;
import com.proyecto.papalotes.escolar.proyecto_escolar.repositories.AlumnoBoletaRepository;
import com.proyecto.papalotes.escolar.proyecto_escolar.repositories.AlumnoRepository;
import com.proyecto.papalotes.escolar.proyecto_escolar.repositories.BoletaRepository;

@Controller
@RequestMapping("/alumno")
public class AlumnoController {

    @Autowired
    private BoletaRepository boletaRepository;
    
    @Autowired
    private AlumnoBoletaRepository alumnoBoletaRepository;
    
    @Autowired
    private AlumnoRepository alumnoRepository;

    @GetMapping("/dashboard/{id}")
    public String dashboard(@PathVariable Long id, Model model) {
        Alumno alumno = alumnoRepository.findById(id).orElseThrow();
        Boleta boleta = boletaRepository.findByAlumnoId(id).orElseThrow();
        List<AlumnoBoleta> calificaciones = alumnoBoletaRepository.findByBoletaId(boleta.getId());
        model.addAttribute("alumno", alumno);
        
        if (calificaciones.isEmpty()) {
            model.addAttribute("mensaje", "No tienes calificaciones registradas.");
        } else {
            model.addAttribute("calificaciones", calificaciones);
        }
        return "alumno";
    }
}