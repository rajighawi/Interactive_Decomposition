package fd;

public class Symbol {
	String str;
	boolean subscriped;
	
	public Symbol(String str, boolean subscriped) {
		super();
		this.str = str;
		this.subscriped = subscriped;
	}
	
	public String getStr() {
		return str;
	}
	
	public boolean isSubscriped() {
		return subscriped;
	}

	@Override
	public String toString() {
		return str;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((str == null) ? 0 : str.hashCode());
		result = prime * result + (subscriped ? 1231 : 1237);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Symbol other = (Symbol) obj;
		if (str == null) {
			if (other.str != null)
				return false;
		} else if (!str.equals(other.str))
			return false;
		if (subscriped != other.subscriped)
			return false;
		return true;
	}
	
	
		
}

