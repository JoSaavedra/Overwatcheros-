package com.example.techflow.techflow.Service;
import com.example.techflow.techflow.model.Trabajador;
import java.util.List;
public interface TrabajadorService {
    List<Trabajador> getTrabajadores();
    Trabajador saveTrabajador(Trabajador trabajador);
    Trabajador getTrabajador(int id);
    Trabajador updateTrabajador(String rut, Trabajador trabajador);
    String deleteTrabajador(int id);

}
