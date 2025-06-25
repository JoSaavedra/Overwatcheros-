package tech.flow.software.entidades;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "asistencias")
public class Asistencia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(nullable = false)
    private LocalDateTime fechaHora;

    @Enumerated(EnumType.STRING)
    private TipoAsistencia tipo;

    private String observaciones;

    // Constructor vacío
    public Asistencia() {
        this.fechaHora = LocalDateTime.now();
    }

    // Constructor con parámetros
    public Asistencia(Usuario usuario, TipoAsistencia tipo, String observaciones) {
        this.usuario = usuario;
        this.tipo = tipo;
        this.observaciones = observaciones;
        this.fechaHora = LocalDateTime.now();
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public LocalDateTime getFechaHora() { return fechaHora; }
    public void setFechaHora(LocalDateTime fechaHora) { this.fechaHora = fechaHora; }

    public TipoAsistencia getTipo() { return tipo; }
    public void setTipo(TipoAsistencia tipo) { this.tipo = tipo; }

    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }

    public enum TipoAsistencia {
        ENTRADA, SALIDA
    }
}
