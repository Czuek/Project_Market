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

    public Stock(String symbol, double startPrice) {
        this.symbol = symbol;
        this.currentPrice = startPrice;
        this.observers = new ArrayList<>();
        this.priceHistory = new ArrayList<>();
        this.priceHistory.add(startPrice);
        this.priceHistory.add(startPrice);
    }

    public void simulatePriceChange() {
        Random r = new Random();
        int liczba;
        if(r.nextFloat(1) <= 0.95) {
            System.out.println("Czas dobrej koniunktury");
        } else {
            System.out.println("Donald Trump planuje zaatakować kraj!");
            liczba = 10;
            while()
        }

        if(this.currentPrice < 1) this.currentPrice = 1;
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
