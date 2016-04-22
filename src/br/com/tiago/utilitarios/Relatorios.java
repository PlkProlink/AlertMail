package br.com.tiago.utilitarios;

import br.com.tiago.factory.ConnectionFactory;
import br.com.tiago.model.ModelFile;
import br.com.tiago.model.ModelUsuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRResultSetDataSource;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;

/*
 * Todos direitos reservados a Tiago Dias de Souza.
 * OpenSource Project www.github.com.br/tiagods
 */

/**
 *
 * @author Tiago Dias
 */
public class Relatorios {
    
    private String nomeArquivo;
    private Connection con;
    
    public Connection getCon(){
        return this.con = new ConnectionFactory().getConnetion();
    }
    
    public String imprimir(ModelUsuario user, String diretorio){
        
        ModelFile file = new ModelFile();
        try{
            
            String sql ="select a.cod, a.Data_Recebimento, a.Quem_Entregou, "+
                    "a.ID, a.Empresa, a.Historico, B.Nome as NomeCompleto " +
                    "from documentos_recebidos as a " +
                    "inner join login as b " +
                    "on a.Para_Quem = b.Usuario " +
                    "where a.Para_Quem=? and a.Recebido='N'";
            
            PreparedStatement ps = getCon().prepareStatement(sql);
            ps.setString(1, "Telmon");
            ResultSet rs = ps.executeQuery();
            
            //pegando o resultado da consulta e e jogando no jasper result set
            JRResultSetDataSource jrRS = new JRResultSetDataSource(rs);
            JasperPrint jasperPrint = JasperFillManager.fillReport(
            "relatorios/docListUsuario.jasper", new HashMap(), jrRS);
            //ignorando a instrução de mostrar na tela
            //JasperViewer.viewReport(jasperPrint, false);
            nomeArquivo=diretorio+"/"+user.getNome()+file.getDataFile()+file.getHoraFile()+".pdf";
            JasperExportManager.exportReportToPdfFile(jasperPrint, nomeArquivo);
            return nomeArquivo;
        }catch (SQLException | JRException erro){
        }finally{try {con.close();} catch (SQLException ex) {}
        }
        return "";
    }
}
