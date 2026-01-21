package interfaces;

import model.Stock;

public interface Observer {
    void update(Stock stock);
}
