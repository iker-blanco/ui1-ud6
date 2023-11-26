package utils;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class JsonUtil {

    public static JSONArray readJsonArrayFromFile(String filePath) {
        JSONParser parser = new JSONParser();
        try {
            String content = new String(Files.readAllBytes(Paths.get(filePath)));
            Object obj = parser.parse(content);
            return (JSONArray) obj;
        } catch (IOException | ParseException e) {
            e.printStackTrace();
            return new JSONArray();
        }
    }

    public static boolean writeJsonArrayToFile(JSONArray array, String filePath) {
        try (FileWriter file = new FileWriter(filePath)) {
            file.write(array.toJSONString());
            file.flush();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
