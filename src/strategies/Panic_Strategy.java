package strategies;

import java.util.List;
import interfaces.Strategy;

public class Panic_Strategy implements Strategy {
    @Override
    public String analyze(String symbol, List<Double> history, double currentPrice) {
        if(history.size()<3) return "HOLD";

        double oldPrice = history.get(history.size()-3);

        if(currentPrice < oldPrice * 0.93) return "SELL";
        if(currentPrice > oldPrice * 1.07) return "BUY";

        return "HOLD";
    }
}
