package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;
import model.Stock;

public class MainFrame extends JFrame {
    private DefaultListModel<String> listModel;
    private JList<String> logList;
    private ChartPanel chartPanel;
    private JScrollPane chartScrollPane;
    private Runnable nextstepAction;

    public MainFrame(List<Stock> stocks, Runnable onnextStep) {
        this.nextstepAction = onnextStep;

        this.listModel = new DefaultListModel<>();
        this.logList = new JList<>(listModel);

        setTitle("Project Market");
        setSize(1000, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10,10));

        chartPanel = new ChartPanel(stocks);
        this.chartScrollPane = new JScrollPane(chartPanel);
        chartScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        add(chartScrollPane, BorderLayout.CENTER);

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton nextStepButton = new JButton("Generuj nowe wartości");
        JButton dodajSpolke = new JButton("Dodaj spółkę");
        JButton autogenerate = new JButton("Automatycznie generuj");

        nextStepButton.setBackground(new Color(119, 168, 119));
        dodajSpolke.setBackground(new Color(206, 149, 235));
        autogenerate.setBackground(new Color(66, 209, 192));

        topPanel.add(nextStepButton);
        topPanel.add(dodajSpolke);
        topPanel.add(autogenerate);

        add(topPanel, BorderLayout.NORTH);

        nextStepButton.setFont(new Font("Monospaced", Font.BOLD, 16));
        dodajSpolke.setFont(new Font("Monospaced", Font.BOLD, 16));
        autogenerate.setFont(new Font("Monospaced", Font.BOLD, 16));

        JScrollPane logScrollPane = new JScrollPane(logList);
        logScrollPane.setPreferredSize(new Dimension(0, 150));
        logScrollPane.setBorder(BorderFactory.createTitledBorder("Logi"));
        add(logScrollPane, BorderLayout.SOUTH);


        nextStepButton.addActionListener(e -> {
            clearLog();
            nextstepAction.run();
            chartPanel.repaint();

            chartPanel.revalidate();

            SwingUtilities.invokeLater(() -> {
               JScrollBar horizontal = chartScrollPane.getHorizontalScrollBar();
               horizontal.setValue(horizontal.getMaximum());
            });
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

                    int currentMaxSteps = 0;
                    for(Stock exist : stocks) {
                        currentMaxSteps = Math.max(currentMaxSteps, exist.getPriceHistory().size());
                    }

                    s.getPriceHistory().clear();

                    for(int i = 0; i < currentMaxSteps-1; i++) s.getPriceHistory().add(null);
                    s.getPriceHistory().add(price);
                    stocks.add(s);
                    chartPanel.repaint();
                } catch(NumberFormatException ex) {
                }
            }
        });

        autogenerate.addActionListener(e -> automatycznedzialanie());
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void addLog(String text) {
        listModel.add(0, text);
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
                    horizontal.setValue(horizontal.getMaximum());

                    licznik--;


                } else timer.stop();
            }
        });
        timer.start();
    }

    public void clearLog() {listModel.clear();}
}
