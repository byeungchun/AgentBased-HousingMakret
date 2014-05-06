package san.main;

import san.entity.Chromosome;
import uchicago.src.sim.engine.SimInit;

public class ThreadRunner implements Runnable{

	private HsingMktModel hsingMktModel = null;
	private Chromosome chrome = null;
	public ThreadRunner(){
		
	}
	
	public ThreadRunner(HsingMktModel hsingMktModel,Chromosome chrome){
		this.hsingMktModel = hsingMktModel;
		this.chrome = chrome;
	}
	
	public void run(){
		hsingMktModel.setChrome(chrome);
		new SimInit().loadModel(hsingMktModel, null, true);
	}
}
