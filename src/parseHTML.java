
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import com.mongodb.client.MongoDatabase;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.MongoCredential;
import com.mongodb.client.MongoCollection;
import java.util.Arrays;
import java.util.List;
import org.jsoup.nodes.Node;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Itzik
 */
public class parseHTML {

    public static void main(String[] args) throws IOException {
        
        MongoClient mongoClient = new MongoClient();
        DB database = mongoClient.getDB("WebSite");
         

        File input = new File("C:\\Users\\Itzik\\Desktop\\Recip\\2.html");
        Document doc = Jsoup.parse(input, "UTF-8", "http://www.nikib.co.il/cakes-dessert/45670/");
        Element content = doc.getElementById("content");
        Elements links = content.getElementsByTag("a");
        Element rec = doc.select("div.pf-content").first();
        List<Node> list = rec.childNodes();
        String str = "";
        for (Node node : list) {
            String g = node.toString();
            if (g.contains("מצרכים")) {
                str = node.nextSibling().nextSibling().toString();
                break;
            }
        }
        String[] Ingredients = str.split("<br>");
        for (int i = 0 ; i < Ingredients.length ; i++) {
            if(Ingredients[i].contains("<p>")){
                Ingredients[i] = Ingredients[i].replace("<p>", "");
            }
            else if(Ingredients[i].contains("</p>")){
                Ingredients[i] = Ingredients[i].replace("</p>", ""); 
            }
        }
        for (String Ingredient : Ingredients) {
            System.out.println(Ingredient);
        }
        System.out.println("/////////////////////");
        int i1 = rec.text().indexOf("מצרכים");
        String Description = rec.text().substring(0, i1);
        System.out.println(Ingredients);
        System.out.println(Description);
        
        
        DBObject recipe = new BasicDBObject("link" , "http://www.nikib.co.il/cakes-dessert/45670/")
                                      .append("Description", Description)
                                      .append("Ingredients", Ingredients);
        DBCollection collection = database.getCollection("recipe");
        collection.insert(recipe);
    }
}
