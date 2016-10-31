package interactive;

import main.Global;
import fd.Decomposition;

public class Recommendation implements Comparable<Recommendation>{

	public static final byte _Better = 1;
	public static final byte _Equiv  = 2;
	public static final byte _Worse  = 3;
	
	String action;
	Decomposition deco; // recommended Decomposition
	float[] scores;	
	float score;
	
	byte comp;


	public Recommendation(String action, Decomposition deco) {
		super();
		this.action = action;
		this.deco = deco;
		
		InterSettings settings = Global.getInstance().getSettings();
		
		this.scores = Interactive.scores(deco, settings.isPartialDP());
		this.score = 0;
		for (int j = 0; j < scores.length; j++) {
			score += scores[j] * settings.getWeights()[j];
		}
		this.comp = _Equiv;
	}
	
	public float getScore() {
		return score;
	}

	@Override
	public String toString() {
		return action + " [" + score + "]";
	}

	@Override
	public int compareTo(Recommendation o) {
		return score<o.score?1:score==o.score?0:-1;
	}
	
	public Decomposition getDeco() {
		return deco;
	}

	public byte getComp() {
		return comp;
	}

	public void setComp(byte comp) {
		this.comp = comp;
	}
		
}
