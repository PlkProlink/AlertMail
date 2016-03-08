package br.com.tiago.controller;

import br.com.tiago.model.TrataCalendario;
import br.com.tiago.model.ConfigModifyDao;
import br.com.tiago.model.Model;
import br.com.tiago.model.ModelFile;
import br.com.tiago.model.TodosOsUsuariosDao;
import br.com.tiago.utilitarios.Config;
import br.com.tiago.utilitarios.FileLog;

/*
 * Todos direitos reservados a Tiago Dias de Souza.
 * OpenSource Project www.github.com.br/tiagods
 */

/**
 *
 * @author Tiago Dias
 */
public class ControllerPrincipal {
    
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
            usuarios.ListarTodos(model);
        }
    }
    
}
