package org.balrial.bot.handlers;

import org.balrial.connection.ConexionORM;
import org.balrial.dao.usuario.UsuarioDAO;
import org.balrial.factory.DAOFactory;
import org.balrial.factory.DAOFactoryORM;
import org.balrial.model.Planificacion;
import org.balrial.model.Usuario;
import org.springframework.stereotype.Component;
import org.telegram.abilitybots.api.objects.Ability;
import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.abilitybots.api.sender.DefaultSender;
import org.telegram.abilitybots.api.sender.SilentSender;


import java.io.IOException;
import java.util.List;

import static org.telegram.abilitybots.api.objects.Locality.ALL;
import static org.telegram.abilitybots.api.objects.Privacy.PUBLIC;

/**
 * todo: Buscar una forma de hacer configurable el user y el token
 */
@Component
public class MyBot extends AbilityBot {

    RecordatorioManager manager = new RecordatorioManager();
    DAOFactory factory;


    public MyBot() {
        super("2026788812:AAHVIpeM9Lkd_Ke1Tv5Nis7rCbY_6VBnmtM", "TestBot");
        manager.setSilent(new SilentSender(new DefaultSender(this)));
        factory = DAOFactoryORM.getDAOFactory(1);
    }

    @Override
    public long creatorId() {
        return 729192255;
    }


    /**
     * Método para registrar el token de un usuario
     *
     * @return movidas de la librería
     */
    public Ability registrarToken() {

        return Ability.builder()
                .name("registrarse")
                .info("Registrar un token a una cuenta de Telegram")
                .privacy(PUBLIC)
                .locality(ALL)
                .input(1)
                .action(ctx -> {

                    UsuarioDAO usuarioDAO = factory.getUsuarioDAO();
                    usuarioDAO.abrirConexion();

                    int userId = Math.toIntExact(ctx.user().getId());
                    Usuario telegramUser = usuarioDAO.consultarPorToken(ctx.firstArg());

                    // Si hay un usuario con el token
                    if (telegramUser != null) {

                        // Busco a ver si hay un usuario con la id de telegram del que pregunta
                        Usuario bdUser = usuarioDAO.consultarTelegramId(userId);

                        // Si hay un usuario con dicha id, que no sea el del token, se le borra la id de telegram
                        if (bdUser != null && !telegramUser.equals(bdUser)) {

                            bdUser.setTelegramId(null);
                            usuarioDAO.actualizar(bdUser);
                        }

                        telegramUser.setTelegramId(userId);
                        usuarioDAO.actualizar(telegramUser);
                        silent.send("Esta cuenta ha sido registrada", ctx.chatId());
                    } else {
                        silent.send("Este token es incorrecto", ctx.chatId());
                    }

                    usuarioDAO.cerrarConexion();
                })
                .build();
    }


    /**
     * Método para que un usuario con token active sus recordatorio
     * todo
     *
     * @return
     */
    public Ability activarRecordatorios() {
        return Ability.builder()
                .name("activar_recordatorios")
                .info("Envía un recordatorio 24h antes de un plan")
                .privacy(PUBLIC)
                .locality(ALL)
                .input(0)
                .action(ctx -> {

                    try {
                        if (manager.registrarId(ctx.user().getId())) {
                            silent.send("Se han activado los recordatorios", ctx.chatId());
                        } else {
                            silent.send("Los recordatorios ya están activos", ctx.chatId());
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                })
                .build();
    }


    /**
     * Método para desactivar los recordatorios de un usuario
     *
     * @return
     */
    public Ability desactivarRecordatorios() {
        return Ability.builder()
                .name("desactivar_recordatorios")
                .info("Says hello world!")
                .privacy(PUBLIC)
                .locality(ALL)
                .input(0)
                .action(ctx -> {
                    try {

                        if (manager.bajaId(ctx.user().getId())) {
                            silent.send("Se han desactivado los recordatorios", ctx.chatId());
                        } else {
                            silent.send("Los recordatorios no están activados", ctx.chatId());
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                })
                .build();
    }


    /**
     * Método para recuperar los planes de un usuario
     * todo mejorar el string que envío
     * @return los planes
     */
    public Ability recuperarPlanes() {
        return Ability.builder()
                .name("planes")
                .info("Recuperas loos planes")
                .privacy(PUBLIC)
                .locality(ALL)
                .input(0)
                .action(ctx -> {

                    UsuarioDAO usuarioDAO = factory.getUsuarioDAO();
                    usuarioDAO.abrirConexion();

                    Usuario user = usuarioDAO.consultarTelegramId(Math.toIntExact(ctx.user().getId()));
                    List<Planificacion> planList = user.getPlanificaciones();


                    StringBuilder sb = new StringBuilder();


                    planList.stream().forEach(plan -> {

                        sb.append("Día: " + plan.getFecha() + "\n" +
                                "Hora de inicio: " + plan.getHoraInicio() + "\n" +
                                "Hora de finalizacion: " + plan.getHoraFin() + "\n\n" +
                                "Coordinador: " + plan.getUbicacionProyecto().getCoordinador() + "\n\n" +
                                "Ubicación: " + plan.getUbicacionProyecto().getUbicacion().getDireccion() + "\n"
                        );

                        silent.send(sb.toString(), ctx.chatId());
                    });


                    usuarioDAO.cerrarConexion();
                })
                .build();
    }
}
