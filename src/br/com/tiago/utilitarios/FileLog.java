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

    //static File file = new File(nome+".txt");
    File file;
    FileWriter fWriter;

    private void gerarTXT(Model model) {
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException ex) {
                model.setMensagem("Não foi possivel gerar o arquivo de log!");
            }
        }
    }

    public boolean writer(Model model, ModelFile modelFile, String diretorio) {
        
        //conteudo do log é a mensagem do view
        String conteudo = model.getMensagem();
        //jogar nome do arquivo no metodo nomeLog da classe ModelFile
        modelFile.setNomeLog(diretorio+"/log" + modelFile.getDataFile() + modelFile.getHoraFile() + ".txt");
        file = new File(modelFile.getNomeLog());
        //gerar arquivo txt se não existe
        gerarTXT(model);
        try {
            fWriter = new FileWriter(modelFile.getNomeLog(), true);
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
