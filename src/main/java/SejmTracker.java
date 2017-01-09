import org.json.JSONException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SejmTracker {
    //TODO : "Serialization of parliament hashmap"
    public static Map<String,Integer> parliamentMap = new HashMap<>(); // NAME -> ID posłowie wszystkich kadnecji
    private final static String allMPsUrl = "https://api-v3.mojepanstwo.pl/dane/poslowie.json?limit=188";

    private static String prop1 = "java.util.concurrent.ForkJoinPool.common.parallelism";
    private static String threads = "32";


    public static void main(String[] args) {
        try {
            long startTime = System.currentTimeMillis();
            System.setProperty(prop1, threads);
            System.out.println(run(args));
            long stopTime = System.currentTimeMillis();
            long elapsedTime = stopTime - startTime;
            System.out.println(elapsedTime);
        }
        catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    //TODO : "Catching exceptions"
    private static String run(String[] args) throws IllegalArgumentException{
        try {
            Parliament.makeParliament(Parliament.prepareParliamentLinks(JsonParser.readJsonFromUrl(allMPsUrl)));
            parliamentMap = Parliament.getMpMap();
            System.out.println(parliamentMap.size()); // 3-40 sec
            //return InputParser.run(args);
        } catch (IOException | JSONException e){
            e.printStackTrace();
        }
        return null;
    }
}
//"Rafał Grupiński, Joanna Fabisiak, Andrzej Czerwiński, Cezary Grabarczyk, Agnieszka Pomaska, Jan Dziedziczak, Grzegorz Raniewicz, Antoni Mężydło, Roman Jacek Kosecki, Ireneusz Raś, Grzegorz Schetyna, Stefan Niesiołowski, Sławomir Neumann, Jakub Rutnicki, Krystyna Skowrońska, Jacek Falfus, Adam Abramowicz, Ewa Kopacz, Anna Nemś, Robert Tyszkiewicz, Marek Rząsa, Cezary Tomczyk, Michał Jaros, Marek Matuszewski, Wojciech Ziemniak"