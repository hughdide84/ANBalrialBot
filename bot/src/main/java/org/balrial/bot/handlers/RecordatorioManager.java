package org.balrial.bot.handlers;

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
import java.util.ArrayList;

@Component
public class RecordatorioManager {

    DAOFactory factory = DAOFactory.getDAOFactory(1);
    UsuarioDAO userDao = factory.getUsuarioDAO();
    String filePath = "src/main/resources/registro_recordatorio.json";
    static SilentSender silent;

    RecordatorioManager() {
    }

    /**
     * Método run encargado de hacer los recordatorios
     */
    @Scheduled(fixedRate = 5000)
    public void mandarAvisos() {

        System.out.println(silent);
        if (silent != null) {
            ArrayList<Long> idList = JsonHandler.readJson();

            idList.forEach(e -> {
                silent.send("Recordatorio", e);
            });
        }
    }

    public void setSilent(SilentSender silent) {
        RecordatorioManager.silent = silent;
    }

    /**
     * Método para registrar ids en el json de usuarios a recordar
     *
     * @param id el id del usuario a registrar
     * @return true en caso de que el id NO esté dado de alta, false en caso contrario
     * @throws IOException error de lectura de archivo
     */
    public boolean registrarId(long id) throws IOException {
        // TODO migrar esto a los métodos de la bd

        if (!estaRegistrado(id)) {
            JsonHandler.writeId(id);
            return true;
        }

        return false;
    }


    /**
     * Método para dar de baja ids en el json de usuarios a recordar
     *
     * @param id el id del usuario a registrar
     * @return true en caso de que el id esté dado de alta, false en caso contrario
     * @throws IOException error de lectura de archivo
     */
    public boolean bajaId(long id) throws IOException {


        Usuario user =  userDao.consultarTelegramId((int)id);

        // TODO migrar esto a los métodos de la bd
        if (estaRegistrado(id)) {
            JsonHandler.removeId(id);
            return true;
        }
        return false;
    }


    private boolean estaRegistrado(long id) throws IOException {
        // TODO migrar esto a los métodos de la bd

        return JsonHandler.readJson().contains(id);
    }
}
