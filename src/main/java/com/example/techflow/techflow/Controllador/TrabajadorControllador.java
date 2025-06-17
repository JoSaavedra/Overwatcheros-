package com.example.techflow.techflow.Controllador;

import com.example.techflow.techflow.model.Trabajador;
import com.example.techflow.techflow.Service.TrabajadorService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/trabajador")
public class TrabajadorControllador {
    @Autowired
    private TrabajadorService trabajadorService;

    @GetMapping("/test")
    public String test(){return "test";}

    @GetMapping
    public List<Trabajador> getTrabajadores(){return trabajadorService.getTrabajadores();}

    @PostMapping

    public Trabajador saveTrabajador(@RequestBody Trabajador trabajador){return trabajadorService.saveTrabajador(trabajador);}

    @GetMapping("{id}")
    public Trabajador getTrabajador(@PathVariable int id){return trabajadorService.getTrabajador(id);}

    @PutMapping("{id}")
    public Trabajador updateTrabajador(@PathVariable int id, @RequestBody Trabajador trabajador){return  trabajadorService.updateTrabajador(String.valueOf(id), trabajador);}

    @DeleteMapping
    public String deleteTrabajador(@PathVariable int id){return trabajadorService.deleteTrabajador(id);}
}
