package view;

import model.Stock;
import javax.swing.*;
import java.awt.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ChartPanel extends JPanel {
    private List<Stock> stocks;
    private final int xStep = 30;
    private final int MARGIN_LEFT = 60;
    private final int MARGIN_RIGHT = 60;
    private final int MARGIN_BOTTOM = 40;
    private Set<String> hiddenStocks = new HashSet<String>();

    public ChartPanel(List<Stock> stocks) {
        this.stocks = stocks;
        this.setBackground(Color.WHITE);
    }

    public void setStocksVisible(String symbol, boolean visible) {
        if(visible) {
            this.setVisible(true);
        } else {
            this.setVisible(false);
        }
        repaint();
    }

    public boolean isStockVisible(String symbol) {
        return !hiddenStocks.contains(symbol);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if(stocks == null || stocks.isEmpty()) return;

        int maxPoints = 0;
        for(Stock s : stocks) maxPoints = Math.max(maxPoints, s.getPriceHistory().size());

        double maxPrice = 0;
        for(Stock s : stocks) for(Double p : s.getPriceHistory()) if(p != null) maxPrice = Math.max(maxPrice, p);
        maxPrice *= 1.2;

        int prefferedWidth = Math.max(800, maxPoints * xStep + MARGIN_LEFT + 50);
        if(getPreferredSize().width != prefferedWidth) {
            setPreferredSize(new Dimension(prefferedWidth, 400));
            revalidate();
        }

        int drawHeight = this.getHeight() - MARGIN_BOTTOM;
        int drawWidth = this.getWidth() - MARGIN_RIGHT;


        g2.setColor(Color.LIGHT_GRAY);
        for(int i = 0; i <= 5; i++) {
            int y = drawHeight - (int)(i * (drawHeight - 20) / 5.0);
            g2.setColor(new Color(220,220,220));
            g2.drawLine(MARGIN_LEFT, y, drawWidth, y);

            g2.setColor(Color.BLACK);
            g2.drawString(String.format("%.2f", (maxPrice / 5 ) * i), 5,y + 5);
        }

        g2.setColor(Color.BLACK);
        g2.drawLine(MARGIN_LEFT, drawHeight, drawWidth, drawHeight);
        g2.drawLine(MARGIN_LEFT, 0, MARGIN_LEFT, drawHeight);

        for(int i = 0; i < maxPoints; i += 5) {
            int x = MARGIN_LEFT + i * xStep;

            g2.setColor(new Color(220,220,220));
            g2.drawLine(x, 0, x, drawHeight);

            g2.setColor(Color.BLACK);
            g2.drawString("IDX " + i, x - 15, drawHeight + 20);
        }

        Color[] colors = {Color.RED, Color.GREEN, Color.ORANGE, Color.BLUE, Color.MAGENTA, Color.CYAN, Color.PINK, Color.YELLOW, Color.GRAY};
        int coloridx = 0;
        for(Stock s : stocks) {
            drawStockLine(g2, s, colors[coloridx % colors.length], drawHeight, maxPrice);
            coloridx++;
        }
    }

    private void drawStockLine(Graphics2D g, Stock s, Color color, int height, double maxPrice) {
        g.setColor(color);
        List<Double> history = s.getPriceHistory();

        for(int i = 0; i < history.size() - 1; i++) {
            Double p1 = history.get(i);
            Double p2 = history.get(i + 1);

            if(p1 == null || p2 == null) continue;

            int x1 = MARGIN_LEFT + (i * xStep);
            int y1 = height - (int)(p1 / maxPrice * (height - 20));
            int x2 = MARGIN_LEFT + ((i + 1) * xStep);
            int y2 = height - (int)(p2 / maxPrice * (height - 20));

            g.setStroke(new BasicStroke(2f));
            g.drawLine(x1, y1, x2, y2);
            g.fillOval(x1 - 3, y1 - 3, 6, 6);

            if(i == history.size() - 2) {
                g.drawString(s.getSymbol(), x2 + 5, y2);
            }
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
