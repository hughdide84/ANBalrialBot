package org.balrial.bot.handlers;

import org.balrial.dao.planificacion.PlanificacionDAO;
import org.balrial.factory.DAOFactory;
import org.balrial.model.Usuario;
import org.telegram.abilitybots.api.objects.MessageContext;


public class Recordatorio extends Thread {


    DAOFactory factory = DAOFactory.getDAOFactory(1);

    PlanificacionDAO planDao = factory.getPlanificacionDAO();

    Usuario user;
    MessageContext ctx;

    public Recordatorio(Usuario usuarios, MessageContext ctx) {
        user = usuarios;
        this.ctx = ctx;
    }


    @Override
    public void run() {
        super.run();
        }
    }

