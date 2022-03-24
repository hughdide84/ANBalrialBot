package org.balrial.bot.handlers;

import org.balrial.dao.planificacion.PlanificacionDAO;
import org.balrial.dao.planificacionUsuario.PlanificacionUsuarioDAO;
import org.balrial.dao.usuario.UsuarioDAO;
import org.balrial.factory.DAOFactory;
import org.balrial.model.Usuario;
import org.checkerframework.checker.units.qual.C;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.telegram.abilitybots.api.sender.SilentSender;
import org.telegram.telegrambots.meta.api.objects.User;

import java.io.*;
import java.lang.annotation.Repeatable;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Component
public class RecordatorioManager {

    DAOFactory factory;
    static SilentSender silent;

    RecordatorioManager() {

        factory = DAOFactory.getDAOFactory(1);
    }

    /**
     * Método run encargado de hacer los recordatorios
     */
    @Scheduled(fixedRate = 5000)
    public void mandarAvisos() {

        UsuarioDAO usuarioDAO = factory.getUsuarioDAO();
        usuarioDAO.abrirConexion();

        PlanificacionUsuarioDAO planUserDAO = factory.getPlanificacionUsuarioDAO();
        PlanificacionDAO planDao = factory.getPlanificacionDAO();


        List<Usuario> usuarioList = usuarioDAO.consultarNotificaciones("T");

        usuarioList.stream()

                // Obtengo a los usuarios que tengan una planificación este día o el día anterior
                // (y tengan notificación de telegram)
                .filter(user -> !user.getPlanificaciones().stream()
                        .filter(planificacion ->
                                (timeToDay(planificacion.getHoraInicio()) == currentDay() - 1)
                                        || (timeToDay(planificacion.getHoraInicio()) == currentDay()))
                        .toList().isEmpty())

                // Obtengo a los usuarios que
                .filter(user -> {

                    planDao.abrirConexion();

                    planDao.listarPorUsuario(user)



                    planDao.cerrarConexion();
                }).toList();


    }


    public int timeToDay(Time time) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(time);
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    public int currentDay() {

        return timeToDay(new Time(System.nanoTime()));
    }


    public void setSilent(SilentSender silent) {
        RecordatorioManager.silent = silent;
    }

    /**
     * @param id el id del usuario a registrar
     * @return true en caso de que el id NO esté dado de alta, false en caso contrario
     * @throws IOException error de lectura de archivo
     */
    public boolean registrarId(long id) throws IOException {

        UsuarioDAO usuarioDAO = factory.getUsuarioDAO();
        usuarioDAO.abrirConexion();

        Usuario user = usuarioDAO.consultar(Math.toIntExact(id));

        if (!user.getNotificaciones().contains("T")) {

            user.setNotificaciones("T");
            usuarioDAO.actualizar(user);

            silent.send("Las notificaciones han sido activadas de baja", id);
            return true;
        } else {
            silent.send("Las notificaciones ya estaban activadas", id);
            return false;
        }
    }


    /**
     * Método para dar de baja ids en el json de usuarios a recordar
     *
     * @param id el id del usuario a registrar
     * @return true en caso de que el id esté dado de alta, false en caso contrario
     * @throws IOException error de lectura de archivo
     */
    public boolean bajaId(long id) throws IOException {

        UsuarioDAO usuarioDAO = factory.getUsuarioDAO();
        usuarioDAO.abrirConexion();

        Usuario user = usuarioDAO.consultar(Math.toIntExact(id));

        if (user.getNotificaciones().contains("T")) {

            user.setNotificaciones(null);
            usuarioDAO.actualizar(user);

            silent.send("Las notificaciones han sido dadas de baja", id);
            return true;
        } else {
            silent.send("Las notificaciones no estaban activadas", id);
            return false;
        }
    }
}
