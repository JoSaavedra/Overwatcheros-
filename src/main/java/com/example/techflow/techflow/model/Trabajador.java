package com.example.techflow.techflow.model;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class Trabajador {
    private int id;
    private String nombre;
    private String apellido;
    private String rut;
}
