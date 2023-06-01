package MavenProject.SponsoredAds.Dao;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

import MavenProject.SponsoredAds.Model.Campaign;

import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class CampaignDAO {
    private static final String DATABASE_NAME = "my_database_name";
    private static final String COLLECTION_NAME = "my_collection_name";

    //in pom.xml dependencies set up for MongoDB Java driver (mongodb-driver-sync and mongodb-driver-core) and the Jackson JSON library (jackson-databind).
   
    //getAllCampaignsFromMongoDB method connects to the MongoDB server, 
    //retrieves the database and collection, and iterates over the documents in the collection using a MongoCursor. 
    //For each document, the convertDocumentToCampaign method is called to convert the Document object to a Campaign object.
    
    public List<Campaign> getAllCampaignsFromMongoDB() {
        List<Campaign> campaigns = new ArrayList<Campaign>();

        try (MongoClient mongoClient = MongoClients.create()) {
            MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);
            MongoCollection<?> collection = database.getCollection(COLLECTION_NAME); // <?> MongoCollection can work with any type of documents 

            try (MongoCursor<?> cursor = collection.find().iterator()) {
                while (cursor.hasNext()) {
                    Document document = (Document) cursor.next();
                    Campaign campaign = convertDocumentToCampaign(document);
                    campaigns.add(campaign);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return campaigns;
    }

    //this method uses the Jackson ObjectMapper to convert the JSON string representation of the document to a Campaign object.
   
    private Campaign convertDocumentToCampaign(Document document) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String json = document.toJson();
            return objectMapper.readValue(json, Campaign.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}