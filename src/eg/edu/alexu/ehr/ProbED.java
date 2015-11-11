package eg.edu.alexu.ehr;

public class ProbED implements IDistanceMetric {
	public int tau;
	public float prob;
	public int length;
	float val;

	public ProbED add(ProbED d) {
		ProbED p = new ProbED();
		p.tau = d.tau + tau;
		p.val = d.val;
		return p;
	}

	private ProbED() {

	}

	public ProbED(int tau, float prob, int maxlen) {
		this.tau = tau;
		this.prob = prob;
		this.length = maxlen;
		CalcProbED(tau, prob, maxlen);
	}

	public ProbED(int tau) {
		this.tau = tau;
		val = 10000;
		prob = -1;
		length = -1;
	}
    void CalcProbED(){
    	val = (1 - prob + (float) tau / length * prob);
    }
	void CalcProbED(int tau, float prob, int l) {		
		CalcProbED();
	}

	@Override
	public String toString() {
		return "T:" + tau + " P:" + prob + " l:" + length + "V:" + val;
	}

	@Override
	public double GetDistance() {
		return tau;
	}

	@Override
	public double GetLimit() {
		return val;
	}

//	@Override
//	public IDistanceMetric add(int i) {
//		ProbED d = new ProbED();
//		d.tau = tau + i;
//		d.length=length;
//		d.prob = prob;
//		d.CalcProbED();
//		return d;
//	}

	@Override
	public int compareTo(IDistanceMetric o) {
		if(o instanceof ProbED)
			return Float.compare(this.val,(int) o.GetLimit());
			else 
				return 0;
	}
}
