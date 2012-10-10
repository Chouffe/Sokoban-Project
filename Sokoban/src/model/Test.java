package model;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		StringBuffer sb = new StringBuffer();
		System.out.println(sb.toString());
		addStuff(sb);
		System.out.println(sb.toString());

		System.out.println("Clone test: ");
		cloneThings(sb);
		System.out.println(sb.toString());
	}

	public static void addStuff(StringBuffer s) {
		s.append("This test works");
	}

	public static void cloneThings(StringBuffer s) {
		StringBuffer a = new StringBuffer();
		addStuff(a);
		System.out.println("s length: " + s.length());
		s.delete(0,s.length());
		s.append(a);
	}

}
