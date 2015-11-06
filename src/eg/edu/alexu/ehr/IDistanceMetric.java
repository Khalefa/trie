package eg.edu.alexu.ehr;

public interface IDistanceMetric {
double GetLimit();
double GetDistance();
IDistanceMetric add(int i);
}
