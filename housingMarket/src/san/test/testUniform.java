package san.test;

import uchicago.src.sim.util.Random;

public class testUniform {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		Random.createUniform();
		
		for(int i =0; i < 10; i ++){
			System.out.println(Random.uniform.nextDoubleFromTo(0.6D, 0.9D));
		}

	}

}
