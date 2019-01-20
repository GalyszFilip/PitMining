package isep.ii.project.pitMining;
import java.awt.Font;
import java.util.Collections;
import java.util.List;
import edu.princeton.cs.introcs.StdDraw;

public class Window<Vertex, Edge> {
	private double line, column;
	private int maxProfit, minProfit;
	private int xMax, yMax;
	private int xMax_Block, yMax_Block;
	private PitMiningOperator<Vertex, Edge> p;
	private String digSoundFile = "src\\Files\\dig.wav";
	private String backgroundSoundFile = "src\\Files\\background.wav";
	private int timeOfDigging = 3000; // time of digging in miliseconds
	AePlayWave backgroundSound = new AePlayWave(backgroundSoundFile);

	public Window() {
		StdDraw.setCanvasSize(1000, 900);
		StdDraw.setXscale(0, 100);
		StdDraw.setYscale(0, 90);
		xMax = 100;
		yMax = 90;
		xMax_Block = xMax;
		yMax_Block = yMax - 10;
		open();
		StdDraw.setFont(new Font("CENTER_BASELINE", Font.PLAIN, 20));
	}
	
	public void playGame() {
		this.showInterface();
		this.pitMine();
		this.bestSotution();
		this.win();
	}
	
	public void showInterface() {
		StdDraw.clear(StdDraw.WHITE);
		DiGraph<Vertex, Edge> g = p.getGraph();
		List<String> a = g.getNames();
		int maxColumn = 0;

		for (String s : a) {
			if (s.split(",")[0].equals("0") && Integer.parseInt(s.split(",")[1]) > maxColumn) {
				maxColumn = Integer.parseInt(s.split(",")[1]);
			}
		}

		maxColumn += 1;
		int maxLi = g.getNames().size() / maxColumn;
		

		this.line = (double) maxLi;
		this.column = (double) maxColumn;
		this.maxProfit = (int) Collections.max(g.getProfits());
		this.minProfit = (int) Collections.min(g.getProfits());

		int x = 0, x2, profit;
		double degrade;

		for (double i = yMax_Block - yMax_Block / (2 * line); i >= 0; i -= yMax_Block / line, x++) {
			for (double j = xMax_Block / (2 * column); j <= xMax_Block; j += xMax_Block / column) {
				x2 = (int) (column / xMax_Block * (j - xMax_Block / (2 * column)));
				profit = g.getProfit(g.getVertexByName(new String(x + "," + x2)));
				degrade = (double) (maxProfit - profit) / (double) (maxProfit - minProfit);
				int[] color = rgb(47, 0.90f, (200 * degrade + (78 * (1 - degrade))));
				StdDraw.setPenColor(color[0], color[1], color[2]);
				StdDraw.filledRectangle(j, i, xMax_Block / (2 * column), yMax_Block / (2 * line));
				StdDraw.setPenColor(StdDraw.CYAN);

				if (g.getNames().contains(new String(x + "," + x2))) {
					StdDraw.text(j, i, profit + "");

				}
			}
		}
		try{
			StdDraw.picture((double)xMax/16f, (double)yMax_Block + (double)(yMax-yMax_Block)/2.0f, "src\\Files\\gold.png", (double)xMax/16f, (double)(yMax-yMax_Block)/2.0f);
		}catch(IllegalArgumentException i) {
			System.err.println("File not found: "+"src\\Files\\gold.png");
		}
		
		StdDraw.setPenColor();
		StdDraw.line((double)xMax-(double)xMax/8f, yMax_Block+(double)(yMax-yMax_Block)/2.0f, xMax, yMax);
		StdDraw.line(xMax, yMax_Block+(double)(yMax-yMax_Block)/2.0f, (double)xMax-(double)xMax/8f, yMax);
		
		StdDraw.show(100);
	}

