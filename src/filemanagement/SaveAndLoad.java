package filemanagement;

import model.Stock;

import java.io.File;
import java.util.Scanner;

public class SaveAndLoad {

    public static Stock loadStock(String filename) {
        File plik = new File(filename);
        String nameonly = plik.getName();
        String companyname = nameonly.split("_")[0].toUpperCase();
        try(Scanner sc = new Scanner(plik)) {
            if(!sc.hasNextLine()) return null;
            sc.nextLine();

            Stock stock = null;
            double lastPrice = 0;

            while(sc.hasNextLine()) {
                String line = sc.nextLine();
                if(line.isEmpty()) continue;

                String[] parts = line.split(",");
                if(parts.length < 5) continue;


                String date = parts[0];
                int year = Integer.parseInt(date.substring(0, 4));

                if(year >= 2010) {
                    double openPrice = Double.parseDouble(parts[1]);
                    double minPrice = Double.parseDouble(parts[3]);
                    double maxPrice = Double.parseDouble(parts[2]);
                    double closePrice = Double.parseDouble(parts[4]);

                    double avgPrice = (openPrice + closePrice + minPrice + maxPrice) / 4;

                    lastPrice = avgPrice;

                    if(stock == null) stock = new Stock(companyname, avgPrice);
                    else { stock.getPriceHistory().add(avgPrice);}
                }
            }

            if(stock != null) {
                stock.setPrice(lastPrice);
            }
            return stock;
        } catch (Exception e) {
            return null;
        }
    }
}
