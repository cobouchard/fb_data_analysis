package hard;

import java.sql.Timestamp;
import java.time.LocalDate;

public abstract class UsefulFunc {
    public static LocalDate timestampToDate(long timestamp)
    {
        Timestamp foobar = new Timestamp(timestamp);
        return foobar.toLocalDateTime().toLocalDate();
    }

    public static boolean anneeBissextile(int annee)
    {
        return (annee%4==0 && annee%4!=100) || annee%400==0;
    }

    //1.b
    public static int jourDansMois(int annee, int mois)
    {
        int jours = 0;
        switch(mois)
        {
            case 1:
                jours=31;break;
            case 2:
                jours = anneeBissextile(annee)? 29 : 28; break;
            case 3:
                jours=31;break;
            case 4:
                jours=30;break;
            case 5:
                jours=31;break;
            case 6:
                jours=30;break;
            case 7:
                jours=31;break;
            case 8:
                jours=31;break;
            case 9:
                jours=30;break;
            case 10:
                jours=31;break;
            case 11:
                jours=30;break;
            case 12:
                jours=31;break;
        }

        return jours;
    }


    public static void main(String[] args) {
        System.out.println(timestampToDate(1570282962054L));
    }
}
