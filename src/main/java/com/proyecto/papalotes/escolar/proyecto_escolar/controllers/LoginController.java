package com.proyecto.papalotes.escolar.proyecto_escolar.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.proyecto.papalotes.escolar.proyecto_escolar.entities.Alumno;
import com.proyecto.papalotes.escolar.proyecto_escolar.entities.Profesor;
import com.proyecto.papalotes.escolar.proyecto_escolar.repositories.AlumnoRepository;
import com.proyecto.papalotes.escolar.proyecto_escolar.repositories.ProfesorRepository;

@Controller
public class LoginController {

    @Autowired
    private AlumnoRepository alumnoRepository;

    @Autowired
    private ProfesorRepository profesorRepository;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/login")
    public String authenticateUser(@RequestParam String username, @RequestParam String password, Model model) {

        if (username.toUpperCase().equals("ADMIN")&&password.equals("Contra123")) {
            return "redirect:/admin/dashboard"; 
        }
        Alumno alumno = alumnoRepository.findByEmailAndPassword(username, password);
        Profesor profesor = profesorRepository.findByEmailAndPassword(username, password);
        if (alumno != null) {
            return "redirect:/alumno/dashboard/"+ alumno.getId();
        }

        if (profesor != null) {
            return "redirect:/profesor/dashboard/"+ profesor.getId();
        }
        model.addAttribute("error", "Usuario o contraseña incorrectos");
        return "login"; 
    }

}