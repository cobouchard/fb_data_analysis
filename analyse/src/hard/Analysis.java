package hard;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class Analysis {

    public static HashMap<LocalDate, Integer> readOnePersonData(String person)
    {
        HashMap<LocalDate, Integer> map = new HashMap<>();
        //parcours du fichier
        JSONParser jsonParser = new JSONParser();

        try (FileReader reader = new FileReader(person))
        {
            //Read JSON file
            JSONObject obj = (JSONObject)jsonParser.parse(reader);

            JSONArray msgList = (JSONArray) obj.get("messages");
            int nb_messages = 0;
            for(int i=0; i!=msgList.size(); i++)
            //for(int i=0; i!=10; i++)
            {
                JSONObject msg = (JSONObject) (msgList.get(i));
                long timestamp = (long) msg.get("timestamp_ms");
                LocalDate dat = UsefulFunc.timestampToDate(timestamp);
                if(! map.containsKey(dat))
                    map.put(dat, 1);
                else
                    map.replace(dat, map.get(dat)+1 );
                nb_messages++;
            }

            System.out.println("Nombre de message : " + nb_messages);


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (org.json.simple.parser.ParseException e) {
            e.printStackTrace();
        }

        return map;
    }

    public static void main(String[] args) {
        HashMap<LocalDate, Integer> map = readOnePersonData("../facebook-corentinbouchard_06102019/messages/inbox/YannRousseau_Yh-E59p1mQ/message_1.json");

        for(HashMap.Entry<LocalDate, Integer> entry : map.entrySet())
        {
            System.out.println("Date : " + entry.getKey() + " nb messages = " + entry.getValue());
        }

    }
}
