package isep.ii.project.pitMining;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Factory {

	public Factory() {
		// TODO Auto-generated constructor stub
	}
	
	public DiGraph<Cell, Integer>creates(String path) {
		DiGraph<Cell, Integer> g = new pitMiningGraph<Cell, Integer>(); 
		
		try{
    		InputStream is = new FileInputStream(new File(path));
    		BufferedReader br = new BufferedReader(new InputStreamReader(is));
    		
    		
    		int numEdge = -1;
    
    		for(int line = 0, col = 0 ; br.ready(); ++line, col = 0) {
    			String currentLine = br.readLine();
    			for(String num: currentLine.split(" ")) {
    				Cell c = new Cell2D(line, col);				
					g.addEdge(++numEdge, c, g.getVertexByName(new String(toS(line-1)+","+toS(col-1))));
					g.addEdge(++numEdge, c, g.getVertexByName(new String(toS(line-1)+","+toS(col))));
					g.addEdge(++numEdge, c, g.getVertexByName(new String(toS(line-1)+","+toS(col+1))));
					g.putProfit(c, Integer.parseInt(num));
					col++;
					//print(g.getNames().toString());
    			}
    			
    			
    		}is.close();
		}catch(FileNotFoundException E) {
			System.out.println("file not found");
			
		}catch(IOException f) {
			
		}
		return g;
		
		
	}
	
	public String toS(int i) {
		return Integer.toString(i);
	}
	
	
}
