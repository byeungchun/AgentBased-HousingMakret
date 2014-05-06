package san.entity;

import java.awt.Color;

import uchicago.src.sim.gui.DrawableEdge;
import uchicago.src.sim.gui.SimGraphics;
import uchicago.src.sim.network.DefaultEdge;
import uchicago.src.sim.network.Node;

public class HouseholdEdge extends DefaultEdge implements DrawableEdge{

private Color color;
	
	public HouseholdEdge(){}
	
	public HouseholdEdge(Node from, Node to, Color color){
		super(from, to, "");
		this.color = color;
	}
	
	public void setColor(Color c){
		color = c;
	}
	
	@Override
	 public void draw(SimGraphics g, int fromX, int toX, int fromY, int toY) {
		g.drawDirectedLink(color, fromX, toX, fromY, toY);
		
	}

	
}
