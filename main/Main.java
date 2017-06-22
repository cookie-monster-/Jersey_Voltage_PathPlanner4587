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
		
		
		l.drawStraightPath(3.7916666665,0.0,2.5);//3.875
		a.drawArcPath(60, 7.125, 2.5, 0.5);
		l.drawStraightPath(0.04166666667,0.5,0.0);
		
		
		Writer w = new Writer();
		//w.printArrays();
		w.writeFile("testPath");
	}
}
