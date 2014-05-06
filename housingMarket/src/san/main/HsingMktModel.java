package san.main;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import san.CONSTANT;
import san.entity.Chromosome;
import san.entity.GovernmentAgent;
import san.entity.HouseholdNode;
import san.logic.GovernmentLgic;
import san.logic.MarketLgic;
import san.logic.SocialIdxLgic;
import san.logic.StdDstLgic;
import san.test.TestNodeDistribution;
import san.util.ResultWriter;
import uchicago.src.sim.analysis.NetSequenceGraph;
import uchicago.src.sim.analysis.OpenSequenceGraph;
import uchicago.src.sim.engine.BasicAction;
import uchicago.src.sim.engine.Schedule;
import uchicago.src.sim.engine.SimInit;
import uchicago.src.sim.engine.SimModelImpl;
import uchicago.src.sim.gui.AbstractGraphLayout;
import uchicago.src.sim.gui.DisplaySurface;
import uchicago.src.sim.util.Random;

public class HsingMktModel extends SimModelImpl{

	private static Logger log = Logger.getLogger(HsingMktModel.class);
	
	public HsingMktModel(boolean isGUI){ this.isGUI = isGUI; }
	public HsingMktModel(){}
	public HsingMktModel(boolean isGUI, double gene1, double gene2, double gene3, double gene4,
			double gene5, double gene6, double gene7, double gene8, double gene9, double gene10){
		this.isGUI = isGUI;
		this.socialIdxLgicF1 = gene1;
		this.socialIdxLgicF2 = gene2;
		this.socialIdxLgicF3 = gene3;
		this.socialIdxLgicF4 = gene4;
		this.socialIdxLgicF5 = gene5;
		this.socialIdxLgicF6 = gene6;
		this.socialIdxLgicF7 = gene7;
		this.marketLgicExistHouseSupplyRto = gene8;
		this.governmentLgicStrongPolicy = gene9;
		this.governmentLgicWeakenPolicy = gene10;
		
	}
	private boolean isGUI = true;
	private int numNodes =100;
	private int marketXSize = 5000;
	private int marketYSize = 5000;
	private double avgLinks = 20.0D;
	private double stdDev = 2.0D;
	
	//주택 시장에서 사용하는 전역 변수
//	protected int mktPurschasingIdx = 90;
	protected int housingPrice = 10000000;
	protected LinkedList<Integer> historyHousingPrices = new LinkedList<Integer>(); 
	protected int prvNumBidNodes = 0;
	protected double avgNodeAsset = 20.0D; //표준정규분포이나 파레토 분포로 바뀌어야 함
	protected double stdDevNodeAsset = 10.0D;	
	protected double avgNodeEarning = 5.0D;
	protected double stdDevNodeEarning = 5.0D;
	protected double avgPurchasingIdx = 4.0D;
	protected double stdDevPurchasingIdx = 2.0D;
	protected List<HouseholdNode> agentList = new ArrayList<HouseholdNode>();
	protected GovernmentAgent governmentA = new GovernmentAgent(this);
	protected int participant1st = 0;
	protected int participant2nd = 0;
	protected double housePriceSlope = 0.0D;
	private double socialIdxLgicF1 = 0.7D; //주택 심리 지수, 직 전 Agent의 가중치 0.7을 의미함
	private double socialIdxLgicF2 = 0.3D; //주택 심리 지수, 현재 모든 Agent의 평균 가중치 0.3을 의미
	private double socialIdxLgicF3 = 0.8D; //매매 시장 1차 후보 선정. Agent의 심리지수가 평균보다 큰 경우
	private double socialIdxLgicF4 = 0.2D; //매매 시장 1차 후보 선정. Agent의 심리지수가 평균보다 작은 경우 
	private double socialIdxLgicF5 = 5.0D; //매매 시장 최종 후보 선정. 정책 가중치
	private double socialIdxLgicF6 = 3.0D; //매매 시장 최종 후보 선정. 세금 가중치
	private double socialIdxLgicF7 = 2.0D; //매매 시장 최종 후보 선정. 금리 가중치
	private int governmentAgentTaxLvl = 2; 
	private int governmentAgentPolicyLvl = 2;
	private int governmentAgentInterestRate = 3;
	private double marketLgicExistHouseSupplyRto = 0.01D; //신규 주택 공급량의 비율 (총 주택수에서)
	private double governmentLgicStrongPolicy = 0.0005D; 
	private double governmentLgicWeakenPolicy = -0.0005D;
	
	protected String layoutType = "Fruch";
	protected DisplaySurface surface;
	protected Schedule schedule;
	protected AbstractGraphLayout graphLayout;
	protected BasicAction initialAction;
	private BasicAction mainAction;
	private BasicAction writeAction;
	private BasicAction governmentAction;
	private BasicAction testEnd;
	protected int updateEveryN = 5;
	
