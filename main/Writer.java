package main;

import java.awt.List;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Writer {
	private static ArrayList<Double[]> leftAccs = new ArrayList<Double[]>();
	//private double[] leftAccs = new double[1];
	public void addLeftAcc(Double[] acc){
		leftAccs.add(acc);
	}
	
	private static ArrayList<Double[]> rightAccs = new ArrayList<Double[]>();
	public void addRightAcc(Double[] acc){
		rightAccs.add(acc);
	}
	public void printArrays(){
		System.out.println(leftAccs);
		for(int x =0;x<leftAccs.size();x++){
			Double[] q = leftAccs.get(x);
			System.out.println(q[0]+","+q[1]);
		}
		for(int x =0;x<rightAccs.size();x++){
			Double[] q = rightAccs.get(x);
			System.out.println(q[0]+","+q[1]);
		}
	}
	
	public void writeFile(String filename){
		double lineNum = leftAccs.size();
		String filepath="C:/Users/Drew/Desktop/pathGui/"+filename+".txt";
		FileWriter m_writer;
		try {
			m_writer = new FileWriter(filepath, false);
			m_writer.write(filename + "\n" + ((int)lineNum+1) + "\n");
		} catch ( IOException e ) {
			System.out.println(e);
			m_writer = null;
		}
		double acc;
		double velNow;
		double velLast=0;
		double posNow;
		double posLast=0;
		double timeStep = Constants.TIMESTEP;
		double wheelbase = Constants.WHEELBASE;
		double x=0;
		double y=0;
		double radDelta;
		double radians=0;
		double radius;

	    y=-wheelbase/24;//-1.125;
	    x=0.0;
	    double startX=x;
	    double startY=y;
	    double startAngle=0.0;
		for(int line = -1;line < lineNum;line++){
			if(line >= 0){
				Double[] accVel = leftAccs.get(line);
				acc=accVel[0];
				velNow = accVel[1];
				radDelta = accVel[2];	
				radius = accVel[3];
				radians+=radDelta;
					//System.out.println("acc: "+acc+" vel: "+velNow);
					posNow = posLast + (velLast + velNow)/2 * timeStep;
					//radians = posNow*24/wheelbase;//360/(wheelbase*Math.PI)/4.77464829275769;//magic num??????
					//x+=Math.sin(radians)*(posNow-posLast);
					//y+=Math.cos(radians)*(posNow-posLast);
					
					//http://rossum.sourceforge.net/papers/CalculationsForRobotics/CirclePath.htm
					//double startAngle = -Math.PI/2;//Math.PI/2;
					//x=-wheelbase/24 - wheelbase/24*Math.sin(startAngle)+wheelbase/24*Math.sin((radians/(line*0.02-0.0))*(line*0.02-0.0)+startAngle);
					//y=0.0 - wheelbase/24*Math.cos(startAngle)+wheelbase/24*Math.cos((radians/(line*0.02-0.0))*(line*0.02-0.0)+startAngle);
					//x = posNow;
					//y = -wheelbase/24;
					//x=Math.sin(radians)*radius;
					//y=wheelbase/24+radius-Math.cos(radians)*radius;
					double deltaPos=posNow-posLast;
					if(radius>0){
						x=startX+radius*(Math.sin(0)+Math.sin(radians));
						y=startY+radius*(Math.cos(0)-Math.cos(radians));
						startAngle=radians;
					}else{
						y+=deltaPos*(/*Math.sin(0)+*/Math.sin(radians));
						x+=deltaPos*(/*Math.cos(0)-*/Math.cos(radians));
						startX=x;
						startY=y;
					}
					velLast = velNow;
					posLast = posNow;
					
				    if(m_writer != null){try{
							m_writer.write(posNow + " "+velNow+" "+acc+" 0 "+radians+" "+timeStep+" "+x+" "+y+"\n");// jerk, x, y = 0
						}catch(Exception e){}}
		
					/*if(line+1==lineNum){//last one
						System.out.println("---right side---");
						System.out.println("posNow: "+posNow);
						System.out.println("posError: "+(totalDistance-Math.abs(posNow)));
					}*/
				
			}else{
				/*Double[] accVel = leftAccs.get(line);
				acc = accVel[0];
				velNow = accVel[1];
				velLast = velNow - acc*timeStep;*/
				 if(m_writer != null){try{
						m_writer.write("0 "+0+" 0 0 0 "+timeStep+" "+x+" "+y+"\n");//first line 0 everything
					}catch(Exception e){}}
				
			}
			
		}
	    y=wheelbase/24;//1.125;
	    x=0.0;
	    posLast=0;
	    radians=0;
	    startX=x;
	    startY=y;
	    startAngle=0.0;
		velLast=0;
		for(int line = -1;line < lineNum;line++){
			if(line >= 0){
				Double[] accVel = rightAccs.get(line);
				acc = accVel[0];
				velNow = accVel[1];
				radDelta = accVel[2];
				radius = accVel[3];
				radians+=radDelta;
				//System.out.println("acc: "+acc+" vel: "+velNow);
				posNow = posLast + (velLast + velNow)/2 * timeStep;
				//radians = posNow*24/wheelbase;//360/(wheelbase*Math.PI)/4.77464829275769;//magic num??????
				//x+=Math.sin(radians)*(posNow-posLast);
				//y+=Math.cos(radians)*(posNow-posLast);
				
				//http://rossum.sourceforge.net/papers/CalculationsForRobotics/CirclePath.htm
				//double startAngle = -Math.PI/2;//Math.PI/2;
				//x=-wheelbase/24 - wheelbase/24*Math.sin(startAngle)+wheelbase/24*Math.sin((radians/(line*0.02-0.0))*(line*0.02-0.0)+startAngle);
				//y=0.0 - wheelbase/24*Math.cos(startAngle)+wheelbase/24*Math.cos((radians/(line*0.02-0.0))*(line*0.02-0.0)+startAngle);
				//x = posNow;
				//y = wheelbase/24;
				//x=Math.sin(radians)*radius;
				//y=wheelbase/24+radius-Math.cos(radians)*radius;
				double deltaPos=posNow-posLast;
				if(radius>0){
					x=startX+radius*(Math.sin(0)+Math.sin(radians));
					y=startY+radius*(Math.cos(0)-Math.cos(radians));
					startAngle=radians;
				}else{
					y+=deltaPos*(/*Math.sin(0)+*/Math.sin(radians));
					x+=deltaPos*(/*Math.cos(0)-*/Math.cos(radians));
					startX=x;
					startY=y;
				}
				velLast = velNow;
				posLast = posNow;
				
			    if(m_writer != null){try{
						m_writer.write(posNow + " "+velNow+" "+acc+" 0 "+radians+" "+timeStep+" "+x+" "+y+"\n");// jerk, x, y = 0
					}catch(Exception e){}}
	
				/*if(line+1==lineNum){//last one
					System.out.println("---right side---");
					System.out.println("posNow: "+posNow);
					System.out.println("posError: "+(totalDistance-Math.abs(posNow)));
				}*/
			}else{
				/*Double[] accVel = rightAccs.get(line);
				acc = accVel[0];
				velNow = accVel[1];
				velLast = velNow - acc*timeStep;*/
				 if(m_writer != null){try{
						m_writer.write("0 "+0+" 0 0 0 "+timeStep+" "+x+" "+y+"\n");//first line 0 everything
					}catch(Exception e){}}
			}
		}

		try{m_writer.close();System.out.println("Wrote to "+filepath);}catch(Exception e){}
	}
}
