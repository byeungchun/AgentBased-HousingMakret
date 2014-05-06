package san.main;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math.distribution.NormalDistribution;
import org.apache.commons.math.distribution.NormalDistributionImpl;
import org.apache.log4j.Logger;

import com.sun.media.Log;

import san.entity.HouseholdNode;
import san.entity.NodePosition;
import uchicago.src.sim.analysis.OpenSequenceGraph;
import uchicago.src.sim.analysis.Sequence;
import uchicago.src.sim.engine.BatchController;
import uchicago.src.sim.engine.Controller;
import uchicago.src.sim.gui.FruchGraphLayout;
import uchicago.src.sim.gui.Network2DDisplay;
import uchicago.src.sim.network.DefaultDrawableNode;
import uchicago.src.sim.util.Random;

public class BuildModel  {

	private static Logger log = Logger.getLogger(BuildModel.class);
	
	public static void buildModel(HsingMktModel hsingMktModel) {
		int n = 0;
		List<NodePosition> lstNodeXY = new ArrayList<NodePosition>();
		List<HouseholdNode> lstHouseholdNode = hsingMktModel.agentList;
		while(n < hsingMktModel.getNumNodes()){
			int x = Random.uniform.nextIntFromTo(0, hsingMktModel.getMarketXSize());
			int y = Random.uniform.nextIntFromTo(0, hsingMktModel.getMarketYSize());
			
			NodePosition nodeXY = new NodePosition(x,y);
			if(isOwn(nodeXY, lstNodeXY)) continue;
			 
			lstNodeXY.add(nodeXY);
			lstHouseholdNode.add(new HouseholdNode(n,x,y));
			n++;
		}	
		initNodeVar(hsingMktModel);
		
//		if(hsingMktModel.isShowPlot()) makePlot(hsingMktModel);
	}

//	private static void makePlot(HsingMktModel hsingMktModel) {
//		final double housePrice = (double)hsingMktModel.getHousingPrice();
//		hsingMktModel.graph = new OpenSequenceGraph("HousePrice vs. Time", hsingMktModel);
//		hsingMktModel.registerMediaProducer("House Graph", hsingMktModel.graph);
//		hsingMktModel.graph.addSequence("House Price", new Sequence() {
//			public double getSValue(){
//				return housePrice;
//			}
//		});
//		hsingMktModel.graph.setAxisTitles("Time", "House Price");
//		hsingMktModel.graph.setXRange(0,20);
//		hsingMktModel.graph.setYRange(0, housePrice);
//		
//		
//	}

	private static void initNodeVar(HsingMktModel hsingMktModel) {
		// 각 노드들이 가지고 있는 변수들을 초기화 함
		// numFamilies, totAsset, earning, numHouses, avgTotAsset, avgEarning, purchasingIdx
		
		double assetExp = 1000000.0D;
		double earningExp = 100000.0D;
		
		List<HouseholdNode> lstHouseholdNode = hsingMktModel.getAgentList();
		NormalDistribution assetDist = new NormalDistributionImpl(hsingMktModel.avgNodeAsset, hsingMktModel.stdDevNodeAsset);
		NormalDistribution earningDist = new NormalDistributionImpl(hsingMktModel.avgNodeEarning, hsingMktModel.stdDevNodeEarning);
		NormalDistribution purchasingDist = new NormalDistributionImpl(hsingMktModel.avgPurchasingIdx, hsingMktModel.stdDevPurchasingIdx);
		try{
		for(HouseholdNode hhn : lstHouseholdNode){
			int nodeAsset = (int)(assetDist.inverseCumulativeProbability(Math.random()) * assetExp);
			int nodeEarning = (int)(earningDist.inverseCumulativeProbability(Math.random()) * earningExp);
			hhn.setTotAsset(nodeAsset);
			hhn.setAvgTotAsset(nodeAsset);
			hhn.setEarning(nodeEarning);
			hhn.setAvgEarning(nodeEarning);
			hhn.setPurchasingIdx((int)purchasingDist.inverseCumulativeProbability(Math.random()));
			
		}
		}catch(Exception e){
			log.error("Node 초기값 설정 중 에러 " + e.toString());
			System.exit(-1);
		}
		
	}

	private static boolean isOwn(NodePosition nodeXY,
			List<NodePosition> lstNodeXY) {
		
		int x = nodeXY.getX();
		int y = nodeXY.getY();
		
		for(NodePosition aNodeXY : lstNodeXY){
			if( x == aNodeXY.getX() && y == aNodeXY.getY()) return true;
		}
		
		return false;
	}

	public static void buildDisplay(HsingMktModel hsingMktModel) {
		
		hsingMktModel.graphLayout = new FruchGraphLayout(hsingMktModel.agentList,
				hsingMktModel.getMarketXSize(), hsingMktModel.getMarketYSize(), hsingMktModel.surface, hsingMktModel.updateEveryN );
		Controller c = (Controller) hsingMktModel.getController();
	    c.addStopListener(hsingMktModel.graphLayout);
	    c.addPauseListener(hsingMktModel.graphLayout);
	    c.addExitListener(hsingMktModel.graphLayout);

	    Network2DDisplay display = new Network2DDisplay(hsingMktModel.graphLayout);
	    hsingMktModel.surface.addDisplayableProbeable(display, "House Market Model Display");

	    // add the display as a Zoomable. This means we can "zoom" in on
	    // various parts of the network.
	    hsingMktModel.surface.addZoomable(display);
	    hsingMktModel.surface.setBackground(java.awt.Color.white);
	    hsingMktModel.addSimEventListener(hsingMktModel.surface);
		
	}

	
}
