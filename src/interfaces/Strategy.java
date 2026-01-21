package interfaces;

import java.util.List;

public interface Strategy {
    String analyze(String stocksymbol, List<Double> history, double currentPrice);
}
