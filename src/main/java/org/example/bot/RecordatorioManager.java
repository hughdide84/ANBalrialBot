package org.example.bot;


import org.balrial.dao.usuario.UsuarioDAO;
import org.balrial.factory.DAOFactory;

import org.telegram.abilitybots.api.objects.MessageContext;

import java.util.TreeSet;

public class RecordatorioManager {

    TreeSet<Long> registeredId = new TreeSet<>();
    DAOFactory factory = DAOFactory.getDAOFactory(1);
    UsuarioDAO userDao = factory.getUsuarioDAO();


    public void doStuff(MessageContext ctx) {


        for (int i = 0; i < 10; i++) {

            userDao.listar().stream()
                    .filter(user -> registeredId.contains((long) user.getTelegramId()))
                    .forEach(user -> new Thread(new Recordatorio(user, ctx)).start());
        }
    }
}
