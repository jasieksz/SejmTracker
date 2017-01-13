import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static java.lang.Integer.valueOf;

public class Parliament {

    private final static String allMPsUrl = "https://api-v3.mojepanstwo.pl/dane/poslowie.json?limit=186";
    private final Integer kadencja;
    private List<MP> mpList = Collections.synchronizedList(new ArrayList<MP>()); //lista mp aktualnej kadencji
    public Map<String,Integer> mpMap = new ConcurrentHashMap<>();


    public Parliament(Integer kadencja) throws IOException {
        this.kadencja = kadencja;
        makeMPList(Parliament.prepareParliamentLinks(JsonParser.readJsonFromUrl(InputParser.makeUrl(this.kadencja.toString(),0, "parliament"))));
        makeParliament(Parliament.prepareParliamentLinks(JsonParser.readJsonFromUrl(allMPsUrl)));
    }

    public Parliament() throws IOException { //1 posel
        this.kadencja = null;
        makeParliament(Parliament.prepareParliamentLinks(JsonParser.readJsonFromUrl(allMPsUrl)));
    }

//========================================== MAPA WSZYSTKICH POSLOW ==================================
    public void makeParliament(List<JSONArray> parlimentLinks){
        parlimentLinks.parallelStream().forEach(this::addMPMap);
    }

    private void addMPMap (JSONArray parliament){
        for (Object mp : parliament) {
            JSONObject mpObject = (JSONObject) mp;
            String id = mpObject.getString("id");
            String nazwa = mpObject.getJSONObject("data").getString("poslowie.nazwa");
            this.mpMap.put(nazwa, valueOf(id));
        }
    }

//========================================== LISTA POSLOW PRZETWARZANEJ KADENCJI==================================
    private void makeMPList (List<JSONArray> parliamentLinks){
        parliamentLinks.parallelStream().forEach(this::addMPList);
        //System.out.println("rozmiar listy mp : "+mpList.size());
    }
    private void addMPList(JSONArray parliament) {
        for (Object mp : parliament) {
            JSONObject mpObject = (JSONObject) mp;
            Integer id = valueOf(mpObject.getString("id"));
            String name = mpObject.getJSONObject("data").getString("poslowie.nazwa");
            JSONObject details = null;
            try {
                details = JsonParser.readJsonFromUrl(InputParser.makeUrl(name,id,"everything"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            MP tmpMP = new MP(id,name,details);
            this.mpList.add(tmpMP);
            //System.out.println(tmpMP.toString());
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
    public Map<String, Integer> getMpMap() {
        return this.mpMap;
    }

    public Double averageExpenses(){
        return mpList.parallelStream().mapToDouble(MP::sumExpenses).sum() / mpList.size();
       // return round(value * 100)/100.00;
    }

    public List<String> italyTravels(){
        return mpList.parallelStream().filter(MP::italyTravels).map(MP::toString).collect(Collectors.toList());
    }


    //TODO : "Funkcje wyższego rzędu, java 8"
    public MP mostTravels(){
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

    public MP longestTravels(){
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

    public MP expensiveTravels(){
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

    public List<MP> getMpList() {
        return mpList;
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
