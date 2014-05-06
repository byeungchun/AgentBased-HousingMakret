package san.logic;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.math.MathException;
import org.apache.commons.math.distribution.NormalDistribution;
import org.apache.commons.math.distribution.NormalDistributionImpl;
import org.apache.log4j.Logger;

import san.entity.GovernmentAgent;
import san.entity.HouseholdNode;
import san.main.HsingMktModel;

public class SocialIdxLgic {

	private static Logger log = Logger.getLogger(SocialIdxLgic.class);

	public static void calNodeSocialIdx(HsingMktModel hsingMktModel) {
		
		
		List<Integer> lstNeighborIdx;
		List<HouseholdNode> lstHouseholdNode = hsingMktModel.getAgentList();
		//각 노드의 부동산 심리지수를 업데이트 하기전에 기존 값을 저장하여야 함
		for(HouseholdNode hhn : lstHouseholdNode){
			lstNeighborIdx = new ArrayList<Integer>();
			for(Object obj : hhn.getOutNodes()){
				HouseholdNode hhnLink = (HouseholdNode) obj;
				lstNeighborIdx.add(hhnLink.getPurchasingIdx());
			}
			hhn.setLstNeighborIdx(lstNeighborIdx);
		}
		
		//저장된 부동산 심리지수를 불러와 나의 심리지수를 업데이트
		for(HouseholdNode hhn : lstHouseholdNode){
			lstNeighborIdx = hhn.getLstNeighborIdx();
			int sum = 0;
			int nextPurchasingIdx = 0;
			for(Integer puchasingIdx : lstNeighborIdx){
				sum +=  puchasingIdx;
			}
			nextPurchasingIdx = (int)(hsingMktModel.getSocialIdxLgicF1() * hhn.getPurchasingIdx() + 
					hsingMktModel.getSocialIdxLgicF2() * sum/lstNeighborIdx.size());
			hhn.setPurchasingIdx(nextPurchasingIdx);
//			log.debug("노드 " + hhn.getIdx() + " 의 이웃 개수, 이웃의 지수 합계 및 차기 지수 "+lstNeighborIdx.size()+","+sum+"," + hhn.getPurchasingIdx()); 
		}
		
	}

	public static int choose1stCandidate(HsingMktModel hsingMktModel) {
		
		List<HouseholdNode> agentList = hsingMktModel.getAgentList();
		double highThreshold1st = hsingMktModel.getSocialIdxLgicF3(); //1차 참여자를 선택할때 평균보다 높은 노드가 시장에 뛰어들 확율
		double lowThreshold1st = hsingMktModel.getSocialIdxLgicF4();
		
		// 전체 노드의 평균 부동산 심리 지수를 계산한 뒤
		int avgPurchasingIdx = 0;
		
		int trueCnt = 0;
		
		for(HouseholdNode hhn : agentList){
			avgPurchasingIdx += hhn.getPurchasingIdx();
		}
		avgPurchasingIdx = (int)(avgPurchasingIdx / agentList.size());
		
		// 평균 보다 큰 노드는 0.8 확률로 참여 . 작으면 0.2 확률로 참여
		for(HouseholdNode hhn : agentList){
			if(hhn.getPurchasingIdx() > avgPurchasingIdx){
				if(Math.random()< highThreshold1st) {
					hhn.setIs1stCandidate(true);
					trueCnt++;
				}
				else hhn.setIs1stCandidate(false);
			}else{
				if(Math.random() < lowThreshold1st){
					hhn.setIs1stCandidate(true);
					trueCnt++;
				}
				else hhn.setIs1stCandidate(false);
				
			}
		}
		log.debug(agentList.size() + " 의 노드 중 " + trueCnt + "의 노드 1차 참여");
		
		return trueCnt;
	}

