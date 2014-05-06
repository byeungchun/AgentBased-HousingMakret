package san.util;

import java.io.FileWriter;
import java.io.IOException;

import org.apache.log4j.Logger;

import san.entity.GovernmentAgent;
import san.main.HsingMktModel;

public class ResultWriter {

	private static Logger log = Logger.getLogger(ResultWriter.class);
	
	public static void writeResult(HsingMktModel hsingMktModel, FileWriter resultFile) throws IOException {
		
		GovernmentAgent aGov = hsingMktModel.getGovernmentAgent();
		
		if(hsingMktModel.getTickCount() == 1){
			resultFile.write("총노드수, 총 주택 수,1차참여자, 2차참여자, 시장가격, 집값변화율,  금리, 정책, 세금 \n");
		}
		
		//총노드수, 총 주택 수,1차참여자, 2차참여자, 시장가격, 집값변화율, 금리, 정책, 세금
		resultFile.write(hsingMktModel.getAgentList().size()+","
				+ hsingMktModel.getGovernmentAgent().getTotHouses() +"," 
				+ hsingMktModel.getParticipant1st() + ","
				+ hsingMktModel.getParticipant2nd() + ","
				+ hsingMktModel.getHousingPrice()+","
				+ hsingMktModel.getHousePriceSlope() + ","
				+ aGov.getInterestRate() + ","
				+ aGov.getPolicyLvl() + ","
				+ aGov.getTaxLvl() +
				"\n");
		
		resultFile.flush();
		
		
	}

}
