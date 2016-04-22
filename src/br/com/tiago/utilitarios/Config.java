/*
Classe responsavel pela configuracao :

Hora de alerta
 */
package br.com.tiago.utilitarios;

import br.com.tiago.model.Model;
import br.com.tiago.view.MenuView;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

/*
 * Todos direitos reservados a Tiago Dias de Souza.
 * OpenSource Project www.github.com.br/tiagods
 */
/**
 *
 * @author Tiago Dias
 */
public class Config {

    File file;
    FileWriter fWriter;
    MenuView menu;
    
    String dataResgate, horaAlerta, diaInical, diaFim, diaDivisivel;
    String mensagem, nomeDiretorio;

    public void criarArquivoConfig(Model model, String diretorioConfig) {
        
        //se o arquivo config não existe ele sera recriado
        if (!file.exists()) {
            try {
                file.createNewFile();
                fWriter = new FileWriter(this.nomeDiretorio, true);
                String valores = "#Data inicial de contagem do programa\n"
                        + "DateRescue=01/02/2015\n"
                        + "#Hora do Alerta\n"
                        + "HourValue=" +model.getHoraAlerta() + "\n"
                        + "#DayOn e Day Off de 1 a 7 (1 para domingo e 7 para sabado)\n"
                        + "DayOn=2\n"
                        + "DayOff=6\n"
                        + "#Divide o Dia do mes pos n, se conseguir enviara o alerta\n"
                        + "DaysDivisible=1";
                fWriter.write(valores);
                fWriter.close();
                model.setMensagem("Arquivo config recriado!");

            } catch (IOException Ex) {
                model.setMensagem("Não foi possivel criar o arquivo config!");
            }
        }
    }
    public void pegaDiretorio(String diretorioConfig){
        this.nomeDiretorio=diretorioConfig+"/config.txt";
    }
    
    public void lerArquivo(Model model, String diretorioConfig) {
        pegaDiretorio(diretorioConfig);
        
        file = new File(nomeDiretorio);
        //se o arquivo nao existe ele sera recriado
        criarArquivoConfig(model, diretorioConfig);
        
        try {
            //instanciando classe para ler atributos do arquivo
            Properties properties = new Properties();
            //carregando arquivo
            FileInputStream stream = new FileInputStream(this.nomeDiretorio);
            //carregando atributos
            properties.load(stream);
            
            dataResgate = properties.getProperty("DateRescue");
            horaAlerta = properties.getProperty("HourValue");
            diaInical = properties.getProperty("DayOn");
            diaFim = properties.getProperty("DayOff");
            diaDivisivel = properties.getProperty("DaysDivisible");
            
            stream.close();
            model.setMensagem("Arquivo config lido!");
        } catch (FileNotFoundException ex) {
            model.setMensagem("Arquivo config não encontrado!");
        } catch (IOException ex) {
            model.setMensagem("Não foi possivel ler o arquivo config!");
        }

    }

    public void trataValores(Model model, String diretorio) {
        //trata cada campo do arquivo
        
        //verificando se a data e hora são validas
        if (validaData()==true) {
            //validar dia da semana
            int v1 = Integer.parseInt(this.diaInical);
            if (v1 > 0 || v1 < 6) {
                int v2 = Integer.parseInt(this.diaFim);
                if (v2 >= v1 && v2 <= 7) {
                    int v3 = Integer.parseInt(this.diaDivisivel);
                    if (v3 >= 1 && v3 <= 30) {
                        model.setDataInicio(this.dataResgate);
                        model.setMensagem("Data Inicio=" + this.dataResgate + "=Sucesso");
                        model.setHoraAlerta(this.horaAlerta);
                        model.setMensagem("Hora do Alerta=" + this.horaAlerta + "=Sucesso");
                        model.setDiaInicio(this.diaInical);
                        model.setMensagem("Dia Inicio=" + this.diaInical + "=Sucesso");
                        model.setDiaFim(this.diaFim);
                        model.setMensagem("Dia fim=" + this.diaFim + "=Sucesso");
                        model.setDiaDivisivel(this.diaDivisivel);
                        model.setMensagem("Intervalo=" + this.diaDivisivel + "=Sucesso");

                        model.setMensagem("O arquivo config esta legivel!");
                        model.setMensagem("Finalizando verificação das configurações! Pronto para a proxima etapa!");

                    } else  setMensagem(model, "Valor no campo intervalo em config esta incorreto!", diretorio);
                } else  setMensagem(model, "Valor no campo dia final em config esta incorreto!", diretorio);
            } else  setMensagem(model, "Valor no campo dia inicial em config esta incorreto!", diretorio);
        } else setMensagem(model, "Valor no campo data ou hora em config esta incorreto!", diretorio);
    }
    private boolean validaData(){
        SimpleDateFormat sdh, sdf;
        Date date, hour;
        sdf = new SimpleDateFormat("dd/MM/yyyy");
        sdh = new SimpleDateFormat("HH:mm:ss");
        try {
          date = sdf.parse(this.dataResgate);
          hour = sdh.parse(this.horaAlerta);
          return true;
        } catch (ParseException e) {
          System.out.println("Favor digitar a data no formato informado.");
          return false;
        }
    }
    public void setMensagem(Model model, String mensagem, String diretorio){
        model.setMensagem(mensagem);
        FileControle file = new FileControle();
        if(file.removeArquivos(diretorio)){
            model.setMensagem("Resolvido o erro, deletado os arquivos em config, reiniciando o serviço!");
            if(file.criarDiretorio(diretorio))
                model.setMensagem("Diretorio "+diretorio+" foi recriado com sucesso!");       
        }
        else
            model.setMensagem("Resolva esse erro deletando o arquivo config e reiniciando o serviço manualmente!");
    }
    public void alterarHora(Model model, String diretorio) {
        pegaDiretorio(diretorio);
        file = new File(this.nomeDiretorio);
        file.delete();
        model.setMensagem("Arquivo config deletado");
        lerArquivo(model,diretorio);
        trataValores(model, diretorio);
        model.setMensagem("Novo horário informado:" + model.getHoraAlerta() + " está ativo!");
    }
}
