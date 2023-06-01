package MavenProject.SponsoredAds.Controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import MavenProject.SponsoredAds.Dao.CampaignDAO;
import MavenProject.SponsoredAds.Model.Campaign;
import MavenProject.SponsoredAds.Model.Product;
import MavenProject.SponsoredAds.Processing.PromotedProduct;



@RestController
public class CampaignController {

	//API for creating a campaign and returning a JSON representation of the created campaign, 
	//we define a POST mapping for the /campaigns endpoint. The createCampaign method receives a CampaignRequest object in the request body, which contains the campaign parameters (name, startDate, productSet, bid).
    //Inside the createCampaign method, we extract the parameters from the request and create a new Campaign object. 
	//We then return a ResponseEntity with the created campaign in the response body and the appropriate HTTP status (CREATED).
    @PostMapping("/campaigns")
    public ResponseEntity<Campaign> createCampaign(@RequestBody Campaign campaign) {
        // Extract campaign parameters from the request
        String name = campaign.getName();
        LocalDate startDate = campaign.getStartDate();
        List<Product> productSet = campaign.getProducts();
        double bid = campaign.getBid();

        // Create a new campaign object
        Campaign campaignRequest = new Campaign(name, startDate, productSet, bid);

        // Return the created campaign in the response body
        return ResponseEntity.status(HttpStatus.CREATED).body(campaignRequest);
    }
    
    //Inside the serveAd method, we filter the active campaigns and their associated products based on the specified category. 
    //Then, we find the product with the highest bid among the filtered products. 
    //If such a product exists, we create a PromotedProduct object with the product's details and return it in the response. 
    //If no promoted product is found for the specified category, we find the product with the highest bid among all the active campaigns' 
  
    @GetMapping("/ads/{category}")
    public ResponseEntity<PromotedProduct> serveAd(@PathVariable String category) {
        PromotedProduct promotedProduct = null;
        
        //creates an instance of CampaignDAO and calls the getAllCampaignsFromMongoDB method to fetch all campaigns from MongoDB.

        CampaignDAO campaignDAO = new CampaignDAO();
        List<Campaign> campaigns = campaignDAO.getAllCampaignsFromMongoDB(); // request to MongoDB

        //to check what was received from the database
        ObjectMapper objectMapper = new ObjectMapper();
        String campaignsJson = null;
		try {
			campaignsJson = objectMapper.writeValueAsString(campaigns);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
        System.out.println(campaignsJson);
        
   
        // Iterate through Active campaigns and find the promoted product with the highest bid for the specified category
        for (Campaign campaign : campaigns) {
            if (campaign.isActive(LocalDate.now())) {
                PromotedProduct productByCategory = campaign.getPromotedProductByCategory(category);
                if (promotedProduct == null || productByCategory.getBid() > promotedProduct.getBid()) {
                    promotedProduct = productByCategory;
                }
            }
        }

        // If no promoted product found for the specified category, return the one with the highest bid
        //since the bid is a campaign parameter - first we find the Active campaign with the maximum bid, and then the first product from it
        
        if (promotedProduct == null) {
        	Campaign highestCampaignBid = null;
            for (Campaign campaign : campaigns) {
                if (campaign.isActive(LocalDate.now())) {
                    if (highestCampaignBid == null || campaign.getBid() > highestCampaignBid.getBid()) {
                    	highestCampaignBid = campaign;
                    
                    }
                    
                }
           
            }
            List<Product> productHighestBid = highestCampaignBid.getProducts();
            Product productMaxBid = productHighestBid.get(0);
            promotedProduct = new PromotedProduct(
            		productMaxBid.getTitle(),
            		productMaxBid.getCategory(),
            		productMaxBid.getPrice(),
            		productMaxBid.getSerialNumber(),
            		highestCampaignBid.getBid()
            );
        }


            return ResponseEntity.ok(promotedProduct);
        } 
    }
