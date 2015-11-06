package eg.edu.alexu.ehr;

/**
 * @author khalefa
 *
 */
public class ED implements IDistanceMetric {
int distance;

public ED(int dist){
	distance=dist;	
}
@Override
public double GetDistance() {
	// TODO Auto-generated method stub
	return distance;
}
@Override
public double GetLimit() {
	// TODO Auto-generated method stub
	return distance;
}
@Override
public IDistanceMetric add(int i) {
	// TODO Auto-generated method stub
	return new ED(distance+i);
}

}
