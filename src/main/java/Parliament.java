import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static java.lang.Integer.valueOf;

public class Parliament {
    private static List<MP> mpList = new ArrayList<>(); //lista mp aktualnej kadencji

    public static Double averageExpenses(){
        Double result = 0.0;
        for (MP tmpMp : mpList){
            result += tmpMp.sumExpenses();
        }
        result = result/mpList.size();
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

    public static List<MP> italyTravels(){
        List<MP> memberList = new ArrayList<>();
        for (MP tmpMp : mpList){
            if (tmpMp.italyTravels())
                memberList.add(tmpMp);
        }
        return memberList;
    }

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
        //TODO : "poprawic warunki w petli tak zeby obslugiwala ostatnio strone"
        Integer pages = getNumberOfLast(parliamentObject);
       // MP tmpMP = new MP(2,"name",JsonParser.readJsonFromUrl(InputParser.makeUrl("name","everything")));

        while (pages > 0 && isLinkToNext(parliamentObject)) { //wystarczy jeden warunek
            JSONArray parliament = parliamentObject.getJSONArray("Dataobject");
            for (Object mp : parliament) {
                JSONObject mpObject = (JSONObject) mp;
                Integer id = valueOf(mpObject.getString("id"));
                String name = mpObject.getJSONObject("data").getString("ludzie.nazwa");
                JSONObject details = JsonParser.readJsonFromUrl(InputParser.makeUrl(name,"everything"));
                MP tmpMP = new MP(id,name,details);
                mpList.add(tmpMP);
            }

            String nextLink = getLinkToNext(parliamentObject);
            parliamentObject = JsonParser.readJsonFromUrl(nextLink);
            pages--;
        }
        //PRZETRWOZENIE OSTANTIEJ STRONY
        JSONArray parliament = parliamentObject.getJSONArray("Dataobject");
        for (Object mp : parliament) {
            JSONObject mpObject = (JSONObject) mp;
            Integer id = valueOf(mpObject.getString("id"));
            String name = mpObject.getJSONObject("data").getString("ludzie.nazwa");
            JSONObject details = JsonParser.readJsonFromUrl(InputParser.makeUrl(name,"everything"));
            MP tmpMP = new MP(id,name,details);
            mpList.add(tmpMP);
        }
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

    private static Integer getNumberOfLast(JSONObject jsonObject){
        String last = jsonObject.getJSONObject("Links").getString("last");
        last = new StringBuilder(last).reverse().toString();
        last = last.substring(0,last.indexOf("=")); // czy dobrze indeksy, zakaldam ze parametr page=1 ostatni
        last = new StringBuilder(last).reverse().toString();
        return valueOf(last);
    }
}
