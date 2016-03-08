package br.com.tiago.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * Todos direitos reservados a Tiago Dias de Souza.
 * OpenSource Project www.github.com.br/tiagods
 */

/**
 *
 * @author Tiago Dias
 */
public class TrataCalendario {
    
    String diasPassados; //dias que a validação encontra-se pendente
    
    private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    
    public boolean tratamentoDatas(Model model){
    
        
    Date data = new Date();
    //*Instanciando o dia atual, mudei de Date para GegorianCalendar por causa dos codigos depreciados
    GregorianCalendar calendar = new GregorianCalendar();
    calendar.setTime(data);
    //Pegando dia do mes ex:25
    int diaDoMes = calendar.get(Calendar.DAY_OF_MONTH);
    //Pegando o dia da semana de Domingo a Sabado
    int diaSemana = calendar.get(Calendar.DAY_OF_WEEK);
    
    //dia divisivel, valor informado dentro do config
    int diaDivisivel= Integer.parseInt(model.getDiaDivisivel());
    //Pegando dia inicial de tratamento (1 para domingo e 6 para sabado)- para enviar alertas, valor informado dentro do config
    int diaInicial= Integer.parseInt(model.getDiaInicio());
    //Pegando dia fim de tratamento (1 para domingo e 6 para sabado), valor informado dentro do config
    int diaFim = Integer.parseInt(model.getDiaFim());
    
    //dia da semana é maior ou igual ao dia inicial e menor que o ultimo dia
    if(diaSemana>=diaInicial && diaSemana <= diaFim){
        //divide a data pelo dia divisivel; 24/2 = Aprovado, 25/2=Reprovado 
            if(diaDoMes % diaDivisivel == 0){
               model.setMensagem("Data e horário aprovado para envio de alertas!");
               //aprovado envio se iniciará
               return true;
            }
            else
               model.setMensagem("Dia não aprovado para envio de alertas!\n"+
                                "Nova tentativa no proximo dia!");
    }
        return false;
    }
    public long calcularIntervalo(String inicio){
        //esse trecho de codigo é justamente para não enviar alertas caso o dia de recebimento seja o mesmo dia do alerta, ou seja 0
        long dias=0;
        try {
            Date dataIn = sdf.parse(inicio);
            Date dataFim = new Date();
            dias = (dataFim.getTime() - dataIn.getTime()+ 3600000L) / 86400000L;
            
        } catch (ParseException ex) {
            Logger.getLogger(TrataCalendario.class.getName()).log(Level.SEVERE, null, ex);
        }
        return dias;
    }
    public String converterDataInicial(String inicio){
        //conversor simples de datas
        String ano = inicio.substring(0, 4);
        String mes = inicio.substring(5, 7);
        String dia = inicio.substring(8);
        return dia+"/"+mes+"/"+ano;
    }
    
}
