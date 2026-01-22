package filemanagement;

import model.Stock;

import java.util.Scanner;

public class SaveAndLoad {

    public static Stock loadStock(String filename) {
        String[] companynamesplit = filename.split("_");
        String companyname = (companynamesplit[0] + "_" + companynamesplit[1]).toUpperCase();
        try(Scanner sc = new Scanner(filename)) {
            sc.nextLine();
            String line = sc.nextLine();
            if(line == null) return null;
            String[] parts = line.split(",");
            while(sc.hasNextLine()) {
                if(line.isEmpty()) continue;
                if(parts.length >= 6) {
                    double openPrice = Double.parseDouble(parts[1]);
                    double minPrice = Double.parseDouble(parts[3]);
                    double maxPrice = Double.parseDouble(parts[2]);
                    double closePrice = Double.parseDouble(parts[4]);

                    double avgPrice = (openPrice + closePrice + minPrice + maxPrice) / 4;

                    return new Stock(companyname, avgPrice);
                }
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }
}
