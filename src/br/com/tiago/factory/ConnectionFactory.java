package br.com.tiago.factory;



import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

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
	private String url; // ex:jdbc:mysql://localhost/clientev1
	private final String user = "prolink";
	private final String password   = "77i#EU&K"; 
        
        public ConnectionFactory(){
            try {
                Properties p = new Properties();
                FileInputStream file = new FileInputStream("configBanco.txt");
                p.load(file);
                file.close();
                url = p.getProperty("urlBanco");
            } catch (IOException ex) {
                throw new RuntimeException();
            }
        }
	public Connection getConnetion(){
		try {
			Class.forName(driver);
                        return  DriverManager.getConnection(url, user, password);
		} catch (SQLException | ClassNotFoundException erro) {
			throw new RuntimeException(erro);	
                                                      
		}
	}
}