package br.com.tiago.controller;

import br.com.tiago.model.ConfigModifyDao;
import br.com.tiago.model.DaoUsuario;
import br.com.tiago.model.Model;
import br.com.tiago.model.ModelRelacao;
import br.com.tiago.model.ModelContador;
import br.com.tiago.model.ModelFile;
import br.com.tiago.model.ModelUsuario;
import br.com.tiago.model.TodosOsUsuariosDao;
import br.com.tiago.model.TrataCalendario;
import br.com.tiago.model.UsuarioCalculos;
import br.com.tiago.utilitarios.AvisoEmail;
import br.com.tiago.utilitarios.Config;
import br.com.tiago.utilitarios.FileDelete;
import br.com.tiago.utilitarios.FileLog;
import br.com.tiago.utilitarios.Grafico;
import br.com.tiago.utilitarios.HtmlEntities;
import br.com.tiago.utilitarios.Relatorios;
import java.util.ArrayList;
import java.util.List;

/*
 * Todos direitos reservados a Tiago Dias de Souza.
 * OpenSource Project www.github.com.br/tiagods
 */

/**
 *
 * @author Tiago Dias
 */
public class ControllerPrincipal {
    
    List<ModelUsuario> listarTodos = new ArrayList<>();
            
    Model model;
    
