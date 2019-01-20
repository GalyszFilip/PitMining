package isep.ii.project.pitMining;

public class App {
	public static void main(String[] args) {

		new GenerateFile().generateArray();
		DiGraph<Cell, Integer> g = new Factory().creates("pitMining.txt");
		PitMiningOperator<Cell, Integer> p = new PitMiningOperator<Cell, Integer>(g);
		Window<Cell, Integer> wind = new Window<Cell, Integer>(p);
		wind.showInterface();
		wind.playGame();
		wind.bestSotution();
		wind.win();

		System.out.println("fin de programe!");
	}

}
