package org.balrial.bot;

import org.balrial.dao.usuario.UsuarioDAO;
import org.balrial.factory.DAOFactory;
import org.balrial.factory.DAOFactoryORM;
import org.balrial.model.Usuario;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BotApplicationTests {

	DAOFactory factory = DAOFactoryORM.getDAOFactory(1);
	UsuarioDAO usuarioDAO = factory.getUsuarioDAO();

	@Test
	void contextLoads() {
		usuarioDAO.abrirConexion();
		System.out.println(usuarioDAO.listar());
		usuarioDAO.cerrarConexion();
	}

}
