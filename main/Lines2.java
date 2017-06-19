package main;

import java.io.FileWriter;
import java.io.IOException;

public class Lines2 {
	double acc1;
	double acc2;
	double acc3;
	double acc1Lines;
	double acc2Lines;
	double acc3Lines;
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
		double timeStep = Constants.TIMESTEP;
		double x;
		double y;
		
		//totalDistance = wheelbase * Math.PI /360 * totalDegrees / 12; // divide 12 = ft
		
		double timeOff=0;
		double trapTestAcc=0;
		double trapTestVel=0;
		double triTime=0;
		double doubleTriDist=0;
		double distLeftover=0;
		double timeAtMaxVel=0;
		

		double totalTime=0;
		double totalLineNum = 0;
		double trapDist=0;
		double trapTime=0;
		double trapLineNum=0;
		
		double startTriTime=0;
		double endTriTime=0;
		double startTriDist=0;
		double endTriDist=0;
		double startTriTestAcc=0;
		double endTriTestAcc=0;
		double startTriLineNum=0;
		double endTriLineNum=0;
		double hexDist=0;
		/*if(startVel>0){
			ghostTime=startVel/acceleration;
			ghostDist=ghostTime*startVel/2;
			System.out.println("ghostTime: "+ghostTime+" ghostDist: "+ghostDist+" totalDistance(no ghost): "+totalDistance);
			totalDistance+=ghostDist;
		}*/


