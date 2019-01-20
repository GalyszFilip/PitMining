package isep.ii.project.pitMining;

public class App 
{
    public static void main( String[] args )
    {
    	Factory factory = new Factory();
    	DiGraph<Cell, Integer> graph;
    	MaxFlowOperator<Cell, Integer> mO;
    	PitMiningOperator<Cell, Integer> pG;
    	Window<Cell, Integer> wind = new Window<Cell, Integer> ();
    	FileGenerator fg = new FileGenerator();
    	boolean playAgain = true;
    	
    	
    	while(playAgain) {
    		fg.generateArray();
    		graph = factory.createGraph();
    		mO = factory.createMaxFlowOperator(2);
    		pG = new PitMiningOperator<Cell, Integer>(graph, mO);
    		wind.setPitMingOperator(pG);
        	wind.playGame();
        	playAgain = wind.playAgain();	
    	}
    	
    	System.out.println("end of programme !");
		
    }
}
