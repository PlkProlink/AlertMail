package br.com.tiago.utilitarios;

import br.com.tiago.model.Model;
import br.com.tiago.model.ModelFile;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/*
 * Todos direitos reservados a Tiago Dias de Souza.
 * OpenSource Project www.github.com.br/tiagods
 */
/**
 *
 * @author Tiago Dias
 */
public class FileLog {

    String nome;
    //static File file = new File(nome+".txt");
    File file;
    FileWriter fWriter;

    private void gerarTXT(Model model) {
        if (file.exists() == false) {
            try {
                file.createNewFile();
            } catch (IOException ex) {
                model.setMensagem("Não foi possivel gerar o arquivo de log!");
            }
        }
    }

    public boolean writer(Model model, ModelFile modelFile) {
        String conteudo = model.getMensagem();
        nome = ("log/log" + modelFile.getDataFile() + modelFile.getHoraFile() + ".txt");
        file = new File(nome);
        gerarTXT(model);
        try {
            fWriter = new FileWriter(nome, true);
            conteudo += System.getProperty("line.separator");
            fWriter.write(conteudo);
            fWriter.close();
            model.setMensagem("Log gerado com sucesso!");
            return true;
        } catch (IOException ex) {
            model.setMensagem("Não foi possivel gerar o arquivo de log!");
            return false;
        }
    }
}
