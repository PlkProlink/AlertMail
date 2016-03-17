/*
 * Todos direitos reservados a Tiago Dias de Souza.
 * OpenSource Project www.github.com.br/tiagods
 */
package br.com.tiago.model;

import br.com.tiago.utilitarios.HtmlEntities;
import br.com.tiago.factory.ConnectionFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Tiago Dias
 */
public class UsuarioCalculos {
    
    Connection con;
    
    public Connection getCon(){
        this.con= new ConnectionFactory().getConnetion();
        return con;
    }
    
    
    public String verificaMargem(ModelContador contador){
        HtmlEntities html = new HtmlEntities();
        String margem="";
        if(contador.getMedia()!=100){
            if(contador.getMedia()>=90 && contador.getMedia()<100){
                margem = html.Converter("Isso é Otimo, você esta muito proximo de ficar 100%.")
                        + "<br>"+ html.Converter("Em dois passos você pode limpar todo historico de uma vez!");
            }
            else if(contador.getMedia()>=80 && contador.getMedia()<90){
                margem = html.Converter("Isso é Bom, mas ainda pode melhorar!")
                        +"<br>"+ html.Converter("Em dois passos você pode limpar todo historico de uma vez!");
            }
            else if(contador.getMedia()>=60 && contador.getMedia()<70){
                margem = html.Converter("É muito ruim, seu historico é péssimo! ")
                        + "<br>"+ html.Converter("Estou muito chateado que você tenha deixado chegar a esse ponto."
						+"Mas em dois passos você pode limpar todo historico de uma vez");
            }
            else{
                if(contador.getContNegativo()<=5){
                    margem = html.Converter("Você não tem muita documentação pendente para finalizar")
                            +"<br>"+html.Converter("Mesmo assim você deve finalizar todos o historico");
                }
                else
                    margem = html.Converter("Isso é horrivel,  ")+"<br>"+ html.Converter("estou muito chateado que você tenha deixado chegar a esse ponto."
                 					+"Mas em dois passos você pode limpar todo historico de uma vez!");
             }
        }
        else
            margem = html.Converter("Excelente! Não podia ser melhor");
        
        return margem;
    }
    
    public void pegaNegativo(ModelUsuario user, ModelContador contador){
        con = null;
        String sql = "select count(Recebido) as Negativo from documentos_recebidos where Para_Quem like '"+user.getNome()+"' and Recebido='N'";
        try{
            PreparedStatement ps = getCon().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
                if(rs!=null){
                    if(rs.first()){
                        contador.setContNegativo(rs.getInt(1));
//                        System.out.println("\nRelação negativa pronta:"+contador.getContNegativo());
                    }
                }
                con.close();
        }catch(SQLException e){
            //model.setMensagem(sql);
        }
    }
    public void pegaPositivo (ModelUsuario user, ModelContador contador){
        con = null;
        String sql = "select count(Recebido) as Positivo from documentos_recebidos where Para_Quem like '"+user.getNome()+"' and Recebido='S'";
        try{
            PreparedStatement ps = getCon().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
                if(rs!=null){
                    if(rs.first()){
                        contador.setContPositivo(rs.getInt(1));
//                        System.out.println("\nRelação positiva pronta:"+contador.getContPositivo());
                    }
                }
                con.close();
                
        }catch(SQLException e){
            //model.setMensagem(sql);
        }
          
    }
    //calcula a media de fechamento do usuario
    public void calcula(ModelContador contador){
        int pos = contador.getContPositivo();
        int neg = contador.getContNegativo();
        int saldo = pos + neg;
        double media = (pos * 100) /saldo;
        contador.setSaldo(saldo);
        contador.setMedia(media);
    }
    
    
    
}
