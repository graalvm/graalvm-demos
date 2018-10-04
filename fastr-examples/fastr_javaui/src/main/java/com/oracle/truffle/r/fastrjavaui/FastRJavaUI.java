package com.oracle.truffle.r.fastrjavaui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionListener;
import java.util.function.Supplier;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Value;

public class FastRJavaUI {
    static final class PlotParams {
        final int clustersCount;
        final String xVar;
        final String yVar;

        PlotParams(int clustersCount, String xVar, String yVar) {
            this.clustersCount = clustersCount;
            this.xVar = xVar;
            this.yVar = yVar;
        }
    }

    static final class PlotJPanel extends JPanel {
        private final Supplier<PlotParams> paramsSupplier;
        private Context context = null;
        private Value showPlot = null;

        PlotJPanel(Supplier<PlotParams> paramsSupplier) {
            this.paramsSupplier = paramsSupplier;
        }

        @Override
        public void paint(Graphics g) {
            super.paint(g);
            if (context == null) {
                context = Context.newBuilder("R").allowAllAccess(true).build();
                // This R function opens FastR graphics device passing it Graphics2D object,
                // then it plots the graph and closes the device
                String src = "library(grid); library(lattice); " +
                    "function(g, w, h, clustersCount, x, y) { " +
                    "   grDevices:::awt(w, h, g);" +
                    "   iris$cluster <- factor(kmeans(iris[, c(y, x)], clustersCount)$cluster);" +
                    "   print(xyplot(as.formula(paste0(y,'~',x)), data=iris, groups=cluster, pch=20, cex=3));" +
                    "   dev.off();" +
                    "   NULL;" +
                    "}";
                showPlot = context.eval("R", src);
            }
            PlotParams params = paramsSupplier.get();
            // The MAGIC happens HERE: we invoke R plotting code and pass it graphics object
            showPlot.execute((Graphics2D) g, getWidth(), getHeight(), params.clustersCount, params.xVar, params.yVar);
        }
    }

    //Create and set up the window -- this is standard Java/Swing
    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Hello World to R from Java");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(750, 500));

        JComboBox<Integer> clustersCombo = new JComboBox<>(new Integer[]{2, 3, 4, 5, 6, 7});
        clustersCombo.setSelectedIndex(2);
        String[] variables = {"Sepal.Length", "Sepal.Width", "Petal.Length", "Petal.Width"};
        JComboBox<String> xAxisCombo = new JComboBox<>(variables);
        JComboBox<String> yAxisCombo = new JComboBox<>(variables);
        yAxisCombo.setSelectedIndex(1);

        JPanel plot = new PlotJPanel(() ->
                new PlotParams(
                        (Integer) clustersCombo.getSelectedItem(),
                        (String) xAxisCombo.getSelectedItem(),
                        (String) yAxisCombo.getSelectedItem()));
        ActionListener updatePlot = e -> plot.repaint();
        clustersCombo.addActionListener(updatePlot);
        xAxisCombo.addActionListener(updatePlot);
        yAxisCombo.addActionListener(updatePlot);

        JPanel options = new JPanel();
        options.setLayout(new FlowLayout(FlowLayout.CENTER));
        options.add(new JLabel("X Variable: "));
        options.add(xAxisCombo);
        options.add(new JLabel("Y Variable: "));
        options.add(yAxisCombo);
        options.add(new JLabel("Clusters count: "));
        options.add(clustersCombo);
        xAxisCombo.setBorder(new EmptyBorder(0, 0, 0, 40));
        yAxisCombo.setBorder(new EmptyBorder(0, 0, 0, 40));
        clustersCombo.setBorder(new EmptyBorder(0, 0, 0, 40));

        frame.setLayout(new BorderLayout(20, 20));
        Container pane = frame.getContentPane();
        pane.add(options, BorderLayout.PAGE_START);
        pane.add(plot, BorderLayout.CENTER);

        frame.pack();
        frame.setVisible(true);
    }

    // We call to R through this interface.
    @FunctionalInterface
    interface ShowPlot {
        void show(Graphics2D graphics2D, int width, int height, int clustersCount, String x, String y);
    }

    public static void main(String[] args) throws Exception {
        javax.swing.SwingUtilities.invokeLater(() -> createAndShowGUI());
    }
}