		if(startVel>endVel){
			//find endVel triangle
			endTriTime=startVel/acceleration;
			endTriTime=findTime(endTriTime);
			endTriLineNum=endTriTime/timeStep;
			endTriDist=endTriTime*startVel/2;
			endTriTestAcc = -(2*endTriDist)/(endTriTime*endTriTime);
			System.out.println("endTriTime: "+endTriTime+" endTriDist: "+endTriDist+" endTriTestAcc: "+endTriTestAcc+" totalDistance: "+totalDistance);
			trapDist=totalDistance-endTriDist;
			acc3 = endTriTestAcc;
			acc3Lines = endTriLineNum;
		}else if(endVel>startVel){
			//find startVel triangle
			startTriTime=endVel/acceleration;
			startTriTime=findTime(startTriTime);
			startTriLineNum=startTriTime/timeStep;
			startTriDist=startTriTime*endVel/2;
			startTriTestAcc = (2*startTriDist)/(startTriTime*startTriTime);
			System.out.println("startTriTime: "+startTriTime+" startTriLineNum: "+startTriLineNum+" startTriDist: "+startTriDist+" startTriTestAcc: "+startTriTestAcc+" totalDistance: "+totalDistance);
			trapDist=totalDistance-startTriDist;
			acc1 = startTriTestAcc;
			acc1Lines = startTriLineNum;
		}else if(endVel==startVel&&startVel>0){
			//both = and > 0
		}else{
			//both 0
			trapDist=totalDistance;
			hexDist=trapDist;
			trapTime = Math.sqrt(4*trapDist/acceleration);
			trapTime = findTime(trapTime);
			trapLineNum = trapTime/timeStep;
			trapTestAcc = (4*trapDist)/(trapTime*trapTime);
			acc1=trapTestAcc;
			acc2=-trapTestAcc;
			acc1Lines=trapLineNum/2;
			acc2Lines=trapLineNum/2;
			System.out.println("tLines: "+trapLineNum+" acc1: "+acc1+" acc2: "+acc2);
		}
		double ghostTestVel;
		if(trapDist<totalDistance){
			if(startVel>endVel){
				//real endTri, ghostStartTri
				totalTime = Math.sqrt(4*(totalDistance+endTriDist)/acceleration);
				totalTime = findTime(totalTime);
				totalLineNum = findLineNum(totalTime);
				
				boolean goodTrap=false;
				boolean forwards=true;
				boolean backwards=false;
				int i=-1;
				while(goodTrap==false){
					System.out.println("hi");
					double lastError=9999999999.0;
					int lastI=0;
					while(forwards){
						i++;
						trapTime=totalTime-(endTriTime*2)-(i*timeStep);
						trapTime = findTime(trapTime);
						trapLineNum = trapTime/timeStep;
						trapDist=(totalDistance-endTriDist)-(trapTime*startVel);
						if(trapTime%0.04>=0.019){
							trapTestAcc = trapDist/(((trapTime-0.02)/2)*((trapTime+0.02)/2));
							trapTestVel=trapTestAcc*(trapLineNum-1)/2*timeStep;
						}else{
							trapTestAcc=(4*trapDist)/(trapTime*trapTime);
							trapTestVel=trapTestAcc*(trapLineNum)/2*timeStep;
						}
						double nowError = Math.abs(trapTestAcc-acceleration);
						if(nowError>lastError){
							forwards=false;
							backwards=true;
							lastError=9999999999.0;
							lastI=i;
							i=-1;
						}
						lastError=nowError;
					}
					int secondLastI=0;
					while(backwards){
						i++;
						trapTime=totalTime-(endTriTime*2)-(lastI*timeStep)+(i*timeStep);
						trapTime = findTime(trapTime);
						trapLineNum = trapTime/timeStep;
						trapDist=(totalDistance-endTriDist)-(trapTime*startVel);
						if(trapTime%0.04>=0.019){
							trapTestAcc = trapDist/(((trapTime-0.02)/2)*((trapTime+0.02)/2));
							trapTestVel=trapTestAcc*(trapLineNum-1)/2*timeStep;
						}else{
							trapTestAcc=(4*trapDist)/(trapTime*trapTime);
							trapTestVel=trapTestAcc*(trapLineNum)/2*timeStep;
						}
						double nowError = Math.abs(trapTestAcc-acceleration);
						if(nowError>lastError){
							forwards=false;
							backwards=false;
							secondLastI=i;
						}
						lastError=nowError;
					}
					int finalI = secondLastI-lastI-1;
					trapTime=totalTime-(endTriTime*2)+(finalI*timeStep);
					trapTime = findTime(trapTime);
					trapLineNum = trapTime/timeStep;
					trapDist=(totalDistance-endTriDist)-(trapTime*startVel);
					if(trapTime%0.04>=0.019){
						trapTestAcc = trapDist/(((trapTime-0.02)/2)*((trapTime+0.02)/2));
						trapTestVel=trapTestAcc*(trapLineNum-1)/2*timeStep;
					}else{
						trapTestAcc=(4*trapDist)/(trapTime*trapTime);
						trapTestVel=trapTestAcc*(trapLineNum)/2*timeStep;
					}
					goodTrap=true;
				}
				double guessTrapDist=trapTestVel*trapTime;
				hexDist=trapDist+(trapTime*startVel);
				System.out.println("totalDist: "+totalDistance+" trapDist: "+trapDist+" triDist: "+endTriDist+" rectDist: "+(trapTime*startVel)+" hexDist: "+hexDist);
				//ghostTestVel = endTriTestAcc*(totalLineNum-1)/2*timeStep;
				ghostTestVel=trapTestVel+startVel;
				double guessDist=guessTrapDist+endTriDist+(trapTime*startVel);
				System.out.println("ghostTestVel: "+ghostTestVel+" guessDist: "+guessDist+" trapTime: "+trapTime+" topTrapDist: "+trapDist+" trapTestAcc: "+trapTestAcc+
						"\nguessTrapDist: "+guessTrapDist+" totalTime: "+totalTime+" triTime: "+endTriTime+" trapTestVel: "+trapTestVel);
				acc1=Math.abs(trapTestAcc);
				acc1Lines=trapTime/2/0.02;
				acc2=-Math.abs(trapTestAcc);
				acc2Lines=trapTime/2/0.02;
				if(ghostTestVel>velocityMax){
					//trapezoid path
					double newVelocityMax;
					
					double trapTriTime = (velocityMax-startVel)/acceleration;///timeStep;
					double trapTriSteps = trapTriTime/timeStep;
					double trapTriTimeOff = trapTriSteps%1;
					trapTriSteps -= trapTriTimeOff;
					if(trapTriTimeOff>=0.5){
						trapTriSteps+=1;
					}
					trapTriTime = trapTriSteps*timeStep;
					double trapDoubleTriDist = trapTriTime*(velocityMax-startVel);
					
					double trapTriTestAcc = trapDoubleTriDist/(trapTriTime*trapTriTime);
					//acceleration = trapTriTestAcc;
					
					double trapDistLeftover = hexDist - (trapDoubleTriDist+(trapTriTime*startVel));
					System.out.println("left: "+trapDistLeftover+" tri: "+trapDoubleTriDist);
					double trapTimeAtMaxVel = trapDistLeftover/velocityMax;
					double trapTotalTime = (trapTriTime)+trapTimeAtMaxVel;
					double trapLineNum2 = trapTotalTime / timeStep;// + 1;// +1 = line of 0's
					System.out.println("trapLineNum(unadjusted): "+trapLineNum2);
					if (trapLineNum2%1>0){
						if(trapLineNum2%1>=0.5){
							trapLineNum2+=1;
						}
						trapLineNum2-=trapLineNum2%1;
					}
					trapTimeAtMaxVel = (trapLineNum2-(trapTriSteps))*timeStep;
					trapTotalTime = (trapTriTime)+trapTimeAtMaxVel;
					double rectDist = trapTotalTime*startVel;
					double TRAPDIST = hexDist-rectDist;
					double trapTestMaxVel=TRAPDIST/(trapTotalTime-(trapTriTime/2));
					double trapTestDist = (trapTimeAtMaxVel+(trapTriTime/2))*trapTestMaxVel;
					double hexTestDist = trapTestDist+(trapTotalTime*startVel);
					System.out.println("trapTime: "+trapTotalTime+" trapLineNum: "+trapLineNum2+" trapTestDist: "+trapTestDist+" trapRealDist: "+trapDist+" hexTestDist: "+hexTestDist+" hexOff: "+(hexDist-hexTestDist));
					System.out.println("triTime: "+trapTriTime+" timeAtMax: "+trapTimeAtMaxVel);
					//change max velocity to get exact distance
					trapTestDist=(trapTimeAtMaxVel+(trapTriTime/2))*trapTestMaxVel;
					System.out.println("adjustedMaxVel: "+trapTestMaxVel+" adjustedTrapDist: "+trapTestDist+" trapDoubleTriDist: "+trapDoubleTriDist+" trapTriTestAcc: "+trapTriTestAcc);
					trapDoubleTriDist = trapTriTime*(trapTestMaxVel-startVel);
					trapTriTestAcc = (trapDoubleTriDist)/(trapTriTime*trapTriTime);
					System.out.println("trapDoubleTriDist: "+trapDoubleTriDist+" trapTriTestAcc: "+trapTriTestAcc+" "+(trapTimeAtMaxVel*trapTestMaxVel)+" = "+(trapTestDist-trapDoubleTriDist));
				}else{
					//triangle path
					
				}
			}else{
				//real startTri, ghostEndTri
				//trapDist=trapDist-(*endVel);
				ghostTestVel = startTriTestAcc*(totalLineNum-1)/2*timeStep;
				double guessDist=ghostTestVel*(totalTime-startTriTime);
				System.out.println("guessDist: "+guessDist);
				if(ghostTestVel>velocityMax){
					//trapezoid path
					
				}else{
					//triangle path
					
				}
				//real endTri, ghostStartTri
				totalTime = Math.sqrt(4*(totalDistance+startTriDist)/acceleration);
				totalTime = findTime(totalTime);
				totalLineNum = findLineNum(totalTime);
				
				boolean goodTrap=false;
				boolean forwards=true;
				boolean backwards=false;
				int i=-1;
				while(goodTrap==false){
					double lastError=9999999999.0;
					int lastI=0;
					while(forwards){
						i++;
						trapTime=totalTime-(startTriTime*2)-(i*timeStep);
						trapTime = findTime(trapTime);
						trapLineNum = trapTime/timeStep;
						trapDist=(totalDistance-startTriDist)-(trapTime*endVel);
						if(trapTime%(timeStep*2)>=(timeStep*0.99)){
							trapTestAcc = trapDist/(((trapTime-timeStep)/2)*((trapTime+timeStep)/2));
							trapTestVel=trapTestAcc*(trapLineNum-1)/2*timeStep;
						}else{
							trapTestAcc=(4*trapDist)/(trapTime*trapTime);
							trapTestVel=trapTestAcc*(trapLineNum)/2*timeStep;
						}
						double nowError = Math.abs(trapTestAcc-acceleration);
						if(nowError>lastError){
							forwards=false;
							backwards=true;
							lastError=9999999999.0;
							lastI=i;
							i=-1;
						}
						lastError=nowError;
						System.out.println("tAcc: "+trapTestAcc+" tVel: "+trapTestVel+" tTime: "+trapTime+" tDist: "+trapDist);
					}
					int secondLastI=0;
					while(backwards){
						i++;
						trapTime=totalTime-(startTriTime*2)-(lastI*timeStep)+(i*timeStep);
						trapTime = findTime(trapTime);
						trapLineNum = trapTime/timeStep;
						trapDist=(totalDistance-startTriDist)-(trapTime*endVel);
						if(trapTime%(timeStep*2)>=timeStep*0.99){
							System.out.println("oddddd");
							trapTestAcc = trapDist/(((trapTime-timeStep)/2)*((trapTime+timeStep)/2));
							trapTestVel=trapTestAcc*(trapLineNum-1)/2*timeStep;
						}else{
							trapTestAcc=(4*trapDist)/(trapTime*trapTime);
							trapTestVel=trapTestAcc*(trapLineNum)/2*timeStep;
						}
						double nowError = Math.abs(trapTestAcc-acceleration);
						if(nowError>lastError){
							forwards=false;
							backwards=false;
							secondLastI=i;
						}
						lastError=nowError;
						System.out.println("tAcc: "+trapTestAcc+" tVel: "+trapTestVel+" tTime: "+trapTime+" tDist: "+trapDist);
					}
					int finalI = secondLastI-lastI-1;
					trapTime=totalTime-(startTriTime*2)+(finalI*timeStep);
					trapTime = findTime(trapTime);
					trapLineNum = trapTime/timeStep;
					trapDist=(totalDistance-startTriDist)-(trapTime*endVel);
					if(trapTime%(timeStep*2)>=timeStep*0.99){
						trapTestAcc = trapDist/(((trapTime-timeStep)/2)*((trapTime+timeStep)/2));
						trapTestVel=trapTestAcc*(trapLineNum-1)/2*timeStep;
					}else{
						trapTestAcc=(4*trapDist)/(trapTime*trapTime);
						trapTestVel=trapTestAcc*(trapLineNum)/2*timeStep;
					}
					goodTrap=true;
					System.out.println("tAcc: "+trapTestAcc+" tVel: "+trapTestVel+" tTime: "+trapTime+" tDist: "+trapDist);
				}
				double guessTrapDist=trapTestVel*trapTime;
				hexDist=trapDist+(trapTime*endVel);
				System.out.println("totalDist: "+totalDistance+" trapDist: "+trapDist+" triDist: "+startTriDist+" rectDist: "+(trapTime*endVel)+" hexDist: "+hexDist);
				//ghostTestVel = endTriTestAcc*(totalLineNum-1)/2*timeStep;
				ghostTestVel=trapTestVel+endVel;
				guessDist=guessTrapDist+endTriDist+(trapTime*endVel);
				System.out.println("ghostTestVel: "+ghostTestVel+" guessDist: "+guessDist+" trapTime: "+trapTime+" topTrapDist: "+trapDist+" trapTestAcc: "+trapTestAcc+
						"\nguessTrapDist: "+guessTrapDist+" totalTime: "+totalTime+" triTime: "+startTriTime+" trapTestVel: "+trapTestVel);
				//acc2=Math.abs(trapTestAcc);
				acc2=trapTestAcc;
				acc2Lines=trapTime/2/0.02;
				//acc3=-Math.abs(trapTestAcc);
				acc3=-trapTestAcc;
				acc3Lines=trapTime/2/0.02;
				if(ghostTestVel>velocityMax){
					//trapezoid path
					double newVelocityMax;
					
					double trapTriTime = (velocityMax-endVel)/acceleration;///timeStep;
					double trapTriSteps = trapTriTime/timeStep;
					double trapTriTimeOff = trapTriSteps%1;
					trapTriSteps -= trapTriTimeOff;
					if(trapTriTimeOff>=0.5){
						trapTriSteps+=1;
					}
					trapTriTime = trapTriSteps*timeStep;
					double trapDoubleTriDist = trapTriTime*(velocityMax-endVel);
					
					double trapTriTestAcc = trapDoubleTriDist/(trapTriTime*trapTriTime);
					//acceleration = trapTriTestAcc;
					
					double trapDistLeftover = hexDist - (trapDoubleTriDist+(trapTriTime*endVel));
					System.out.println("left: "+trapDistLeftover+" tri: "+trapDoubleTriDist);
					double trapTimeAtMaxVel = trapDistLeftover/velocityMax;
					double trapTotalTime = (trapTriTime)+trapTimeAtMaxVel;
					double trapLineNum2 = trapTotalTime / timeStep;// + 1;// +1 = line of 0's
					System.out.println("trapLineNum(unadjusted): "+trapLineNum2);
					if (trapLineNum2%1>0){
						if(trapLineNum2%1>=0.5){
							trapLineNum2+=1;
						}
						trapLineNum2-=trapLineNum2%1;
					}
					trapTimeAtMaxVel = (trapLineNum2-(trapTriSteps))*timeStep;
					trapTotalTime = (trapTriTime)+trapTimeAtMaxVel;
					double rectDist = trapTotalTime*endVel;
					double TRAPDIST = hexDist-rectDist;
					double trapTestMaxVel=TRAPDIST/(trapTotalTime-(trapTriTime/2));
					double trapTestDist = (trapTimeAtMaxVel+(trapTriTime/2))*trapTestMaxVel;
					double hexTestDist = trapTestDist+(trapTotalTime*endVel);
					System.out.println("trapTime: "+trapTotalTime+" trapLineNum: "+trapLineNum2+" trapTestDist: "+trapTestDist+" trapRealDist: "+trapDist+" hexTestDist: "+hexTestDist+" hexOff: "+(hexDist-hexTestDist));
					System.out.println("triTime: "+trapTriTime+" timeAtMax: "+trapTimeAtMaxVel);
					//change max velocity to get exact distance
					trapTestDist=(trapTimeAtMaxVel+(trapTriTime/2))*trapTestMaxVel;
					System.out.println("adjustedMaxVel: "+trapTestMaxVel+" adjustedTrapDist: "+trapTestDist+" trapDoubleTriDist: "+trapDoubleTriDist+" trapTriTestAcc: "+trapTriTestAcc);
					trapDoubleTriDist = trapTriTime*(trapTestMaxVel-endVel);
					trapTriTestAcc = (trapDoubleTriDist)/(trapTriTime*trapTriTime);
					System.out.println("trapDoubleTriDist: "+trapDoubleTriDist+" trapTriTestAcc: "+trapTriTestAcc+" "+(trapTimeAtMaxVel*trapTestMaxVel)+" = "+(trapTestDist-trapDoubleTriDist));
				}else{
					//triangle path
					
				}
			}
		}
		//trapTime = Math.sqrt(4*trapDist/acceleration);
		//trapTime = findTime(trapTime);
		//trapLineNum = findLineNum(trapTime);
		//trapTestAcc = (4*trapDist)/(trapTime*trapTime);
		//acceleration = testAcc;
		
