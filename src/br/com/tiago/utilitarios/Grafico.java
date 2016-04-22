/*
 * Todos direitos reservados a Tiago Dias de Souza.
 * OpenSource Project www.github.com.br/tiagods
 */
package br.com.tiago.utilitarios;

import br.com.tiago.model.ModelContador;
import br.com.tiago.model.ModelFile;
import br.com.tiago.model.ModelUsuario;
import java.awt.Color;
import java.io.File;
import java.io.IOException;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.util.Rotation;

/**
 *
 * @author Tiago
 */
public class Grafico {
    public String gerarPizza(ModelUsuario user, ModelContador contador, String diretorio){
        
        // create a dataset...
        PieDataset dataset = createSampleDataset(contador);
        // create the chart...
        JFreeChart chart = createChart(dataset);
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setSize(650,380);
        
        ModelFile moFile = new ModelFile();
        String nomeAndDir = diretorio+"/"+user.getNome()+moFile.getDataFile()+moFile.getHoraFile()+".jpg";
        File filePie = new File(nomeAndDir);
        try {
            ChartUtilities.saveChartAsJPEG(filePie, chart, 650,380);
            return nomeAndDir;
        } catch (IOException ex) {
            return "";
        }
    }
    
    private PieDataset createSampleDataset(ModelContador contador) {
        
        final DefaultPieDataset result = new DefaultPieDataset();
        result.setValue("Abertos", new Double(contador.getContNegativo()));
        result.setValue("Finalizados", new Double(contador.getContPositivo()));
        return result;
        
    }
    private JFreeChart createChart(final PieDataset dataset) {
        
        final JFreeChart chart = ChartFactory.createPieChart3D(
            "Minha Visão",  // chart title
            dataset,                // data
            true,                   // include legend
            true,
            false
        );

        final PiePlot3D plot = (PiePlot3D) chart.getPlot();
        plot.setLabelGenerator(new StandardPieSectionLabelGenerator("{0} = {1} ({2})"));
        plot.setStartAngle(290);
        plot.setDirection(Rotation.CLOCKWISE);
        plot.setForegroundAlpha(0.5f);
        plot.setBackgroundPaint(Color.white);
        //plot.setNoDataMessage("No data to display");
        return chart;
        
    }
}
