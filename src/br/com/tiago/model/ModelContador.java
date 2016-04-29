package br.com.tiago.model;

import java.io.Serializable;

/*
 * Todos direitos reservados a Tiago Dias de Souza.
 * OpenSource Project www.github.com.br/tiagods
 */

/**
 *
 * @author Tiago Dias
 */
public class ModelContador implements Serializable{

    private int contPositivo;
    private int contNegativo;
    private int saldo;
    private Double media;
    private String nomeRelatorio;
    /**
     * @return the contPositivo
     */
    public int getContPositivo() {
        return contPositivo;
    }

    /**
     * @param contPositivo the contPositivo to set
     */
    public void setContPositivo(int contPositivo) {
        this.contPositivo = contPositivo;
    }

    /**
     * @return the contNegativo
     */
    public int getContNegativo() {
        return contNegativo;
    }

    /**
     * @param contNegativo the contNegativo to set
     */
    public void setContNegativo(int contNegativo) {
        this.contNegativo = contNegativo;
    }

    /**
     * @return the saldo
     */
    public int getSaldo() {
        return saldo;
    }

    /**
     * @param saldo the saldo to set
     */
    public void setSaldo(int saldo) {
        this.saldo = saldo;
    }

    /**
     * @return the media
     */
    public Double getMedia() {
        return media;
    }

    /**
     * @param media the media to set
     */
    public void setMedia(Double media) {
        this.media = media;
    }

    /**
     * @return the nomeRelatorio
     */
    public String getNomeRelatorio() {
        return nomeRelatorio;
    }

    /**
     * @param nomeRelatorio the nomeRelatorio to set
     */
    public void setNomeRelatorio(String nomeRelatorio) {
        this.nomeRelatorio = nomeRelatorio;
    }
}
