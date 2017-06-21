package main;

import java.io.FileWriter;
import java.io.IOException;


public class Main {
	// Get everything set up
	public static boolean firstStep = false;
	public static void main(String[] args){
	    Circles c = new Circles();
		//c.drawCirclePath("testPath", 90);
		Lines2 l = new Lines2();
		Arcs2 a = new Arcs2();
		
		
		l.drawStraightPath("testPath", 2.0,0.0,2.0);
		a.drawArcPath("testPath", 90, 10, 2.0, 2.0);
		l.drawStraightPath("testPath", 3.0,2.0,0.0);
		
		
		Writer w = new Writer();
		//w.printArrays();
		w.writeFile("banana");
	}
}
