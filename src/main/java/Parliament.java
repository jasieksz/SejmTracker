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

        return 0.0;
    }

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

        while (pages > 0) {
            JSONObject nextPage = JsonParser.readJsonFromUrl(getLinkToNext(parliamentObject));
            JSONArray parliament = parliamentObject.getJSONArray("Dataobject");

            for (Object mp : parliament) {
                JSONObject mpObject = (JSONObject) mp;
                String id = mpObject.getString("id");
                String nazwa = mpObject.getJSONObject("data").getString("ludzie.nazwa");
                result.put(nazwa, valueOf(id));
            }

            parliamentObject = nextPage;
            pages--;
        }
        return result;
    }

    public static void makeMPList(JSONObject parliamentObject){ //tworzy liste MP danej kadencji
        //TODO : "kod jak makeParliament, tylko dodac fragment konstruktora MP.details i wrzucoc do listy

    }

    private static String getLinkToNext(JSONObject jsonObject) {
        try { //ostatnia strona nie ma linku next , czy bedzie wtedy exception??/
            return jsonObject.getJSONObject("Links").getString("next");
        }catch (JSONException e){
            return null;
        }
    }

    private static Integer getNumberOfLast(JSONObject jsonObject){
        String last = jsonObject.getJSONObject("Links").getString("last");
        last = new StringBuilder(last).reverse().toString();
        last = last.substring(0,last.indexOf("=")-1); // czy dobrze indeksy, zakaldam ze parametr page=1 ostatni
        Integer result = valueOf(last);
        return result;
    }
}
