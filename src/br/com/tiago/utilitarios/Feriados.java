/*
 * Todos direitos reservados a Tiago Dias de Souza
 * www.github.com/tiagods
 */
package br.com.tiago.utilitarios;

/**
 *
 * @author Tiago
 */
public class Feriados {
    static String[] dados = {"01/01","25/01","09/02","25/03","27/03","21/04",
    "01/05","26/05","07/09","12/10","02/11","15/11","25/12"};
/*    
"01/01" //Ano Novo
"25/01" //Aniversario de São Paulo
"09/02" //Carnaval
"25/03" //Sexta-Feira da Paixa
"27/03" //Pascoa
"21/04" //Tiradentes
"01/05" //Dia do Trabalho
"26/05" //Corpus Christi
"07/09" //Independência do Brasil
"12/10" //Nossa Senhora Aparecida
"02/11" //Finados
"15/11" //Proclamação da República
"25/12" //Natal
*/
    public String buscarFeriado(String valor){
        for(int i=0; i<dados.length; i++){
            if(dados[i].equals(valor)){
                return comemoracao(dados[i]);
            }
        }
        return null;
    }
    private String comemoracao(String valor){
        switch(valor){
            case "01/01":
                return "Ano Novo";
            case "25/01":
                return "Aniversario de São Paulo";
            case "09/02": 
                return "Carnaval";
            case "25/03": 
                return "Sexta-Feira da Paixa";
            case "27/03":
                return "Pascoa";
            case "21/04":
                return "Tiradentes";
            case "01/05":
                return "Dia do Trabalho";
            case "26/05":
                return "Corpus Christi";
            case "07/09":
                return "Independência do Brasil";
            case "12/10":
                return "Nossa Senhora Aparecida";
            case "02/11":
                return "Finados";
            case "15/11":
                return "Proclamação da República";
            case "25/12":
                return "Natal";
            default:
                break;
        }
        return null;
    }
}
