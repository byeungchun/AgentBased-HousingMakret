package san.main;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.commons.math.util.MathUtils;
import org.apache.log4j.Logger;

import san.entity.Chromosome;
import uchicago.src.sim.engine.SimInit;
import uchicago.src.sim.util.Random;

public class GeneticExer {

	private static Logger log = Logger.getLogger(GeneticExer.class);
	
	 private static ExecutorService indexLoadService = Executors.newFixedThreadPool(10);
	
	public static void main(String[] args){
		GeneticExer.init();
	}

	private static void init() {
		// 1. 1세대 만들기
		Map<String,Chromosome> population = generate1stPopulation(10);
		Iterator it = population.keySet().iterator();
		
		try{
		
			while (it.hasNext()) {
				String key = (String) it.next();
				Chromosome chrome = population.get(key);
				beginABM(chrome);

			}
		
			indexLoadService.shutdown();
		
		
			while(!indexLoadService.awaitTermination(2000, TimeUnit.MILLISECONDS)){
			}
		} catch(InterruptedException e){
			log.error("Thread 에러 발생 " + e.toString());
		}
		log.info("1세대 완료 및 평가");
		
		analyzeResult(population);
		
	}

	private static void analyzeResult(Map<String, Chromosome> population) {
		Iterator it = population.keySet().iterator();
		while(it.hasNext()){
			String key = (String) it.next();
			Chromosome chrome = population.get(key);
			log.info("평가시 염색체  집값 개수 : " + chrome.getLstHousePrice().size());
		}
	}

	private static void beginABM(Chromosome chrome) throws InterruptedException {
		HsingMktModel model = new HsingMktModel(
				false, //GUI로 할지 Auto exec로 할지 결정. Auto Exec 이면 false
				chrome.getGene1_socialIdxLgicF1(),
				chrome.getGene2_socialIdxLgicF2(),
				chrome.getGene3_socialIdxLgicF3(),
				chrome.getGene4_socialIdxLgicF4(),
				chrome.getGene5_socialIdxLgicF5(),
				chrome.getGene6_socialIdxLgicF6(),
				chrome.getGene7_socialIdxLgicF7(),
				chrome.getGene8_marketLgicExistHouseSupplyRto(),
				chrome.getGene9_governmentLgicStrongPolicy(),
				chrome.getGene10_governmentLgicWeakenPolicy()
		);
		
//		new SimInit().loadModel(model, null, true);
		indexLoadService.execute(new ThreadRunner(model,chrome));
		
			Thread.sleep(1000);
		 
	}

	private static Map<String, Chromosome> generate1stPopulation(int chromeCnt) {
		
		Map<String,Chromosome> population = new HashMap<String,Chromosome>();
		Random.createUniform();
		
		for(int i =0 ; i < chromeCnt ; i++){
			Chromosome chrome = new Chromosome();
			chrome.setGene1_socialIdxLgicF1(Random.uniform.nextDoubleFromTo(0.6D, 0.9D));
			chrome.setGene2_socialIdxLgicF2(1.0D - chrome.getGene1_socialIdxLgicF1());
			chrome.setGene3_socialIdxLgicF3(Random.uniform.nextDoubleFromTo(0.6D, 0.9D));
			chrome.setGene4_socialIdxLgicF4(Random.uniform.nextDoubleFromTo(0.1D, 0.4D));
			chrome.setGene5_socialIdxLgicF5(Random.uniform.nextDoubleFromTo(0.1D, 0.5D));
			chrome.setGene6_socialIdxLgicF6(Random.uniform.nextDoubleFromTo(0.1D, 0.5D));
			chrome.setGene7_socialIdxLgicF7(Random.uniform.nextDoubleFromTo(0.1D, 0.5D));
			chrome.setGene8_marketLgicExistHouseSupplyRto(Random.uniform.nextDoubleFromTo(0.01D, 0.1D));
			chrome.setGene9_governmentLgicStrongPolicy(Random.uniform.nextDoubleFromTo(0.00001D, 0.001D));
			chrome.setGene10_governmentLgicWeakenPolicy(Random.uniform.nextDoubleFromTo(0.00001D, 0.001D));
			chrome.setChromeName(chrome.getChromeName());
			
			//동일한 염색체가 만들어 질 경우에는 세대에 넣치 말고 제외
			if(population.containsKey(chrome.getChromeName())){
				i--;
				log.info("Same Chrome : "+ chrome.getChromeName());
				continue;
			}
			
			population.put(chrome.getChromeName(), chrome);
		}
		
		return population;
	}
}
