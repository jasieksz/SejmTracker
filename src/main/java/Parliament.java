import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

    public static HashMap<String,Integer> makeParliament(JSONObject parliamentObject) throws JSONException {

        makeMPList(parliamentObject);
        HashMap<String,Integer> result = new HashMap<>();
        JSONArray parliament = parliamentObject.getJSONArray("Dataobject");

        for (Object mp: parliament) {
            JSONObject mpObject = (JSONObject) mp;
            String id = mpObject.getString("id");
            String nazwa = mpObject.getJSONObject("data").getString("ludzie.nazwa");
            result.put(nazwa,valueOf(id));
        }
        return result;
    }

    private static void makeMPList(JSONObject parliamentObject){

    }
}
