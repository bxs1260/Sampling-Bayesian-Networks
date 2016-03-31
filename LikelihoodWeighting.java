import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.w3c.dom.Node;

public class LikelihoodWeighting {
	
	public enum Nodes {
		I, C, W, O, TS, R
	};
	
	final static int NO_NODES = 6;
	static double []currentSample = new double[6];
	final static double[][] probabilities = { { 0.6 }, { 0.7 }, { 0.2 }, { 0.1, 0.5, 0.4, 0.7 },
			{ 0.1, 0.5, 0.05, 0.6 }, { 0.8, 0.5, 0.4, 0.2 } };

	/**
	 * Generate weighted sample
	 * @param node
	 */
	static void weightedSample(Nodes node){
		for(int i=0; i<NO_NODES; ++i){
			if(node.ordinal() == i){
				currentSample[node.ordinal()] = getWeight(node);
			} else {
				currentSample[i] = sample(getProbability(node));
			}
		}
	}
	
	/**
	 * 
	 * @param favourableCases
	 * @param node
	 * @param noOfSamples
	 * @return Calculate likelihood weighting
	 */
	
	static double likelihoodWeighting(Map<Integer, Double> favourableCases, Nodes node, int noOfSamples){
		double favourableWeight = 0.0, unfavourableWeight = 0.0;
		for(int i=0; i<noOfSamples; ++i){
			weightedSample(node);
			System.out.println(Arrays.toString(currentSample));
			if(isSampleFavourable(favourableCases)){
				favourableWeight += currentSample[node.ordinal()]; 
			} else {
				unfavourableWeight += currentSample[node.ordinal()];
			}
		}
		return favourableWeight/(favourableWeight + unfavourableWeight);
	}
	
	/**
	 * 
	 * @param favourableCases
	 * @return true if sample is favourable case
	 */
	static boolean isSampleFavourable(Map<Integer, Double> favourableCases){
		boolean result = false, othersTrue = true;
		Iterator<Integer> keyIterator = favourableCases.keySet().iterator();
		while(keyIterator.hasNext()){
			int index = keyIterator.next();
			if(currentSample[index] == favourableCases.get(index) && othersTrue){
				result = true;
			} else {
				result = false;
				othersTrue = false;
			}
		}
		return result;
	}
	public static void main(String[] args) {
		int noOfSamples = 1000;
		Nodes node = Nodes.TS;		//Given node
		Map<Integer, Double> favourableCases = new HashMap<Integer, Double>(){{
			put(3, 0.0);
			put(5, 1.0);
		}}; 
		
		System.out.println(likelihoodWeighting(favourableCases, node, noOfSamples));
	}
	
	private static int sample(double probability){
		double rand = Math.random();
		if( rand < probability){
			return 1;
		} else {
			return 0;
		}
	}
	
	/**
	 * 
	 * @param node
	 * @return Weight of particular node given parents sample
	 */
	private static double getWeight(Nodes node){
		int index = node.ordinal();
		double probability = 0.0;
		if(Nodes.I == node ||Nodes.C == node || Nodes.W == node){
			probability = probabilities[index][0];
		} else if(Nodes.O == node){
			 probability = probabilities[index][(int) (2*currentSample[0]+currentSample[1])];
		} else if(Nodes.TS == node){
			probability = probabilities[index][(int) (2*currentSample[1]+currentSample[2])];
		} else if(Nodes.R == node){
			probability = probabilities[index][(int) (2*currentSample[3]+currentSample[4])];
		}
		return probability;
	}
	
	/**
	 * 
	 * @param node
	 * @return Probability of a node given its parents
	 */
	private static double getProbability(Nodes node){
		int index1, index2, index = node.ordinal();
		double probability = 0.0;
		if(Nodes.I == node ||Nodes.C == node || Nodes.W == node){
			probability = probabilities[index][0];
		} else if(Nodes.O == node){
				index1 = currentSample[0] == probabilities[0][0] ? 1 : (int)currentSample[0];
				index2 = currentSample[1] == probabilities[1][0] ? 1 : (int)currentSample[1];
				probability = probabilities[index][(int) (2*index1+index2)];
		} else if(Nodes.TS == node){
			index1 = currentSample[1] == probabilities[1][0] ? 1 : (int)currentSample[1];
			index2 = currentSample[2] == probabilities[2][0] ? 1 : (int)currentSample[2];
			probability = probabilities[index][(int) (2*currentSample[1]+currentSample[2])];
		} else if(Nodes.R == node){
			index1 = currentSample[3] == currentSample[index] ? 1 : (int)currentSample[3];
			index2 = currentSample[4] == currentSample[index] ? 1 : (int)currentSample[4];
			probability = probabilities[index][(int) (2*index1+index2)];
		}
		return probability;
	}
}
