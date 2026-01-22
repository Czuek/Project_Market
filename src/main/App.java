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

public class App {
    public static void main(String[] args) {
        FlatLightLaf.setup();
        AppController controller = new AppController();

        Stock sTEST = new Stock("TEST", 0);

        Stock s1 = new Stock("AAPL", 100);
        Stock s2 = new Stock("MSFT", 200);
        Stock s3 = new Stock("ALPH", 50);
        Stock s4 = new Stock("TESL", 150);
        Stock s5 = new Stock("AMZN", 120);
        Stock s6 = new Stock("ORACLE", 130);
        Stock s7 = new Stock("FB", 140);
        Stock s8 = new Stock("OIL", 160);



        TradingBot sma = new TradingBot("Bot Powazna Analiza", new SMA_Strategy(), controller);
        TradingBot panic = new TradingBot("Bot Chaosu", new Panic_Strategy(), controller);

        s1.registerObserver(sma);
        s1.registerObserver(panic);

        /*
        MainFrame view = new MainFrame(stocks, () -> {
            for(Stock s : stocks) s.generatenewPrice();
        }
        );

        controller.setView(view);

         */
    }

}
