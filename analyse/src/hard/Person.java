package hard;

import java.time.LocalDate;
import java.util.HashMap;

public class Person {
    public HashMap<LocalDate, Integer> map;
    public String nom = "";
    public int nb_messages = 0;

    Person(HashMap<LocalDate, Integer> map_, String nom_)
    {
        map=map_;
        nom=nom_;
    }
    Person(){}
}
