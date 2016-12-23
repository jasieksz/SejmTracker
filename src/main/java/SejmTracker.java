import org.json.JSONException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SejmTracker {
    public static Map<String,Integer> parliament = new HashMap<>(); // NAME -> ID posłowie wszystkich kadnecji
    private final static String allMPsUrl = "https://api-v3.mojepanstwo.pl/dane/poslowie.json";

    public static void main(String[] args) {
        try {
            System.out.println(run(args));
        }
        catch (IllegalArgumentException e){
            e.printStackTrace();
        }
    }

    //TODO : "Catching exceptions"
    private static String run(String[] args) {

        try {
            //TODO : "potrzebuje stworzyc na samym poczatku mape poslow, ale nie wiem dla ktorej kadencji, robie to dopiero w InputParser"
            parliament = Parliament.makeParliament(JsonParser.readJsonFromUrl(allMPsUrl));
            InputParser.run(args);


        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }


        return "result";
    }
}
