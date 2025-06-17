package com.example.techflow.techflow.Service;

import java.util.ArrayList;
import java.util.List;

import com.example.techflow.techflow.model.Trabajador;
import org.springframework.stereotype.Service;

@Service
public class TrabajadorServiceImp implements TrabajadorService {

    private List<Trabajador> trabajadores = new ArrayList<>();

    @Override
    public List<Trabajador> getTrabajadores() {
        return trabajadores;
    }

    @Override
    public Trabajador saveTrabajador(Trabajador trabajador) {
        trabajadores.add(trabajador);
        return trabajador;
    }

    @Override
    public Trabajador getTrabajador(int id) {
        return trabajadores.stream().filter(x -> x.getId() == id).findFirst().orElse(null);
    }

    @Override
    public Trabajador updateTrabajador(String rut, Trabajador trabajador) {
        Trabajador existente = getTrabajador(trabajador.getId());
        if (existente != null) {
            existente.setNombre(trabajador.getNombre());
            existente.setApellido(trabajador.getApellido());
            existente.setRut(rut);
        }

        return existente;
    }

    @Override
    public String deleteTrabajador(int id) {
        Trabajador trabajador = getTrabajador(id);
        if (trabajador != null) {
            trabajadores.remove(trabajador);
            return "Usuario eliminado";
        }
        return "Usuario no encontrado";
    }
}

