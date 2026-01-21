package main;

import controller.AppController;
import model.Stock;
import observers.TradingBot;
import strategies.Panic_Strategy;
import strategies.SMA_Strategy;
import view.MainFrame;

import java.util.ArrayList;
import java.util.List;

public class App {
    public static void main(String[] args) {
        AppController controller = new AppController();

        List<Stock> market = new ArrayList<>();
        Stock s1 = new Stock("AAPL", 100);
        Stock s2 = new Stock("MSFT", 200);
        Stock s3 = new Stock("ALPH", 50);
        Stock s4 = new Stock("TESL", 150);
        Stock s5 = new Stock("AMZN", 120);
        Stock s6 = new Stock("ORACLE", 130);
        Stock s7 = new Stock("FB", 140);
        Stock s8 = new Stock("OIL", 160);

        market.add(s1);
        market.add(s2);
        market.add(s3);
        market.add(s4);
        market.add(s5);
        market.add(s6);
        market.add(s7);
        market.add(s8);

        TradingBot sma = new TradingBot("Bot Powazna Analiza", new SMA_Strategy(), controller);
        TradingBot panic = new TradingBot("Bot Chaosu", new Panic_Strategy(), controller);

        for(Stock s : market) {
            s.registerObserver(sma);
            s.registerObserver(panic);
        }

        MainFrame view = new MainFrame(market, () -> {
            for(Stock s : market) s.generatenewPrice();
        }
        );

        controller.setView(view);
    }
}
