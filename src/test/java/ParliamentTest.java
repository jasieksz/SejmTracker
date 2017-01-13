import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class ParliamentTest {
   /* static {
        try {
            String url = "https://api-v3.mojepanstwo.pl/dane/poslowie.json?conditions[poslowie.kadencja]=8";
            Parliament.makeParliament(Parliament.prepareParliamentLinks(JsonParser.readJsonFromUrl(url)));
            parliamentMap = Parliament.getMpMap();
            Parliament.makeMPList(Parliament.prepareParliamentLinks(JsonParser.readJsonFromUrl(url)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/

    private static Parliament parliament;

    static {
        try {
            parliament = new Parliament(8);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void averageExpensesTest() throws Exception {
        assertEquals(144882.96,parliament.averageExpenses(),0.01);
    }

    @Test
    public void italyTravelsTest() throws Exception {
        assertEquals(25,parliament.italyTravels().size());
    }

    @Test
    public void mostTravelsTest() throws Exception {
        assertEquals("Jan Dziedziczak", parliament.mostTravels().toString());
    }

    @Test
    public void longestTravelsTest() throws Exception {
        assertEquals("Killion Munyama", parliament.longestTravels().toString());
    }

    @Test
    public void expensiveTravelsTest() throws Exception {
        assertEquals("Witold Waszczykowski",parliament.expensiveTravels().toString());
    }

}