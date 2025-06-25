package tech.flow.software.servicios;

import tech.flow.software.entidades.Usuario;
import tech.flow.software.entidades.Asistencia;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.flow.software.repositorios.AsistenciaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AsistenciaService {

    @Autowired
    private AsistenciaRepository asistenciaRepository;

    public Asistencia marcarAsistencia(Usuario usuario, Asistencia.TipoAsistencia tipo, String observaciones) {
        Asistencia asistencia = new Asistencia(usuario, tipo, observaciones);
        return asistenciaRepository.save(asistencia);
    }

    public List<Asistencia> obtenerAsistenciasPorUsuario(Usuario usuario) {
        return asistenciaRepository.findByUsuarioOrderByFechaHoraDesc(usuario);
    }

    public List<Asistencia> obtenerAsistenciasPorUsuarioYFecha(Usuario usuario, LocalDateTime fecha) {
        return asistenciaRepository.findByUsuarioAndFecha(usuario, fecha);
    }

    public Optional<Asistencia> obtenerUltimaAsistencia(Usuario usuario) {
        return asistenciaRepository.findTopByUsuarioOrderByFechaHoraDesc(usuario);
    }

    public Asistencia.TipoAsistencia obtenerProximoTipo(Usuario usuario) {
        Optional<Asistencia> ultimaAsistencia = obtenerUltimaAsistencia(usuario);
        if (ultimaAsistencia.isEmpty()) {
            return Asistencia.TipoAsistencia.ENTRADA;
        }
        return ultimaAsistencia.get().getTipo() == Asistencia.TipoAsistencia.ENTRADA ?
                Asistencia.TipoAsistencia.SALIDA : Asistencia.TipoAsistencia.ENTRADA;
    }
}
