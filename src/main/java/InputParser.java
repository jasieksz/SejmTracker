import org.json.JSONException;

import java.io.IOException;

public class InputParser {

    public static void run(String[] args) throws IOException, JSONException, IndexOutOfBoundsException {

        String name;
        //1 POSEL - suma wydatkow
        if(args[0].equals("sumexpenses")) {
            name = args[1];
            if (SejmTracker.parliament.containsKey(name)) {
                Integer id = SejmTracker.parliament.get(name);
                System.out.println("Debug : " + makeUrl(name, "expenses"));
                MP mp = new MP(id, name, JsonParser.readJsonFromUrl(makeUrl(name, "expenses")));
                System.out.println(mp.toString() + " wszystkie wydatki " + mp.sumExpenses());
            }
            else {
                //RZUC EXCEPTION - NIE MA TAKIEGO POSLA
            }
        }
        //1 POSEL - drobne wydatki na naprawy
        else if(args[0].equals("smallexpenses")) {
            name = args[1];
            if (SejmTracker.parliament.containsKey(name)) {
                Integer id = SejmTracker.parliament.get(name);
                MP mp = new MP(id, name, JsonParser.readJsonFromUrl(makeUrl(name, "expenses")));
                System.out.println(mp.toString() + " male wydatki " + mp.smallExpenses());
                /*
                MP mp = new MP(id, name, JsonParser.readJsonFromUrl(makeUrl(name, "everything")));
                System.out.println(mp.toString()+" podroze IT "+mp.italyTravels().toString());
                */

            }
            else {
                //EXCE
            }
        }
        //CALY PARLAMENT
        else {
            name = args[1]; // name to numer kadnecji ---> w makeUrl tworzy się odpowiedni adres
            //Parliament.makeMPList(JsonParser.readJsonFromUrl(makeUrl(name, "parliament")));
            Parliament.makeMPList2(Parliament.prepareParliamentLinks(JsonParser.readJsonFromUrl(makeUrl(name, "parliament"))));

            if (args[0].equals("avgexpenses"))
                System.out.println("Srednie wydatki " + Parliament.averageExpenses());

            if (args[0].equals("mosttravels"))
                System.out.println("Najwiecej podrozy " + Parliament.mostTravels().toString());

            if (args[0].equals("longesttravels"))
                System.out.println("Najdluza podroz " + Parliament.longestTravels().toString());

            if (args[0].equals("expensivetravels"))
                System.out.println("Najdrozsza podroz " + Parliament.expensiveTravels().toString());

            if (args[0].equals("italytravels"))
                System.out.println("Wloskie podroze " + Parliament.italyTravels().toString());
        }

    }


    public static String makeUrl(String name, String option){
        String url = "https://api-v3.mojepanstwo.pl/dane/poslowie";
        Integer id = SejmTracker.parliament.get(name);
        if (option.equals("expenses"))
            url = String.join("",url,"/",id.toString(),".json","?layers[]=wydatki");
        if (option.equals("travels"))
            url = String.join("",url,"/"+id.toString(),".json","?layers[]=wyjazdy");
        if (option.equals("everything"))
            url = String.join("",url,"/"+id.toString(),".json","?layers[]=wydatki&layers[]=wyjazdy");
        if (option.equals("parliament"))
            url = String.join("",url,".json?conditions[poslowie.kadencja]="+name);

        return url;
    }
}
/*
INPUT EXAMPLES
sumexpenses "Jan Kowalski"  //posel jan kowalski, jego srednie wyadtki
avgexpenses 7 //srednie wydatki poslow 7 kadencji
longesttravels 8 //posel 8 kadencji, najdluzsza podroz


 */