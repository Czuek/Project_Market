package view;

import model.Stock;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ChartPanel extends JPanel {
    private List<Stock> stocks;
    private final int xStep = 30;

    public ChartPanel(List<Stock> stocks) {
        this.stocks = stocks;
        this.setBackground(Color.WHITE);
        this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if(stocks == null || stocks.isEmpty()) return;

        int maxPoints = 0;
        for(Stock s : stocks) maxPoints = Math.max(maxPoints, s.getPriceHistory().size());

        int prefferedWidth = Math.max(620, maxPoints * xStep + 20);
        if(getPreferredSize().width != prefferedWidth) {
            setPreferredSize(new Dimension(prefferedWidth, 300));
            revalidate();
        }

        Color[] colors = {Color.RED, Color.GREEN, Color.ORANGE, Color.BLUE, Color.MAGENTA, Color.CYAN, Color.PINK, Color.YELLOW, Color.GRAY};
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

        int height = this.getHeight() - 20;
        double maxPrice = 0;
        for(double p : history) maxPrice = Math.max(maxPrice, p);
        maxPrice *= 1.1;

        for(int i = 0; i < history.size() - 1; i++) {
            int x1 = (int) (i * xStep);
            int y1 = (int) (height - (history.get(i) / maxPrice * height));
            int x2 = (int) ((i + 1) * xStep);
            int y2 = (int) (height - (history.get(i + 1) / maxPrice * height));
            g.drawLine(x1, y1, x2, y2);

            g.fillOval(x1 - 2, y1 - 2, 4, 4);
        }

        if(!history.isEmpty()) {
            int lastIdx = history.size() - 1;
            int lastX = (int) (lastIdx * xStep);
            int lastY = (int) (height - (history.get(lastIdx) / maxPrice * height));


            g.setColor(color);
            g.drawString(s.getSymbol(), lastX + 5, lastY);
        }
    }
}