	private Chromosome chrome = null;
	private FileWriter resultFile = null;
	
	public double getHousePriceSlope(){	return housePriceSlope;}
	public int getParticipant1st() {	return participant1st;	}
	public int getParticipant2nd() {	return participant2nd;	}
	public LinkedList<Integer> getHistoryHousingPrices(){	return historyHousingPrices;	}
	public int getHousingPrice() {	return housingPrice;	}
	public void setHousingPrice(int housingPrice) {	this.housingPrice = housingPrice;	}
	public int getPrvNumBidNodes() {	return prvNumBidNodes;	}
	public void setPrvNumBidNodes(int prvNumBidNodes) {	this.prvNumBidNodes = prvNumBidNodes;	}
	public GovernmentAgent getGovernmentAgent(){	return governmentA;	}
	public int getNumNodes() {	return numNodes;}
	public void setNumNodes(int numNodes) {	this.numNodes = numNodes;	}
	public int getMarketXSize() {	return marketXSize;	}
	public void setMarketXSize(int marketXSize) {	this.marketXSize = marketXSize;	}
	public int getMarketYSize() {	return marketYSize;	}
	public void setMarketYSize(int marketYSize) {	this.marketYSize = marketYSize;	}
	public double getAvgLinks() {	return avgLinks;	}
	public void setAvgLinks(double avgLinks) {	this.avgLinks = avgLinks;}
	public double getStdDev() {	return stdDev;	}
	public void setStdDev(double stdDev) {	this.stdDev = stdDev;	}
	public List<HouseholdNode> getAgentList(){	return agentList;	}
	public double getSocialIdxLgicF1() {	return socialIdxLgicF1;	}
	public void setSocialIdxLgicF1(double socialIdxLgicF1) {this.socialIdxLgicF1 = socialIdxLgicF1;	}
	public double getSocialIdxLgicF2() {	return socialIdxLgicF2;	}
	public void setSocialIdxLgicF2(double socialIdxLgicF2) {	this.socialIdxLgicF2 = socialIdxLgicF2;	}
	public double getSocialIdxLgicF3() {	return socialIdxLgicF3;	}
	public void setSocialIdxLgicF3(double socialIdxLgicF3) {this.socialIdxLgicF3 = socialIdxLgicF3;	}
	public double getSocialIdxLgicF4() {	return socialIdxLgicF4;	}
	public void setSocialIdxLgicF4(double socialIdxLgicF4) {	this.socialIdxLgicF4 = socialIdxLgicF4;	}	
	public double getSocialIdxLgicF5() { 		return socialIdxLgicF5; 	}
	public void setSocialIdxLgicF5(double socialIdxLgicF5) {this.socialIdxLgicF5 = socialIdxLgicF5;	}
	public double getSocialIdxLgicF6() {	return socialIdxLgicF6;	}
	public void setSocialIdxLgicF6(double socialIdxLgicF6) {this.socialIdxLgicF6 = socialIdxLgicF6;	}
	public double getSocialIdxLgicF7() {	return socialIdxLgicF7;	}
	public void setSocialIdxLgicF7(double socialIdxLgicF7) {	this.socialIdxLgicF7 = socialIdxLgicF7;	}
	public int getGovernmentAgentTaxLvl() { return governmentAgentTaxLvl;	}
	public void setGovernmentAgentTaxLvl(int governmentAgentTaxLvl) {this.governmentAgentTaxLvl = governmentAgentTaxLvl;	}
	public int getGovernmentAgentPolicyLvl() {return governmentAgentPolicyLvl;	}
	public void setGovernmentAgentPolicyLvl(int governmentAgentPolicyLvl) {	this.governmentAgentPolicyLvl = governmentAgentPolicyLvl;	}
	public int getGovernmentAgentInterestRate() {return governmentAgentInterestRate;	}
	public void setGovernmentAgentInterestRate(int governmentAgentInterestRate) {this.governmentAgentInterestRate = governmentAgentInterestRate;}
	public double getMarketLgicExistHouseSupplyRto() {	return marketLgicExistHouseSupplyRto;	}
	public void setMarketLgicExistHouseSupplyRto(double marketLgicExistHouseSupplyRto) {this.marketLgicExistHouseSupplyRto = marketLgicExistHouseSupplyRto;	}
	public double getGovernmentLgicStrongPolicy() {	return governmentLgicStrongPolicy;	}
	public void setGovernmentLgicStrongPolicy(double governmentLgicStrongPolicy) {		this.governmentLgicStrongPolicy = governmentLgicStrongPolicy;	}
	public double getGovernmentLgicWeakenPolicy() {		return governmentLgicWeakenPolicy;	}
	public void setGovernmentLgicWeakenPolicy(double governmentLgicWeakenPolicy) {		this.governmentLgicWeakenPolicy = governmentLgicWeakenPolicy;	}
	
