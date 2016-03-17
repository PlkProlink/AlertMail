/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.tiago.model;

import br.com.tiago.factory.ConnectionFactory;
import br.com.tiago.utilitarios.AvisoEmail;
import br.com.tiago.utilitarios.Grafico;
import br.com.tiago.utilitarios.HtmlEntities;
import br.com.tiago.utilitarios.Relatorios;
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
public class UsuarioDao {

    ConnectionFactory conection;

    Connection con;

    Date data = new Date();

    SimpleDateFormat sdm = new SimpleDateFormat("yyyy-MM-dd");

    ModelContador contador = new ModelContador();

    UsuarioCalculos calculos = new UsuarioCalculos();

    int cont = 0;

    String margem;

    String mensagem = "";

    private String[] codigo;
    private String[] dataRecebimento;
    private String[] quemEntregou;
    private String[] id;
    private String[] empresa;
    private String[] historico;

    public Connection getCon() {
        conection = new ConnectionFactory();
        con = conection.getConnetion();
        return con;
    }

    public List<ModelUsuario> listarIndividual(ModelUsuario user, Model model) {
        calculos.pegaPositivo(user, contador);
        calculos.pegaNegativo(user, contador);
        calculos.calcula(contador);
        this.margem = calculos.verificaMargem(contador);

        model.setMensagem(user.getNome() + "=+" + contador.getContPositivo() + "=-" + contador.getContNegativo() + "=Saldo=" + contador.getMedia() + "%");

        con = null;

        List<ModelUsuario> listarIndividual = new ArrayList<>();

        String sql = "select * from documentos_recebidos where Para_Quem like '" + user.getNome() + "' and Recebido='N'";

        int negativo = contador.getContNegativo();

        try {
            PreparedStatement ps = getCon().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if (rs != null) {

                model.setMensagem(user.getNome() + "=Pegando Relação de Documentos!");

                //declarando o tamanho do array, eu nao sei quanto é, mas será o resultado da conta do negativo
                this.codigo = new String[negativo];
                this.dataRecebimento = new String[negativo];
                this.quemEntregou = new String[negativo];
                this.empresa = new String[negativo];
                this.id = new String[negativo];
                this.historico = new String[negativo];

                while (rs.next()) {
                    this.codigo[cont] = rs.getString(1);
                    this.dataRecebimento[cont] = rs.getString(2);
                    this.quemEntregou[cont] = rs.getString(3);
                    this.empresa[cont] = rs.getString(4);
                    this.id[cont] = rs.getString(5);
                    this.historico[cont] = rs.getString(6);

                    cont++;
                }
                for (int i = 0; i < negativo; i++) {

                    TrataCalendario calendario = new TrataCalendario();
                    String novaData = calendario.converterDataInicial(this.dataRecebimento[i]);
                    long dias = calendario.calcularIntervalo(novaData);
                    
                    //messagem so receberá valor se o dia do recebimento do documentos não for o mesmo do alerta
                    if (dias > 0) {
                        //classe htmlentities vai formatar o meu texto para html, impedindo de aparecer palavras incompletas ou ilegiveis
                        HtmlEntities html = new HtmlEntities();
                        
                        mensagem += "<ul><li>"
                                + html.Converter("Protocolo: " + this.codigo[i])
                                + "\tData: " + novaData
                                + html.Converter("Entregue: " + this.quemEntregou[i])
                                + "\t"
                                + html.Converter("Empresa: " + this.empresa[i])
                                + "\t"
                                + html.Converter("ID: " + this.id[i])
                                + "</li><li>"
                                + html.Converter("Historico: " + this.historico[i])
                                +"</li></ul>";

                    }
                }
                /*caso o usuario so tenha 1 documento pendente e que chegou no dia atual,
                o codigo não sera executado, relatorio não sera gerado e email nao enviado
                */
                if (!mensagem.equals("")) {

                    model.setMensagem(user.getNome() + "=Relação pega! Montando Relatorio!");
                    Relatorios relatorios = new Relatorios();
                    //gerando pdf do relatorio e nomeando
                    String arquivo = relatorios.imprimir(user);
                    //*Enviara o email caso o obedeça a seguinte regra, arquivo gerado e mensagem existe
                    if (!arquivo.equals("")) {
                        model.setMensagem(user.getNome() + "=Relatorio gerado em:" + arquivo);
                        Grafico grafico = new Grafico();
                        String arquivoGrafico = grafico.gerarPizza(user, contador);
                        if(arquivoGrafico!=null){
                            model.setMensagem(user.getNome() + "=Grafico gerado em:" + arquivoGrafico);
                            AvisoEmail email = new AvisoEmail();
                            email.enviaAlerta(model, user, arquivoGrafico, mensagem, arquivo, contador, margem);
                        }
                    } else {
                        model.setMensagem(user.getNome() + "=Falha ao enviar o email, problema no arquivo gerado!");
                    }
                }
                else
                    model.setMensagem(user.getNome() + "=Não será gerado arquivo nem email, o funcionario so tem um registro da data atual!");
                con.close();
                return listarIndividual;
            }

        } catch (SQLException e) {
            model.setMensagem("Falha na relação da tabela do funcionario:"+user.getNome()+"\nSistema exige atenção! :"+sql);
        }
        return null;
    }

}
