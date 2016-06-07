package br.com.tiago.controller;

import br.com.tiago.factory.ConnectionFactory;
import br.com.tiago.model.ConfigModifyDao;
import br.com.tiago.model.UsuarioDao;
import br.com.tiago.model.Model;
import br.com.tiago.model.ModelRelacao;
import br.com.tiago.model.ModelContador;
import br.com.tiago.model.ModelFile;
import br.com.tiago.model.ModelUsuarioBean;
import br.com.tiago.model.TodosOsUsuariosDao;
import br.com.tiago.utilitarios.TrataCalendario;
import br.com.tiago.model.UsuarioCalculos;
import br.com.tiago.utilitarios.AvisoEmail;
import br.com.tiago.utilitarios.AvisoSimples;
import br.com.tiago.utilitarios.Config;
import br.com.tiago.utilitarios.Feriados;
import br.com.tiago.utilitarios.FileControle;
import br.com.tiago.utilitarios.FileLog;
import br.com.tiago.utilitarios.Grafico;
import br.com.tiago.utilitarios.HtmlEntities;
import br.com.tiago.utilitarios.Relatorios;
import java.sql.Connection;
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
    
    List<ModelUsuarioBean> listarTodos = new ArrayList<>();
    String arquivo="arquivos", grafico="graficos", log="log", config="config";         
    Model model;
    int time=600;
    
    public void iniciarDiretorios(){
        FileControle diretorios = new FileControle();
        diretorios.criarDiretorio(arquivo);
        diretorios.criarDiretorio(grafico);
        diretorios.criarDiretorio(log);
        diretorios.criarDiretorio(config);
    }
    public void alterarHora(String hora){
        iniciarDiretorios();        
        //chama alteração de hora
        model = new Model();
        ConfigModifyDao modify = new ConfigModifyDao();
        modify.alterarHora(model, hora, config);
        model.setMensagem("Serviço Reiniciado!");
        
    }
    //leitura completa do arquivo config
    public void lerConfig(Model model){
        Config configuracao = new Config();
        configuracao.lerArquivo(model, config);
        configuracao.trataValores(model, config);
    
    }
    //criar log do textArea, geralmente ao parar o serviço ou ao finalizar tela
    public void gerarLog(String aviso){
        iniciarDiretorios();
        model = new Model();
        
        model.setMensagem(aviso);
        ModelFile modelFile = new ModelFile();
        //classe que realiza as conversoes de data e hora
        FileLog file = new FileLog();
        //pegando data e hora para nome do arquivo
        if(file.writer(model, modelFile, log)){
            model.limparTela();
            model.setMensagem("Arquivo de log criando com sucesso");
            lerConfig(model);
            TrataCalendario c = new TrataCalendario(model);
            if(c.pegaSexta())
                enviarLog(modelFile, model);
        }
        else{
            model.setMensagem("Log não criado!");
        }
    }
    private void enviarLog(ModelFile modelFile, Model model){
        AvisoSimples simplesMail = new AvisoSimples();
        if(simplesMail.enviarEmail(modelFile.getNomeLog(), "suporte.ti@prolinkcontabil.com.br"))
            model.limparTela();
    }
    public void verificaConexao(Model model){
        Connection con=null;
        try{
            con = new ConnectionFactory().getConnetion();
            if(con==null) model.setMensagem("Conexao com o banco bem sucedida!");
        }catch(Exception e){if(con==null) model.setMensagem("Conexao com o banco com problemas!");}
    }
    
    //responsavel por iniciar os tratamentos de envio, so carrega se a hora for aprovada
    public void tratandoAlerta(){
        iniciarDiretorios();
        model = new Model();
        lerConfig(model);
        TrataCalendario c = new TrataCalendario(model);
        Feriados feriado = new Feriados();
      
        if(c.tratamentoDatas(model)==true){
            
            //se a data atua obedecer ao tratamento da classe calendario os envios serão iniciados
            String dataHoje = c.verificarDataHoje();
            if(feriado.buscarFeriado(dataHoje)==null){

                model.setMensagem("Dia e hora aprovada para envio!");
                model.setMensagem("Iniciando alerta!");

                TodosOsUsuariosDao usuarios = new TodosOsUsuariosDao();
                listarTodos = usuarios.ListarTodos();
                Thread();
            }else
                model.setMensagem("Hoje não será enviado alerta! É feriado de " +dataHoje+
                        " \nNova tentativa no proximo dia útil.");
        }
        }

    public void Thread(){
        EnviarAlerta ealerta = new EnviarAlerta();
        Thread alerta = new Thread(ealerta);
        alerta.start();
    }    
    public class EnviarAlerta implements Runnable{
         @Override
         public void run() {
             FileControle fl = new FileControle();
                  for(ModelUsuarioBean user : listarTodos){
                           System.out.println("Tratamento de Usuario: "+user.getNome()+" e Email: "+user.getEmail());

                           ModelContador contador = new ModelContador();
                           UsuarioCalculos calculos = new UsuarioCalculos();
                           //calculando saldo de documentos finalizados e pendentes
                           calculos.pegaPositivo(user, contador);
                           calculos.pegaNegativo(user, contador);
                           calculos.calcula(contador);
                           //pegando mensagem para margem
                           String margem = calculos.verificaMargem(contador);
                                                      
                           UsuarioDao pegaRelacao = new UsuarioDao();
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
                                    TrataCalendario calendario = new TrataCalendario(model);
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
                                    //criando diretorios se nao existir
                                    fl.criarDiretorio(arquivo);
                                    //gerando pdf do relatorio e renomeando
                                    String arquivoRelatorio = relatorios.imprimir(user, arquivo);
                                    
                                    //*Enviara o email caso o obedeça a seguinte regra, arquivo gerado e mensagem existe
                                    if (!arquivoRelatorio.equals("")) {
                                             model.setMensagem(user.getNome() + "=Relatorio gerado em:" +arquivoRelatorio);
                                             Grafico graf = new Grafico();
                                             //criar diretorio se nao existir
                                             fl.criarDiretorio(grafico);
                                             String arquivoGrafico = graf.gerarPizza(user, contador, grafico);
                                             if(arquivoGrafico!=null){
                                                      model.setMensagem(user.getNome() + "=Grafico gerado em:" + arquivoGrafico);
                                                      AvisoEmail email = new AvisoEmail();
                                                      //montando detalhes do relatório
                                                      String detalhes =  inicioTabela+cabecalho+builder.toString()+fimTabela;
                                                      if(email.enviaAlerta(model, user, arquivoGrafico, detalhes, arquivoRelatorio, contador, margem)){
                                                               model.setMensagem(user.getEmail()+"=Alerta Enviado com Sucesso!");
                                                      }
                                                      else
                                                          model.setMensagem(user.getEmail()+"=Não foi possivel disparar o alerta!");
                                                      model.setMensagem("=Aguardando cronômetro para disparar o próximo aviso em "+time/60+" minutos!");
                                                      
                                             }else
                                                      model.setMensagem(user.getNome()+"=Falha ao enviar o e-mail, problema no arquivo gráfico!");
                                    }else model.setMensagem(user.getNome() + "=Falha ao enviar o e-mail, problema no arquivo gerado!");
                           }else{model.setMensagem(user.getNome() + "=Não será gerado arquivo nem email, "
                                                                                            + "o funcionario só tem apenas um registro da data atual!");}
                           try{
                               
                                    Thread.sleep(time*1000);
                           }catch(InterruptedException e){  
                           }
                  }
        //Removendo todos os arquivos criados não necessários
        if(fl.removeArquivos(arquivo)){
            model.setMensagem("Diretorio "+arquivo+" limpo!");
        }
        else
            model.setMensagem("Opa, algo errado no diretorio "+arquivo+", não consegui limpar!");
        if(fl.removeArquivos(grafico)){
            model.setMensagem("Diretorio "+grafico+" limpo!");
        }
        else
            model.setMensagem("Opa, algo errado no diretorio "+grafico+", não consegui limpar!");
         
        model.setMensagem("Concluido! Já enviei tudo hoje, até logo!");
        gerarLog("Finalizando todas as entregas!");
        Thread.interrupted();
        }
        
    }
    
}
