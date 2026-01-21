package controller;

import view.MainFrame;

public class AppController {
    private MainFrame view;

    public void setView(MainFrame view) {
        this.view = view;
    }

    public void logBotDecision(String botName, String stockName, String decision) {
        if(view != null) {
            String msg = String.format("[%s] dla %s: %s", botName, stockName, decision);
            view.addLog(msg);
        }
    }
}
