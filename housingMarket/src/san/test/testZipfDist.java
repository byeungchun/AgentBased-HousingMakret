package san.test;

import org.apache.commons.math.MathException;
import org.apache.commons.math.distribution.ZipfDistributionImpl;

public class testZipfDist {
	
	public static void main(String[] args){
		
		ZipfDistributionImpl zipF = new ZipfDistributionImpl(1000, 0.15D);
		
//		for(int i = 1 ; i < 1000 ; i++){
//			System.out.println(zipF.probability(i));
//		}
//		
		try {
			System.out.println(zipF.inverseCumulativeProbability(0.99D));
		} catch (MathException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
