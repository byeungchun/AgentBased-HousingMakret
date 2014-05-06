package san.logic;

import java.util.LinkedList;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.math.stat.regression.SimpleRegression;
import org.apache.log4j.Logger;

import san.entity.GovernmentAgent;
import san.main.HsingMktModel;

public class GovernmentLgic {

	private static Logger log = Logger.getLogger(GovernmentLgic.class);
	
	public static double decidePolcy(HsingMktModel hsingMktModel) {
		
		GovernmentAgent aGov = hsingMktModel.getGovernmentAgent();
		LinkedList<Integer> historyHousingPrices = hsingMktModel.getHistoryHousingPrices();
		SimpleRegression sRegression = new SimpleRegression();
		int totPrice = 0;
		for(int i = 0; i < historyHousingPrices.size() ; i++){
			sRegression.addData(historyHousingPrices.get(i),i);
			totPrice += historyHousingPrices.get(i);
//			log.debug(historyHousingPrices.get(i));
		}
		
		
		//상관계수를 구하기 위해서 100개의 이전 가격의 평균을 구하여 삽입
		(hsingMktModel.getChrome()).getLstHousePrice().add(totPrice/historyHousingPrices.size());
		
		double priceSlope = sRegression.getSlope();
		log.debug("주택 가격 기울기 "+ priceSlope + ArrayUtils.toString(historyHousingPrices.toArray()));
		
		//기울기가 1보다 클때, -1~1 사이일 때 그리고 -1 보다 작을때로 정부의 정책이 변함
		if(priceSlope > hsingMktModel.getGovernmentLgicStrongPolicy()){
			aGov.setInterestRate(5);
			aGov.setPolicyLvl(3);
			aGov.setTaxLvl(3);
		}else if(priceSlope < hsingMktModel.getGovernmentLgicWeakenPolicy()){
			aGov.setInterestRate(1);
			aGov.setPolicyLvl(1);
			aGov.setTaxLvl(1);
		}else{
			aGov.setInterestRate(3);
			aGov.setPolicyLvl(2);
			aGov.setTaxLvl(2);
		}
		
		return priceSlope;
	}

}
