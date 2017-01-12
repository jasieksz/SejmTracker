import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class ParliamentTest {
    public static Map<String,Integer> parliamentMap = new HashMap<>();
    static {
        try {
            String url = "https://api-v3.mojepanstwo.pl/dane/poslowie.json?conditions[poslowie.kadencja]=8";
            Parliament.makeParliament(Parliament.prepareParliamentLinks(JsonParser.readJsonFromUrl(url)));
            parliamentMap = Parliament.getMpMap();
            Parliament.makeMPList(Parliament.prepareParliamentLinks(JsonParser.readJsonFromUrl(url)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void averageExpensesTest() throws Exception {
        assertEquals(144882.96,Parliament.averageExpenses(),0.01);
    }

    @Test
    public void italyTravelsTest() throws Exception {
        assertEquals(25,Parliament.italyTravels().size());
    }

    @Test
    public void mostTravelsTest() throws Exception {
        assertEquals("Jan Dziedziczak", Parliament.mostTravels().toString());
    }

    @Test
    public void longestTravelsTest() throws Exception {
        assertEquals("Killion Munyama", Parliament.longestTravels().toString());
    }

    @Test
    public void expensiveTravelsTest() throws Exception {
        assertEquals("Witold Waszczykowski",Parliament.expensiveTravels().toString());
    }

}