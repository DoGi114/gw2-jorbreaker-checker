import netscape.javascript.JSObject;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

public class GW2DAO {
    private static final String GUILD_ID = "7B762D91-35FA-E411-A278-AC162DC0070D";
    private static final String API_TOKEN = "7BD3786E-CD25-8A41-BB5D-CDD754787756A404C00C-ECD5-4654-B8E9-75D294958B0F";

    //TODO:To enums and think of better names
    private static final String ACTION_TYPE = "type";
    private static final String OPERATION_TYPE = "operation";
    private static final String ITEM_ID = "item_id";
    private static final String ADMIN_NAME_TO_EXCLUDE = "MarkoGold.7126";
    private static final String USER = "user";
    private static final String COUNT = "count";
    private static final String ACTION_TYPE_EXPECTED = "stash";
    private static final String OPERATION_TYPE_EXPECTED = "deposit";
    private static final int ITEM_ID_EXPECTED = 43320;

    private URL getGuildAccessURL(String GUILD_ID, String API_TOKEN) throws MalformedURLException {
        return new URL("https://api.guildwars2.com/v2/guild/" + GUILD_ID + "/log?access_token=" + API_TOKEN);
    }

    public JSONArray getGuildLog(){
        URL url ;
        StringBuilder jsonString = new StringBuilder();

        try {
            url = getGuildAccessURL(GUILD_ID, API_TOKEN);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));

            String output;
            while ((output = br.readLine()) != null) {
                jsonString.append(output);
            }

            conn.disconnect();

            return new JSONArray(jsonString.toString());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Map<String, Integer> getJorbreakerCompetitionLeaderboard(JSONArray jsonArray){
        final Map<String, Integer> entryList = new HashMap<>();

        if(jsonArray != null){
            IntStream.range(0, jsonArray.length())
                    .mapToObj(i -> jsonArray.get(i))
                    .map(obj -> (JSONObject) obj)
                    .filter(record -> record.getString(ACTION_TYPE).equals(ACTION_TYPE_EXPECTED))
                    .filter(record -> record.getString(OPERATION_TYPE).equals(OPERATION_TYPE_EXPECTED))
                    .filter(record -> record.getInt(ITEM_ID) == ITEM_ID_EXPECTED)
                    .filter(record -> !record.getString(USER).equals(ADMIN_NAME_TO_EXCLUDE))
                    .forEach(record -> entryList.put(record.getString(USER), entryList.containsKey(record.getString(USER)) ? (entryList.get(record.getString(USER)) + record.getInt(COUNT)) : record.getInt(COUNT)));

        }

        return entryList;
    }
}
