package san.logic;

import java.util.List;

import org.apache.commons.math.MathException;
import org.apache.commons.math.distribution.NormalDistribution;
import org.apache.commons.math.distribution.NormalDistributionImpl;
import org.apache.log4j.Logger;

import san.entity.HouseholdNode;
import san.main.HsingMktModel;

public class StdDstLgic {

	private static Logger log = Logger.getLogger(StdDstLgic.class);
	
	public static void stdDstInit(HsingMktModel hsingMktModel) {
		// TODO ǥ�� ���� ������ ���� �� ��� ������ ������. �̻�ǥ�����Ժ�����
		int numLink = 0;
		int maxLink = 100;
		double nodeMean = hsingMktModel.getAvgLinks();
		double nodeStdDev = hsingMktModel.getStdDev();
		List<HouseholdNode> lstAgentNode = hsingMktModel.getAgentList();
		
		NormalDistribution norDst = new NormalDistributionImpl(nodeMean, nodeStdDev);
		
		try {
			for(HouseholdNode iNode : lstAgentNode){
				numLink = (int)norDst.inverseCumulativeProbability(Math.random());
				if(numLink > maxLink) numLink = maxLink;
				iNode.setLink(lstAgentNode, numLink);
			}
		} catch (MathException e) {
			log.error("�ʱ� ��� ��ũ ���Ժ��� ���� �� ���� �߻�");
			System.exit(-1);
		}
		
		
	}

}
