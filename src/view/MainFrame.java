package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import model.Stock;

public class MainFrame extends JFrame {
    private DefaultListModel<String> listModel;
    private JList<String> logList;
    private ChartPanel chartPanel;
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
        add(chartPanel, BorderLayout.CENTER);

        JPanel sidepanel = new JPanel();
        sidepanel.setLayout(new BorderLayout());
        sidepanel.setPreferredSize(new Dimension(300, 0));

        JButton nextStepButton = new JButton("GENERUJ WARTOŚCI");
        nextStepButton.setFont(new Font("Monospaced", Font.BOLD, 16));
        nextStepButton.setBackground(new Color(119, 168, 119));

        JButton autogenerate = new JButton("Automatycznie generuj");
        nextStepButton.setFont(new Font("Monospaced", Font.BOLD, 16));
        nextStepButton.setBackground(new Color(119, 168, 119));

        nextStepButton.addActionListener(e -> {
            nextstepAction.run();
            chartPanel.repaint();
        });

        autogenerate.addActionListener(e -> automatycznedzialanie());

        sidepanel.add(nextStepButton, BorderLayout.NORTH);
        sidepanel.add(autogenerate, BorderLayout.SOUTH);

        sidepanel.add(new JScrollPane(logList), BorderLayout.CENTER);
        add(sidepanel, BorderLayout.EAST);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void addLog(String text) {
        listModel.add(0, text);
    }

    public void automatycznedzialanie() {
        Timer timer = new Timer(200, null);
        timer.addActionListener(new ActionListener() {
            int licznik = 100;

            @Override
            public void actionPerformed(ActionEvent e) {
                if(licznik > 0) {
                    nextstepAction.run();
                    chartPanel.repaint();
                    licznik--;
                } else timer.stop();
            }
        });
        timer.start();
    }
}
