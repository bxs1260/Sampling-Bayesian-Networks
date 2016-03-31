import java.util.Arrays;

public class GibbsSampling {
	/*
	 * currentSample[0] --> F
	 * currentSample[1] --> R
	 * currentSample[2] --> W
	 * currentSample[3] --> S
	 */
	static int []currentSample = new int[4];
	
	final static int NO_OF_SAMPLES = 1000;
	static int favourableCount = 0;
	
	
	final static double []fR = {0.4, 0.8}; //P(F) [when r is 0, when r is 1]
	final static double []rFW = {0.048, 0.4, 0.23, 0.8}; //P(R) = [F0W0, F0W1, F1W0, F1W1]
	final static double []wRS = {0.19, 0.83, 0.76, 0.98}; //P(W) = [R0S0, R0S1, R1S0, R1S1]
	final static double []sW = {0.13, 0.75}; //P(S) = [W0, W1]
	
	private static int sample(double probability){
		double rand = Math.random();
		if( rand < probability){
			return 1;
		} else {
			return 0;
		}
	}
	
	private static void sampleF(){
		if(currentSample[1] == 1){
			currentSample[0] = sample(fR[1]);
		} else {
			currentSample[0] = sample(fR[0]);
		}
	}
	
	private static void sampleR(){
		if(currentSample[0] == 0 && currentSample[3] == 0){
			currentSample[1] = sample(rFW[0]);
		} else if(currentSample[0] == 0 && currentSample[3] == 1){
			currentSample[1] = sample(rFW[1]);
		} else if(currentSample[0] == 1 && currentSample[3] == 0){
			currentSample[1] = sample(rFW[2]);
		} else {
			currentSample[1] = sample(rFW[3]);
		}
	}
	
	private static void sampleW(){
		if(currentSample[1] == 0 && currentSample[3] == 0){
			currentSample[2] = sample(wRS[0]);
		} else if(currentSample[1] == 0 && currentSample[3] == 1){
			currentSample[2] = sample(wRS[1]);
		} else if(currentSample[1] == 1 && currentSample[3] == 0){
			currentSample[2] = sample(wRS[2]);
		} else {
			currentSample[2] = sample(wRS[3]);
		}
	}
	
	private static void sampleS(){
		if(currentSample[2] == 0){
			currentSample[3] = sample(sW[0]);
		} else {
			currentSample[3] = sample(sW[1]);
		}
	}
	
	private static void countFavourableSamples(){
		if(currentSample[0] == 1 && currentSample[3] == 0)
			++favourableCount;
	}
	
	public static void main(String[] args) {
		for(int i=0; i<4;++i)
			currentSample[i] = sample(0.5);
		for(int i=0; i<250;++i){
			sampleF();
			countFavourableSamples();
			System.out.println(Arrays.toString(currentSample));
			
			sampleR();
			countFavourableSamples();
			System.out.println(Arrays.toString(currentSample));
			sampleW();
			countFavourableSamples();
			System.out.println(Arrays.toString(currentSample));
			sampleS();
			countFavourableSamples();
			System.out.println(Arrays.toString(currentSample));
		}
		
		System.out.println("Probability(F^~S/ Ru) = "+(double) favourableCount/NO_OF_SAMPLES);
		
	}
}
