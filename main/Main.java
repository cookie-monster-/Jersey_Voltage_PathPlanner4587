package main;

public class Main {
	// Get everything set up
	public static void main(String[] args){
	    //Circles c = new Circles();
		//c.drawCirclePath("testPath", 90);
		
		Lines l = new Lines();
		Arcs a = new Arcs();
		
		/* 
		 * Draw lines format = l.drawStraightPath(distance(ft), startVelocity(ft/s), endVelocity(ft/s));
		 * 
		 * Draw arcs format = a.drawArcPath(angle(degrees), circleRadius(ft), startVelocity(ft/s), endVelocity(ft/s));
		 * the code will determine the distance needed based on the radius of the circle and how many degrees along the circle you want to turn
		 * 
		 * You shouldn't ever need to change the start or end velocities once the path is set up, just tweak the distances,
		 * but if you do, it is possible to create a "bad" path, ex: we can't go from 0 to 10 ft/s in 6 inches,
		 * if the path doesn't work, there will be an error in the console that looks something like "ERROR: Bad Path"
		 * if that happens you need to lower either the start or end velocity, or both 
		 * (technically you could also increase distance, but you probably don't want to because that would mess up the path)
		*/
		
		//these 3 lines are for a side gear auto, +60 degrees for left side gear -60 for right
		
		//l.drawStraightPath(3.25,0.0,2.5);
		//a.drawArcPath(60, 7.2, 2.5, 0.5);
		
		//a.drawArcPath(-120, -7.2, 2.5, 0.5);
		l.drawStraightPath(-2.0,0.0,0.0);
		//l.drawStraightPath(10.0,0.0,0.0);
		//l.drawStraightPath(0.5,0.5,0.0);
//1678 auto
		//l.drawStraightPath(6.7, 0.0, 0.0);
		
		//a.drawArcPath(95, -3.5, 0.0, 0.5);
		//l.drawStraightPath(-3.5, 0.5, 0.0);
		
		//l.drawStraightPath(25.0, 0.0, 0.0);
		/*
		a.drawArcPath(90, -5, 0, 1.0);
		l.drawStraightPath(-10, 1.0, 1.0);
		a.drawArcPath(45, -5, 1.0, 1.0);
		l.drawStraightPath(-15, 1.0, 0.0);
		*/
//hopperPath
		//a.drawArcPath(-51.5, 5, 0, 3.0);
		//l.drawStraightPath(3.8, 3.0, 0.0);
		/*
		 * you can write multiple files that are exactly the same by calling w.writeFile multiple times with different filenames
		 * one file ("testPath") will be the file that the path visualizer uses
		 * The other file that you write will be named based on what it's used for and then that one will be copied onto the robot.  
		 * ex: hopperPath, sideGearPath, centerGearPath
		 * The filepath where these files will be located is found in Constants.java
		 */
		Writer w = new Writer();
		//w.printArrays();
		w.writeFile("testPath");
		w.writeFile("sideGearDownfieldPath0");
	}
}
