import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static java.lang.Integer.valueOf;

public class Parliament {

    public static Integer getMpList() {
        return mpList.size();
    }

    private static List<MP> mpList = new ArrayList<>();//Collections.synchronizedList(new ArrayList<MP>()); //lista mp aktualnej kadencji
    private static Map<String,Integer> mpMap = new ConcurrentHashMap<>();

//========================================== MAPA WSZYSTKICH POSLOW ==================================
    public static void makeParliament(List<JSONArray> parlimentLinks){
        parlimentLinks.parallelStream().forEach(Parliament::addMPMap);
    }

    private static void addMPMap (JSONArray parliament){
        for (Object mp : parliament) {
            JSONObject mpObject = (JSONObject) mp;
            String id = mpObject.getString("id");
            String nazwa = mpObject.getJSONObject("data").getString("poslowie.nazwa");
            mpMap.put(nazwa, valueOf(id));
        }
    }

//========================================== LISTA POSLOW PRZETWARZANEJ KADENCJI==================================
    public static void makeMPList (List<JSONArray> parliamentLinks){
        parliamentLinks.parallelStream().forEach(Parliament::addMPList);
    }
    private static void addMPList(JSONArray parliament) {
        for (Object mp : parliament) {
            JSONObject mpObject = (JSONObject) mp;
            Integer id = valueOf(mpObject.getString("id"));
            String name = mpObject.getJSONObject("data").getString("poslowie.nazwa");
            JSONObject details = null;
            try {
                details = JsonParser.readJsonFromUrl(InputParser.makeUrl(name,"everything"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            MP tmpMP = new MP(id,name,details);
            mpList.add(tmpMP);
        }
    }
//====================================== METODY DO OBSLUGI POZOSTALYCH ===============================================
    public static List<JSONArray> prepareParliamentLinks(JSONObject JsonPage) throws IOException {
        List<JSONArray> result = new ArrayList<>();
        JSONObject tmpPage;
        do {
            tmpPage = JsonPage;
            JSONArray parliamentPage = JsonPage.getJSONArray("Dataobject");
            result.add(parliamentPage);
            String nextLink = getLinkToNext(JsonPage);
            if (nextLink != null)
                JsonPage = JsonParser.readJsonFromUrl(nextLink);

        }while (isLinkToNext(tmpPage));
        return result;
    }

    private static String getLinkToNext(JSONObject jsonObject) {
        try { //ostatnia strona nie ma linku next , czy bedzie wtedy exception??/
            return jsonObject.getJSONObject("Links").getString("next");
        }catch (JSONException e){
            return null;
        }
    }

    private static Boolean isLinkToNext(JSONObject jsonObject) {
        try { //ostatnia strona nie ma linku next , czy bedzie wtedy exception??/
            String s = jsonObject.getJSONObject("Links").getString("next");
            return true;
        }catch (JSONException e){
            return false;
        }
    }

//====================================================== PYTANIA DOTYCZACE POSLOW=================================
    public static Map<String, Integer> getMpMap() {
        return mpMap;
    }

    public static Double averageExpenses(){
        Double result = 0.0;
        result = mpList.parallelStream().mapToDouble(MP::sumExpenses).sum() /mpList.size();
        return result;
    }
    //TODO : "Funkcje wyższego rzędu, java 8"
    public static MP mostTravels(){
        MP member = null;
        Integer tmpMax = -1;
        for (MP tmpMp : mpList){
            if (tmpMp.numberTravels()>tmpMax){
                tmpMax = tmpMp.numberTravels();
                member = tmpMp;
            }
        }
        return member;
    }

    public static MP longestTravels(){
        MP member = null;
        Integer tmpMax = -1;
        for (MP tmpMp : mpList){
            if (tmpMp.timeTravels()>tmpMax){
                tmpMax = tmpMp.timeTravels();
                member = tmpMp;
            }
        }
        return member;
    }

    public static MP expensiveTravels(){
        MP member = null;
        Double tmpMax = -1.0;
        for (MP tmpMp : mpList){
            if (tmpMp.costTravels()>tmpMax){
                tmpMax = tmpMp.costTravels();
                member = tmpMp;
            }
        }
        return member;
    }

    public static List<String> italyTravels(){
        //List<MP> memberList = new ArrayList<>();
       /* for (MP tmpMp : mpList){
            if (tmpMp.italyTravels())
                memberList.add(tmpMp);
        }*/
        //return memberList;
        return mpList.stream().filter(MP::italyTravels).map(MP::toString).collect(Collectors.toList());
    }


}

//  "Cezary Grabarczyk, Antoni Mężydło, Jan Dziedziczak, Krystyna Skowrońska, Ireneusz Raś, Adam Abramowicz, Michał Jaros, Ewa Kopacz, Anna Nemś, Agnieszka Pomaska, Cezary Tomczyk, Grzegorz Raniewicz, Marek Matuszewski, Roman Jacek Kosecki, Joanna Fabisiak, Sławomir Neumann, Rafał Grupiński, Wojciech Ziemniak, Andrzej Czerwiński, Jakub Rutnicki, Robert Tyszkiewicz, Marek Rząsa, Jacek Falfus, Grzegorz Schetyna, Stefan Niesiołowski"


/*
    public static HashMap<String,Integer> makeParliament(JSONObject parliamentObject) throws JSONException, IOException {
        HashMap<String,Integer> result = new HashMap<>();
        Integer pages = getNumberOfLast(parliamentObject);
        while (pages > 0 && isLinkToNext(parliamentObject)) { //wystarczy jeden warunek

            JSONArray parliament = parliamentObject.getJSONArray("Dataobject");
            for (Object mp : parliament) {
                JSONObject mpObject = (JSONObject) mp;
                String id = mpObject.getString("id");
                String nazwa = mpObject.getJSONObject("data").getString("ludzie.nazwa");
                result.put(nazwa, valueOf(id));
            }

            String nextLink = getLinkToNext(parliamentObject);
            parliamentObject = JsonParser.readJsonFromUrl(nextLink);
            pages--;
        }
//PRZETRWOZENIE OSTANTIEJ STRONY
        JSONArray parliament = parliamentObject.getJSONArray("Dataobject");
        for (Object mp : parliament) {
            JSONObject mpObject = (JSONObject) mp;
            String id = mpObject.getString("id");
            String nazwa = mpObject.getJSONObject("data").getString("ludzie.nazwa");
            result.put(nazwa, valueOf(id));
        }
        return result;
    }

    public static void makeMPList(JSONObject parliamentObject) throws IOException { //tworzy liste MP danej kadencji
        JSONObject tmpPage;
        do {
            tmpPage = parliamentObject;
            JSONArray parliament = parliamentObject.getJSONArray("Dataobject");
            addMPList(parliament);
            String nextLink = getLinkToNext(parliamentObject);
            if (nextLink != null)
                parliamentObject = JsonParser.readJsonFromUrl(nextLink);

        }while (isLinkToNext(tmpPage));
    }

        private static Integer getNumberOfLast(JSONObject jsonObject){
        String last = jsonObject.getJSONObject("Links").getString("last");
        last = new StringBuilder(last).reverse().toString();
        last = last.substring(0,last.indexOf("=")); // czy dobrze indeksy, zakaldam ze parametr page=1 ostatni
        last = new StringBuilder(last).reverse().toString();
        return valueOf(last);
    }
*/