		trapTestVel = trapTestAcc*((trapLineNum-1)/2)*timeStep;
		double triSteps=0;
		if(trapTestVel>velocityMax){
			//is trapezoid, not triangle
			triTime = (velocityMax/acceleration);///timeStep;
			triSteps = triTime/timeStep;
			double triTimeOff = triSteps%1;
			triSteps -= triTimeOff;
			if(triTimeOff>=0.5){
				triSteps+=1;
			}
			triTime = triSteps*timeStep;
			doubleTriDist = triTime*velocityMax;
			
			trapTestAcc = (doubleTriDist)/(triTime*triTime);
			acceleration = trapTestAcc;
			
			distLeftover = totalDistance - doubleTriDist;
			timeAtMaxVel = distLeftover/velocityMax;
			/*totalTime = (triTime*2)+timeAtMaxVel;
			trapLineNum = totalTime / timeStep + 1;// +1 = line of 0's
			if (trapLineNum%1>0){
				if(trapLineNum%1>=0.5){
					trapLineNum+=1;
				}
				lineNum-=lineNum%1;
			}*/
		}
		double testVel =0;
		if(Math.abs(hexDist)<=0.01){
			System.out.println("ERROR: HexDist is: "+hexDist);
			trapLineNum=0;
			//acc2 is always part of trapezoid, 1 and 3 swap depending on if startVel or endVel is greater
			acc2Lines=0;
			if(acc1==trapTestAcc){
				acc1Lines=0;
			}else{
				acc3Lines=0;
			}
		}
		if(hexDist<0){
			System.out.println("ERROR: Bad Path - hexDist: "+hexDist+"ft");
			return;
		}
		double lineNum=endTriLineNum+startTriLineNum+trapLineNum+1;//zero line
		System.out.println("endTLine: "+endTriLineNum+" startTLine: "+startTriLineNum+" trapLine: "+trapLineNum);
		double testAcc=0;
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
	    boolean hitMaxVel=false;
		for(int line = 0;line < lineNum;line++){
			if(line != 0){
				acc = findAcc(line);
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
				if(velLast>velNow){
					hitMaxVel=true;
				}
				velLast = velNow;
				posLast = posNow;
				
			    if(m_writer != null){try{
						m_writer.write(posNow + " "+velNow+" "+acc+" 0 "+0+" "+timeStep+" "+x+" "+y+"\n");// jerk, x, y = 0
					}catch(Exception e){}}

			    /*if(hitMaxVel){
			    	if(velNow<=endVel+0.011){
				    	lineNum=line+1;
						System.out.println("---left side---");
						System.out.println("posNow: "+posNow);
						System.out.println("posError: "+(totalDistance-Math.abs(posNow)));
			    	}else if(velNow<=0.011){
				    	lineNum=line+1;
						System.out.println("---left side---");
						System.out.println("posNow: "+posNow);
						System.out.println("posError: "+(totalDistance-Math.abs(posNow)));
				    }else if(line+1==lineNum){//last one
						if(velNow>=0.01){
							lineNum+=1;
						}else{
							System.out.println("---left side---");
							System.out.println("posNow: "+posNow);
							System.out.println("posError: "+(totalDistance-Math.abs(posNow)));
						}
					}
			    }*/
			    if(line+1==lineNum){//last one
					System.out.println("---left side---");
					System.out.println("posNow: "+posNow);
					System.out.println("posError: "+(totalDistance-Math.abs(posNow)));
				}
			}else{
				 if(m_writer != null){try{
						m_writer.write("0 "+startVel+" 0 0 0 "+timeStep+" "+x+" "+y+"\n");//first line 0 everything
					}catch(Exception e){}}
			}
		}
		//second side
	    y=wheelbase/24;//1.125;
	    x=0.0;
		posLast =0.0;
		velLast=startVel;
		for(int line = 0;line < lineNum;line++){
			if(line != 0){
				acc = findAcc(line);
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
					System.out.println("---right side---");
					System.out.println("posNow: "+posNow);
					System.out.println("posError: "+(totalDistance-Math.abs(posNow)));
				}
			}else{
				 if(m_writer != null){try{
						m_writer.write("0 "+startVel+" 0 0 0 "+timeStep+" "+x+" "+y+"\n");//first line 0 everything
					}catch(Exception e){}}
			}
		}
		
		try{m_writer.close();System.out.println("Wrote to "+filepath);}catch(Exception e){}
	}
	
	/*private double findAcc(double lineNum, double line, double triSteps,double acceleration){
		double acc;
		if(triSteps>0){
			if(lineNum%2==0){
				if (line<triSteps+1){
					acc=acceleration;
				}else if(line>lineNum-triSteps){
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
	}*/
	private double findAcc(double line){
		if(line<=acc1Lines){
			return acc1;
		}else if(Math.abs(acc2Lines%1-0.5)<=0.1){//should be 0.5
			if(Math.abs(Math.abs(line-0.5)-acc1Lines)<=0.1){
				return 0.0;
			}else if(Math.abs(Math.abs(line-0.5)-(acc1Lines+acc2Lines))<=0.1){
				return 0.0;
			}else if(line<=acc1Lines+acc2Lines){
				return acc2;
			}else{
				return acc3;
			}
		}else{
			System.out.println(acc2Lines%1);
			if(line<=acc1Lines+acc2Lines){
				return acc2;
			}else{
				return acc3;
			}
		}
	}

	
	private double findTime(double time){
		double timeOff;
		double timeStep = Constants.TIMESTEP;
		timeOff = time % timeStep;
		time -= timeOff;
		if(timeOff >= timeStep/2){
			time+=timeStep;
		}
		time*=100;
		time=Math.round(time);
		time/=100;
		return time;
	}
	private double findLineNum(double time){
		double timeStep = Constants.TIMESTEP;
		double lineNum = time / timeStep + 1;// +1 = line of 0's
		return lineNum;
	}
}
