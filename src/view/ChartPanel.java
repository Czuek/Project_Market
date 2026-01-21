package view;

import model.Stock;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ChartPanel extends JPanel {
    private List<Stock> stocks;

    public ChartPanel(List<Stock> stocks) {
        this.stocks = stocks;
        this.setPreferredSize(new Dimension(600, 300));
        this.setBackground(Color.WHITE);
        this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if(stocks == null || stocks.isEmpty()) return;

        Color[] colors = {Color.RED, Color.GREEN, Color.ORANGE};
        int coloridx = 0;

        for(Stock s : stocks) {
            drawStockLine(g2, s, colors[coloridx % colors.length]);
            coloridx++;
        }
    }

    private void drawStockLine(Graphics2D g, Stock s, Color color) {
        g.setColor(color);
        List<Double> history = s.getPriceHistory();

        if(history.size() < 2) return;

        int witdh = this.getWidth();
        int height = this.getHeight();

        double xStep = (double) witdh / (Math.max(history.size(), 20));

        double maxPrice = 0;
        for(double p : history) maxPrice = Math.max(maxPrice, p);
        maxPrice *= 1.1;

        for(int i = 0; i < history.size() - 1; i++) {
            int x1 = (int) (i * xStep);
            int y1 = (int) (height - (history.get(i) / maxPrice * height));
            int x2 = (int) ((i + 1) * xStep);
            int y2 = (int) (height - (history.get(i + 1) / maxPrice * height));
            g.drawLine(x1, y1, x2, y2);

        }
    }
}
