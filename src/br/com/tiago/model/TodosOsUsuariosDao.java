package br.com.tiago.model;

import br.com.tiago.factory.ConnectionFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
/*
 * Todos direitos reservados a Tiago Dias de Souza.
 * OpenSource Project www.github.com.br/tiagods
 */
/**
 *
 * @author Tiago Dias
 */
public class TodosOsUsuariosDao {
    
    ConnectionFactory conection;
    
    Connection con;
    
    Date data = new Date();
    
    SimpleDateFormat sdm = new SimpleDateFormat("yyyy-MM-dd");
    
    public Connection getCon(){
        conection = new ConnectionFactory();
        con = conection.getConnetion();
        return con;
    }
    
    public List<ModelUsuarioBean> ListarTodos(){
        
        String dataFim = sdm.format(data);
        
        con = null;
        
        String sql ="select Usuario, Email, Nome from login as l where l.Usuario in "+
                "(select distinct(Para_Quem) from documentos_recebidos as d where d.Recebido='N' and "
                + "Data_Recebimento between '2016-03-01' and '"+dataFim+"')";
    
        try{
            List<ModelUsuarioBean> listaTodos = new ArrayList<>();
            
            PreparedStatement ps = getCon().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if(rs!=null){
                while(rs.next()){
                    ModelUsuarioBean userBean = new ModelUsuarioBean();
                    userBean.setNome(rs.getString(1));
                    userBean.setEmail(rs.getString(2));
                    listaTodos.add(userBean);
                }
                con.close();
                return listaTodos;
            }
            else
                return null;
        }catch(SQLException e){
            
            return null;
        }
    }
}
