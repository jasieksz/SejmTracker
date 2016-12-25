import org.json.JSONArray;
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

        Double result = 0.0;
        JSONArray expensesYears = details.getJSONObject("layers").getJSONObject("wydatki").getJSONArray("roczniki");

        for (Object expensesObject : expensesYears){
            JSONObject expenses = (JSONObject) expensesObject;

            for (Object pointValue : expenses.getJSONArray("pola")) { // SUMUJE WSZYSTKIE POLE
                double value = Double.parseDouble((String) pointValue);
                result += value;
            }
        }

        return result;
    }



    public Double smallExpenses(){

        Double result = 0.0;
        JSONArray expensesYears = details.getJSONObject("layers").getJSONObject("wydatki").getJSONArray("roczniki");

        for (Object expensesObject : expensesYears){
            JSONObject expenses = (JSONObject) expensesObject;
            result += expenses.getJSONArray("pola").getDouble(12);
        }

        return result;

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
