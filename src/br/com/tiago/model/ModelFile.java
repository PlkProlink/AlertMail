package br.com.tiago.model;

import java.text.SimpleDateFormat;
import java.util.Date;

/*
 * Todos direitos reservados a Tiago Dias de Souza.
 * OpenSource Project www.github.com.br/tiagods
 */

/**
 *
 * @author Tiago Dias
 */
public class ModelFile {
    
private String horaFile;
private String dataFile;

//*Classe importante para gerar a nomeclatura do arquivos log/relatorio
    SimpleDateFormat sdfF = new SimpleDateFormat("yyyyMMdd");
    SimpleDateFormat sdhF = new SimpleDateFormat("HHmm");
    
    Date data = new Date();

    
    public String getDataFile(){
        this.dataFile = sdfF.format(data);
        return dataFile; 
    }
      
    public String getHoraFile(){
        this.horaFile = sdhF.format(data);
        return horaFile;
    }
}
