package MavenProject.SponsoredAds.Model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import MavenProject.SponsoredAds.Processing.PromotedProduct;

public class Campaign {
    private String name;
    private LocalDate startDate;
    private List<Product> products;
    private double bid;

    public Campaign(String name, LocalDate startDate, List<Product> products, double bid) {
        this.name = name;
        this.startDate = startDate;
        this.bid = bid;
        this.products = new ArrayList<Product>();
    }

    public String getName() {
        return name;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public double getBid() {
        return bid;
    }

    public void addProduct(Product product) {
        products.add(product);
    }

    public List<Product> getProducts() {
        return products;
    }

    //Campaign is considered active for the 10 days following its start-date
    //It returns true if the campaign is active on the given date and false otherwise.
    public boolean isActive(LocalDate currentDate) {
        LocalDate endDate = startDate.plusDays(10);
        return currentDate.isAfter(startDate) && currentDate.isBefore(endDate.plusDays(1));
    }
  
    public PromotedProduct getPromotedProductByCategory(String category) {
        List<PromotedProduct> promotedProducts = new ArrayList<PromotedProduct>();
        for (Product product : products) {
            if (product.getCategory().equals(category)) {
                promotedProducts.add(new PromotedProduct(
                    product.getTitle(),
                    product.getCategory(),
                    product.getPrice(),
                    product.getSerialNumber(),
                    this.bid
                ));
            }
        }

        // If there are promoted products in the specified category, return the one with the highest bid
        if (!promotedProducts.isEmpty()) {
            PromotedProduct highestBidProduct = promotedProducts.get(0);
            for (PromotedProduct promotedProduct : promotedProducts) {
                if (promotedProduct.getBid() > highestBidProduct.getBid()) {
                    highestBidProduct = promotedProduct;
                }
            }
            return highestBidProduct;
        }

        
        return null;
    }
}



