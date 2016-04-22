package br.com.tiago.model;

import br.com.tiago.factory.ConnectionFactory;
import br.com.tiago.utilitarios.Config;
import br.com.tiago.view.MenuView;
import java.sql.Connection;

/*
 * Todos direitos reservados a Tiago Dias de Souza.
 * OpenSource Project www.github.com.br/tiagods
 */

/**
 *
 * @author Tiago Dias
 */
public class ConfigModifyDao {

    ConnectionFactory conection;
    
    Connection con;
    
    int quantidade = 0;
    //modificador de horas
    public void alterarHora(Model model,String hora, String diretorio){
    //obedecendo o criterio de ter sido preenchido corretamente prosseguira para a proxima validação
        if(hora.trim().length()==5){
            //usar string para enviar para o config
            String hh = hora.substring(0, 2);
            int horas = Integer.parseInt(hh);
            String min= hora.substring(3);
            int minutos = Integer.parseInt(hora.substring(3));
            //validando hora informada
            if(horas<8 && horas>18){
                MenuView.txtAlerta.setText("Horário de Alerta não pode ser menor que 8:00 ou maior que 18:00!");
            }
            //validando os minutos informados
            else if(minutos>59)
                MenuView.txtAlerta.setText("Minutos foram informados incorretamente!");
            
            else{
                //tempo aceito, será gravado no config a alteração
                String time=hh+":"+min+":00";
                model.setHoraAlerta(time);
                
                Config config = new Config();
                config.alterarHora(model, diretorio);
                
                model.setMensagem("Nova execução agendada as"+time+"\n");
                

            }
        }        
    }
    
}
