import java.util.ArrayList;
import java.util.List;

public class Item {
    private String name;
    private double price;
    private Category category; // Using enum Category
    private boolean isAvailable;
    public List<String> reviews;
    public int count;
    private int quantity;

    public Item(String name, double price, Category category, int quantity) {
        this.name = name;
        this.price = price;
        this.category = category;
        this.quantity = quantity;
        this.isAvailable = quantity > 0;
        this.reviews = new ArrayList<>();
        this.count = 0;
    }

    public Item(String name) {
        this.name = name;
        this.price = 0.0;
        this.category = Category.SNACKS;
        this.quantity = 0;
        this.isAvailable = false;
        this.reviews = new ArrayList<>();
        this.count = 0;
    }


    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public Category getCategory() {
        return category;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public List<String> getReviews() {
        return reviews;
    }

    public int getCount() {
        return count;
    }


    public int getQuantity(){
        return quantity;
    }

    public void updatePrice(double newPrice) {
        this.price = newPrice;
    }

    public void updateAvailability(boolean availability) {
        this.isAvailable = availability;
    }

    public void addReview(String review) {
        this.reviews.add(review);
    }

    public void incrementCount() {
        this.count++;
    }

    public void decrementQuantity(int quantity) {
        if (quantity > 0 && this.quantity >= quantity) {
            this.quantity -= quantity;
        }
        if (this.quantity == 0) {
            this.isAvailable = false;
        }
    }


    public void incrementQuantity(int quantity){
        this.quantity+=quantity;
    }



    public void updateQuantity(int newQuan) {
        this.quantity = newQuan;
        this.isAvailable = newQuan > 0; // Update availability based on quantity
    }

    @Override
    public String toString() {
        return "Item{" +
                "name='" + name + '\'' +
                ", price=" + price +
                ", category='" + category + '\'' +
                ", availability=" + (isAvailable ? "Available" : "Not Available") +
                '}';
    }

    // Enum for Item Categories
    public enum Category {
        SNACKS,
        MEALS,
        BEVERAGES,
        DESSERTS,
        APPETIZERS
    }
}