	@Override
	public void begin() {
		BuildModel.buildModel(this);
//		BuildModel.buildDisplay(this);
		buildSchedule();
		if(isGUI){
			graphLayout.updateLayout();
			surface.display();
		}
	}

	private void buildSchedule(){
		initialAction = schedule.scheduleActionAt(1, this, "initialAction");
		mainAction = schedule.scheduleActionAtInterval(1,this, "mainAction");
		testEnd = schedule.scheduleActionAtInterval(1, this,"testEnd");
		writeAction = schedule.scheduleActionAtInterval(1, this,"writeResult");
		governmentAction = schedule.scheduleActionAtInterval(100, this,"doGovernmentAction");
//		schedule.scheduleActionAt(100, this, "stop");
		
//		class StopModel extends BasicAction {
//			 public void execute(){
//			  // ensure that Java stays running
//			  getController().setExitOnExit(false);
//			  // stop the model
//			  stop();
//			 }
//		}
//		int nTicks = 1000;
//		schedule.scheduleActionAt(nTicks, new StopModel(), Schedule.LAST);

	}
	
	public void testEnd(){
		if(this.getTickCount() == 18800){ //실제 월간 주택 지수를 188개월이 있기 때문에 비교를 위해서 적용
			 getController().setExitOnExit(false);
//			 log.info("Thread end : " + chrome.getLstHousePrice().size() + "----" + chrome.getChromeName());
			 stop();
		}
//		else if(this.getTickCount() < 1000){
//			mainAction.execute();
//			writeAction.execute();
//			governmentAction.execute();
//		}else{
//			
//		}
		
	}
	
	public void initialAction() {
		
		StdDstLgic.stdDstInit(this);
//		TestNodeDistribution.checkDistribution(agentList);
		if(isGUI){
			graphLayout.updateLayout();
			surface.updateDisplay();
		}
	}
	
//	public void stop(){
//		this.fireEndSim();
//	}
	
	public void mainAction(){
//		log.info("Tick Count " + this.getTickCount());
		//1. 이웃간의 주택 심리 지수를 계산하여 노드 심리지수를 계산
		SocialIdxLgic.calNodeSocialIdx(this);
		//2. 매매 시장 참여 1차 후보자 선정
		participant1st = SocialIdxLgic.choose1stCandidate(this);
		//3. 매매 시장 참여 후보자 확정
		participant2nd = SocialIdxLgic.chooseFinalCandidate(this);
		//4. 정부의 공급 수량과 매매 참여자 비교하여 주택 가격 결정
		MarketLgic.decideHousePrice(this);		
		
	}
	
	public void writeResult() throws IOException{
		ResultWriter.writeResult(this, resultFile);		
	}
	
	public void doGovernmentAction(){
		housePriceSlope = GovernmentLgic.decidePolcy(this);
	}
	
	@Override
	public String[] getInitParam() {
		String[] params = {"numNodes", "marketXSize", "marketYSize", "avgLinks", "stdDev","Plot","socialIdxLgicF1"};
		return params;
	}

	@Override
	public String getName() {
		return "Housing Market Model";
	}

	@Override
	public Schedule getSchedule() {
		return schedule;
	}

	@Override
	public void setup() {
		Random.createUniform();
		if(surface != null) surface.dispose();
		surface = null;
		schedule = null;
		System.gc();
		
		historyHousingPrices.add(housingPrice);
		Calendar cal = Calendar.getInstance();
	    String dateString, timeString;
	    
		dateString = String.format("%02d%02d", cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH));
	    timeString = String.format("%02d%02d%02d", cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), cal.get(Calendar.SECOND));

		try{
			if(resultFile != null) resultFile.close();
			
			resultFile = new FileWriter(new File(CONSTANT.RESULT_DIR+dateString+"_"+timeString));
		}catch(Exception e){
			log.error("결과 파일 생성 실패 " + e.toString());
			System.exit(-1);
		}
		
		
		surface = new DisplaySurface(this, "Housing Market Model Display");
		registerDisplaySurface("Main Display", surface);
		schedule = new Schedule();
		agentList = new ArrayList();
		
	}

	
	public static void main(String[] agrs){
		boolean isGUI = false;
		SimInit init = new SimInit();
		HsingMktModel model = new HsingMktModel(isGUI);
		init.loadModel(model, null, isGUI); // 파라미터 파일은  "./params/param1.txt"
	}
	public void setChrome(Chromosome chrome) {
		this.chrome = chrome;		
	}
	public Chromosome getChrome() { return chrome; }
}
