package san.entity;

import java.util.ArrayList;
import java.util.List;

public class Chromosome {
	
	private String chromeName = "";
	private Double Gene1_socialIdxLgicF1 = 0.0D;
	private Double Gene2_socialIdxLgicF2 = 0.0D;
	private Double Gene3_socialIdxLgicF3 = 0.0D;
	private Double Gene4_socialIdxLgicF4 = 0.0D;
	private Double Gene5_socialIdxLgicF5 = 0.0D;
	private Double Gene6_socialIdxLgicF6 = 0.0D;
	private Double Gene7_socialIdxLgicF7 = 0.0D;
	private Double Gene8_marketLgicExistHouseSupplyRto = 0.0D;
	private Double Gene9_governmentLgicStrongPolicy = 0.0D;
	private Double Gene10_governmentLgicWeakenPolicy = 0.0D;
	private List<Integer> lstHousePrice = new ArrayList<Integer>();
	
	public String getChromeName() {
		return "_"
			+Gene1_socialIdxLgicF1
			+Gene2_socialIdxLgicF2
			+Gene3_socialIdxLgicF3
			+Gene4_socialIdxLgicF4
			+Gene5_socialIdxLgicF5
			+Gene6_socialIdxLgicF6
			+Gene7_socialIdxLgicF7
			+Gene8_marketLgicExistHouseSupplyRto
			+Gene9_governmentLgicStrongPolicy
			+Gene10_governmentLgicWeakenPolicy;
	}
	public void setChromeName(String chromeName) {
		this.chromeName = chromeName;
	}
	public Double getGene1_socialIdxLgicF1() {
		return Gene1_socialIdxLgicF1;
	}
	public void setGene1_socialIdxLgicF1(Double gene1_socialIdxLgicF1) {
		Gene1_socialIdxLgicF1 = gene1_socialIdxLgicF1;
	}
	public Double getGene2_socialIdxLgicF2() {
		return Gene2_socialIdxLgicF2;
	}
	public void setGene2_socialIdxLgicF2(Double gene2_socialIdxLgicF2) {
		Gene2_socialIdxLgicF2 = gene2_socialIdxLgicF2;
	}
	public Double getGene3_socialIdxLgicF3() {
		return Gene3_socialIdxLgicF3;
	}
	public void setGene3_socialIdxLgicF3(Double gene3_socialIdxLgicF3) {
		Gene3_socialIdxLgicF3 = gene3_socialIdxLgicF3;
	}
	public Double getGene4_socialIdxLgicF4() {
		return Gene4_socialIdxLgicF4;
	}
	public void setGene4_socialIdxLgicF4(Double gene4_socialIdxLgicF4) {
		Gene4_socialIdxLgicF4 = gene4_socialIdxLgicF4;
	}
	public Double getGene5_socialIdxLgicF5() {
		return Gene5_socialIdxLgicF5;
	}
	public void setGene5_socialIdxLgicF5(Double gene5_socialIdxLgicF5) {
		Gene5_socialIdxLgicF5 = gene5_socialIdxLgicF5;
	}
	public Double getGene6_socialIdxLgicF6() {
		return Gene6_socialIdxLgicF6;
	}
	public void setGene6_socialIdxLgicF6(Double gene6_socialIdxLgicF6) {
		Gene6_socialIdxLgicF6 = gene6_socialIdxLgicF6;
	}
	public Double getGene7_socialIdxLgicF7() {
		return Gene7_socialIdxLgicF7;
	}
	public void setGene7_socialIdxLgicF7(Double gene7_socialIdxLgicF7) {
		Gene7_socialIdxLgicF7 = gene7_socialIdxLgicF7;
	}
	public Double getGene8_marketLgicExistHouseSupplyRto() {
		return Gene8_marketLgicExistHouseSupplyRto;
	}
	public void setGene8_marketLgicExistHouseSupplyRto(
			Double gene8_marketLgicExistHouseSupplyRto) {
		Gene8_marketLgicExistHouseSupplyRto = gene8_marketLgicExistHouseSupplyRto;
	}
	public Double getGene9_governmentLgicStrongPolicy() {
		return Gene9_governmentLgicStrongPolicy;
	}
	public void setGene9_governmentLgicStrongPolicy(
			Double gene9_governmentLgicStrongPolicy) {
		Gene9_governmentLgicStrongPolicy = gene9_governmentLgicStrongPolicy;
	}
	public Double getGene10_governmentLgicWeakenPolicy() {
		return Gene10_governmentLgicWeakenPolicy;
	}
	public void setGene10_governmentLgicWeakenPolicy(
			Double gene10_governmentLgicWeakenPolicy) {
		Gene10_governmentLgicWeakenPolicy = gene10_governmentLgicWeakenPolicy;
	}
	public List<Integer> getLstHousePrice() {
		return lstHousePrice;
	}
	public void setLstHousePrice(List<Integer> lstHousePrice) {
		this.lstHousePrice = lstHousePrice;
	}
	
	
	
}
