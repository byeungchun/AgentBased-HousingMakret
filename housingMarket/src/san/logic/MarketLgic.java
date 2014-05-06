package san.logic;

import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import san.entity.GovernmentAgent;
import san.entity.HouseholdNode;
import san.main.HsingMktModel;

public class MarketLgic {

	private static Logger log = Logger.getLogger(MarketLgic.class);
	
	public static void decideHousePrice(HsingMktModel hsingMktModel) {
		
		GovernmentAgent ga = hsingMktModel.getGovernmentAgent();
		List<HouseholdNode> lstHouseholdNode = hsingMktModel.getAgentList();
		LinkedList<Integer> historyHousingPrices = hsingMktModel.getHistoryHousingPrices(); 
		double existHouseSupplyRto = hsingMktModel.getMarketLgicExistHouseSupplyRto(); //�������ÿ��� �ŸŽ��忡 ������ ����
		int numBidNode = 0;
		int numHouseSupply = (int)(ga.getTotHouses() * existHouseSupplyRto + ga.getSupplyHouses());
		
		double houseSupplyRto = (numHouseSupply - ga.getPrvTotHouses() * existHouseSupplyRto) / ga.getPrvTotHouses();
		
		// �ŸŽ��忡 �����ϴ� �� ��� ���� ���� ���ü��� ���Ͽ� ���� ������ ����
		for(HouseholdNode hhn : lstHouseholdNode){
			if(hhn.getIsIs2ndCandidate()) numBidNode++; 
		}
		
		if(numBidNode == 0) numBidNode = 1;
		//�ʱ⿡�� ���� �Ÿ� �����ڰ� 0�̱� ������ �̶��� ����� ��⸦ ���� �ϴ� ���� �߰�
		if(hsingMktModel.getPrvNumBidNodes() == 0)
			hsingMktModel.setPrvNumBidNodes(numBidNode);
		
		int newHousePrice = hsingMktModel.getHousingPrice() + (int) ((numBidNode / hsingMktModel.getPrvNumBidNodes()) * houseSupplyRto * hsingMktModel.getHousingPrice());
		
		log.debug("���� ���ü�, ���� ���ü�, �ű� ���ð��� "+ ga.getTotHouses() + "," + numHouseSupply + "," + newHousePrice);
		
		ga.setPrvTotHouses(ga.getTotHouses());
		ga.setPrvSupplyHouses(ga.getPrvSupplyHouses());
		ga.setTotHouses(ga.getTotHouses() + ga.getSupplyHouses());
		hsingMktModel.setPrvNumBidNodes(numBidNode);
		hsingMktModel.setHousingPrice(newHousePrice);
		
		//historyHousingPrices�� 100�� �� ������ �ִ´�.
		if(historyHousingPrices.size() >= 100) historyHousingPrices.remove();
		historyHousingPrices.add(newHousePrice);
	}

}