	public void pitMine() {
		boolean test = true;
		DiGraph<Vertex, Edge> g = p.getGraph();
		while (test) {
			while (!StdDraw.mousePressed()) {

			}
			Double x = StdDraw.mouseX(), y = StdDraw.mouseY();
			
			if(!endMining(x,y)) {
				if(y<=yMax_Block  && y>=(double)yMax/100f 
						&& x<=(double)this.xMax && x>=(double)this.xMax/100f) {
					int[] c = getCell(x, y);
					for (Vertex v : p.cellsToExcavate(c[0] + "," + c[1])) {
						this.excavate(g.getNameOrNullByVertex(v));
						this.updateProfit(1);
						StdDraw.show(15);
					}
				}
				
			}else {
				test = false;
			}
			
		}
	}
	
	@SuppressWarnings("unchecked")
	public void bestSotution() {
		this.showInterface();
		new AePlayWave(digSoundFile).start();
		Cell src = new Cell2D(-1,-1);
		Cell t = new Cell2D(-2,-2);
		List<Vertex> cM = p.maxFlow((Vertex)src, (Vertex)t);
		for(Vertex v: cM) {
			this.excavate(((Cell)v).getName());
			p.addProfitMf(v);
			this.updateProfit(0);
			StdDraw.show((int) (timeOfDigging / cM.size()));
		}StdDraw.show(1200);
	}

	public double getLine() {
		return line;
	}

	public void setLine(double line) {
		this.line = line;
	}

	public double getColumn() {
		return column;
	}

	public void setColumn(double column) {
		this.column = column;
	}

	private int[] rgb(double h, double s, double v) {
		double ti = (double) h / 60.0;
		double f = ti - (int) ti;
		double l = v * (1 - s);
		double m = v * (1 - f * s);
		double n = v * (1 - (1 - f) * s);
		switch ((int) f) {
		case 0:
			return new int[] { (int) v, (int) n, (int) l };
		case 1:
			return new int[] { (int) m, (int) v, (int) l };
		case 2:
			return new int[] { (int) l, (int) v, (int) n };
		case 3:
			return new int[] { (int) l, (int) m, (int) v };
		case 4:
			return new int[] { (int) n, (int) l, (int) v };
		case 5:
			return new int[] { (int) v, (int) l, (int) m };
		}
		return null;

	}
	


	public int[] getCell(double x, double y) {
		int a = (int) (x / ((double) xMax_Block / column));
		int b = (int) (((double) yMax_Block - y) / ((double) yMax_Block / line));
		return new int[] { b, a };
	}

	public String excavate(String s) {
		String[] tabS = s.split(",");
		int[] coords = new int[tabS.length];
		int i = 0;
		for (String aCoord : tabS) {
			coords[i++] = Integer.parseInt(aCoord);
		}

		double x = (double) coords[1] * ((double) xMax_Block / column) + ((double) xMax_Block / (2f * column));
		double y = ((double) yMax_Block - coords[0] * (double) yMax_Block / line) - (double) yMax_Block / (2f * line);
		StdDraw.setPenColor(249,244,225);
		StdDraw.filledRectangle(x, y, xMax_Block / (2f * column), yMax_Block / (2f * line));
		return y + "," + x;
	}
	
	public void updateProfit(int i) {
		double y = (double)yMax_Block + (double)(yMax-yMax_Block)/2.0f;
		double  x = 3*(double)xMax/16f + (double)xMax/8f;
		StdDraw.setPenColor(StdDraw.WHITE);
		StdDraw.filledRectangle(x, y, 3*(double)xMax/16, (double)(yMax-this.yMax_Block)/2.0f);
		StdDraw.setPenColor();
		if(i == 1) {
			StdDraw.text(x, y, p.getSommeProfit()+"€");
		}else {
			StdDraw.text(x, y, p.getSommeProfitMf()+"€");
		}
		
	}
	
