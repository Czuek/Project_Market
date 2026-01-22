package main;

import com.formdev.flatlaf.FlatLightLaf;
import controller.AppController;
import model.Stock;
import observers.TradingBot;
import strategies.Panic_Strategy;
import strategies.SMA_Strategy;
import view.MainFrame;
import com.formdev.flatlaf.FlatDarkLaf;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class App {
    public static void main(String[] args) {
        FlatLightLaf.setup();
        AppController controller = new AppController();
        List<Stock> stocks = new ArrayList<>();

        TradingBot sma = new TradingBot("Bot Powazna Analiza", new SMA_Strategy(), controller);
        TradingBot panic = new TradingBot("Bot Chaosu", new Panic_Strategy(), controller);

        Consumer<Stock> registerBottoStock = (s) -> {
            s.registerObserver(sma);
            s.registerObserver(panic);
        };

        Stock s1 = new Stock("AAPL", 100);
        Stock s2 = new Stock("MSFT", 200);
        Stock s3 = new Stock("ALPH", 50);
        Stock s4 = new Stock("TESL", 150);
        Stock s5 = new Stock("AMZN", 120);
        Stock s6 = new Stock("ORACLE", 130);
        Stock s7 = new Stock("FB", 140);
        Stock s8 = new Stock("OIL", 160);

        stocks.add(s1);
        registerBottoStock.accept(s1);
        stocks.add(s2);
        registerBottoStock.accept(s2);
        stocks.add(s3);
        registerBottoStock.accept(s3);

        MainFrame view = new MainFrame(stocks, () -> {
            for(Stock s : stocks) s.generatenewPrice();
        }
        );

        controller.setView(view);

    }

}
