import org.json.JSONException;

import java.io.IOException;
import java.net.UnknownHostException;

public class SejmTracker {
    //public static Map<String,Integer> parliamentMap = new HashMap<>(); // NAME -> ID posłowie wszystkich kadnecji


    private static String prop1 = "java.util.concurrent.ForkJoinPool.common.parallelism";
    private static String threads = "6";


    public static void main(String[] args) {
        try {

            System.setProperty(prop1, threads);
            long startTime = System.currentTimeMillis();

            System.out.println(run(args));

            long stopTime = System.currentTimeMillis();
            long elapsedTime = stopTime - startTime;
            System.out.println(elapsedTime);
        }
        catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }
    private static String run(String[] args) throws IllegalArgumentException{
        try {
            return InputParser.run(args);
        } catch (UnknownHostException e){
            System.out.println("Problem z połączeniem z serwerem : " + e.getMessage());
        }
        catch (IOException | JSONException e){
            System.out.println("Wystąpił błąd : "+e.getMessage());
        }

        return null;
    }
}
