import org.json.JSONException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SejmTracker {
    //TODO : "Serialization of parliament hashmap"
    public static Map<String,Integer> parliament = new HashMap<>(); // NAME -> ID posłowie wszystkich kadnecji
    private final static String allMPsUrl = "https://api-v3.mojepanstwo.pl/dane/poslowie.json";

    public static void main(String[] args) {
        try {
            System.out.println(run(args));
        }
        catch (IllegalArgumentException e){
            e.printStackTrace();
        }
        catch (IndexOutOfBoundsException e){
            System.out.println("Nie podano argumentow\n" + "Piekna instrukcja obslugi");
        }
    }

    //TODO : "Catching exceptions"
    private static String run(String[] args) {

        try {
            parliament = Parliament.makeParliament(JsonParser.readJsonFromUrl(allMPsUrl)); //size = 728
            System.out.println("time");
            InputParser.run(args);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }


        return "result";
    }
}
