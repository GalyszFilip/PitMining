package isep.ii.project.pitMining;

import java.awt.Font;
import java.util.Collections;
import java.util.List;

import edu.princeton.cs.introcs.StdDraw;

public class Window<Vertex, Edge> {

	String digSoundFile = "dig.wav";
	String backgroundSoundFile = "background.wav";

	double line;
	double column;

	int timeOfDigging = 3000; // time of digging in miliseconds

	int maxProfit;
	int minProfit;

	int xMax;
	int yMax;

	int xMax_Block;
	int yMax_Block;
	PitMiningOperator<Vertex, Edge> p;
	AePlayWave backgroundSound = new AePlayWave(backgroundSoundFile);

//	Thread t1 = new Thread(new Runnable() {
//		public void run() {
//			AePlayWave digSound = new AePlayWave(digSoundFile);
//			if (digSound.isAlive()) {
//				System.out.println("jest alive" + digSound.isAlive());
//			} else {
//				System.out.println("nie jest alive" + digSound.isAlive());
//				digSound.start();
//				try {
//					Thread.sleep(100);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//
//			}
//
//		}
//	});

	public Window(PitMiningOperator<Vertex, Edge> po) {
		StdDraw.setCanvasSize(1000, 900);
		StdDraw.setXscale(0, 100);
		StdDraw.setYscale(0, 90);
		xMax = 100;
		yMax = 90;
		xMax_Block = xMax;
		yMax_Block = yMax - 10;
		p = po;
		open();
		StdDraw.setFont(new Font("CENTER_BASELINE", Font.PLAIN, 20));
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

		/**
		 * for (double i = 0; i <= yMax_Block; i += yMax_Block / (double) maxLi) {
		 * StdDraw.line(0, i, xMax_Block, i); }
		 * 
		 * for (double j = 0; j <= xMax_Block; j += xMax_Block / (double) maxColumn) {
		 * StdDraw.line(j, 0, j, yMax_Block); }
		 **/

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
		try {
			StdDraw.picture((double) xMax / 16f, (double) yMax_Block + (double) (yMax - yMax_Block) / 2.0f,
					"D:\\ALGOA2\\pitMining\\gold.png", (double) xMax / 16f, (double) (yMax - yMax_Block) / 2.0f);
		} catch (IllegalArgumentException i) {
			System.out.println("File not found! ");
		}

		StdDraw.setPenColor();
		StdDraw.line((double) xMax - (double) xMax / 8f, yMax_Block + (double) (yMax - yMax_Block) / 2.0f, xMax, yMax);
		StdDraw.line(xMax, yMax_Block + (double) (yMax - yMax_Block) / 2.0f, (double) xMax - (double) xMax / 8f, yMax);

		StdDraw.show(100);
	}

	public void playGame() {
		boolean test = true;
//		AePlayWave dig = new AePlayWave(digSoundFile);
		DiGraph<Vertex, Edge> g = p.getGraph();
		while (test) {
			System.out.println((int) p.getGraph().getNames().size());
//			if (StdDraw.mousePressed() && !dig.isAlive()) {
//
//				dig.run();
//			}
			while (!StdDraw.mousePressed()) {

			}
			Double x = StdDraw.mouseX(), y = StdDraw.mouseY();

			if (!endGame(x, y)) {
				if (y <= yMax_Block) {

					int[] c = getCell(x, y);
					for (Vertex v : p.cellsToExcavate(c[0] + "," + c[1])) {
						this.excavate(g.getNameOrNullByVertex(v));
						this.updateProfit(1);
						StdDraw.show(15);
					}
				}

			} else {
				test = false;
			}

		}

	}

	public static synchronized void playSound() {

	}