	public static int chooseFinalCandidate(HsingMktModel hsingMktModel) {
		//현 소득(영구연금으로 가정)의 현재 가치와 총 자산 이 
		//해당 노드의 평균 소득(영구연금)과 평균 소득보다 크다면 정부정책에 의해 결정된 환율에 따라
		List<HouseholdNode> agentList = hsingMktModel.getAgentList(); 
		GovernmentAgent governmentA = hsingMktModel.getGovernmentAgent();
		
		double threshold = calcuateFinalThreshold(hsingMktModel);
		double interestRate = governmentA.getInterestRate();
		int curVal = 0;
		int avgVal = 0;
		int trueCnt = 0;
		
		//각 노드의 자산과 소득을 변화시키는데 사용하는 정규분포
		NormalDistribution assetEarningDist = new NormalDistributionImpl(1.1D, 0.05D);
				
		for(HouseholdNode hhn : agentList){
			curVal = (int)(hhn.getEarning() / interestRate + hhn.getTotAsset());
			avgVal = (int)(hhn.getAvgEarning() / interestRate + hhn.getAvgTotAsset());
			
			if(hhn.getIs1stCandidate() && curVal > avgVal && Math.random() < threshold){
				hhn.setIs2ndCandidate(true);
				trueCnt++;
			}else{
				hhn.setIs2ndCandidate(false);
			}
			//노드의 자산과 소득을 변화
			calculateNodeAssetEarning(hhn, assetEarningDist, hsingMktModel.getTickCount());
			
		}
		log.debug("최종 참여 노드, 최종 THRESHOLD 값 : " + trueCnt + "," + StringUtils.substring(Double.toString(threshold),0,4));
		
		return trueCnt;
	}


	private static void calculateNodeAssetEarning(HouseholdNode hhn,
			NormalDistribution aED, double tickCnt) {
		try {
			hhn.setEarning((int)(hhn.getEarning() * aED.inverseCumulativeProbability(Math.random())));
			hhn.setAvgEarning((int)((hhn.getAvgEarning() * tickCnt + hhn.getEarning()) / (tickCnt + 1)));
			
			hhn.setTotAsset((int)(hhn.getTotAsset() * aED.inverseCumulativeProbability(Math.random())));
			hhn.setAvgTotAsset((int)((hhn.getAvgTotAsset() * tickCnt + hhn.getTotAsset()) / (tickCnt + 1)));
			
		} catch (MathException e) {
			log.error("노드 소득 및 자산 변경 중 에러 " + e.toString());
			System.exit(-1);
		}
		
		
	}

	private static double calcuateFinalThreshold(HsingMktModel hsingMktModel) {
		
		int taxIdx = hsingMktModel.getGovernmentAgentTaxLvl();
		int policyIdx = hsingMktModel.getGovernmentAgentPolicyLvl();
		int interestIdx = hsingMktModel.getGovernmentAgentInterestRate();
		
		double taxWeight = hsingMktModel.getSocialIdxLgicF5();
		double policyWeight = hsingMktModel.getSocialIdxLgicF6(); 
		double interestWeight = hsingMktModel.getSocialIdxLgicF7();
		
		double maxVal = taxIdx * taxWeight + policyIdx * policyWeight + interestIdx * interestWeight;
		
		// 정부의 세금, 규제, 금리를 이용하여 Threshold 결정
		// 가중치 : 세금 5, 규제 3, 금리 2
		GovernmentAgent aGov = hsingMktModel.getGovernmentAgent();
		double sumGovValues = aGov.getTaxLvl() * taxWeight + aGov.getPolicyLvl() * policyWeight + aGov.getInterestRate() * interestWeight;
		
		//정부가 규제를 최대로 했을때 나올 수 있는 값은 세금 3, 규제 3, 이자율 5 일때 가중치 합한 값 : 34
		//따라서 35를 분모로 하여 참여 확률을 구함. 즉 규제가 적어질 때는 참여 확률이 커짐
				
		return (maxVal - sumGovValues) / maxVal;
	}
	
}
