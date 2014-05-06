package san.test;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math.genetics.BinaryChromosome;
import org.apache.commons.math.genetics.Chromosome;

public class testGA {

	
	public static void main(String[] args){
		
		List<BinaryChromosome> lstChrom = new ArrayList<BinaryChromosome>(10);
		
		for(int i =0; i < 10 ; i++){
			Integer[] genes = new Integer[20];
			for(int j = 0; j < 20; j++){
				if(Math.random() > 0.5D){
					genes[i] = 1;
				}else{
					genes[i] = 0;
				}
			}
//			BinaryChromosome chrom = new BinaryChromosome(genes);
			
		}
		
	}
}
