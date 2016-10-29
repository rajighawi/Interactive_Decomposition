package normalization;

import java.util.Comparator;

import fd.Relation;

public class MyRelComparator implements Comparator<Relation> {
	@Override
	public int compare(Relation o1, Relation o2) {
		int n1 = o1.getAttributes().size();
		int n2 = o2.getAttributes().size();
		return (n1<n2?-1:(n1==n2?0:1));
	}

}
