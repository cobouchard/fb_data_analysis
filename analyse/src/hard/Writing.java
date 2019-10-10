package hard;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

public abstract class Writing {
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
            for(int i=0; i!= Analysis.allDates.size();i++)
            {
                LocalDate ld = Analysis.allDates.get(i);

                if(ld.getDayOfMonth()!=1 && ld.getDayOfMonth()!=15)
                    continue;

                writer.write(ld.getDayOfMonth() + "/" + ld.getMonthValue() + "/" + ld.getYear());
                if(i!= Analysis.allDates.size()-1)
                    writer.write(',');
            }
            writer.write('\n'); //fin de ligne 1


            for(Person p : personnes)
            {
                if(p.nb_messages<500)
                    continue;

                writer.write(p.nom+",");

                int nb_messages=0;
                for(int i=0; i!= Analysis.allDates.size();i++)
                {
                    LocalDate ld = Analysis.allDates.get(i);
                    nb_messages += p.map.get(ld);

                    if(ld.getDayOfMonth()==1 || ld.getDayOfMonth()==15)
                    {
                        writer.write(String.valueOf(nb_messages));
                        if(i!= Analysis.allDates.size()-1)
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
}