	@SuppressWarnings("unchecked")
	public void bestSotution() {
		this.showInterface();
		new AePlayWave(digSoundFile).start();
		Cell src = new Cell2D(-1, -1);
		Cell t = new Cell2D(-2, -2);
		p.maxFlow((Vertex) src, (Vertex) t);
		for (Vertex v : p.getCellsMaxFlow()) {
			this.excavate(((Cell) v).getName());
			p.addProfitMf(p.getGraph().getProfit(v));
			this.updateProfit(0);
			StdDraw.show((int) (timeOfDigging / p.getCellsMaxFlow().size()));
		}
		StdDraw.show(1000);
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
		StdDraw.setPenColor(249, 244, 225);
		StdDraw.filledRectangle(x, y, xMax_Block / (2f * column), yMax_Block / (2f * line));
		return y + "," + x;
	}

	public void updateProfit(int i) {
		double y = (double) yMax_Block + (double) (yMax - yMax_Block) / 2.0f;
		double x = 3 * (double) xMax / 16f + (double) xMax / 8f;
		StdDraw.setPenColor(StdDraw.WHITE);
		StdDraw.filledRectangle(x, y, 3 * (double) xMax / 16, (double) (yMax - this.yMax_Block) / 2.0f);
		StdDraw.setPenColor();
		if (i == 1) {
			StdDraw.text(x, y, p.getSommeProfit() + "€");
		} else {
			StdDraw.text(x, y, p.getSommeProfitMf() + "€");
		}

	}

	public void open() {
		backgroundSound.start();
		StdDraw.setPenColor(200, 161, 20);
		StdDraw.filledPolygon(new double[] { 0, 0, xMax }, new double[] { 0, yMax, 0 });
		StdDraw.setPenColor(0, 0, 0);
		StdDraw.filledPolygon(new double[] { xMax, 0, xMax }, new double[] { 0, yMax, yMax });
		StdDraw.setPenColor(StdDraw.WHITE);
		StdDraw.setFont(new Font("CENTER_BASELINE", Font.ITALIC, 50));
		StdDraw.text((double) xMax / 2f, (double) yMax / 2f, "CLICK !!");

		while (!StdDraw.mousePressed()) {

		}
		double i = 0;
		while (i < ((double) yMax) / 2f) {
			StdDraw.setPenColor(200, 161, 20);
			StdDraw.filledPolygon(new double[] { 0, 0, xMax, xMax }, new double[] { 0, (double) yMax - i, i, 0 });
			StdDraw.setPenColor(0, 0, 0);
			StdDraw.filledPolygon(new double[] { xMax, 0, 0, xMax }, new double[] { i, (double) yMax - i, yMax, yMax });
			StdDraw.show(10);
			i++;
		}
		i = ((double) yMax) / 2f;
		while (i <= yMax) {
			StdDraw.setPenColor(200, 161, 20);
			StdDraw.filledPolygon(new double[] { 0, 0, xMax, xMax }, new double[] { 0, i, i, 0 });
			StdDraw.show(5);
			i++;
		}
	}

	public void win() {
		StdDraw.setPenColor(200, 161, 20);
		StdDraw.clear(StdDraw.getPenColor());
		int i = 1;
		String s = p.win() ? "Well Done !! you win " + p.getSommeProfitMf() + "€"
				: "Could have win " + (p.getSommeProfitMf() - p.getSommeProfit()) + "€ more";
		while (i != 50) {
			StdDraw.clear(StdDraw.getPenColor());
			StdDraw.setFont(new Font("CENTER_BASELINE", Font.ITALIC, i));
			StdDraw.setPenColor();
			StdDraw.text((double) xMax / 2f, (double) yMax / 2f, s);
			StdDraw.show(2);
			StdDraw.setPenColor(200, 161, 20);
			i++;
		}
		StdDraw.setFont(new Font("CENTER_BASELINE", Font.PLAIN, 20));

	}

	public boolean endGame(double x, double y) {
		if (x >= xMax - xMax / 8 && y >= (double) yMax_Block + (double) (yMax - yMax_Block) / 2.0f)
			return true;
		return false;
	}
}
