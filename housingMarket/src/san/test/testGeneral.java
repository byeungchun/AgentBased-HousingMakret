package san.test;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;

public class testGeneral {

	public static void main(String[] args){
		System.out.println(Calendar.getInstance().getTime().toString());
		
		Calendar cal = Calendar.getInstance();
	    String dateString, timeString;


		dateString = String.format("%04d-%02d-%02d", cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH));
	    timeString = String.format("%02d:%02d:%02d", cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), cal.get(Calendar.SECOND));

	    System.out.println(dateString + " " + timeString);

	    int tax = 5;
	    System.out.println(5/3.0D);
	    
	    int[] arrInt = {1,2};
	    
	    ArrayUtils.add(arrInt, 3);
	    ArrayUtils.remove(arrInt, 1);
	    for(int i =0; i < arrInt.length ; i++){
	    	System.out.println(arrInt[i]);
	    }
	    
	    LinkedList<String> l = new LinkedList<String>();
	    l.add("x");
	    l.add("y");
	    l.add("z");
	    l.remove();
	    
	    System.out.println(l.get(0));
	    System.out.println(l.get(1));
	    System.out.println(l.size());
	}
}
