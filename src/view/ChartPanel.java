package view;

import model.Stock;
import javax.swing.*;
import java.awt.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ChartPanel extends JPanel {
    private List<Stock> stocks;
    private final int defXStep = 30;
    private final int MARGIN_LEFT = 60;
    private final int MARGIN_RIGHT = 60;
    private final int MARGIN_BOTTOM = 40;
    private Set<String> hiddenStocks = new HashSet<String>();
    private int viewLimit = -1;

    public ChartPanel(List<Stock> stocks) {
        this.stocks = stocks;
        this.setBackground(Color.WHITE);
    }

    public void setViewLimit(int viewLimit) {
        this.viewLimit = viewLimit;
        revalidate();
        repaint();
    }

    public int getViewLimit() {
        return viewLimit;
    }

    public void setStocksVisible(String symbol, boolean visible) {
        if(visible) {
            hiddenStocks.remove(symbol);
        } else {
            hiddenStocks.add(symbol);
        }
        repaint();
    }

    public boolean isStockVisible(String symbol) {
        return !hiddenStocks.contains(symbol);
    }

    @Override
    public Dimension getPreferredSize() {
        if(stocks == null || stocks.isEmpty()) return new Dimension(800, 400);


        if(viewLimit > 0) {
            Container parent = getParent();
            int w = parent != null ? parent.getWidth() : 800;
            return new Dimension(w, 400);
        }

        int maxPoints = 0;
        for(Stock s : stocks) {
            maxPoints = Math.max(maxPoints, s.getPriceHistory().size());
        }
        int cwidth = maxPoints * defXStep + MARGIN_LEFT + 100;
        return new Dimension(Math.max(cwidth, 800), 400);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if(stocks == null || stocks.isEmpty()) return;

        int maxPoints = 0;
        for(Stock s : stocks) maxPoints = Math.max(maxPoints, s.getPriceHistory().size());
        if(maxPoints == 0) maxPoints = 10;

        double maxPrice = 0;
        for(Stock s : stocks) for(Double p : s.getPriceHistory()) if(p != null) maxPrice = Math.max(maxPrice, p);
        if(maxPrice == 0) maxPrice = 100;
        maxPrice *= 1.2;

        //int prefferedWidth = Math.max(800, maxPoints * defXStep + MARGIN_LEFT + 50);

        int drawHeight = this.getHeight() - MARGIN_BOTTOM;
        int drawWidth = this.getWidth() - MARGIN_RIGHT;

        int sIndex = 0;
        double cXStep = defXStep;

        if(viewLimit > 0) {
            sIndex = Math.max(0, maxPoints - viewLimit);
            int pointstoShow = maxPoints - sIndex;
            int aWidth = drawWidth - MARGIN_LEFT;
            cXStep = (double) aWidth / Math.max(1, pointstoShow -1);
        } else {
            sIndex = 0;
            cXStep = defXStep;
        }

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

        int gridStep = 5;
        if(viewLimit > 100) gridStep = viewLimit / 10;

        Shape oClip = g2.getClip();
        g2.setClip(MARGIN_LEFT, 0, this.getWidth() - MARGIN_LEFT, drawHeight);

        for(int i = sIndex; i < maxPoints; i++ ) {
            if(i % gridStep == 0 || i == maxPoints - 1) {
                int x = MARGIN_LEFT + (int)((i - sIndex) * cXStep);

                if(x > drawWidth) continue;

                g2.setColor(new Color(220, 220, 220));
                g2.drawLine(x, 0, x, drawHeight);

                g2.setColor(Color.BLACK);
                g2.drawString(String.format("%d", i), x - 15, drawHeight + 20);
            }
        }

        g2.setClip(oClip);

        for(int i = sIndex; i < maxPoints; i++) {
            if(i % gridStep == 0 || i == maxPoints - 1) {
                int x = MARGIN_LEFT + (int)((i - sIndex) * cXStep);
                if(x > drawWidth) continue;
                g2.setColor(new Color(220, 220, 220));
                g2.drawString(String.format("%d", i), x - 10, drawHeight + 20);
            }
        }

        g2.setClip(MARGIN_LEFT, 0, drawWidth - MARGIN_LEFT, drawHeight + 5);

        Color[] colors = {Color.RED, Color.GREEN, Color.ORANGE, Color.BLUE, Color.MAGENTA, Color.CYAN, Color.PINK, Color.YELLOW, Color.GRAY};
        int coloridx = 0;
        for(Stock s : stocks) {
            if(isStockVisible(s.getSymbol())) {
                drawStockLine(g2, s, colors[coloridx % colors.length], drawHeight, maxPrice, sIndex, cXStep);
            }
            coloridx++;
        }

        g2.setClip(oClip);

        g2.setClip(null);

        int legendX = MARGIN_LEFT + 20;
        int legendY = 30;
        int lColoridx = 0;

        for(Stock s : stocks) {
            if(isStockVisible(s.getSymbol())) {
                g2.setColor(colors[lColoridx % colors.length]);
                g2.fillRect(legendX, legendY, 12, 12);
                g2.setColor(Color.BLACK);
                g2.drawString(s.getSymbol(), legendX + 18, legendY + 11);
                legendX += 70;
            }
            lColoridx++;
        }
    }

    private void drawStockLine(Graphics2D g, Stock s, Color color, int height, double maxPrice, int startIndex, double step) {
        g.setColor(color);
        List<Double> history = s.getPriceHistory();

        int loopStart = Math.max(0, startIndex-1);

        for(int i = loopStart; i < history.size() - 1; i++) {
            Double p1 = history.get(i);
            Double p2 = history.get(i + 1);

            if(p1 == null || p2 == null) continue;

            int x1 = MARGIN_LEFT + (int)((i - startIndex) * step);
            int y1 = height - (int)(p1 / maxPrice * (height - 20));
            int x2 = MARGIN_LEFT + (int)((i + 1 - startIndex) * step);
            int y2 = height - (int)(p2 / maxPrice * (height - 20));

            if(x2 < MARGIN_LEFT) continue;

            g.setStroke(new BasicStroke(2f));
            g.drawLine(x1, y1, x2, y2);

            if(x1 >= MARGIN_LEFT) {
                g.fillOval(x1 - 3, y1 - 3, 6, 6);
            }

            if(i == history.size() - 2) {
                g.drawString(s.getSymbol(), x2 + 5, y2);
                g.fillOval(x2 - 3, y2 - 3, 6, 6);
            }
        }

        if(!history.isEmpty()) {
            int lastIdx = history.size() - 1;
            if(lastIdx >= startIndex) {
                Double val = history.get(lastIdx);
                if(val != null) {
                    int lastX = MARGIN_LEFT + (int)((lastIdx - startIndex) * step);
                    int lastY = (int) (height - (history.get(lastIdx) / maxPrice * height));

                    g.setColor(color);
                    if(history.size() == 1 || (history.size() > 1 && history.get(history.size()-2) == null)) {
                        g.fillOval(lastX - 3, lastY - 3, 6, 6);
                        g.drawString(s.getSymbol(), lastX + 5, lastY);
                    }
                }
            }
        }
    }
}
