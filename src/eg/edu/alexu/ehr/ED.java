package eg.edu.alexu.ehr;

/**
 * @author khalefa
 *
 */
public class ED implements IDistance {
	int distance;

	public ED(int dist) {
		distance = dist;
	}

	@Override
	public int getDistance() {
		return distance;
	}

	// @Override
	// public double GetLimit() {
	// // TODO Auto-generated method stub
	// return distance;
	// }
	@Override
	public int compareTo(IDistance o) {
		if (o instanceof ED)
			return Integer.compare(this.distance, (int) o.getDistance());
		else
			return 0;
	}

	@Override
	public String toString() {

		return " " + distance;
	}
}
