import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class JsonUtil {
    public JSONArray readJsonArrayFromFile(String filePath) {
        JSONParser parser = new JSONParser();

        try {
            String content = new String(Files.readAllBytes(Paths.get(filePath)));
            Object obj = parser.parse(content);
            return (JSONArray) obj;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return new JSONArray(); // Return empty array in case of error
    }

    public void writeJsonArrayToFile(JSONArray jsonArray, String filePath) {
        try {
            Files.write(Paths.get(filePath), jsonArray.toString().getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
