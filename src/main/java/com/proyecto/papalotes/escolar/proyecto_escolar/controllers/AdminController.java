package com.proyecto.papalotes.escolar.proyecto_escolar.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.proyecto.papalotes.escolar.proyecto_escolar.entities.Alumno;
import com.proyecto.papalotes.escolar.proyecto_escolar.entities.AlumnoGrupo;
import com.proyecto.papalotes.escolar.proyecto_escolar.entities.Grupo;
import com.proyecto.papalotes.escolar.proyecto_escolar.entities.Materia;
import com.proyecto.papalotes.escolar.proyecto_escolar.entities.MateriaGrupo;
import com.proyecto.papalotes.escolar.proyecto_escolar.entities.Profesor;
import com.proyecto.papalotes.escolar.proyecto_escolar.repositories.AlumnoGrupoRepository;
import com.proyecto.papalotes.escolar.proyecto_escolar.repositories.AlumnoRepository;
import com.proyecto.papalotes.escolar.proyecto_escolar.repositories.GrupoRepository;
import com.proyecto.papalotes.escolar.proyecto_escolar.repositories.MateriaGrupoRepository;
import com.proyecto.papalotes.escolar.proyecto_escolar.repositories.MateriaRepository;
import com.proyecto.papalotes.escolar.proyecto_escolar.repositories.ProfesorRepository;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    MateriaGrupoRepository materiaGrupoRepository;

    @Autowired
    private MateriaRepository materiaRepository;

    @Autowired
    private GrupoRepository grupoRepository;

    @Autowired
    private AlumnoRepository alumnoRepository;

    @Autowired
    private ProfesorRepository profesorRepository;

    @Autowired
    private AlumnoGrupoRepository alumnoGrupoRepository;

    @GetMapping("/dashboard")
    public String dashboard() {
        return "admin";
    }
    @GetMapping("/usuarios")
    public String roles(Model model) {
        //Grupos sin Profe
        List<Grupo> gruposSinProfesor = grupoRepository.findByProfesorIdIsNull();
        List<Profesor> profesores = profesorRepository.findAll();

        model.addAttribute("gruposSinProfesor", gruposSinProfesor);
        model.addAttribute("profesores", profesores);

        //Alumnos sin grupo
        List<Alumno> alumnos = alumnoRepository.findAll();
        List<AlumnoGrupo> alumnoGrupos = alumnoGrupoRepository.findAll();
        List<Alumno> alumnosNoAsignados = alumnos.stream()
            .filter(alumno -> alumnoGrupos.stream()
                .noneMatch(ag -> ag.getAlumno().equals(alumno)))
            .collect(Collectors.toList());
        List<Grupo> grupos = grupoRepository.findAll();

        // Pasar los datos al modelo
        model.addAttribute("alumnosNoAsignados", alumnosNoAsignados);
        model.addAttribute("grupos", grupos);

        return "usuarios";
    }
    @GetMapping("/asignacion")
    public String asignacion(Model model) {
        List<Grupo> grupos = grupoRepository.findAll();

        // Obtener todas las materias que no están asignadas a los grupos
        List<Materia> materias = materiaRepository.findAll();

        model.addAttribute("grupos", grupos);
        model.addAttribute("materias", materias);
        return "asignacion";
    }

    @GetMapping("/gestion")
    public String gestion(Model model) {
        model.addAttribute("profesores", profesorRepository.findAll());
        model.addAttribute("materias", materiaRepository.findAll());
        model.addAttribute("grupos", grupoRepository.findAll());
        model.addAttribute("alumnos", alumnoRepository.findAll());
        List<Grupo> todosLosGrupos = grupoRepository.findAll();

        // Filtrar los grupos que no tienen un profesor asignado (id_profesor == null)
        List<Grupo> gruposSinProfesor = todosLosGrupos.stream()
                .filter(grupo -> grupo.getProfesor() == null) // Filtra aquellos sin profesor
                .collect(Collectors.toList()); // Recolecta los resultados en una lista

        // Agregar los grupos sin profesor al modelo
        model.addAttribute("grupos2", gruposSinProfesor);

        return "gestion";
    }

    @PostMapping("/gestion/materia")
    public String agregarMateria(@RequestParam String nombre) {
        Materia materia = new Materia();
        materia.setNombre(nombre);
        materiaRepository.save(materia);
        return "redirect:/admin/gestion";
    }

    @PostMapping("/gestion/grupo")
    public String agregarGrupo(@RequestParam String nombre) {
        Grupo grupo = new Grupo();
        grupo.setNombre(nombre);
        grupoRepository.save(grupo);
        return "redirect:/admin/gestion";
    }

    @PostMapping("/gestion/cambiarGrupo")
    public String cambiarGrupo(@RequestParam Long alumnoId, @RequestParam Long nuevoGrupoId) {
        Alumno alumno = alumnoRepository.findById(alumnoId).orElseThrow();
        Grupo nuevoGrupo = grupoRepository.findById(nuevoGrupoId).orElseThrow();
        // Cambiar el grupo del alumno
        AlumnoGrupo nvoAlGrup= new AlumnoGrupo();
        nvoAlGrup.setAlumno(alumno);
        nvoAlGrup.setGrupo(nuevoGrupo);
        nvoAlGrup.setId(alumnoGrupoRepository.findByAlumnoId(alumnoId).orElseThrow().getId());
        alumnoGrupoRepository.save(nvoAlGrup);
        return "redirect:/admin/gestion";
    }

    @PostMapping("/gestion/cambiarProfesor")
    public String cambiarProfesor(@RequestParam Long grupoId, @RequestParam Long nuevoProfesorId) {
    Grupo grupo = new Grupo();
    Profesor nuevoProfesor = profesorRepository.findById(nuevoProfesorId).orElseThrow();
    grupo.setId(grupoRepository.findByProfesorId(nuevoProfesorId).orElseThrow().getId());
    grupo.setNombre(grupoRepository.findByProfesorId(nuevoProfesorId).orElseThrow().getNombre());
    grupoRepository.save(grupo);
    Grupo grupo2 = new Grupo();
    grupo2.setProfesor(nuevoProfesor);
    grupo2.setId(grupoId);
    grupo2.setNombre(grupoRepository.findById(grupoId).orElseThrow().getNombre());
    grupoRepository.save(grupo2);
    
    return "redirect:/admin/gestion";
    }

    @PostMapping("/usuarios/alumno")
    public String crearAlumno(@RequestParam String nombre, @RequestParam String email, @RequestParam String password) {
    Alumno alumno = new Alumno();
    alumno.setNombre(nombre);
    alumno.setEmail(email);
    alumno.setPassword(password); // Es recomendable encriptar la contraseña antes de guardarla
    alumnoRepository.save(alumno);
    return "redirect:/admin/usuarios";
    }

    @PostMapping("/usuarios/profesor")
    public String crearProfesor(@RequestParam String nombre, @RequestParam String email, @RequestParam String password) {
        Profesor profesor = new Profesor();
        profesor.setNombre(nombre);
        profesor.setEmail(email);
        profesor.setPassword(password); // También es recomendable encriptar la contraseña
        profesorRepository.save(profesor);
        return "redirect:/admin/usuarios";
    }

    @PostMapping("/asignar-profesor-a-grupo")
    public String asignarProfesorAGrupo(@RequestParam Long profesorId, @RequestParam Long grupoId) {
        Profesor profesor = profesorRepository.findById(profesorId).orElseThrow();
        Grupo grupo = grupoRepository.findById(grupoId).orElseThrow();

        // Asignar el profesor al grupo
        grupo.setProfesor(profesor);
        grupoRepository.save(grupo);
        
        return "redirect:/admin/usuarios"; // Redirigir a la página de gestión
    }

    @PostMapping("/asignar-alumno-a-grupo")
    public String asignarAlumnoAGrupo(@RequestParam Long alumnoId, @RequestParam Long grupoId) {
        Alumno alumno = alumnoRepository.findById(alumnoId).orElseThrow();
        Grupo grupo = grupoRepository.findById(grupoId).orElseThrow();

        // Asignar el alumno al grupo
        AlumnoGrupo alumnoGrupo = new AlumnoGrupo();
        alumnoGrupo.setAlumno(alumno);
        alumnoGrupo.setGrupo(grupo);
        alumnoGrupoRepository.save(alumnoGrupo);

        return "redirect:/admin/usuarios"; // Redirigir a la página de gestión
    }

    @PostMapping("/asignar-materia-a-grupo")
    public String asignarMateriaAGrupo(@RequestParam Long grupoId, @RequestParam Long materiaId) {
        // Verificar si la materia ya está asignada al grupo
        if (materiaGrupoRepository.existsByGrupoIdAndMateriaId(grupoId, materiaId)) {
            // Si ya está asignada, redirigir con un mensaje de error
            return "redirect:/admin/asignacion?error=Materia%20ya%20asignada%20a%20este%20grupo";
        }
        
        // Obtener el grupo y la materia
        Grupo grupo = grupoRepository.findById(grupoId).orElseThrow();
        Materia materia = materiaRepository.findById(materiaId).orElseThrow();

        // Asignar la materia al grupo
        MateriaGrupo materiaGrupo = new MateriaGrupo();
        materiaGrupo.setGrupo(grupo);
        materiaGrupo.setMateria(materia);
        materiaGrupoRepository.save(materiaGrupo);

        return "redirect:/admin/asignacion"; // Redirigir después de la asignación
    }
}