	public void open() {
		this.backgroundSound.start();
		StdDraw.setPenColor(200, 161, 20);
		StdDraw.filledPolygon(new double[] {0, 0, xMax},new double[] {0, yMax,0});
		StdDraw.setPenColor(0,0,0);
		StdDraw.filledPolygon(new double[] {xMax, 0, xMax},new double[] {0, yMax,yMax});
		StdDraw.setPenColor(StdDraw.WHITE);
		StdDraw.setFont(new Font("CENTER_BASELINE", Font.ITALIC, 50));
		StdDraw.text((double)xMax / 2f, (double)yMax /2f, "CLICK !!");

		while(!StdDraw.mousePressed()) {
			
		}double i = 0;
		while(i<((double)yMax)/2f) {
			StdDraw.setPenColor(200, 161, 20);
			StdDraw.filledPolygon(new double[] {0, 0, xMax, xMax},new double[] {0, (double)yMax-i,i,0});
			StdDraw.setPenColor(0,0,0);
			StdDraw.filledPolygon(new double[] {xMax, 0,0, xMax},new double[] {i,(double)yMax-i, yMax, yMax});
			StdDraw.show(10);
			i++;
		}
		i = ((double)yMax)/2f;
		while(i<=yMax) {
			StdDraw.setPenColor(200, 161, 20);
			StdDraw.filledPolygon(new double[] {0, 0, xMax, xMax},new double[] {0, i,i,0});
			StdDraw.show(5);
			i++;
		}
	}
	public void win() {
		StdDraw.setPenColor(200, 161, 20);
		StdDraw.clear(StdDraw.getPenColor());
		int i = 1;
		String s = p.win()?"Well Done !! you win "+p.getSommeProfitMf()+"€":"Could have win "+(p.getSommeProfitMf()-p.getSommeProfit())+"€ more";
		while(i!=50) {
			StdDraw.clear(StdDraw.getPenColor());
			StdDraw.setFont(new Font("CENTER_BASELINE", Font.ITALIC, i));
			StdDraw.setPenColor();
			StdDraw.text((double)xMax / 2f, (double)yMax /2f, s);
			StdDraw.show(2);
			StdDraw.setPenColor(200, 161, 20);
			i++;
		}StdDraw.show(3000);
		StdDraw.setFont(new Font("CENTER_BASELINE", Font.PLAIN, 20));

		
	}
	
	public boolean endMining(double x, double y) {
		if(x >= xMax-xMax/8 && y>=(double)yMax_Block + (double)(yMax-yMax_Block)/2.0f)
			return true;
		return false;
	}

	public PitMiningOperator<Vertex, Edge> getPitMingOperator() {
		return p;
	}

	public void setPitMingOperator(PitMiningOperator<Vertex, Edge> p) {
		this.p = p;
	}

	public boolean playAgain() {
		StdDraw.setPenColor(200, 161, 20);
		StdDraw.clear(StdDraw.getPenColor());
		StdDraw.setFont(new Font("CENTER_BASELINE", Font.ITALIC, 20));
		
		StdDraw.setPenColor();
		StdDraw.text((double)xMax /2, yMax-10, "Mine Again ?");
		StdDraw.rectangle(20, (double)yMax /2, 10, 10); StdDraw.text(20, (double)this.yMax /2, "YES");
		StdDraw.rectangle(xMax-20, (double)yMax /2, 10, 10); StdDraw.text(xMax - 20, (double)yMax /2, "NO");
		StdDraw.show();
		
		double x, y; 
		boolean test = true;
		
		while(test) {
			while(!StdDraw.mousePressed()) {
				
			}x = StdDraw.mouseX(); y = StdDraw.mouseY();
			if(x>=10 && x<= 30 && y>=((double)yMax /2) - 10f && y<=((double)yMax) /2 + 10f) {
				StdDraw.show(500);
				return true;
			}else {
				if(x<=xMax-10 && x>= xMax-30 && y>=((double)yMax /2) - 10f && y<=((double)yMax) /2 + 10f) 
					StdDraw.clear();
					return false;
			}
		}
		return false;
	}
	
	
	
}
