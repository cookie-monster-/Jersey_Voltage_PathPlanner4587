package main;

import java.io.FileWriter;
import java.io.IOException;

public class Lines {
	public void drawStraightPath(String filename, double totalDistance,double startVel,double endVel) {
	    String filepath = "C:/Users/Drew/Desktop/pathGui/"+filename+".txt";
		//double totalDegrees = 900;
		double acceleration = Constants.ACC_MAX;
		double velocityMax = Constants.VEL_MAX;
		double wheelbase = Constants.WHEELBASE;
		double velLast=startVel;
		double velNow;
		double posNow;
		double posLast=0.0;
		double acc;
		double totalTime=0;
		double lineNum = 0;
		double timeStep = Constants.TIMESTEP;
		double x;
		double y;
		
		//totalDistance = wheelbase * Math.PI /360 * totalDegrees / 12; // divide 12 = ft
		
		double timeOff=0;
		double testAcc=0;
		double testVel=0;
		double triTime=0;
		double doubleTriDist=0;
		double distLeftover=0;
		double timeAtMaxVel=0;

		totalTime = Math.sqrt(4*totalDistance/acceleration);
			timeOff = totalTime % timeStep;
			totalTime -= timeOff;
			if(timeOff >= timeStep/2){
				totalTime+=timeStep;
			}
			lineNum = totalTime / timeStep + 1;// +1 = line of 0's
			if (lineNum%1>0){
				if(lineNum%1>=0.5){
					lineNum+=1;
				}
				lineNum-=lineNum%1;
			}
			testAcc = (4*totalDistance)/(totalTime*totalTime);
			acceleration = testAcc;
			testVel = acceleration*((lineNum-1)/2)*timeStep;
			double triSteps=0;
			if(testVel>velocityMax){
				triTime = (velocityMax/acceleration);///timeStep;
				triSteps = triTime/timeStep;
				double triTimeOff = triSteps%1;
				triSteps -= triTimeOff;
				if(triTimeOff>=0.5){
					triSteps+=1;
				}
				triTime = triSteps*timeStep;
				doubleTriDist = triTime*velocityMax;
				
				testAcc = (doubleTriDist)/(triTime*triTime);
				acceleration = testAcc;
				
				distLeftover = totalDistance - doubleTriDist;
				timeAtMaxVel = distLeftover/velocityMax;
				totalTime = (triTime*2)+timeAtMaxVel;
				lineNum = totalTime / timeStep + 1;// +1 = line of 0's
				if (lineNum%1>0){
					if(lineNum%1>=0.5){
						lineNum+=1;
					}
					lineNum-=lineNum%1;
				}
			}
		System.out.println("testVel: "+testVel+" overMaxVel? "+(testVel>velocityMax)+" steps: "+triSteps+" doubleTriDist: "+doubleTriDist+" timeAtMaxVel: "+timeAtMaxVel);
		System.out.println("distLeftover: "+distLeftover);
		System.out.println("dist: "+totalDistance+" time: "+totalTime+" timeOff: "+timeOff+" lineNum: "+lineNum+" testAcc: "+testAcc);
		FileWriter m_writer;
	    try {
			m_writer = new FileWriter(filepath, false);
			m_writer.write(filename + "\n" + (int)lineNum + "\n");
		} catch ( IOException e ) {
			System.out.println(e);
			m_writer = null;
		}
	    
	    //first side
	    y=-wheelbase/24;//-1.125;
	    x=0.0;
		for(int line = 0;line < lineNum;line++){
			if(line != 0){
				acc = findAcc(lineNum,line,triSteps,acceleration);
				/*if (line<(lineNum-1)/2){
					acc=acceleration;
				}else if(line>(lineNum-1)/2){
					acc=-acceleration;
				}else{
					acc=3;
				}*/
				velNow = velLast + acc * timeStep;
				posNow = posLast + (velLast + velNow)/2 * timeStep;
				//radians = posNow*24/wheelbase;//360/(wheelbase*Math.PI)/4.77464829275769;//magic num??????
				//x+=Math.sin(radians)*(posNow-posLast);
				//y+=Math.cos(radians)*(posNow-posLast);
				
				//http://rossum.sourceforge.net/papers/CalculationsForRobotics/CirclePath.htm
				double startAngle = -Math.PI/2;//Math.PI/2;
				//x=-wheelbase/24 - wheelbase/24*Math.sin(startAngle)+wheelbase/24*Math.sin((radians/(line*0.02-0.0))*(line*0.02-0.0)+startAngle);
				//y=0.0 - wheelbase/24*Math.cos(startAngle)+wheelbase/24*Math.cos((radians/(line*0.02-0.0))*(line*0.02-0.0)+startAngle);
				x = posNow;
				y = -wheelbase/24;
				velLast = velNow;
				posLast = posNow;
				
			    if(m_writer != null){try{
						m_writer.write(posNow + " "+velNow+" "+acc+" 0 "+0+" "+timeStep+" "+x+" "+y+"\n");// jerk, x, y = 0
					}catch(Exception e){}}
	
				if(line+1==lineNum){//last one
					System.out.println("left side");
					System.out.println("posNow: "+posNow);
					System.out.println("posError: "+(totalDistance-Math.abs(posNow)));
				}
			}else{
				 if(m_writer != null){try{
						m_writer.write("0 0 0 0 0 "+timeStep+" "+x+" "+y+"\n");//first line 0 everything
					}catch(Exception e){}}
			}
		}
		//second side
	    y=wheelbase/24;//1.125;
	    x=0.0;
		posLast =0.0;
		velLast=0.0;
		for(int line = 0;line < lineNum;line++){
			if(line != 0){
				acc = findAcc(lineNum,line,triSteps,acceleration);
				/*if (line<(lineNum-1)/2){
					acc=acceleration;
				}else if(line>(lineNum-1)/2){
					acc=-acceleration;
				}else{
					acc=3;
				}*/
				velNow = velLast + acc * timeStep;
				posNow = posLast + (velLast + velNow)/2 * timeStep;
				//radians = posNow*24/wheelbase;//360/(wheelbase*Math.PI)/4.77464829275769;//magic num??????
				//x+=Math.sin(radians)*(posNow-posLast);
				//y+=Math.cos(radians)*(posNow-posLast);
				
				//http://rossum.sourceforge.net/papers/CalculationsForRobotics/CirclePath.htm
				double startAngle = -Math.PI/2;//Math.PI/2;
				//x=-wheelbase/24 - wheelbase/24*Math.sin(startAngle)+wheelbase/24*Math.sin((radians/(line*0.02-0.0))*(line*0.02-0.0)+startAngle);
				//y=0.0 - wheelbase/24*Math.cos(startAngle)+wheelbase/24*Math.cos((radians/(line*0.02-0.0))*(line*0.02-0.0)+startAngle);
				x = posNow;
				y = wheelbase/24;
				velLast = velNow;
				posLast = posNow;
				
			    if(m_writer != null){try{
						m_writer.write(posNow + " "+velNow+" "+acc+" 0 "+0+" "+timeStep+" "+x+" "+y+"\n");// jerk, x, y = 0
					}catch(Exception e){}}
	
				if(line+1==lineNum){//last one
					System.out.println("left side");
					System.out.println("posNow: "+posNow);
					System.out.println("posError: "+(totalDistance-Math.abs(posNow)));
				}
			}else{
				 if(m_writer != null){try{
						m_writer.write("0 0 0 0 0 "+timeStep+" "+x+" "+y+"\n");//first line 0 everything
					}catch(Exception e){}}
			}
		}
		
		try{m_writer.close();System.out.println("Wrote to "+filepath);}catch(Exception e){}
	}
	
	private double findAcc(double lineNum, double line, double triSteps,double acceleration){
		double acc;
		if(triSteps>0){
			if(lineNum%2==0){
				if (line<triSteps+1){
					acc=acceleration;
				}else if(line>/*lineNum-*/triSteps){
					acc=-acceleration;
				}else{
					acc=0;
				}
			}else{
				if (line<=triSteps){
					acc=acceleration;
				}else if(line>lineNum-triSteps-1){
					acc=-acceleration;
				}else{
					acc=0;
				}
			}
		}else{
			if(lineNum%2==0){
				if (line<lineNum/2){
					acc=acceleration;
				}else if(line>lineNum/2){
					acc=-acceleration;
				}else{
					acc=0;//0
				}
			}else{
				if(line==lineNum/2+0.5){
					acc=-acceleration;//0
				}else if (line<lineNum/2){
					acc=acceleration;
				}else{//line>lineNum
					acc=-acceleration;
				}
			}
		}
		return acc;
	}
}
