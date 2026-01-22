package model;

import interfaces.*;
import interfaces.Observer;

import java.util.*;

public class Stock implements Subject {
    private String symbol;
    private double currentPrice;
    private List<Double> priceHistory;
    private List<Observer> observers;
    private Random random;
    private List<Stock> market = new ArrayList<>();

    public Stock(String symbol, double startPrice) {
        this.symbol = symbol;
        this.currentPrice = startPrice;
        this.observers = new ArrayList<>();
        this.priceHistory = new ArrayList<>();
        this.priceHistory.add(startPrice);
        this.priceHistory.add(startPrice);
        market.add(this);
    }

    public void dodajdoMarketu() {
        market.add(this);
    }

    public int getMarketSize() {return market.size();}
    public List<Stock> getMarket() {return market;}

    public void simulatePriceChange() {
        Random r = new Random();
        int liczba;
        if(r.nextFloat(1) < 0.45) { // czas zyskow
            System.out.println("Czas dobrej koniunktury");
            liczba = 10;
            /*
            while(liczba-- > 0) {
                this.currentPrice *= (0.959 + (0.1 * new Random().nextDouble()));
            }
            */
            this.currentPrice *= (0.96 + (0.1 * new Random().nextDouble()));
        } else if(r.nextFloat(1) <= 0.95) { // stagnacja
            System.out.println("Czas stagnacji");
            /*
            liczba = 10;
            while(liczba-- > 0) {
                this.currentPrice *= (0.95 + (0.1 * new Random().nextDouble()));
            }
             */
            this.currentPrice *= (0.95 + (0.1 * new Random().nextDouble()));
        } else { // recesja(mocna)
            liczba = 10;
            System.out.println("Czas recesji");
            /*
            while(liczba-- > 0) {
                this.currentPrice *= (0.871 + (0.1 * new Random().nextDouble()));
            }
             */
            this.currentPrice *= (0.867 + (0.1 * new Random().nextDouble()));
        }

        if(this.currentPrice < 0) this.currentPrice = 0;
        this.priceHistory.add(currentPrice);
        notifyObservers();
    }

    public void setPrice(double price) {
        this.currentPrice = price;
        this.priceHistory.add(price);
        notifyObservers();
    }

    public void generatenewPrice() {simulatePriceChange();}

    @Override
    public void registerObserver(Observer o) {observers.add(o);}
    @Override
    public void removeObserver(Observer o) {observers.remove(o);}
    @Override
    public void notifyObservers() {
        for(Observer o : observers) {
            o.update(this);
        }
    }

    public String getSymbol() {return symbol;}
    public double getCurrentPrice() {return currentPrice;}
    public List<Double> getPriceHistory() {return priceHistory;}

}
