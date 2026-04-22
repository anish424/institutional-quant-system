public class Trade {
    private String id;
    private String symbol;
    private double quantity;
    private double price;
    private String timestamp;

    public Trade(String id, String symbol, double quantity, double price, String timestamp) {
        this.id = id;
        this.symbol = symbol;
        this.quantity = quantity;
        this.price = price;
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }

    public String getSymbol() {
        return symbol;
    }

    public double getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }

    public String getTimestamp() {
        return timestamp;
    }
}