package tech.flow.software.controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.servlet.http.HttpSession;
import tech.flow.software.entidades.Asistencia;
import tech.flow.software.entidades.Usuario;
import tech.flow.software.servicios.AsistenciaService;
import tech.flow.software.servicios.UsuarioService;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Controller
public class AttendanceController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private AsistenciaService asistenciaService;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/registro")
    public String mostrarRegistro(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "registro";
    }

    @PostMapping("/registro")
    public String procesarRegistro(@ModelAttribute Usuario usuario, RedirectAttributes redirectAttributes) {
        try {
            usuarioService.registrarUsuario(usuario);
            redirectAttributes.addFlashAttribute("mensaje", "Usuario registrado exitosamente");
            return "redirect:/login";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/registro";
        }
    }

    @GetMapping("/login")
    public String mostrarLogin() {
        return "login";
    }

    @PostMapping("/login")
    public String procesarLogin(@RequestParam String email, @RequestParam String password,
                                HttpSession session, RedirectAttributes redirectAttributes) {
        try {
            Optional<Usuario> usuarioOpt = usuarioService.autenticarUsuario(email, password);
            if (usuarioOpt.isPresent()) {
                Usuario usuario = usuarioOpt.get();
                session.setAttribute("usuario", usuario);
                System.out.println("Usuario autenticado: " + usuario.getNombre() + " - ID: " + usuario.getId());
                return "redirect:/dashboard";
            } else {
                redirectAttributes.addFlashAttribute("error", "Credenciales inválidas");
                return "redirect:/login";
            }
        } catch (Exception e) {
            System.err.println("Error en login: " + e.getMessage());
            redirectAttributes.addFlashAttribute("error", "Error al procesar el login");
            return "redirect:/login";
        }
    }

    @GetMapping("/dashboard")
    public String mostrarDashboard(HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        try {
            Usuario usuario = (Usuario) session.getAttribute("usuario");
            if (usuario == null) {
                System.out.println("Usuario no encontrado en sesión, redirigiendo a login");
                return "redirect:/login";
            }

            System.out.println("Cargando dashboard para usuario: " + usuario.getNombre() + " - ID: " + usuario.getId());

            // Obtener asistencias del usuario
            List<Asistencia> asistencias = null;
            try {
                asistencias = asistenciaService.obtenerAsistenciasPorUsuario(usuario);
                System.out.println("Asistencias obtenidas: " + (asistencias != null ? asistencias.size() : "null"));
            } catch (Exception e) {
                System.err.println("Error al obtener asistencias: " + e.getMessage());
                // Continuar con lista vacía en caso de error
            }

            // Obtener próximo tipo de asistencia
            Asistencia.TipoAsistencia proximoTipo = null;
            try {
                proximoTipo = asistenciaService.obtenerProximoTipo(usuario);
                System.out.println("Próximo tipo calculado: " + proximoTipo);
            } catch (Exception e) {
                System.err.println("Error al obtener próximo tipo: " + e.getMessage());
                // Por defecto será ENTRADA
                proximoTipo = Asistencia.TipoAsistencia.ENTRADA;
            }

            // Si proximoTipo es null, establecer ENTRADA como default
            if (proximoTipo == null) {
                proximoTipo = Asistencia.TipoAsistencia.ENTRADA;
            }

            // Agregar atributos al modelo
            model.addAttribute("usuario", usuario);
            model.addAttribute("asistencias", asistencias);
            model.addAttribute("proximoTipo", proximoTipo);

            System.out.println("Modelo preparado, renderizando dashboard");
            return "dashboard";

        } catch (Exception e) {
            System.err.println("Error general en dashboard: " + e.getMessage());
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Error al cargar el dashboard");
            return "redirect:/login";
        }
    }

    @PostMapping("/marcar-asistencia")
    public String marcarAsistencia(@RequestParam String tipo,
                                   @RequestParam(required = false) String observaciones,
                                   HttpSession session, RedirectAttributes redirectAttributes) {
        try {
            Usuario usuario = (Usuario) session.getAttribute("usuario");
            if (usuario == null) {
                return "redirect:/login";
            }

            System.out.println("Marcando asistencia - Usuario: " + usuario.getNombre() + ", Tipo: " + tipo);

            // Convertir string a enum
            Asistencia.TipoAsistencia tipoEnum = Asistencia.TipoAsistencia.valueOf(tipo.toUpperCase());

            asistenciaService.marcarAsistencia(usuario, tipoEnum, observaciones);
            redirectAttributes.addFlashAttribute("mensaje",
                    "Asistencia de " + tipoEnum.toString().toLowerCase() + " marcada exitosamente");

        } catch (IllegalArgumentException e) {
            System.err.println("Error: Tipo de asistencia inválido - " + tipo);
            redirectAttributes.addFlashAttribute("error", "Tipo de asistencia inválido");
        } catch (Exception e) {
            System.err.println("Error al marcar asistencia: " + e.getMessage());
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Error al marcar asistencia: " + e.getMessage());
        }

        return "redirect:/dashboard";
    }

    @GetMapping("/horario")
    public String mostrarHorario(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) {
            return "redirect:/login";
        }

        // Horario ficticio
        String[] dias = {"Lunes", "Martes", "Miércoles", "Jueves", "Viernes"};
        String horaEntrada = "08:00";
        String horaSalida = "17:00";

        model.addAttribute("usuario", usuario);
        model.addAttribute("dias", dias);
        model.addAttribute("horaEntrada", horaEntrada);
        model.addAttribute("horaSalida", horaSalida);

        return "horario";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session, RedirectAttributes redirectAttributes) {
        try {
            session.invalidate();
            redirectAttributes.addFlashAttribute("mensaje", "Sesión cerrada exitosamente");
        } catch (Exception e) {
            System.err.println("Error al cerrar sesión: " + e.getMessage());
        }
        return "redirect:/";
    }
}