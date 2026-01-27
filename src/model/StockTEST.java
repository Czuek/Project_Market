package model;

import interfaces.Observer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class StockTEST {

    private Stock stock;

    @BeforeEach
    public void setUp() {
        stock = new Stock("AAPL", 100.0);
    }

    @Test
    @DisplayName("Powinna być poprawna inicjalizacja konstruktora")
    void testKonstruktora() {
        List<Double> historia = stock.getPriceHistory();

        assertEquals(2, historia.size(), "Historia powinna mieć 2 wpisy");
        assertEquals(100.0, historia.get(0));
        assertEquals("AAPL", stock.getSymbol());
    }

    @Test
    @DisplayName("Powinna się zmienić cena")
    void testSetPrice() {
        stock.setPrice(120.0);
        assertEquals(120.0, stock.getCurrentPrice());
        assertEquals(3, stock.getPriceHistory().size());
        assertEquals(120.0, stock.getPriceHistory().get(2));
    }

    @Test
    @DisplayName("Obserwator powinien zostać powiadomiony")
    void testObserwatora() {
        TestObserver observer = new TestObserver();

        stock.registerObserver(observer);
        stock.setPrice(200.0);

        assertTrue(observer.czyZaktualizowano, "Obserwator powinien zostać powiadomiony");
    }

    class TestObserver implements Observer {
        boolean czyZaktualizowano = false;

        @Override
        public void update(Stock s) {
            czyZaktualizowano = true;
        }
    }
}
