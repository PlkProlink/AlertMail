/*
 * Todos direitos reservados a Tiago Dias de Souza.
 * OpenSource Project www.github.com.br/tiagods
 */
package br.com.tiago.utilitarios;

import java.io.File;

/**
 *
 * @author Tiago
 */
public class FileDelete {
    String dir1 = "graficos";
    String dir2 = "arquivos";
    File file1 = new File(dir1);
    File file2 = new File(dir2);
    //função para remover arquivos antigos, limpando a base
    public void removeArquivos(){
                File[] file = file1.listFiles();
                for(File f : file){
                    f.delete();
                }
                File[] fl2 = file2.listFiles();
                for(File fl : fl2){
                    fl.delete();
                }
        
    }
}
