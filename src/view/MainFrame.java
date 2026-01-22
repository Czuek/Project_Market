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
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10,10));

        chartPanel = new ChartPanel(stocks);
        this.chartScrollPane = new JScrollPane(chartPanel);
        chartScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        chartScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);

        add(chartScrollPane, BorderLayout.CENTER);

        JPanel sidepanel = new JPanel();
        sidepanel.setLayout(new BorderLayout());
        sidepanel.setPreferredSize(new Dimension(300, 0));

        JButton nextStepButton = new JButton("Generuj nowe wartości");
        nextStepButton.setFont(new Font("Monospaced", Font.BOLD, 16));
        nextStepButton.setBackground(new Color(119, 168, 119));

        JButton dodajSpolke = new JButton("Dodaj spółkę");
        dodajSpolke.setFont(new Font("Monospaced", Font.BOLD, 16));
        dodajSpolke.setBackground(new Color(206, 149, 235));


        JButton autogenerate = new JButton("Automatycznie generuj");
        autogenerate.setFont(new Font("Monospaced", Font.BOLD, 16));
        autogenerate.setBackground(new Color(66, 209, 192));

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
            String[] etykiety = {"Symbol", "Cena"};
            JTextField[] pola = new JTextField[2];
            wypelnijPola(etykiety, pola, panel);
            int wynik = JOptionPane.showConfirmDialog(null, panel, "Dodaj spolke", JOptionPane.OK_CANCEL_OPTION);

            if(wynik == JOptionPane.OK_OPTION) {
                try {
                    Stock s = new Stock(pola[0].getText(), Double.parseDouble(pola[1].getText()));
                    stocks.add(s);
                } catch(NumberFormatException ex) {
                }
            }
        });



        autogenerate.addActionListener(e -> automatycznedzialanie());

        JPanel przyciski = new JPanel();
        przyciski.setVisible(true);
        przyciski.setLayout(new GridLayout(3,2, 50, 50));

        sidepanel.add(przyciski, BorderLayout.NORTH);
        przyciski.add(nextStepButton, BorderLayout.NORTH);
        przyciski.add(dodajSpolke, BorderLayout.CENTER);
        przyciski.add(autogenerate, BorderLayout.SOUTH);

        sidepanel.add(new JScrollPane(logList), BorderLayout.CENTER);
        add(sidepanel, BorderLayout.EAST);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void operacjaDodajSpolke() {

    }

    public void addLog(String text) {
        listModel.add(0, text);
    }

    public void wypelnijPola(String[] etykieta, JTextField[] pola, JPanel pnl) {
        for(int i = 0; i < etykieta.length; i++) {
            pnl.add(new JLabel(etykieta[i]));
            pola[i] = new JTextField(15);
            pnl.add(pola[i]);
        }
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
