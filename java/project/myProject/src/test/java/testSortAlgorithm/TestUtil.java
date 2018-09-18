package testSortAlgorithm;

public class TestUtil {

	public static void main(String[] args) {
//		String aString = "ab";
//		String bString = "ab1";
//		System.out.println(Judge.less(aString, bString));
//		
//		double aa = 1.2;
//		double bb = 1.21;
//		System.out.println(Judge.less(aa, bb));
		
		String[] aString = {"2","1","a","b"};
		RefUtil.isSorted(aString);
		RefUtil.show(aString);
		System.out.println('a'<'b');
	}

}
