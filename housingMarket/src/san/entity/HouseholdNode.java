package san.entity;

import java.awt.Color;
import java.util.List;

import org.apache.log4j.Logger;

import san.entity.HouseholdEdge;
import san.entity.HouseholdNode;
import uchicago.src.sim.gui.RectNetworkItem;
import uchicago.src.sim.network.DefaultDrawableNode;
import uchicago.src.sim.network.DefaultNode;
import uchicago.src.sim.network.Edge;
import uchicago.src.sim.util.Random;

public class HouseholdNode extends DefaultDrawableNode {

	private static Logger log = Logger.getLogger(HouseholdNode.class);
	
	private int idx = 0;
	
	private int numFamilies = 5; //가구원 수
	private int totAsset = 200000000; //현재 총 자산
	private int earning = 5000000; //월 평균 소득
	private int numHouses = 1; //보유 주택수
	private int avgTotAsset = 150000000; //평균 자산
	private int avgEarning = 3500000; //평균 소득
	private int purchasingIdx = 4;   //부동산 심리지수
	
	private boolean is1stCandidate = false; //매매 시장 참여 1차 후보자
	private boolean is2ndCandidate = false; //매매 시장 참여 2차 후보자
	
	private List<Integer> lstNeighborIdx;
	
	public HouseholdNode(){}
	
	public HouseholdNode(int n, int x, int y){
		this.idx = n;
		init(x, y);
	}
	
	public List<Integer> getLstNeighborIdx() {
		return lstNeighborIdx;
	}

	public void setLstNeighborIdx(List<Integer> lstNeighborIdx) {
		this.lstNeighborIdx = lstNeighborIdx;
	}
	
	public boolean getIs1stCandidate() {
		return is1stCandidate;
	}

	public void setIs1stCandidate(boolean is1stCandidate) {
		this.is1stCandidate = is1stCandidate;
	}

	public boolean getIsIs2ndCandidate() {
		return is2ndCandidate;
	}

	public void setIs2ndCandidate(boolean is2ndCandidate) {
		this.is2ndCandidate = is2ndCandidate;
	}

	public int getIdx() {
		return idx;
	}

	public void setIdx(int idx) {
		this.idx = idx;
	}

	public int getNumFamilies() {
		return numFamilies;
	}

	public void setNumFamilies(int numFamilies) {
		this.numFamilies = numFamilies;
	}

	public int getTotAsset() {
		return totAsset;
	}

	public void setTotAsset(int totAsset) {
		this.totAsset = totAsset;
	}

	public int getEarning() {
		return earning;
	}

	public void setEarning(int earning) {
		this.earning = earning;
	}

	public int getNumHouses() {
		return numHouses;
	}

	public void setNumHouses(int numHouses) {
		this.numHouses = numHouses;
	}

	public int getAvgTotAsset() {
		return avgTotAsset;
	}

	public void setAvgTotAsset(int avgTotAsset) {
		this.avgTotAsset = avgTotAsset;
	}

	public int getAvgEarning() {
		return avgEarning;
	}

	public void setAvgEarning(int avgEarning) {
		this.avgEarning = avgEarning;
	}

	public int getPurchasingIdx() {
		return purchasingIdx;
	}

	public void setPurchasingIdx(int purchasingIdx) {
		this.purchasingIdx = purchasingIdx;
	}

	public void init(int x, int y){
		RectNetworkItem rect = new RectNetworkItem(x,y);
		setDrawable(rect);
	}
	


	public void meetRandom(List<HouseholdNode> list, int maxDegree) {
		int index = Random.uniform.nextIntFromTo(0, list.size()-1);
		HouseholdNode node = (HouseholdNode) list.get(index);
		while(this.equals(node)){
			index = Random.uniform.nextIntFromTo(0, list.size() - 1);
			node = (HouseholdNode) list.get(index);
		}
		
		makeEdgeToFrom(node, maxDegree, Color.red);
	}
	
	public void setLink(List<HouseholdNode> list, int numLink){
		HouseholdNode toNode = null;
		boolean isUnique = true;
		for(int i = 0; i < numLink; i++){
			toNode = (HouseholdNode)list.get(Random.uniform.nextIntFromTo(0, list.size() -1));
			while(true){
				isUnique = true;
				toNode = (HouseholdNode)list.get(Random.uniform.nextIntFromTo(0, list.size() -1));
				if(this.equals(toNode)) continue;
				else{
					for(Object objhhn : getOutNodes()){
						HouseholdNode hhn = (HouseholdNode) objhhn;
						if(hhn.equals(toNode)){
							isUnique = false;
							break;
						}
					}
				}
				if(isUnique) break;
			}
			makeEdgeToFrom(toNode, numLink, Color.red);
		}
	}
	
	public void makeEdgeToFrom(HouseholdNode node, int maxDegree, Color color) {
//	    if ((! hasEdgeTo(node)) && getOutDegree() < maxDegree &&
//		node.getOutDegree() < maxDegree) {
//	      log.debug("출발 노드 " + this.getIdx() + " , 도착노드 " + node.getIdx());
	      Edge edge = new HouseholdEdge(this, node, color);
	      addOutEdge(edge);
//	      node.addInEdge(edge);
//	      Edge otherEdge = new HouseholdEdge(node, this, color);
//	      node.addOutEdge(otherEdge);
//	      addInEdge(otherEdge);
//	    }
	  }
}
