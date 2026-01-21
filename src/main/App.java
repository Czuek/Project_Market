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
        market.add(s1);
        market.add(s2);
        market.add(s3);

        TradingBot sma = new TradingBot("Bot Powazna Analiza", new SMA_Strategy(), controller);
        TradingBot panic = new TradingBot("Bot Chaosu", new Panic_Strategy(), controller);

        s1.registerObserver(sma);
        s1.registerObserver(panic);

        s2.registerObserver(sma);
        s2.registerObserver(panic);

        s3.registerObserver(panic);

        MainFrame view = new MainFrame(market, () -> {
            for(Stock s : market) s.generatenewPrice();
        }
        );

        controller.setView(view);
    }
}
