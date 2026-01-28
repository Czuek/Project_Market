package filemanagement;

import model.Stock;

import java.io.File;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Scanner;

public class SaveAndLoad {

    public static Stock loadStock(String filename) {
        File plik = new File(filename);
        String nameonly = plik.getName();
        String companyname = nameonly.split("_")[0].toUpperCase();

        LocalDate marketStart = LocalDate.of(2010,1,1);
        try(Scanner sc = new Scanner(plik)) {
            if(!sc.hasNextLine()) return null;
            sc.nextLine();

            Stock stock = null;
            double lastPrice = 0;

            LocalDate expectedDate = marketStart;

            while(sc.hasNextLine()) {
                String line = sc.nextLine();
                if(line.isEmpty()) continue;

                String[] parts = line.split(",");
                if(parts.length < 5) continue;

                String date = parts[0];
                int year = Integer.parseInt(date.substring(0, 4));

                if(year >= 2020) {
                    double openPrice = Double.parseDouble(parts[1]);
                    double minPrice = Double.parseDouble(parts[3]);
                    double maxPrice = Double.parseDouble(parts[2]);
                    double closePrice = Double.parseDouble(parts[4]);

                    double avgPrice = (openPrice + closePrice + minPrice + maxPrice) / 4;

                    LocalDate currentDate = LocalDate.parse(date);

                    if(stock == null) {
                        stock = new Stock(companyname, 0.0);
                        stock.getPriceHistory().clear();
                    }


                        while(expectedDate.isBefore(currentDate)) {
                            DayOfWeek dow = expectedDate.getDayOfWeek();
                            if(dow != DayOfWeek.SATURDAY && dow != DayOfWeek.SUNDAY) {
                                double fillPrice = stock.getPriceHistory().isEmpty() ? 0.0 : lastPrice;
                                stock.getPriceHistory().add(fillPrice);
                            }
                            expectedDate = expectedDate.plusDays(1);
                        }
                        stock.getPriceHistory().add(avgPrice);
                        lastPrice = avgPrice;
                        expectedDate = expectedDate.plusDays(1);
                    }
                }
            if(stock != null) {
                stock.setPrice(lastPrice);
            }
            return stock;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
