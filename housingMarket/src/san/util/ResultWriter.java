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
			resultFile.write("�ѳ���, �� ���� ��,1��������, 2��������, ���尡��, ������ȭ��,  �ݸ�, ��å, ���� \n");
		}
		
		//�ѳ���, �� ���� ��,1��������, 2��������, ���尡��, ������ȭ��, �ݸ�, ��å, ����
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
