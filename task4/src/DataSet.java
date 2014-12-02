import java.util.ArrayList;

/**
 * Created by Marta on 2014-12-02.
 */
public class DataSet {
    private ArrayList<double[]> set;
    private double intervalBegin;
    private double intervalEnd;

    public DataSet(){
        set = new ArrayList<double[]>();
    }

    public ArrayList<double[]> getSet() {
        return set;
    }

    public void setSet(ArrayList<double[]> set) {
        this.set = set;
    }

    public double getIntervalBegin() {
        return intervalBegin;
    }

    public void setIntervalBegin(double intervalBegin) {
        this.intervalBegin = intervalBegin;
    }

    public double getIntervalEnd() {
        return intervalEnd;
    }

    public void setIntervalEnd(double intervalEnd) {
        this.intervalEnd = intervalEnd;
    }


}
