/*
Classe responsavel pela configuracao :

Hora de alerta
 */
package br.com.tiago.utilitarios;

import br.com.tiago.model.Model;
import br.com.tiago.view.MenuView;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

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

    String[] dados = new String[5];
    String[] temp = new String[5];

    String mensagem, nome = "config/config.txt";

    public void criarArquivoConfig(Model model) {
        
        String hora=model.getHoraAlerta();
        
        //se o arquivo config não existe ele sera recriado
        if (file.exists() == false) {
            try {
                file.createNewFile();
                fWriter = new FileWriter(nome, true);
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

    public void lerArquivo(Model model) {
        file = new File(nome);
        //se o arquivo nao existe ele sera recriado
        criarArquivoConfig(model);

        FileReader arq;
        try {
            arq = new FileReader("config/config.txt");
            BufferedReader ler = new BufferedReader(arq);
            model.setMensagem("Lendo o arquivo config!");
            String linha = ler.readLine();
            int num = 0;
            while (linha != null) {
                int value = linha.indexOf("#");//ignorar caso a linha contenha #
                if (value == -1) {
                    dados[num] = linha; //jogando a informação da linha dentro do array dados
                    num++;
                }
                linha = ler.readLine();
            }
            model.setMensagem("Arquivo config lido!");
            arq.close(); //importante, fechar o arquivo usado
        } catch (FileNotFoundException ex) {
            model.setMensagem("Arquivo config não encontrado!");
        } catch (IOException ex) {
            model.setMensagem("Não foi possivel ler o arquivo config!");
        }

    }

    public void trataValores(Model model) {
        //trata cada campo do arquivo
        
        temp[0] = dados[0].substring(11);//copiando o valor da array
        //verificando se a data tem o tamanho correto
        if (temp[0].trim().length() == 10) {
            temp[1] = dados[1].substring(10);
            //verfificando o tamanho da hora
            if (temp[1].trim().length() == 8) {
                //
                temp[2] = dados[2].substring(6);
                int v1 = Integer.parseInt(temp[2].trim());
                if (v1 > 0 || v1 < 6) {
                    temp[3] = dados[3].substring(7);
                    int v2 = Integer.parseInt(temp[3].trim());
                    if (v2 >= v1) {
                        temp[4] = dados[4].substring(14);
                        int v3 = Integer.parseInt(temp[4].trim());
                        if (temp[4].length() > 2 && v3 > 30) {

                            model.setMensagem("Valor no campo divisivel final em config esta incorreto!");
                            model.setMensagem("Resolva esse erro deletando o arquivo config e reiniciando o serviço!");
                        } else {
                            model.setDataInicio(temp[0]);
                            model.setMensagem("Data Inicio=" + temp[0] + "=Sucesso");
                            model.setHoraAlerta(temp[1]);
                            model.setMensagem("Hora do Alerta=" + temp[1] + "=Sucesso");
                            model.setDiaInicio(temp[2]);
                            model.setMensagem("Dia Inicio=" + temp[2] + "=Sucesso");
                            model.setDiaFim(temp[3]);
                            model.setMensagem("Dia fim=" + temp[3] + "=Sucesso");
                            model.setDiaDivisivel(temp[4]);
                            model.setMensagem("Intervalo=" + temp[4] + "=Sucesso");

                            model.setMensagem("O arquivo config esta legivel!");
                            model.setMensagem("Finalizando verificação das configurações! Pronto para a proxima etapa!");
                        }
                    } else {
                        model.setMensagem("Valor no campo dia final em config esta incorreto!");
                        model.setMensagem("Resolva esse erro deletando o arquivo config e reiniciando o serviço!");
                    }
                } else {
                    model.setMensagem("Valor no campo dia inicial em config esta incorreto!");
                    model.setMensagem("Resolva esse erro deletando o arquivo config e reiniciando o serviço!");
                }
            } else {
                model.setMensagem("Valor no campo hora em config esta incorreto!");
                model.setMensagem("Resolva esse erro deletando o arquivo config e reiniciando o serviço!");
            }
        } else {
            model.setMensagem("Valor no campo data em config esta incorreto!");
            model.setMensagem("Resolva esse erro deletando o arquivo config e reiniciando o serviço!");
        }
//            for(int i = 0; i<temp.length; i++){
//                System.out.print(temp[i]+"\n");
//            }
    }

    public void alterarHora(Model model) {
        file = new File(nome);
        file.delete();
        model.setMensagem("Arquivo config deletado");
        lerArquivo(model);
        trataValores(model);
        model.setMensagem("Novo horário informado:" + model.getHoraAlerta() + " está ativo!");

    }
}
