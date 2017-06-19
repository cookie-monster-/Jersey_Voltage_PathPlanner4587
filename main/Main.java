package main;

import java.io.FileWriter;
import java.io.IOException;


public class Main {
	// Get everything set up
	
	public static void main(String[] args){
	    Circles c = new Circles();
		//c.drawCirclePath("testPath", 90);
		Lines2 l = new Lines2();
		l.drawStraightPath("testPath", 20.9,5.0,0.0);
		Arcs a = new Arcs();
		//a.drawArcPath("testPath",90, 5);
	}
}
