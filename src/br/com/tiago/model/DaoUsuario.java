/*
 * Todos direitos reservados a Tiago Dias de Souza.
 * OpenSource Project www.github.com.br/tiagods
 */
package br.com.tiago.model;

import br.com.tiago.factory.ConnectionFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Tiago
 */
public class DaoUsuario {
    
    Connection con;
    public Connection getCon() {
        con = new ConnectionFactory().getConnetion();
        return con;
    }
    
    public List<ModelRelacao> listaIndividual (ModelUsuario user){
        
         List<ModelRelacao> listaIndividual = new ArrayList<>();
         String sql = "select * from documentos_recebidos where Para_Quem like '" + user.getNome() + "' and Recebido='N'";

         try {
                 PreparedStatement ps = getCon().prepareStatement(sql);
                 ResultSet rs = ps.executeQuery();
                 if (rs != null) {
                     while(rs.next()){
                         ModelRelacao relacao = new ModelRelacao();
                         relacao.setCodigo(rs.getString("cod"));
                         relacao.setDataRecebimento(rs.getString("Data_Recebimento"));
                         relacao.setEmpresa(rs.getString("Empresa"));
                         relacao.setId(rs.getString("ID"));
                         relacao.setHistorico(rs.getString("Historico"));
                         relacao.setQuemEntregou(rs.getString("Quem_Entregou"));
                         listaIndividual.add(relacao);
                     }
                 }
                 return listaIndividual;
             } catch (SQLException e) {
                 //model.setMensagem("Falha na relação da tabela do funcionario:"+user.getNome()+"\nSistema exige atenção! :"+sql);
                 return null;
             }finally{try {con.close();} catch (SQLException ex) {
             }
             }
    }
}