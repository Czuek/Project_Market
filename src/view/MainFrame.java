package view;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import model.Stock;

public class MainFrame extends JFrame {
    private JPanel stocksLogsContainer;
    private Map<String, JTextArea> stockLogAreas;
    private Map<String, JPanel> stockPanels;
    private DefaultListModel<String> listModel;
    private JList<String> logList;
    private ChartPanel chartPanel;
    private JScrollPane chartScrollPane;
    private Runnable nextstepAction;
    private Consumer<Stock> stockInitializer;
    private List<Stock> stocks;
    private JPanel checkboxPanel;

    public MainFrame(List<Stock> stocks, Runnable onnextStep, Consumer<Stock> stockInitializer) {
        this.stocks = stocks;
        this.nextstepAction = onnextStep;
        this.stockInitializer = stockInitializer;
        this.stockLogAreas = new HashMap<>();
        this.stockPanels = new HashMap<>();

        this.listModel = new DefaultListModel<>();
        this.logList = new JList<>(listModel);

        setTitle("Project Market");
        setSize(1000, 800);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10,10));

        chartPanel = new ChartPanel(stocks);
        this.chartScrollPane = new JScrollPane(chartPanel);
        chartScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        add(chartScrollPane, BorderLayout.CENTER);

        JPanel topContainer = new JPanel();
        topContainer.setLayout(new BoxLayout(topContainer, BoxLayout.Y_AXIS));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton nextStepButton = new JButton("Generuj nowe wartości");
        JButton dodajSpolke = new JButton("Dodaj spółkę");
        JButton autogenerate = new JButton("Automatycznie generuj");


        String[] options = {"Wszystkie(z przewijaniem)", "Ostatnie 100", "Ostatnie 1000"};
        JComboBox<String> viewModeBox = new JComboBox<>(options);
        viewModeBox.addActionListener(e -> {
            int index =  viewModeBox.getSelectedIndex();
            int limit = -1;
            if(index == 1) limit = 100;
            if(index == 2) limit = 1000;

            chartPanel.setViewLimit(limit);

            SwingUtilities.invokeLater(() -> {
                chartScrollPane.revalidate();
                chartScrollPane.repaint();
                JScrollBar bar = chartScrollPane.getHorizontalScrollBar();
                if(index == 0) {
                    bar.setValue(bar.getMaximum());
                } else {
                    bar.setValue(0);
                }
            });
        });


        nextStepButton.setBackground(new Color(119, 168, 119));
        dodajSpolke.setBackground(new Color(206, 149, 235));
        autogenerate.setBackground(new Color(66, 209, 192));

        buttonPanel.add(nextStepButton);
        buttonPanel.add(dodajSpolke);
        buttonPanel.add(autogenerate);
        buttonPanel.add(new JLabel("Widok:"));
        buttonPanel.add(viewModeBox);

        checkboxPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));

        topContainer.add(buttonPanel);
        topContainer.add(checkboxPanel);
        add(topContainer, BorderLayout.NORTH);

        nextStepButton.setFont(new Font("Monospaced", Font.BOLD, 16));
        dodajSpolke.setFont(new Font("Monospaced", Font.BOLD, 16));
        autogenerate.setFont(new Font("Monospaced", Font.BOLD, 16));

        stocksLogsContainer = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        JScrollPane logScrollPane = new JScrollPane(stocksLogsContainer);
        logScrollPane.setPreferredSize(new Dimension(0, 200));
        logScrollPane.setBorder(BorderFactory.createTitledBorder("Logi botow wg spolek"));
        logScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        logScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        add(logScrollPane, BorderLayout.SOUTH);

        for(Stock s : stocks){
            addStockPanel(s);
        }


        nextStepButton.addActionListener(e -> {
            clearLog();
            nextstepAction.run();
            chartPanel.repaint();

            chartPanel.revalidate();

            if(viewModeBox.getSelectedIndex() == 0){
                SwingUtilities.invokeLater(() -> {
                    JScrollBar horizontal = chartScrollPane.getHorizontalScrollBar();
                    horizontal.setValue(horizontal.getMaximum());
                });
            }
        });

        dodajSpolke.addActionListener(e -> {
            JPanel panel = new JPanel(new GridLayout(2,2,5,5));
            JTextField symbolField = new JTextField(10);
            JTextField priceField = new JTextField(10);
            panel.add(new JLabel("Symbol:")); panel.add(symbolField);
            panel.add(new JLabel("Cena:")); panel.add(priceField);
            int wynik = JOptionPane.showConfirmDialog(null, panel, "Dodaj spolke", JOptionPane.OK_CANCEL_OPTION);

            if(wynik == JOptionPane.OK_OPTION) {
                try {
                    String symbol = symbolField.getText();
                    double price = Double.parseDouble(priceField.getText());
                    Stock s = new Stock(symbol, price);

                    if(this.stockInitializer!=null){
                        this.stockInitializer.accept(s);
                    }

                    int currentMaxSteps = 0;
                    for(Stock exist : stocks) {
                        currentMaxSteps = Math.max(currentMaxSteps, exist.getPriceHistory().size());
                    }

                    s.getPriceHistory().clear();

                    for(int i = 0; i < currentMaxSteps-1; i++) s.getPriceHistory().add(null);
                    s.getPriceHistory().add(price);
                    stocks.add(s);

                    addStockPanel(s);
                    refreshCheckBoxes();
                    stocksLogsContainer.revalidate();
                    stocksLogsContainer.repaint();
                    chartPanel.revalidate();
                    chartPanel.repaint();

                } catch(NumberFormatException ex) {
                }
            }
        });

        autogenerate.addActionListener(e -> automatycznedzialanie());
        setLocationRelativeTo(null);
        refreshCheckBoxes();
        setVisible(true);
    }

    private void addStockPanel(Stock stock) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setPreferredSize(new Dimension(200, 150));
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY),
                stock.getSymbol(),
                TitledBorder.CENTER,
                TitledBorder.TOP,
                new Font("SansSerif", Font.BOLD, 14),
                Color.BLUE
        ));

        JTextArea area = new JTextArea();
        area.setEditable(false);
        area.setFont(new Font("Monospaced", Font.BOLD, 11));
        area.setLineWrap(true);
        area.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(area);
        panel.add(scrollPane, BorderLayout.CENTER);


        stockLogAreas.put(stock.getSymbol(), area);
        stockPanels.put(stock.getSymbol(), panel);

        stocksLogsContainer.add(panel);
    }

    private void refreshCheckBoxes() {
        checkboxPanel.removeAll();
        for(Stock s : stocks) {
            JCheckBox checkBox = new JCheckBox(s.getSymbol());

            boolean isVisible = chartPanel.isStockVisible(s.getSymbol());
            checkBox.setSelected(isVisible);

            JPanel p = stockPanels.get(s.getSymbol());
            if(p != null) {p.setVisible(isVisible);}

            checkBox.addActionListener(e -> {
                boolean selected = checkBox.isSelected();

                chartPanel.setStocksVisible(s.getSymbol(), selected);
                JPanel paneltoHide = stockPanels.get(s.getSymbol());
                if(paneltoHide != null) {
                    paneltoHide.setVisible(selected);
                }

                stocksLogsContainer.revalidate();
                stocksLogsContainer.repaint();
            });
            checkboxPanel.add(checkBox);
        }
        checkboxPanel.revalidate();
        checkboxPanel.repaint();
    }

    public void updateBotLog(String stockSymbol, String botName, String decision) {
        JTextArea area = stockLogAreas.get(stockSymbol);
        if(area != null) {
            String logEntry = botName + ": " + decision + "\n";
            area.append(logEntry);
        } else {
            System.out.println("Log error, nie znaleziono panelu: " + stockSymbol);
        }
    }

    public void addLog(String text) {
        System.out.println("System log: " + text);
    }

    public int getCurrentstep(List<Stock> stocks) {
        int currentstep = 0;
        for(Stock s : stocks) {
            currentstep = Math.max(currentstep, s.getPriceHistory().size());
        }
        return currentstep;
    }


    public void automatycznedzialanie() {
        Timer timer = new Timer(200, null);
        timer.addActionListener(new ActionListener() {
            int licznik = 100;

            @Override
            public void actionPerformed(ActionEvent e) {
                if(licznik > 0) {
                    clearLog();
                    nextstepAction.run();
                    chartPanel.repaint();

                    chartPanel.revalidate();
                    JScrollBar horizontal = chartScrollPane.getHorizontalScrollBar();
                    if(horizontal.isEnabled() && chartPanel.getViewLimit() == -1) {
                        horizontal.setValue(horizontal.getMaximum());
                    }

                    licznik--;

                } else timer.stop();
            }
        });
        timer.start();
    }

    public void clearLog() {
        for(JTextArea area : stockLogAreas.values()) {
            area.setText("");
        }
    }
}
