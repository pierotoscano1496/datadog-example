package com.principal.datadogexample;

import com.principal.datadogexample.models.SolicitudFinanciamiento;
import com.principal.datadogexample.models.Usuario;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.nio.file.Paths;
import java.util.Date;
import java.util.HashMap;

@RestController
@RequestMapping("/")
public class MainController {
    Logger logger = LogManager.getLogger(MainController.class.getName());

    @GetMapping("/principal")
    public ModelAndView principal() {
        ModelAndView modelAndView = new ModelAndView("principal");
        modelAndView.addObject("message", "Hello");
        return modelAndView;
    }

    @GetMapping("/info")
    public HashMap<String, String> showInfo() {
        logger.trace("Un simple rastreo");
        logger.debug("Un simple debug");
        logger.info("Un simple info");
        logger.warn("Una simple alerta");
        logger.error("Un simple error");

        HashMap<String, String> obj = new HashMap<>();
        obj.put("message", getParentDirectoryFromJar());

        return obj;
    }

    @GetMapping("/obtener/:dni")
    public Usuario obtener(@RequestParam String dni) {
        Usuario usuario = new Usuario();
        usuario.setId(1);
        usuario.setDni(dni);
        usuario.setNombres("Juan");
        usuario.setApellidos("Perez");

        logger.trace("usuario=" + usuario.getDni() + " action=obtener usuario fecha=" + new Date());

        return usuario;
    }

    @PostMapping("/registrar-solicitud-financiamiento")
    public HashMap<String, String> registrarSoliFinanciamiento(@RequestBody SolicitudFinanciamiento solicitudFinanciamiento) {
        String dni = solicitudFinanciamiento.getDni();
        int monto = solicitudFinanciamiento.getMonto();
        int numCuotas = solicitudFinanciamiento.getCuotas();

        Usuario usuario = obtener(dni);

        logger.info("usuario=" + usuario.getId() + " action=registrar-solicitud-prestamo" + " monto=" + monto);

        HashMap<String, String> obj = new HashMap<>();
        obj.put("numCuotas", String.valueOf(numCuotas));
        obj.put("cuota", String.valueOf(monto / numCuotas));

        return obj;
    }

    public String getParentDirectoryFromJar() {
        String dirtyPath = getClass().getResource("").toString();
        String jarPath = dirtyPath.replaceAll("^.*file:/", ""); //removes file:/ and everything before it
        jarPath = jarPath.replaceAll("jar!.*", "jar"); //removes everything after .jar, if .jar exists in dirtyPath
        jarPath = jarPath.replaceAll("%20", " "); //necessary if path has spaces within
        if (!jarPath.endsWith(".jar")) { // this is needed if you plan to run the app using Spring Tools Suit play button.
            jarPath = jarPath.replaceAll("/classes/.*", "/classes/");
        }
        String directoryPath = Paths.get(jarPath).getParent().toString(); //Paths - from java 8
        return directoryPath;
    }
}
