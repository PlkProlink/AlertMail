/*
 * Todos direitos reservados a Tiago Dias de Souza.
 * OpenSource Project www.github.com.br/tiagods
 */
package br.com.tiago.utilitarios;

import java.io.File;
import java.io.IOException;

/**
 *
 * @author Tiago
 */
public class FileControle {
    String dir = "";
    //função para remover arquivos antigos, limpando a base
    public boolean removeArquivos(String diretorio){
        this.dir = diretorio;
        File file1 = new File(dir);
        if(file1.exists()){
            File[] file = file1.listFiles();
            for(File f : file){
                f.delete();
            }
            return true;
        }
        else
            return false;
    }
    public boolean criarDiretorio(String diretorio){
        this.dir = diretorio;
        File file = new File(dir);
        if(!file.exists()){
            file.mkdir();
            return true;
        }
        return false;
    }
    
    public boolean removerDiretorio(String diretorio){
        this.dir = diretorio;
        File file = new File(dir);
        if(file.exists()){
            file.delete();
            return true;
        }
        return false;
    }
}
