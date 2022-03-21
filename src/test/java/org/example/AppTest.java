package org.example;


import com.google.gson.*;
import org.balrial.dao.planificacionUsuario.PlanificacionUsuarioDAO;
import org.balrial.dao.planificacionUsuario.PlanificacionUsuarioORMDAO;
import org.balrial.factory.DAOFactory;
import org.balrial.factory.DAOFactoryORM;
import org.balrial.model.Planificacion;
import org.balrial.model.PlanificacionUsuario;
import org.junit.Test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Map;


public class AppTest {


   @Test
    public void requestTest() throws IOException {
       String filePath = "src/main/resources/registro_recordatorio.json";


       Files.writeString(Path.of(filePath), "hola me llamo juan", StandardOpenOption.WRITE);
    }


    /**
     * Test para loader de config
     */
    @Test
    public void shouldAnswerWithTrue() throws IOException {
        Gson gson = new GsonBuilder().create();

        String filePath = "src/main/resources/registro_recordatorio.json";
        StringBuilder sb = new StringBuilder();

        Files.lines(new File(filePath).toPath()).forEach(sb::append);

        JsonObject result = JsonParser.parseString(sb.toString()).getAsJsonObject();
        Map map = gson.fromJson(result, Map.class);

        System.out.println(map);
    }
}
