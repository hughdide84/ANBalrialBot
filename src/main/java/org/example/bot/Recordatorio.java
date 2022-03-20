package org.example.bot;

import org.balrial.dao.planificacion.PlanificacionDAO;
import org.balrial.factory.DAOFactory;
import org.balrial.factory.DAOFactoryORM;
import org.balrial.model.Planificacion;
import org.balrial.model.Usuario;
import org.telegram.abilitybots.api.objects.MessageContext;

import java.sql.Time;
import java.sql.Timestamp;


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

        long timenow = System.currentTimeMillis();

            System.out.println(planDao.listarPorUsuario(user.getId()));

            planDao.listarPorUsuario(user.getId()).stream()
                    .filter(plan -> {

                        Timestamp currTime =  Timestamp.valueOf("2022-03-08 22:51:28.0"); //new Timestamp(System.currentTimeMillis());

                        Timestamp fecha = new Timestamp(plan.getFecha().getTime() + plan.getHoraInicio().getTime());
                        Timestamp after = Timestamp.valueOf(fecha.toLocalDateTime().minusDays(1));
                        Timestamp before = Timestamp.valueOf(after.toLocalDateTime().plusMinutes(5));



                        return (currTime.after(after) || currTime.equals(fecha) && currTime.before(before));
                    }).forEach( usuario -> ctx.

            );



            for (Planificacion planes : planDao.listarPorUsuario(user.getId())) {

                Timestamp timestamp = planes.getFecha();
                Time time = planes.getHoraInicio();

                timestamp.setTime(timestamp.getTime() + time.getTime());

                System.out.println("#time: " + timestamp);
            }

            System.out.println("#now: " + timenow);
        }
    }

