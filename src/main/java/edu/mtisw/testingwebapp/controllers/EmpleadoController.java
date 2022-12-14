package edu.mtisw.testingwebapp.controllers;

import edu.mtisw.testingwebapp.entities.EmpleadoEntity;
import edu.mtisw.testingwebapp.services.EmpleadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;

@Controller
@RequestMapping
public class EmpleadoController {
    @Autowired
	EmpleadoService empleadoService;

    @GetMapping("/listar")
	public String listar(Model model) {
    	ArrayList<EmpleadoEntity>empleados=empleadoService.obtenerEmpleados();
    	model.addAttribute("empleados",empleados);
		return "index";
	}

}