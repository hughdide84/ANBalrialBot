package org.example.bot;

import org.balrial.dao.planificacion.PlanificacionDAO;
import org.balrial.dao.planificacionUsuario.PlanificacionUsuarioDAO;
import org.balrial.dao.usuario.UsuarioDAO;
import org.balrial.factory.DAOFactory;
import org.balrial.factory.DAOFactoryORM;
import org.balrial.model.Planificacion;
import org.balrial.model.PlanificacionUsuario;
import org.balrial.model.Usuario;
import org.telegram.abilitybots.api.objects.Ability;
import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.abilitybots.api.sender.DefaultSender;
import org.telegram.abilitybots.api.sender.MessageSender;
import org.telegram.abilitybots.api.sender.SilentSender;


import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static org.telegram.abilitybots.api.objects.Locality.ALL;
import static org.telegram.abilitybots.api.objects.Privacy.PUBLIC;

/**
 * todo: Buscar una forma de hacer configurable el user y el token
 */
public class MyBot extends AbilityBot {

    DAOFactory factory = DAOFactoryORM.getDAOFactory(1);
    PlanificacionDAO planDao = factory.getPlanificacionDAO();
    UsuarioDAO usuarioDAO = factory.getUsuarioDAO();
    RecordatorioManager manager = new RecordatorioManager(new SilentSender(new DefaultSender(this)));


    public MyBot() {
        super("2026788812:AAHVIpeM9Lkd_Ke1Tv5Nis7rCbY_6VBnmtM", "TestBot");

        new Thread(manager).start();
    }

    @Override
    public long creatorId() {
        return 729192255;
    }


    /**
     * Método para registrar el token de un usuario
     * todo
     * @return
     */
    public Ability registrarToken() {
        return Ability.builder()
                .name("registrarse")
                .info("Registrar un token a una cuenta de Telegram")
                .privacy(PUBLIC)
                .locality(ALL)
                .input(0)
                .action(ctx -> {

                    // todo: cambiar esto por un método que busque a un usuario por id de telegram


                    //todo caso de error(?): usuario ya tiene un id
                    //todo caso de error(?): ese id ya tiene un usuario que no es el del token


                    silent.send("Esta cuenta ha sido registrada", ctx.chatId());
                })
                .build();
    }


    /**
     * Método para que un usuario con token active sus recordatorio
     * todo
     * @return
     */
    public Ability activarRecordatorios() {
        return Ability.builder()
                .name("activar_recordatorios")
                .info("Says hello world!")
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
     * todo
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
     *
     * @return
     */
    public Ability recuperarPlanes() {
        return Ability.builder()
                .name("planes")
                .info("Recuperas loos planes")
                .privacy(PUBLIC)
                .locality(ALL)
                .input(0)
                .action(ctx -> {

                       /*todo: falta cambiar la mierda actual por un método
                          que devuelva una lista de planificaciones en base al
                        */

                    /*
                    1. Obtener user de esta telegram id
                    2. Obtener los planes de dicho user
                    3. Obtener
                     */

                    silent.send("prueba: " + ctx.chatId() + "\n" + ctx.user().getId(), ctx.chatId());
                })
                .build();
    }


    public Usuario userByTelegramId(long id) {

        for (Usuario usuario : usuarioDAO.listar()) {

            if ((long) usuario.getTelegramId() == id) {

                return usuario;
            }
        }

        return null;
    }
}
