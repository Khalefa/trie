package eg.edu.alexu.ehr;

public interface IDistanceMetric extends Comparable<IDistanceMetric> {
double GetLimit();
double GetDistance();

}
