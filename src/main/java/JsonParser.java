import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class JsonParser {

    public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException{

        try(InputStream is = new URL(url).openStream()) {
            return new JSONObject(IOUtils.toString(is, "UTF-8"));
        }
    }

    /*
    //towrzy hash mape klucz,wartosc pliku JSON
    public static HashMap<String,Integer> parseJson(JSONObject jsonObj){ // Czy to będzie <String,String>
        HashMap<String,Integer> result = new HashMap<>();

        return result;

    }
    */

}
