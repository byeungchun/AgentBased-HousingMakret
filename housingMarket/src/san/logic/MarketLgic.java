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
		double existHouseSupplyRto = hsingMktModel.getMarketLgicExistHouseSupplyRto(); //기존주택에서 매매시장에 나오는 비율
		int numBidNode = 0;
		int numHouseSupply = (int)(ga.getTotHouses() * existHouseSupplyRto + ga.getSupplyHouses());
		
		double houseSupplyRto = (numHouseSupply - ga.getPrvTotHouses() * existHouseSupplyRto) / ga.getPrvTotHouses();
		
		// 매매시장에 참여하는 총 노드 수와 공급 주택수를 비교하여 주택 가격을 결정
		for(HouseholdNode hhn : lstHouseholdNode){
			if(hhn.getIsIs2ndCandidate()) numBidNode++; 
		}
		
		if(numBidNode == 0) numBidNode = 1;
		//초기에는 전기 매매 참여자가 0이기 때문에 이때는 전기과 당기를 같게 하는 로직 추가
		if(hsingMktModel.getPrvNumBidNodes() == 0)
			hsingMktModel.setPrvNumBidNodes(numBidNode);
		
		int newHousePrice = hsingMktModel.getHousingPrice() + (int) ((numBidNode / hsingMktModel.getPrvNumBidNodes()) * houseSupplyRto * hsingMktModel.getHousingPrice());
		
		log.debug("기존 주택수, 공급 주택수, 신규 주택가격 "+ ga.getTotHouses() + "," + numHouseSupply + "," + newHousePrice);
		
		ga.setPrvTotHouses(ga.getTotHouses());
		ga.setPrvSupplyHouses(ga.getPrvSupplyHouses());
		ga.setTotHouses(ga.getTotHouses() + ga.getSupplyHouses());
		hsingMktModel.setPrvNumBidNodes(numBidNode);
		hsingMktModel.setHousingPrice(newHousePrice);
		
		//historyHousingPrices는 100개 만 가지고 있는다.
		if(historyHousingPrices.size() >= 100) historyHousingPrices.remove();
		historyHousingPrices.add(newHousePrice);
	}

}
