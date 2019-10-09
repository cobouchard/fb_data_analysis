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
    static ArrayList<LocalDate> allDates = new ArrayList<>();

    public static Person readOnePersonData(String chemin, String nom)
    {
        Person p = new Person();
        HashMap<LocalDate, Integer> map = new HashMap<>();
        map = initMap(map);
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

    public static void initAllDates(int an, int mois, int jours)
    {
        allDates.add(LocalDate.of(an, mois, jours));
        if(! (fin.getYear() == an && fin.getMonthValue() == mois && fin.getDayOfMonth() == jours) )
        {
            if(UsefulFunc.jourDansMois(an, mois)==jours)
            {
                jours=1;
                if(mois==12)
                    initAllDates(++an, 1, jours);

                else
                    initAllDates(an, ++mois, jours);
            }
            else
                initAllDates(an, mois, ++jours);
        }

    }

    public static HashMap<LocalDate, Integer> initMap(HashMap<LocalDate, Integer> map)
    {
        for(LocalDate ld : allDates)
            map.put(ld,0);
        return map;
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
            writer = new BufferedWriter(new FileWriter("data.txt"));
            for(Person p : personnes)
            {
                writer.write(p.nom + " nombre de messages : " + p.nb_messages + "\n");
            }

            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeInCSV(ArrayList<Person> personnes)
    {
        BufferedWriter writer;

        try {
            writer = new BufferedWriter(new FileWriter("data.csv"));

            //ligne 1
            writer.write("Nom,");
            for(int i=0; i!= allDates.size();i++)
            {
                LocalDate ld = allDates.get(i);

                if(ld.getDayOfMonth()!=1 && ld.getDayOfMonth()!=15)
                    continue;

                writer.write(ld.getDayOfMonth() + "/" + ld.getMonthValue() + "/" + ld.getYear());
                if(i!= allDates.size()-1)
                    writer.write(',');
            }
            writer.write('\n'); //fin de ligne 1


            for(Person p : personnes)
            {
                if(p.nb_messages<500)
                    continue;

                writer.write(p.nom+",");

                int nb_messages=0;
                for(int i=0; i!= allDates.size();i++)
                {
                    LocalDate ld = allDates.get(i);
                    nb_messages += p.map.get(ld);

                    if(ld.getDayOfMonth()==1 || ld.getDayOfMonth()==15)
                    {
                        writer.write(String.valueOf(nb_messages));
                        if(i!= allDates.size()-1)
                            writer.write(',');
                    }
                }

                writer.write('\n');
            }

            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        initAllDates(debut.getYear(), debut.getMonthValue(), debut.getDayOfMonth());
        ArrayList<Person> persons = readAllPersons();
        writeInCSV(persons);
        //writeInText(persons);
    }
}

//https://www.baeldung.com/java-hashmap-sort
//https://app.flourish.studio/@flourish/horserace/7#api-template-data-header