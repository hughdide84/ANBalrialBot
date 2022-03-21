package org.example.bot;


import com.google.gson.*;
import com.google.gson.stream.JsonWriter;
import org.balrial.dao.usuario.UsuarioDAO;
import org.balrial.factory.DAOFactory;

import org.telegram.abilitybots.api.objects.MessageContext;
import org.telegram.abilitybots.api.sender.SilentSender;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeSet;

public class RecordatorioManager extends Thread {

    DAOFactory factory = DAOFactory.getDAOFactory(1);
    UsuarioDAO userDao = factory.getUsuarioDAO();
    String filePath = "src/main/resources/registro_recordatorio.json";
    SilentSender silent;


    RecordatorioManager(SilentSender silent) {
        this.silent = silent;
    }

    /** Método run encargado de hacer los recordatorios
     *
     */
    @Override
    public void run() {
        super.run();

        while (true) {

            try {
                Thread.sleep(5000);

                ArrayList<Long> idList = JsonHandler.readJson();

                idList.forEach(e -> {
                    silent.send("Recordatorio", e);
                });

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * Método para registrar ids en el json de usuarios a recordar
     *
     * @param id el id del usuario a registrar
     * @throws IOException error de lectura de archivo
     * @return true en caso de que el id NO esté dado de alta, false en caso contrario
     */
    public boolean registrarId(long id) throws IOException {

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
     * @throws IOException error de lectura de archivo
     * @return  true en caso de que el id esté dado de alta, false en caso contrario
     */
    public boolean bajaId(long id) throws IOException {

        if (estaRegistrado(id)) {
            JsonHandler.writeId(id);
            return true;
        }
        return false;
    }


    private boolean estaRegistrado(long id) throws IOException {
        return JsonHandler.readJson().contains(id);
    }
}
