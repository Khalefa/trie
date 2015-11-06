package eg.edu.alexu.ehr;

public class ProbED implements IDistanceMetric  {
public int tau;
public float prob;
public int length;
float val;

public ProbED add(ProbED d){
ProbED p=new ProbED();
p.tau=d.tau+tau;
p.val=d.val;
return p;
}
private ProbED(){

}
public ProbED(int tau, float prob, int maxlen) {
	this.tau=tau;
	CalcProbED(tau, prob,maxlen);
}
public ProbED(int tau){
	this.tau=tau;
	val=10000;
	prob=-1;
	length=-1;
}
void CalcProbED(int tau, float prob, int l){
	val=(1-prob+(float)tau/l*prob);
	this.prob=prob;
	this.length=l;
	
}
@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "T:"+tau+" P:"+prob+" l:"+length+"V:"+val;
	}
@Override
public double GetDistance() {
	// TODO Auto-generated method stub
	return tau;
}
@Override
public double GetLimit() {
	// TODO Auto-generated method stub
	return prob;
}
@Override
public IDistanceMetric add(int i) {
	ProbED d=new ProbED();
	d.tau=tau+i;
	d.prob=prob;
	d.val=val+i;
	return d;
}
}
