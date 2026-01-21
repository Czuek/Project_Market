package observers;

import controller.AppController;
import interfaces.Observer;
import interfaces.Strategy;
import model.Stock;

public class TradingBot implements Observer {
    private String name;
    private Strategy strategy;
    private AppController controller;

    public TradingBot(String name, Strategy strategy, AppController controller) {
        this.name = name;
        this.strategy = strategy;
        this.controller = controller;
    }

    @Override
    public void update(Stock stock) {
        String decision = strategy.analyze(stock.getSymbol(), stock.getPriceHistory(), stock.getCurrentPrice());
        controller.logBotDecision(name, stock.getSymbol(), decision);
    }
}
