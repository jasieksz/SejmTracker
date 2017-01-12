import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class InputParser {


    private static List<String> polecenia = Arrays.asList("sum","repairs","all","avg","number","time","cost","italy");

    public static String run(String[] args) throws IOException, JSONException, IllegalArgumentException {

        if(args.length != 2)
            throw new IllegalArgumentException("Zła ilość argumentów"+"\n"+"INSTRUKCJA OBSLUGI PROGRAMU ...");

        if(polecenia.stream().filter(w -> w.contains(args[0])).collect(Collectors.toCollection(ArrayList::new)).isEmpty())
            throw new IllegalArgumentException("Nie ma takiego polecenia" +"\n"+polecenia.toString());

        String name;
        //1 POSEL - suma wydatkow
        if(args[0].equals("sum")) {
            name = args[1];
            if (Parliament.mpMap.containsKey(name)) {
                Integer id = Parliament.mpMap.get(name);
                MP mp = new MP(id, name, JsonParser.readJsonFromUrl(makeUrl(name, "expenses")));
                return (mp.toString() + " wszystkie wydatki " + mp.sumExpenses().toString());
            }
            else
                throw new IllegalArgumentException("Taki poseł nie istnieje");
        }
        //1 POSEL - drobne wydatki na naprawy
        else if(args[0].equals("repairs")) {
            name = args[1];
            if (Parliament.mpMap.containsKey(name)) {
                Integer id = Parliament.mpMap.get(name);
                MP mp = new MP(id, name, JsonParser.readJsonFromUrl(makeUrl(name, "expenses")));
                return (mp.toString() + " male wydatki " + mp.smallExpenses().toString());
            }
            else
                throw new IllegalArgumentException("Taki poseł nie istnieje");
        }

        else if (args[0].equals("all")){
            name = args[1];
            if( Parliament.mpMap.containsKey(name)) {
                Integer id = Parliament.mpMap.get(name);
                MP mp = new MP(id, name, JsonParser.readJsonFromUrl(makeUrl(name, "everything")));
                return (mp.toString() + "\n"
                        + " wszystkie wydatki " + mp.sumExpenses().toString() + "\n"
                        + " małe wydatki " + mp.smallExpenses().toString() + "\n"
                        + " liczba podroz " + mp.numberTravels().toString() + "\n"
                        + " najdluzsza pdoroz " + mp.timeTravels().toString() + "\n"
                        + " najdrozszy wyjazd " + mp.costTravels().toString() + "\n"
                        + " wloskie podrize " + mp.italyTravels().toString() + "\n");
            }
            else
                throw new IllegalArgumentException("Taki poseł nie istnieje");
        }
        //CALY PARLAMENT
        else {
            name = args[1]; // name to numer kadnecji ---> w makeUrl tworzy się odpowiedni adres
            if (!name.equals("7") && !name.equals("8"))
                throw new IllegalArgumentException("Nie ma takiej kadencji");

            Parliament.makeMPList(Parliament.prepareParliamentLinks(JsonParser.readJsonFromUrl(makeUrl(name, "parliament"))));

            if (args[0].equals("avg"))
                return ("Srednie wydatki " + Parliament.averageExpenses().toString());
            else if (args[0].equals("number"))
                return ("Najwiecej podrozy " + Parliament.mostTravels().toString());
            else if (args[0].equals("time"))
                return ("Najdluza podroz " + Parliament.longestTravels().toString());
            else if (args[0].equals("cost"))
                return ("Najdrozsza podroz " + Parliament.expensiveTravels().toString());
            else if (args[0].equals("italy"))
                return ("Wloskie podroze " + Parliament.italyTravels().toString());
        }
        return null;
    }


    public static String makeUrl(String name, String option){
        String url = "https://api-v3.mojepanstwo.pl/dane/poslowie";
        Integer id = Parliament.mpMap.get(name);
        if (option.equals("expenses"))
            url = String.join("",url,"/",id.toString(),".json","?layers[]=wydatki");
        if (option.equals("travels"))
            url = String.join("",url,"/"+id.toString(),".json","?layers[]=wyjazdy");
        if (option.equals("everything"))
            url = String.join("",url,"/"+id.toString(),".json","?layers[]=wydatki&layers[]=wyjazdy");
        if (option.equals("parliament"))
            url = String.join("",url,".json?conditions[poslowie.kadencja]="+name+"&limit=119");
        return url;
    }
}
/*
INPUT EXAMPLES
sumexpenses "Jan Kowalski"  //posel jan kowalski, jego srednie wyadtki
avgexpenses 7 //srednie wydatki poslow 7 kadencji
longesttravels 8 //posel 8 kadencji, najdluzsza podroz

"sum","repairs","all","avg","number","time","cost","italy"
 */