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
            long startTime = System.currentTimeMillis();
            System.out.println(run(args));
            long stopTime = System.currentTimeMillis();
            long elapsedTime = stopTime - startTime;
            System.out.println(elapsedTime);
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
            InputParser.run(args);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }


        return "result";
    }
}
//"[Rafał Grupiński, Joanna Fabisiak, Andrzej Czerwiński, Cezary Grabarczyk, Agnieszka Pomaska, Jan Dziedziczak, Grzegorz Raniewicz, Antoni Mężydło, Roman Jacek Kosecki, Ireneusz Raś, Grzegorz Schetyna, Stefan Niesiołowski, Sławomir Neumann, Jakub Rutnicki, Krystyna Skowrońska, Jacek Falfus, Adam Abramowicz, Ewa Kopacz, Anna Nemś, Robert Tyszkiewicz, Marek Rząsa, Cezary Tomczyk, Michał Jaros, Marek Matuszewski, Wojciech Ziemniak"]