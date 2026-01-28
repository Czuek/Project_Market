package strategies;

import java.util.List;
import interfaces.Strategy;

public class SMA_Strategy implements Strategy {
    @Override
    public String analyze(String stocksymbol, List<Double> history, double currentPrice) {
        int period = 5;
        if(history.size() < period) return "HOLD";

        double sum = 0;
        for(int i = history.size() - period; i < history.size(); i++) {
            Double val = history.get(i);
            if(val == null) return "HOLD";
            sum += val;
        }

        double average = sum / period;

        if(currentPrice > average * 1.01) return "BUY";
        if(currentPrice < average * 0.99) return "SELL";

        return "HOLD";
    }
}
