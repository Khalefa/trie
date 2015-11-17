package eg.edu.alexu.ehr;

public interface IDistance extends Comparable<IDistance> {
	int getDistance();

	public int compareTo(IDistance o);
}
