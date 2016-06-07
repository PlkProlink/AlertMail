package br.com.tiago.utilitarios;

import br.com.tiago.model.Model;
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
    int diaSemana, diaDoMes, diaDivisivel, diaInicial, diaFim;
    private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    SimpleDateFormat sdtH = new SimpleDateFormat("dd/MM");
    GregorianCalendar calendar;
    
    public TrataCalendario(Model model){
        Date data = new Date();
        //*Instanciando o dia atual, mudei de Date para GegorianCalendar por causa dos codigos depreciados
        calendar = new GregorianCalendar();
        calendar.setTime(data);
        //Pegando dia do mes ex:25
        diaDoMes = calendar.get(Calendar.DAY_OF_MONTH);
        //Pegando o dia da semana de Domingo a Sabado
        diaSemana = calendar.get(Calendar.DAY_OF_WEEK);
            //dia divisivel, valor informado dentro do config
        diaDivisivel= Integer.parseInt(model.getDiaDivisivel());
        //Pegando dia inicial de tratamento (1 para domingo e 6 para sabado)- para enviar alertas, valor informado dentro do config
        diaInicial= Integer.parseInt(model.getDiaInicio());
        //Pegando dia fim de tratamento (1 para domingo e 6 para sabado), valor informado dentro do config
        diaFim = Integer.parseInt(model.getDiaFim());
    }
    
    public boolean tratamentoDatas(Model model){
    //dia da semana é maior ou igual ao dia inicial e menor que o ultimo dia
    if(diaSemana>=diaInicial && diaSemana <= diaFim){
        //divide a data pelo dia divisivel; 24/2 = Aprovado, 25/2=Reprovado 
            if(diaDoMes % diaDivisivel == 0){
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
    public String verificarDataHoje(){
        Date data = new Date();
        GregorianCalendar calendar;
        calendar = new GregorianCalendar();
        //data hora
        calendar.setTime(data);
        String dataHoje = sdtH.format(data);
        return dataHoje;
    }
    public String proximaData(int valor){
        Date data = new Date();
        GregorianCalendar novaData = new GregorianCalendar();
        novaData.setTime(data);
        novaData.add(Calendar.DAY_OF_MONTH, valor);
        Date dataNova = novaData.getTime();
        String proximoDia = sdtH.format(dataNova);
        return proximoDia;
    }
    //pega o dia da semana igual a sexta feira para o log
    public boolean pegaSexta(){
        return calendar.get(Calendar.DAY_OF_WEEK)==6;
    }
}
