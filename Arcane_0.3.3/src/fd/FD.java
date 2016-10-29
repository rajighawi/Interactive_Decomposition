package fd;

public class FD extends Dependency{
	public static final String FD_ARROW = "" + '\u2192';
	
	AttributeSet lhs;
	AttributeSet rhs;
	
	public FD(AttributeSet lhs, AttributeSet rhs) {
		this.lhs = lhs;
		this.rhs = rhs;
	}
	
	public FD(AttributeSet lhs, Attribute a) {
		this.lhs = lhs;
		this.rhs = new AttributeSet();
		this.rhs.add(a);
	}
	
	public FD(String lhs, String rhs) {
		this.lhs = new AttributeSet();
		this.rhs = new AttributeSet();
		this.lhs.addAllC(lhs);
		this.rhs.addAllC(rhs);
	}

	public AttributeSet getLHS() {
		return lhs;
	}

	public AttributeSet getRHS() {
		return rhs;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FD other = (FD) obj;
		if (lhs == null) {
			if (other.lhs != null)
				return false;
		} else if (!lhs.equals(other.lhs))
			return false;
		if (rhs == null) {
			if (other.rhs != null)
				return false;
		} else if (!rhs.equals(other.rhs))
			return false;
		return true;
	}
	
	public boolean isTrivial(){
		return lhs.containsAll(rhs);
	}

	@Override
	public String toString() {
		return lhs + " "+FD.FD_ARROW+" " + rhs ;
	}
	
	
	
}
