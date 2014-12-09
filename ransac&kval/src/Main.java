import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Marta on 2014-12-06.
 */
public class Main {
    static String Path = "C:\\Users\\Marta\\Documents\\CSIT\\II\\PIIS\\task\\in1-polluted.txt";

    public static void main(String[] args) {
        try {

            ArrayList<double[]> data = FileHelper.readFromFile(2, Path);
            Collections.shuffle(data);
            Collections.shuffle(data);
            Kvalidation k = new Kvalidation(data);
            //k.run();
            Ransac ransac = new Ransac(data);

            ransac.run();

//            System.out.println(ransac.getBestinnum());
//            System.out.println(ransac.getBestTheta()[0] + " " + ransac.getBestTheta()[1]);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
