package hard;

import java.io.*;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class Analysis {
    static final LocalDate debut = LocalDate.of(2012,9,1);
    static final LocalDate fin = LocalDate.of(2019,10,6);

    public static Person readOnePersonData(String chemin, String nom)
    {
        Person p = new Person();
        HashMap<LocalDate, Integer> map = new HashMap<>();
        map = initMap(map, debut.getYear(), debut.getMonthValue(), debut.getDayOfMonth());
        //parcours du fichier
        JSONParser jsonParser = new JSONParser();

        try (FileReader reader = new FileReader(chemin))
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
                if(map.containsKey(dat))
                    map.replace(dat, map.get(dat)+1 );
                nb_messages++;
            }


            p.nb_messages = nb_messages;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (org.json.simple.parser.ParseException e) {
            e.printStackTrace();
        }
        p.map = map;

        //clean du nom
        String new_nom = "";
        for(int i=0; i!= nom.length(); i++)
        {
            if(nom.charAt(i)=='_')
                break;
            new_nom = new_nom + nom.charAt(i);
        }
        p.nom=new_nom;

        return p;
    }

    public static HashMap<LocalDate, Integer> initMap(HashMap<LocalDate, Integer> map, int an, int mois, int jours)
    {
        map.put(LocalDate.of(an,mois,jours),0);
        if(fin.getYear() == an && fin.getMonthValue() == mois && fin.getDayOfMonth() == jours)
            return map;

        else
        {
            if(UsefulFunc.jourDansMois(an, mois)==jours)
            {
                jours=1;
                if(mois==12)
                    return initMap(map, ++an, 1, jours);

                else
                    return initMap(map, an, ++mois, jours);
            }
            else
                return initMap(map, an, mois, ++jours);
        }
    }

    public static ArrayList<Person> readAllPersons()
    {
        ArrayList<Person> persons = new ArrayList<>();

        File dir = new File("../facebook-corentinbouchard_06102019/messages/inbox/");
        String liste[] = dir.list();

        for(String s : liste)
        {
            persons.add(readOnePersonData("../facebook-corentinbouchard_06102019/messages/inbox/"+s+"/message_1.json", s));
        }


        return persons;
    }

    public static void writeInText(ArrayList<Person> personnes)
    {
        BufferedWriter writer;

        try {
            writer = new BufferedWriter(new FileWriter("data.csv"));
            for(Person p : personnes)
            {
                writer.write(p.nom + " nombre de messages : " + p.nb_messages + "\n");
            }

            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        ArrayList<Person> persons = readAllPersons();
        writeInText(persons);
    }
}

//https://www.baeldung.com/java-hashmap-sort
//https://app.flourish.studio/@flourish/horserace/7#api-template-data-header