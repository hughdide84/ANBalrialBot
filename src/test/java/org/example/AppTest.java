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
import java.util.Map;


public class AppTest {


    @Test
    public void something() {

        PlanificacionUsuarioDAO planDao = new PlanificacionUsuarioORMDAO();

        PlanificacionUsuario plan = planDao.consultar(2);
        System.out.println(plan == null);
        System.out.println(plan);
        System.out.println(plan.getPlanificacion().toString());

    }


    @Test
    public void requestTest() {

        DAOFactory factory = DAOFactoryORM.getDAOFactory(1);
        Planificacion plan = factory.getPlanificacionDAO().consultar(64);

        System.out.println(plan.getUsuario());
    }


    /**
     * Test para loader de config
     */
    @Test
    public void shouldAnswerWithTrue() throws IOException {
        Gson gson = new GsonBuilder().create();

        String filePath = "src/test/resources/bot_config.json";
        StringBuilder sb = new StringBuilder();

        Files.lines(new File(filePath).toPath()).forEach(sb::append);


        JsonObject result = JsonParser.parseString(sb.toString()).getAsJsonObject();
        Map map = gson.fromJson(result, Map.class);
    }
}
