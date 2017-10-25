package com.company;


import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GraphingData extends JPanel {


    private int padding = 25;
    private int labelPadding = 25;
    private Color lineColor = new Color(0, 0, 255, 180);
    private Color pointColor = new Color(255, 0, 0, 180);
    private Color gridColor = new Color(200, 200, 200, 200);
    private static final Stroke GRAPH_STROKE = new BasicStroke(2f);
    private int pointWidth = 4;
    private int numberYDivisions = 10;
    public static int[] scores;

    public GraphingData(int[] scores) {
        this.scores = scores;


    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        double xScale = ((double) getWidth() - (2 * padding) - labelPadding) / (scores.length-1);
        double yScale = ((double) getHeight() - 2 * padding - labelPadding) / (getMaxScore() - getMinScore());

        List<Point> graphPoints = new ArrayList<>();
        for (int i = 0; i < scores.length; i++) {
            int x1 = (int) (i * xScale + padding + labelPadding);
            int y1 = (int) ((getMaxScore() - scores[i]) * yScale + padding);
            graphPoints.add(new Point(x1, y1));
        }

        // draw white background
        g2.setColor(Color.WHITE);
        g2.fillRect(padding + labelPadding, padding, getWidth() - (2 * padding) - labelPadding,
                getHeight() - 2 * padding - labelPadding);
        g2.setColor(Color.BLACK);

        // create hatch marks and grid lines for y axis.
        for (int i = 0; i < numberYDivisions + 1; i++) {
            int x0 = padding + labelPadding;
            int x1 = pointWidth + padding + labelPadding;
            int y0 = getHeight()
                    - ((i * (getHeight() - padding * 2 - labelPadding)) / numberYDivisions + padding + labelPadding);
            int y1 = y0;
            if (scores.length > 0) {
                g2.setColor(gridColor);
                g2.drawLine(padding + labelPadding + 1 + pointWidth, y0, getWidth() - padding, y1);
                g2.setColor(Color.BLACK);
                String yLabel = ((int) ((getMinScore()
                        + (getMaxScore() - getMinScore()) * ((i * 1.0) / numberYDivisions)) * 100)) / 100.0 + "";
                FontMetrics metrics = g2.getFontMetrics();
                int labelWidth = metrics.stringWidth(yLabel);
                g2.drawString(yLabel, x0 - labelWidth - 5, y0 + (metrics.getHeight() / 2) - 3);
            }
            g2.drawLine(x0, y0, x1, y1);
        }

        // and for x axis
        for (int i = 0; i < scores.length; i++) {
            if (scores.length > 1) {
                int x0 = i * (getWidth() - padding * 2 - labelPadding) / (scores.length -1) + padding + labelPadding;
                int x1 = x0;
                int y0 = getHeight() - padding - labelPadding;
                int y1 = y0 - pointWidth;
                if ((i % ((int) ((scores.length / 20.0)) + 1)) == 0) {
                    g2.setColor(gridColor);
                    g2.drawLine(x0, getHeight() - padding - labelPadding-1 - pointWidth, x1, padding);
                    g2.setColor(Color.BLACK);
                    String xLabel = (i+1) + "";
                    FontMetrics metrics = g2.getFontMetrics();
                    int labelWidth = metrics.stringWidth(xLabel);
                    g2.drawString(xLabel, x0 - labelWidth / 2, y0 + metrics.getHeight() + 3);
                }
                g2.drawLine(x0, y0, x1, y1);
            }
        }

        // create x and y axes
        g2.drawLine(padding + labelPadding, getHeight() - padding - labelPadding, padding + labelPadding, padding);
        g2.drawLine(padding + labelPadding, getHeight() - padding - labelPadding, getWidth() - padding,
                getHeight() - padding - labelPadding);

        Stroke oldStroke = g2.getStroke();
        g2.setColor(lineColor);
        g2.setStroke(GRAPH_STROKE);
        for (int i = 0; i < graphPoints.size() - 1; i++) {
            int x1 = graphPoints.get(i).x;
            int y1 = graphPoints.get(i).y;
            int x2 = graphPoints.get(i + 1).x;
            int y2 = graphPoints.get(i + 1).y;
            g2.drawLine(x1, y1, x2, y2);
        }

        g2.setStroke(oldStroke);
        g2.setColor(pointColor);
        for (int i = 0; i < graphPoints.size(); i++) {
            int x = graphPoints.get(i).x - pointWidth / 2;
            int y = graphPoints.get(i).y - pointWidth / 2;
            int ovalW = pointWidth;
            int ovalH = pointWidth;
            g2.fillOval(x, y, ovalW, ovalH);
        }
    }

    // @Override
    // public Dimension getPreferredSize() {
    // return new Dimension(width, heigth);
    // }
    private double getMinScore() {

        return 0;
    }

    private double getMaxScore() {
        double maxScore = Integer.MIN_VALUE;
        for (Integer score : scores) {
            maxScore = Math.max(maxScore, score);
        }
        return maxScore + 50;
    }

    public static void createGraph(String tickerSymbol) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGui(tickerSymbol);
            }
        });
    }
    private static void createAndShowGui(String tickerSymbol) {
        MainPanel mainPanel = new MainPanel(scores, tickerSymbol);
        mainPanel.setPreferredSize(new Dimension(800, 600));
        JFrame frame = new JFrame("DrawGraph");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(mainPanel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }


    //Main changes underneath

    static class MainPanel extends JPanel {

        public MainPanel(int[] scores, String tickerSymbol) {

            setLayout(new BorderLayout());

            JLabel title = new JLabel("Stock price over the last 10 days for " + tickerSymbol+".");
            title.setFont(new Font("Arial", Font.BOLD, 25));
            title.setHorizontalAlignment(JLabel.CENTER);

            JPanel graphPanel = new GraphingData(scores);

            VerticalPanel vertPanel = new VerticalPanel();

            HorizontalPanel horiPanel = new HorizontalPanel();

            add(title, BorderLayout.NORTH);
            add(horiPanel, BorderLayout.SOUTH);
            add(vertPanel, BorderLayout.WEST);
            add(graphPanel, BorderLayout.CENTER);

        }

        class VerticalPanel extends JPanel {

            public VerticalPanel() {
                setPreferredSize(new Dimension(25, 0));
            }

            @Override
            public void paintComponent(Graphics g) {

                super.paintComponent(g);

                Graphics2D gg = (Graphics2D) g;
                gg.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                Font font = new Font("Arial", Font.PLAIN, 15);

                String string = "Price";

                FontMetrics metrics = g.getFontMetrics(font);
                int width = metrics.stringWidth(string);
                int height = metrics.getHeight();

                gg.setFont(font);

                drawRotate(gg, getWidth(), (getHeight() + width) / 2, 270, string);
            }

            public void drawRotate(Graphics2D gg, double x, double y, int angle, String text) {
                gg.translate((float) x, (float) y);
                gg.rotate(Math.toRadians(angle));
                gg.drawString(text, 0, 0);
                gg.rotate(-Math.toRadians(angle));
                gg.translate(-(float) x, -(float) y);
            }

        }

        class HorizontalPanel extends JPanel {

            public HorizontalPanel() {
                setPreferredSize(new Dimension(0, 25));
            }

            @Override
            public void paintComponent(Graphics g) {

                super.paintComponent(g);

                Graphics2D gg = (Graphics2D) g;
                gg.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                Font font = new Font("Arial", Font.PLAIN, 15);

                String string = "Date";

                FontMetrics metrics = g.getFontMetrics(font);
                int width = metrics.stringWidth(string);
                int height = metrics.getHeight();

                gg.setFont(font);

                gg.drawString(string, (getWidth() - width) / 2, 11);
            }

        }

    }


}

