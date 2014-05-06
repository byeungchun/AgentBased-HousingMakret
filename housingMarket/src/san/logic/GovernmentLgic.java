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
		
		
		//�������� ���ϱ� ���ؼ� 100���� ���� ������ ����� ���Ͽ� ����
		(hsingMktModel.getChrome()).getLstHousePrice().add(totPrice/historyHousingPrices.size());
		
		double priceSlope = sRegression.getSlope();
		log.debug("���� ���� ���� "+ priceSlope + ArrayUtils.toString(historyHousingPrices.toArray()));
		
		//���Ⱑ 1���� Ŭ��, -1~1 ������ �� �׸��� -1 ���� �������� ������ ��å�� ����
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