    public void alterarHora(String hora){
        //chama alteração de hora
        model = new Model();
        ConfigModifyDao modify = new ConfigModifyDao();
        modify.alterarHora(model, hora);
        model.setMensagem("Serviço Reiniciado!");
        
    }
    //leitura completa do arquivo config
    public void lerConfig(Model model){
        Config config = new Config();
        config.lerArquivo(model);
        config.trataValores(model);
    }
    //criar log do textArea, geralmente ao parar o serviço ou ao finalizar
    public boolean gerarLog(String aviso){
        model = new Model();
        
        model.setMensagem(aviso);
        ModelFile modelFile = new ModelFile();
        //classe que realiza as conversoes de data e hora
        FileLog file = new FileLog();
        //pegando data e hora para nome do arquivo
        if(file.writer(model, modelFile)==true){
            model.limparTela();
            model.setMensagem("Arquivo de log criando com sucesso");
            return true;
        }
        else{
            model.setMensagem("Log não criado!");
            return false;
        }
    }
//    public static void main(String[] args){
//        new ControllerPrincipal();
//        
//    }
//    public ControllerPrincipal(){
//        iniciarAlertas();
//    }
    //responsavel por iniciar os tratamentos de envio, so carrega se a hora for aprovada
    public void iniciarAlertas(){
        model = new Model();
        lerConfig(model);
        TrataCalendario c = new TrataCalendario();
        if(c.tratamentoDatas(model)==true){
            //se a data atua obedecer ao tratamento da classe calendario os envios serão iniciados
            model.setMensagem("Dia e hora aprovada para envio!");
            model.setMensagem("Iniciando alerta!");
            
            TodosOsUsuariosDao usuarios = new TodosOsUsuariosDao();
            listarTodos = usuarios.ListarTodos();
            Thread();
        }
        }
    
public class EnviarAlerta implements Runnable{
         @Override
         public void run() {
                  for(ModelUsuario user : listarTodos){
                           System.out.println("Tratamento de Usuario: "+user.getNome()+" e Email: "+user.getEmail());

                           ModelContador contador = new ModelContador();
                           UsuarioCalculos calculos = new UsuarioCalculos();
                           //calculando saldo de documentos finalizados e pendentes
                           calculos.pegaPositivo(user, contador);
                           calculos.pegaNegativo(user, contador);
                           calculos.calcula(contador);
                           //pegando mensagem para margem
                           String margem = calculos.verificaMargem(contador);
                                                      
                           DaoUsuario pegaRelacao = new DaoUsuario();
                           List<ModelRelacao> listaRelacao = new ArrayList<>();
                           listaRelacao = pegaRelacao.listaIndividual(user);
                           
                           //usando String builder para agilizar processamento
                           StringBuilder builder = new StringBuilder();
                           String inicioTabela="<div style=\"text-align: center;\"><table align=\"center\" border=\"1\" cellpadding=\"0\" cellspacing=\"0\">";
                           String cabecalho="<thead><tr>"
                                   +"<th scope=\"col\">Protocolo</th>"
                                   +"<th scope=\"col\">Data</th>"
                                   +"<th scope=\"col\">Entregue Por</th>"
                                   +"<th scope=\"col\">Empresa</th>"
                                   +"<th scope=\"col\">ID</th>"
                                   +"<th scope=\"col\">Historico</th>"
                                   + "</tr>";
                           String fimTabela="</table></div>";
                           
                           for(ModelRelacao relacao :  listaRelacao){
                                    TrataCalendario calendario = new TrataCalendario();
                                    String novaData = calendario.converterDataInicial(relacao.getDataRecebimento());
                                    long dias = calendario.calcularIntervalo(novaData);
                                    //messagem so receberá valor se o dia do recebimento do documentos não for o mesmo do alerta
                                    if(dias>0){
                                             //classe htmlentities vai formatar o meu texto para html, impedindo de aparecer palavras incompletas ou ilegiveis
                                             HtmlEntities html = new HtmlEntities();
                                             builder.append("<tr><td >")
                                                     .append(html.Converter(relacao.getCodigo()))
                                                     .append("</td><td>")
                                                     .append(novaData)
                                                     .append("</td><td>")
                                                     .append(html.Converter(relacao.getQuemEntregou()))
                                                     .append("</td><td>")
                                                     .append(html.Converter(relacao.getEmpresa()))
                                                     .append("</td><td>")
                                                     .append(html.Converter(relacao.getId()))
                                                     .append("</td><td>")
                                                     .append(html.Converter(relacao.getHistorico()))
                                                     .append("</td></tr>");
                                    }
                           
                           }
                           if (!builder.toString().equals("")) {
                                    model.setMensagem(user.getNome() + "=Relação pega! Montando Relatorio!");
                                    Relatorios relatorios = new Relatorios();
                                    //gerando pdf do relatorio e renomeando
                                    String arquivoRelatorio = relatorios.imprimir(user);
                                    //*Enviara o email caso o obedeça a seguinte regra, arquivo gerado e mensagem existe
                                    if (!arquivoRelatorio.equals("")) {
                                             model.setMensagem(user.getNome() + "=Relatorio gerado em:" +arquivoRelatorio);
                                             Grafico grafico = new Grafico();
                                             String arquivoGrafico = grafico.gerarPizza(user, contador);
                                             if(arquivoGrafico!=null){
                                                      model.setMensagem(user.getNome() + "=Grafico gerado em:" + arquivoGrafico);
                                                      AvisoEmail email = new AvisoEmail();
                                                      //montando detalhes do relatório
                                                      String detalhes =  inicioTabela+cabecalho+builder.toString()+fimTabela;
                                                      email.enviaAlerta(model, user, arquivoGrafico, detalhes, arquivoRelatorio, contador, margem);
                                             }else
                                                      model.setMensagem(user.getNome()+"=Falha ao enviar o e-mail, problema no arquivo gráfico!");
                                    }else model.setMensagem(user.getNome() + "=Falha ao enviar o e-mail, problema no arquivo gerado!");
                           }else{model.setMensagem(user.getNome() + "=Não será gerado arquivo nem email, "
                                                                                            + "o funcionario só tem apenas um registro da data atual!");}
                           try{
                                    Thread.sleep(600*1000);
                           }catch(InterruptedException e){  
                           }
                  }
         FileDelete fl = new FileDelete();
         fl.removeArquivos();
         model.setMensagem("Concluido! Já enviei tudo hoje, até logo!");
         Thread.interrupted();
         }
}
    
    public void Thread(){
        EnviarAlerta ealerta = new EnviarAlerta();
        Thread alerta = new Thread(ealerta);
        alerta.start();
    }
    
}
