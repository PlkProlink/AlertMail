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
    
    public String imprimir(ModelUsuario user){
        
        ModelFile file = new ModelFile();
        try{
            
            String sql ="select * from documentos_recebidos where Para_Quem like ? and Recebido='N'";
            
            PreparedStatement ps = getCon().prepareStatement(sql);
            ps.setString(1, user.getNome());
            ResultSet rs = ps.executeQuery();
            
            //pegando o resultado da consulta e e jogando no jasper result set
            JRResultSetDataSource jrRS = new JRResultSetDataSource(rs);
            JasperPrint jasperPrint = JasperFillManager.fillReport(
            "relatorios/docListUsuario.jasper", new HashMap(), jrRS);
            //ignorando a instrução de mostrar na tela
            //JasperViewer.viewReport(jasperPrint, false);
            nomeArquivo="arquivos/"+user.getNome()+file.getDataFile()+file.getHoraFile()+".pdf";
            JasperExportManager.exportReportToPdfFile(jasperPrint, nomeArquivo);
            con.close();
            return nomeArquivo;
        }catch (SQLException | JRException erro){
        }
        return null;
    }
}
