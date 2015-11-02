
public class ProbED {
public int tau;
double val;

public ProbED(int tau, float prob, int maxlen) {
	this.tau=tau;
	CalcProbED(tau, prob,maxlen);
}
public ProbED(int tau){
	this.tau=tau;
	val=-1;
}
void CalcProbED(int tau, double prob, int l){
	val=(1-prob+tau/l*prob);
	
}
}
