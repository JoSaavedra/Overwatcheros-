package tech.flow.software.repositorios;

import tech.flow.software.entidades.Usuario;
import tech.flow.software.entidades.Asistencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AsistenciaRepository extends JpaRepository<Asistencia, Long> {
    List<Asistencia> findByUsuarioOrderByFechaHoraDesc(Usuario usuario);

    @Query("SELECT a FROM Asistencia a WHERE a.usuario = :usuario AND DATE(a.fechaHora) = DATE(:fecha)")
    List<Asistencia> findByUsuarioAndFecha(@Param("usuario") Usuario usuario, @Param("fecha") LocalDateTime fecha);

    Optional<Asistencia> findTopByUsuarioOrderByFechaHoraDesc(Usuario usuario);
}
