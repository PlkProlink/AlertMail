package br.com.tiago.factory;



import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/*
 * Todos direitos reservados a Tiago Dias de Souza.
 * OpenSource Project www.github.com.br/tiagods
 */

/**
 *
 * @author Tiago Dias
 */
public class ConnectionFactory{
        String driver = "com.mysql.jdbc.Driver";
	private final String url = "jdbc:mysql://200.207.224.87/clientev1";
	private final String user = "client";
	private final String password   = "l!nk2016Cont"; 
	public Connection getConnetion(){
		try {
			Class.forName(driver);
                                                      return  DriverManager.getConnection(url, user, password);
		} catch (SQLException | ClassNotFoundException erro) {
			throw new RuntimeException(erro);	
                                                      
		}
	}
}