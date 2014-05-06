package san.test;

import org.apache.commons.math.stat.regression.SimpleRegression;

public class testRegression {

	public static void main(String[] args){
		SimpleRegression sr = new SimpleRegression();
		
		sr.addData(1,18481156);
		sr.addData(2,18579917);
		sr.addData(3,18775495);
		sr.addData(4,18775495);
		sr.addData(5,18871328);
		sr.addData(6,18966173);
		sr.addData(7,19060026);
		sr.addData(8,19152882);
		sr.addData(9,19428448);
		sr.addData(10,19428448);
		
		System.out.println(sr.getSlope());
	}
}
