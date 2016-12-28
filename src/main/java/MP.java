import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static java.lang.Double.max;
import static java.lang.Double.parseDouble;
import static java.lang.Double.valueOf;
import static java.lang.Integer.max;

public class MP {
    public final Integer id; //id
    public final String name; //imie i nazwisko =JSON=> ludzie.nazwa
    private final JSONObject details;

    public MP(Integer id, String name, JSONObject details) {
        this.id = id;
        this.name = name;
        this.details = details;
    }

    public Double sumExpenses() throws JSONException {
        Double result = 0.0;
        JSONArray expensesYears = details.getJSONObject("layers").getJSONObject("wydatki").getJSONArray("roczniki");
        for (Object expensesObject : expensesYears){
            JSONObject expenses = (JSONObject) expensesObject;
            for (Object pointValue : expenses.getJSONArray("pola")) { // SUMUJE WSZYSTKIE POLE
                double value = parseDouble((String) pointValue);
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

    public Integer numberTravels(){ //NAJWIECEJ WYJAZDOW
        if (details.getJSONObject("layers").optJSONArray("wyjazdy") == null)
            return 0;

        return details.getJSONObject("layers").getJSONArray("wyjazdy").length();
    }

    public Integer timeTravels(){ // NAJDLUZSZY WYJAZD
        if (details.getJSONObject("layers").optJSONArray("wyjazdy") == null)
            return 0;
        JSONArray travels = details.getJSONObject("layers").getJSONArray("wyjazdy");
        Integer result = -1;
        for (Object tripObject: travels) {
            JSONObject trip = (JSONObject) tripObject;
            Integer days = trip.getInt("liczba_dni");
            result = max(result,days);
        }
        return result;
    }

    public Double costTravels(){ //NAJDROZSZY WYJAZD
        if (details.getJSONObject("layers").optJSONArray("wyjazdy") == null)
            return 0.0;
        JSONArray travels = details.getJSONObject("layers").getJSONArray("wyjazdy");
        Double result = -1.0;
        for (Object tripObject: travels) {
            JSONObject trip = (JSONObject) tripObject;
            Double value = valueOf(trip.getString("koszt_suma"));
            result = max(result,value);
        }
        return result;
    }

    public Boolean italyTravels(){
        if (details.getJSONObject("layers").optJSONArray("wyjazdy") == null)
            return false;
        JSONArray travels = details.getJSONObject("layers").getJSONArray("wyjazdy");
        for (Object tripObject : travels) {
            JSONObject trip = (JSONObject) tripObject;
            if (trip.getString("kraj").equals("Włochy"))
                return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return name;
    }
}
