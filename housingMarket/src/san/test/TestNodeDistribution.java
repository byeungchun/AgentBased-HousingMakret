package san.test;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math.MathException;
import org.apache.commons.math.distribution.NormalDistribution;
import org.apache.commons.math.distribution.NormalDistributionImpl;
import org.apache.log4j.Logger;

import san.entity.HouseholdNode;

public class TestNodeDistribution {

	private static Logger log = Logger.getLogger(TestNodeDistribution.class);
	
	public static void checkDistribution(List<HouseholdNode> agentList) {
		
		for(HouseholdNode hhn : agentList){
			
			List<HouseholdNode> neighbors = hhn.getOutNodes();
			log.debug("NODE : " + hhn.getIdx() + "ÀÇ ÀÌ¿ô ¼ö " + neighbors.size() );
			
			
		}
	}
	
	public static void main(String[] args){
		NormalDistribution norDst = new NormalDistributionImpl(20.0D, 5.0D);
		
		for(int i =0 ; i < 1000 ; i++){
			try {
				System.out.println((int)norDst.inverseCumulativeProbability(Math.random()));
			} catch (MathException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	
}
