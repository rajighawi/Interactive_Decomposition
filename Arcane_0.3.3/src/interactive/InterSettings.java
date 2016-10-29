package interactive;

public class InterSettings {

	boolean partialDP;
	//float[] weights;
	int[] ws = new int[4];
	
	public InterSettings(boolean partialDP, int[] ws) {
		this.partialDP = partialDP;
		this.ws = ws;
	}

	public boolean isPartialDP() {
		return partialDP;
	}

	public void setPartialDP(boolean partialDP) {
		this.partialDP = partialDP;
	}

	public float[] getWeights() {
		float sum = 0;
		for (int i = 0; i < ws.length; i++) {
			sum += ws[i];
		}
		float[] weights = new float[ws.length];
		for (int i = 0; i < ws.length; i++) {
			weights[i] = ws[i]/sum;
		}
		return weights;
	}
	
	public int[] getWs() {
		return ws;
	}
	
	
}
