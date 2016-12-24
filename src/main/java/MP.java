import org.json.JSONException;
import org.json.JSONObject;

public class MP {
    public final Integer id; //id
    public final String name; //imie i nazwisko =JSON=> ludzie.nazwa
    private final JSONObject details;

    public MP(Integer id, String name, JSONObject details) {
        this.id = id;
        this.name = name;
        this.details = details;
    }

    public MP(Integer id, String name) {
        this.id = id;
        this.name = name;
        this.details = null;
    }

    public Double sumExpenses() throws JSONException {

        details.getJSONObject("cos").getJSONArray("cos2");
        return 0.0;
    }

    public Double smallExpenses(){
        return 0.0;
    }

    public Integer numberTravels(){

        return 0;
    }

    public Integer timeTravels(){

        return 0;
    }

    public Double costTravels(){

        return 0.0;
    }

    public Boolean italyTravels(){

        return false;
    }




    @Override
    public String toString() {
        return name;
    }
}
