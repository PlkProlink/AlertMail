/*
 * Todos direitos reservados a Tiago Dias de Souza.
 * OpenSource Project www.github.com.br/tiagods
 */
package br.com.tiago.model;

import java.io.Serializable;

/**
 *
 * @author Tiago
 */
public class ModelRelacao implements Serializable{
    private String Codigo;
    private String DataRecebimento;
    private String QuemEntregou;
    private String Empresa;
    private String Id;
    private String historico;

    /**
     * @return the Codigo
     */
    public String getCodigo() {
        return Codigo;
    }

    /**
     * @param Codigo the Codigo to set
     */
    public void setCodigo(String Codigo) {
        this.Codigo = Codigo;
    }

    /**
     * @return the DataRecebimento
     */
    public String getDataRecebimento() {
        return DataRecebimento;
    }

    /**
     * @param DataRecebimento the DataRecebimento to set
     */
    public void setDataRecebimento(String DataRecebimento) {
        this.DataRecebimento = DataRecebimento;
    }

    /**
     * @return the QuemEntregou
     */
    public String getQuemEntregou() {
        return QuemEntregou;
    }

    /**
     * @param QuemEntregou the QuemEntregou to set
     */
    public void setQuemEntregou(String QuemEntregou) {
        this.QuemEntregou = QuemEntregou;
    }

    /**
     * @return the Empresa
     */
    public String getEmpresa() {
        return Empresa;
    }

    /**
     * @param Empresa the Empresa to set
     */
    public void setEmpresa(String Empresa) {
        this.Empresa = Empresa;
    }

    /**
     * @return the Id
     */
    public String getId() {
        return Id;
    }

    /**
     * @param Id the Id to set
     */
    public void setId(String Id) {
        this.Id = Id;
    }

    /**
     * @return the historico
     */
    public String getHistorico() {
        return historico;
    }

    /**
     * @param historico the historico to set
     */
    public void setHistorico(String historico) {
        this.historico = historico;
    }
    
    
}
