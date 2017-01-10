import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.UnknownHostException;

public class JsonParser {

    public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException,UnknownHostException {
        try(InputStream is = new URL(url).openStream()) {
            return new JSONObject(IOUtils.toString(is, "UTF-8"));
        }
    }
}
