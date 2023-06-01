package MavenProject.SponsoredAds.Processing;

public class PromotedProduct {

    private String title;
    private String category;
    private double price;
    private String serialNumber;
    private double bid;
    
    //we use this model because the bid is a campaign parameter and so that when comparing products from the same category,
    //but different campaigns, choose the maximum. Then we assign this bid field to each of the products
    public PromotedProduct(String title, String category, double price, String serialNumber,double bid) {
        this.title = title;
        this.category = category;
        this.price = price;
        this.serialNumber = serialNumber;
        this.bid = bid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }
    
    public double getBid() {
        return bid;
    }

    public void setBid(double bid) {
        this.bid = bid;
    }
}
