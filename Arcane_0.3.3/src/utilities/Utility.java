package utilities;
 


public class Utility {
 

	public static String localName(String uri){
		String ur = null;
		if(uri.contains("#")){
			int lio = uri.lastIndexOf("#");
			ur = uri.substring(lio+1);
		} else if(uri.contains("/")){
			int lio = uri.lastIndexOf("/");
			ur = uri.substring(lio+1);
		}		
		return ur;
	}
	
	public static boolean isURI(String uri){
		return uri.startsWith("<");
	}
		
	public static String namespace(String uri){
		String ur = null;
		if(uri.contains("#")){
			int lio = uri.lastIndexOf("#");
			ur = uri.substring(0,lio);
		} else if(uri.contains("/")){
			int lio = uri.lastIndexOf("/");
			ur = uri.substring(0,lio);
		}		
		return ur;
	}
	
	public static String extract(String s){
		if(s.startsWith("<")&& s.endsWith(">")){
			return s.substring(1, s.length()-1);
		} else if(s.startsWith("\"")&& s.endsWith("\"")){
			return s.substring(1, s.length()-1);
		}
		return s;
	}
	
	public static String makeINT(int n, int size){
		int si = sizeINT(n);
		String s = "";
		for (int i = 0; i < size-si; i++) {
			s += "0";
		}
		return s+n;
	}
	
	public static int sizeINT(int n){
		if(n<10) return 1;
		else if(n<100) return 2;
		else if(n<1000) return 3;
		return (int) Math.ceil(Math.log10(n));
	}

	public static String formatINT(int n){
		String s = ""+n;
		s = reverse(s);
		String st = "";
		for (int i = 0; i < s.length(); i++) {
			if(i!=0 && i%3==0) st += ",";
			st += s.charAt(i);			
		}
		return reverse(st);
	}
	
	public static String reverse(String ss){
		String s = "";
		for (int i = ss.length()-1; i >= 0; i--) {
			s += ss.charAt(i);
		}
		return s;		
	}
}
