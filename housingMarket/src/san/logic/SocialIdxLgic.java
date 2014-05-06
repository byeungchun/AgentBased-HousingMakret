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
		//�� ����� �ε��� �ɸ������� ������Ʈ �ϱ����� ���� ���� �����Ͽ��� ��
		for(HouseholdNode hhn : lstHouseholdNode){
			lstNeighborIdx = new ArrayList<Integer>();
			for(Object obj : hhn.getOutNodes()){
				HouseholdNode hhnLink = (HouseholdNode) obj;
				lstNeighborIdx.add(hhnLink.getPurchasingIdx());
			}
			hhn.setLstNeighborIdx(lstNeighborIdx);
		}
		
		//����� �ε��� �ɸ������� �ҷ��� ���� �ɸ������� ������Ʈ
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
//			log.debug("��� " + hhn.getIdx() + " �� �̿� ����, �̿��� ���� �հ� �� ���� ���� "+lstNeighborIdx.size()+","+sum+"," + hhn.getPurchasingIdx()); 
		}
		
	}

	public static int choose1stCandidate(HsingMktModel hsingMktModel) {
		
		List<HouseholdNode> agentList = hsingMktModel.getAgentList();
		double highThreshold1st = hsingMktModel.getSocialIdxLgicF3(); //1�� �����ڸ� �����Ҷ� ��պ��� ���� ��尡 ���忡 �پ�� Ȯ��
		double lowThreshold1st = hsingMktModel.getSocialIdxLgicF4();
		
		// ��ü ����� ��� �ε��� �ɸ� ������ ����� ��
		int avgPurchasingIdx = 0;
		
		int trueCnt = 0;
		
		for(HouseholdNode hhn : agentList){
			avgPurchasingIdx += hhn.getPurchasingIdx();
		}
		avgPurchasingIdx = (int)(avgPurchasingIdx / agentList.size());
		
		// ��� ���� ū ���� 0.8 Ȯ���� ���� . ������ 0.2 Ȯ���� ����
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
		log.debug(agentList.size() + " �� ��� �� " + trueCnt + "�� ��� 1�� ����");
		
		return trueCnt;
	}

	public static int chooseFinalCandidate(HsingMktModel hsingMktModel) {
		//�� �ҵ�(������������ ����)�� ���� ��ġ�� �� �ڻ� �� 
		//�ش� ����� ��� �ҵ�(��������)�� ��� �ҵ溸�� ũ�ٸ� ������å�� ���� ������ ȯ���� ����
		List<HouseholdNode> agentList = hsingMktModel.getAgentList(); 
		GovernmentAgent governmentA = hsingMktModel.getGovernmentAgent();
		
		double threshold = calcuateFinalThreshold(hsingMktModel);
		double interestRate = governmentA.getInterestRate();
		int curVal = 0;
		int avgVal = 0;
		int trueCnt = 0;
		
		//�� ����� �ڻ�� �ҵ��� ��ȭ��Ű�µ� ����ϴ� ���Ժ���
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
			//����� �ڻ�� �ҵ��� ��ȭ
			calculateNodeAssetEarning(hhn, assetEarningDist, hsingMktModel.getTickCount());
			
		}
		log.debug("���� ���� ���, ���� THRESHOLD �� : " + trueCnt + "," + StringUtils.substring(Double.toString(threshold),0,4));
		
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
			log.error("��� �ҵ� �� �ڻ� ���� �� ���� " + e.toString());
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
		
		// ������ ����, ����, �ݸ��� �̿��Ͽ� Threshold ����
		// ����ġ : ���� 5, ���� 3, �ݸ� 2
		GovernmentAgent aGov = hsingMktModel.getGovernmentAgent();
		double sumGovValues = aGov.getTaxLvl() * taxWeight + aGov.getPolicyLvl() * policyWeight + aGov.getInterestRate() * interestWeight;
		
		//���ΰ� ������ �ִ�� ������ ���� �� �ִ� ���� ���� 3, ���� 3, ������ 5 �϶� ����ġ ���� �� : 34
		//���� 35�� �и�� �Ͽ� ���� Ȯ���� ����. �� ������ ������ ���� ���� Ȯ���� Ŀ��
				
		return (maxVal - sumGovValues) / maxVal;
	}
	
}
