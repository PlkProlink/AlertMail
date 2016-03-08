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
    
    public List<ModelUsuario> ListarTodos(Model model){
        
        String dataFim = sdm.format(data);
        
        con = null;
        model.setMensagem("Pegando relação de funcionários!");
        
        List<ModelUsuario> listaTodos = new ArrayList<>();
        
        String sql ="select Usuario, Email from login as l where l.Usuario in "+
                "(select distinct(Para_Quem) from documentos_recebidos as d where d.Recebido='N' and "
                + "Data_Recebimento between '2016-02-01' and '"+dataFim+"')";
    
        try{
            PreparedStatement ps = getCon().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if(rs!=null){
                model.setMensagem("Relação pega! Proximo passo...!");
                while(rs.next()){
                    ModelUsuario modelUser = new ModelUsuario();
                    modelUser.setNome(rs.getString(1));
                    modelUser.setEmail(rs.getString(2));

                    //System.out.println("Usuario:"+modelUser.getNome()+"\nEmail:"+modelUser.getEmail());
                    UsuarioDao ind = new UsuarioDao();
                    ind.listarIndividual(modelUser, model);
                    listaTodos.add(modelUser);
                    //model.setMensagem("Tratamento de Usuario: "+modelUser.getNome()+"e Email: "+modelUser.getEmail());
                    
                }
                con.close();
                return listaTodos;
            }
        }catch(SQLException e){
            model.setMensagem("Não conseguir relacionar lista de usuarios!"+sql);
        }
          return null; 
    }
    
}
