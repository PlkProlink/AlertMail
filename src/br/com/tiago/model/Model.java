package br.com.tiago.model;

import br.com.tiago.view.MenuView;
import java.text.SimpleDateFormat;
import java.util.Date;

/*
 * Todos direitos reservados a Tiago Dias de Souza.
 * OpenSource Project www.github.com.br/tiagods
 */

/**
 *
 * @author Tiago Dias
 */
public class Model {
    
    Date data = new Date();
    
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    SimpleDateFormat sdh = new SimpleDateFormat("HH:mm");
    
    private String horaAgora;
    private String dataAgora;
    
    private String dataInicio;
    private String dataFim;
    
    private String horaAlerta="08:00:00";
    private String diaInicio;
    private String diaFim;
    private String diaDivisivel;
    
    private String mensagem=MenuView.txtMensagem.getText();

    public String getDataInicio(){
        return dataInicio;
    }
    
    public void setDataInicio(String dataInicio){
        this.dataInicio = dataInicio;
    }
    
    public String getDataFim(){
        return dataFim;
    }
    
    public void setDataFim(String dataFim){
        this.dataFim=dataFim;
    }
    
    public void setHoraAgora(String hora){
        this.horaAgora=hora;
    }
    /**
     * @return the horaAlerta
     */
    public String getHoraAlerta() {
        return horaAlerta;
    }
    /**
     * @param horaAlerta the horaAlerta to set
     */
    public void setHoraAlerta(String horaAlerta) {
        this.horaAlerta = horaAlerta;
    }
    /**
     * @return the diaInicio
     */
    public String getDiaInicio() {
        return diaInicio;
    }

    /**
     * @param diaInicio the diaInicio to set
     */
    public void setDiaInicio(String diaInicio) {
        this.diaInicio = diaInicio;
    }
    /**
     * @return the diaFim
     */
    public String getDiaFim() {
        return diaFim;
    }
    /**
     * @param diaFim the diaFim to set
     */
    public void setDiaFim(String diaFim) {
        this.diaFim = diaFim;
    }
    /**
     * @return the diaDivisivel
     */
    public String getDiaDivisivel() {
        return diaDivisivel;
    }
    /**
     * @param diaDivisivel the diaDivisivel to set
     */
    public void setDiaDivisivel(String diaDivisivel) {
        this.diaDivisivel = diaDivisivel;
    }
    
    public String getHoraAgora(){
        this.horaAgora = sdh.format(data);
        //model.setHoraAgora(hora);
        return horaAgora;
    }
    
    public String getDataAgora(){
        this.dataAgora = sdf.format(data);
        
        return dataAgora;
        
    }
    public void setMensagem(String msg){
        this.mensagem = MenuView.txtMensagem.getText();
        String quebra="";
        if(this.mensagem.length()>=1){
            quebra="\n";
        }
        MenuView.txtMensagem.setText(this.mensagem+quebra+getDataAgora()+"="+getHoraAgora()+"="+msg);
    }
    public String getMensagem(){
        return mensagem;
    }
    
    public void limparTela(){
        MenuView.txtMensagem.setText("");
    }
}
