import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MPTest{

    private static String url = "https://api-v3.mojepanstwo.pl/dane/poslowie/197.json?layers[]=wydatki&layers[]=wyjazdy";
    private static MP mp;
    static {
        try {
            mp = new MP(197, "Maks Karczowski", JsonParser.readJsonFromUrl(url));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void sumExpensesTest() throws Exception {
        assertEquals(291868.23, mp.sumExpenses(), 0.01);
    }

    @Test
    public void smallExpensesTest() throws Exception {
        assertEquals(0.0, mp.smallExpenses(), 0.01);
    }

    @Test
    public void numberTravelsTest() throws Exception {
        assertEquals(new Integer(5), mp.numberTravels());
    }

    @Test
    public void timeTravelsTest() throws Exception {
        assertEquals(new Integer(9), mp.timeTravels());
    }

    @Test
    public void costTravelsTest() throws Exception {
        assertEquals(15263.97, mp.costTravels(), 0.01);
    }

    @Test
    public void italyTravelsTest() throws Exception {
        assertTrue(!mp.italyTravels());
    }

}