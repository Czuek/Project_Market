package main;

import com.formdev.flatlaf.FlatLightLaf;
import controller.AppController;
import filemanagement.SaveAndLoad;
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
        //FlatLightLaf.setup();
        AppController controller = new AppController();
        List<Stock> stocks = new ArrayList<>();

        TradingBot sma = new TradingBot("Bot Powazna Analiza", new SMA_Strategy(), controller);
        TradingBot panic = new TradingBot("Bot Chaosu", new Panic_Strategy(), controller);

        Consumer<Stock> registerBotStock = (s) -> {
            s.registerObserver(sma);
            s.registerObserver(panic);
        };

        //Stock s1 = new Stock("AAPL", 100);
            Stock s1 = SaveAndLoad.loadStock("msft_us_d.csv");
            Stock s2 = SaveAndLoad.loadStock("aapl_us_d.csv");
            Stock s3 = new Stock("ALPH", 50);
            Stock s4 = new Stock("TESL", 150);
            Stock s5 = SaveAndLoad.loadStock("amzn_us_d.csv");
            Stock s6 = new Stock("ORACLE", 130);
            Stock s7 = new Stock("FB", 140);
            Stock s8 = new Stock("OIL", 160);

            if (s1 == null) s1 = new Stock("MSFT(fake)", 240);
            if (s2 == null) s2 = new Stock("AAPL(fake)", 160);
            if (s5 == null) s3 = new Stock("AMZN(fake)", 140);

            stocks.add(s1);
            registerBotStock.accept(s1);

            stocks.add(s2);
            registerBotStock.accept(s2);

            stocks.add(s5);
            registerBotStock.accept(s5);

            Stock[] spolki = {s3, s4, s6, s7, s8};
            int targetSize = s1.getPriceHistory().size();
            for (Stock s : spolki) {
                s.getPriceHistory().clear();
                for (int i = 0; i < targetSize - 1; i++) s.getPriceHistory().add(s.getCurrentPrice());
                s.getPriceHistory().add(s.getCurrentPrice());

                stocks.add(s);
                registerBotStock.accept(s);
            }


            MainFrame view = new MainFrame(stocks, () -> {
                for (Stock s : stocks) s.generatenewPrice();
            }
            );

            controller.setView(view);

        }

    }
