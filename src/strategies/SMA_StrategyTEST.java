package strategies;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SMA_StrategyTEST {
    private SMA_Strategy strategy;

    @BeforeEach
    void setUp() {
        strategy = new SMA_Strategy();
    }

    @Test
    @DisplayName("Powinien być HOLD, gdy historia krótsza od 5 elementów")
    void testZaKrotkaHistoria() {
        List<Double> historia = Arrays.asList(100.0, 101.0, 102.0);
        String wynik = strategy.analyze("AAPL", historia, 105.0);
        assertEquals("HOLD", wynik);
    }

    @Test
    @DisplayName("Powinien być BUY, gdy cena jest wyższa o 1% od średniej z przed 5 okresów")
    void testSygnalKupno() {
        List<Double> historia = Arrays.asList(100.0, 100.0, 100.0, 100.0, 100.0);
        String wynik = strategy.analyze("AAPL", historia, 102.0);
        assertEquals("BUY", wynik);
    }

    @Test
    @DisplayName("Powinien być SELL, gdy cena jest o 1% niższa od średniej")
    void testSygnalSprzedaz() {
        List<Double> historia = Arrays.asList(100.0, 100.0, 100.0, 100.0, 100.0);
        String wynik = strategy.analyze("AAPL", historia, 98.0);
        assertEquals("SELL", wynik);
    }

    @Test
    @DisplayName("Powinno być HOLD, gdy cena jest między -1%, a 1% średniej")
    void testSygnalHold() {
        List<Double> historia = Arrays.asList(100.0, 100.0, 100.0, 100.0);
        String wynik = strategy.analyze("AAPL", historia, 99.6);
        assertEquals("HOLD", wynik);
    }
}
