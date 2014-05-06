package san.entity;

import san.main.HsingMktModel;

public class GovernmentAgent {
	
	private int taxLvl = 2;  //�絵��, 2���� �̻� �߰���, ���� : 1 ~ 3
	private int policyLvl = 2; //DTI ����, ����� ����, ���� : 1 ~ 3
	private int interestRate = 3; //���رݸ�, ���� : 1 ~ 5
	
	private int totHouses = 1000;
	private int supplyHouses = 10;
	private int prvTotHouses = 1000;
	private int prvSupplyHouses = 10;
	
	public GovernmentAgent(){}
	
	public GovernmentAgent(HsingMktModel hsingMktModel){
		this.taxLvl = hsingMktModel.getGovernmentAgentTaxLvl();
		this.policyLvl = hsingMktModel.getGovernmentAgentPolicyLvl();
		this.interestRate = hsingMktModel.getGovernmentAgentInterestRate();
	}
	
	public int getTaxLvl() {
		return taxLvl;
	}

	public void setTaxLvl(int taxLvl) {
		this.taxLvl = taxLvl;
	}

	public int getPolicyLvl() {
		return policyLvl;
	}

	public void setPolicyLvl(int policyLvl) {
		this.policyLvl = policyLvl;
	}

	public int getInterestRate() {
		return interestRate;
	}

	public void setInterestRate(int interestRate) {
		this.interestRate = interestRate;
	}

	public int getTotHouses() {
		return totHouses;
	}

	public void setTotHouses(int totHouses) {
		this.totHouses = totHouses;
	}

	public int getSupplyHouses() {
		return supplyHouses;
	}

	public void setSupplyHouses(int supplyHouses) {
		this.supplyHouses = supplyHouses;
	}

	public int getPrvTotHouses() {
		return prvTotHouses;
	}

	public void setPrvTotHouses(int prvTotHouses) {
		this.prvTotHouses = prvTotHouses;
	}

	public int getPrvSupplyHouses() {
		return prvSupplyHouses;
	}

	public void setPrvSupplyHouses(int prvSupplyHouses) {
		this.prvSupplyHouses = prvSupplyHouses;
	}
	
	
}
