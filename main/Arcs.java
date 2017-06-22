package main;

import java.io.FileWriter;
import java.io.IOException;

public class Arcs {
	double acc1;
	double acc2;
	double acc3;
	double acc1Lines;
	double acc2Lines;
	double acc3Lines;
	double flatAccLines;
	public void drawArcPath(double totalDegrees,double radius,double startVel,double endVel) {
		String filename = "banana";
	    String filepath = "C:/Users/Drew/Desktop/pathGui/"+filename+".txt";
		//double totalDegrees = 900;
	    double totalDistance=Math.abs((2*Math.PI*radius)*(totalDegrees/360));
	    Writer w = new Writer();
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
		
		double trapRectLines=0;
		double trapSingleTriLines=0;
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
			//System.out.println("endTriTime: "+endTriTime+" endTriDist: "+endTriDist+" endTriTestAcc: "+endTriTestAcc+" totalDistance: "+totalDistance);
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
			//System.out.println("startTriTime: "+startTriTime+" startTriLineNum: "+startTriLineNum+" startTriDist: "+startTriDist+" startTriTestAcc: "+startTriTestAcc+" totalDistance: "+totalDistance);
			trapDist=totalDistance-startTriDist;
			acc1 = startTriTestAcc;
			acc1Lines = startTriLineNum;
		}else if(endVel==startVel&&startVel>0){
			//both = and > 0
			double ghostSingleTriTime=startVel/acceleration;
			double ghostDoubleTriDist=startVel*ghostSingleTriTime;
			double ghostTotalDist = ghostDoubleTriDist+totalDistance;
			trapTime = Math.sqrt(4*ghostTotalDist/acceleration);
			trapTime -= (ghostSingleTriTime*2);
			trapTime = findTime(trapTime);
			trapLineNum = trapTime/timeStep;
			totalTime = trapTime;
			
			boolean goodTrap=false;
			boolean forwards=true;
			boolean backwards=false;
			int i=-1;
			while(goodTrap==false){
				double lastError=9999999999.0;
				int lastI=0;
				while(forwards){
					i++;
					trapTime=totalTime-(i*timeStep);
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
			trapTestVel+=startVel;
			
			if(trapTestVel<=velocityMax){
				acc1=Math.abs(trapTestAcc);
				acc1Lines=Math.round(trapTime/2/0.02*10);
				acc1Lines/=10;
				acc2=-Math.abs(trapTestAcc);
				acc2Lines=Math.round(trapTime/2/0.02*10);
				acc2Lines/=10;
				
				trapLineNum=Math.round(trapTime/0.02);
			}else{
				double trapSingleTriTime=(velocityMax-startVel)/acceleration;
				trapSingleTriTime=findTime(trapSingleTriTime);
				double trapDoubleTriDist=(velocityMax-startVel)*trapSingleTriTime;
				double trapSingleTriTestAcc=(velocityMax-startVel)/trapSingleTriTime;
				
				double trapDoubleTriRectDist=(trapSingleTriTime*2)*startVel;
				double bigRectDist=totalDistance-trapDoubleTriRectDist-trapDoubleTriDist;
				//System.out.println(" bigRectDist: "+bigRectDist+" trapDoubleTriRectDist: "+trapDoubleTriRectDist+" trapDoubleTriDist: "+trapDoubleTriDist);
				double bigRectTime=bigRectDist/velocityMax;
				bigRectTime=findTime(bigRectTime);
				double newMaxVel=startVel+((bigRectDist-(bigRectTime*startVel))+trapDoubleTriDist)/(bigRectTime+trapSingleTriTime);
				trapDoubleTriDist=(newMaxVel-startVel)*trapSingleTriTime;
				trapSingleTriTestAcc=(newMaxVel-startVel)/trapSingleTriTime;
				bigRectDist=bigRectTime*newMaxVel;
				
				//System.out.println("trapSingleTriTime: "+trapSingleTriTime+" trapDoubleTriDist: "+trapDoubleTriDist+" trapSingleTriTestAcc: "+trapSingleTriTestAcc+" newMaxVel: "+newMaxVel+" bigRectDist: "
				//		+bigRectDist+" trapDoubleTriRectDist: "+trapDoubleTriRectDist+" trapDoubleTriDist: "+trapDoubleTriDist);
				acc1=trapSingleTriTestAcc;
				acc1Lines=Math.round(trapSingleTriTime/0.02*10);
				acc1Lines/=10;
				acc2=-trapSingleTriTestAcc;
				acc2Lines=Math.round(trapSingleTriTime/0.02*10);
				acc2Lines/=10;
				flatAccLines=Math.round(bigRectTime/0.02*10);
				flatAccLines/=10;
				trapLineNum=acc1Lines+acc2Lines+flatAccLines;
			}
			hexDist=totalDistance;
			//System.out.println("trapTestVel: "+trapTestVel);
			System.out.println("acc1: "+acc1+" acc2: "+acc2+" acc1Lines: "+acc1Lines+" acc2Lines: "+acc2Lines);
		}else{
			//both 0
			trapDist=totalDistance;
			hexDist=trapDist;
			trapTime = Math.sqrt(4*trapDist/acceleration);
			trapTime = findTime(trapTime);
			trapLineNum = trapTime/timeStep;
			trapTestAcc = (4*trapDist)/(trapTime*trapTime);
			if(trapTime%(timeStep*2)>=timeStep*0.9){
				trapTestVel = trapTestAcc*((trapTime-timeStep)/2);
			}else{
				trapTestVel = trapTestAcc*(trapTime/2);
			}
			acc1=trapTestAcc;
			acc2=-trapTestAcc;
			acc1Lines=Math.round(trapLineNum/2*10);
			acc1Lines/=10;
			acc2Lines=Math.round(trapLineNum/2*10);
			acc2Lines/=10;
			//System.out.println("tLines: "+trapLineNum+" acc1: "+acc1+" acc2: "+acc2+" tVel: "+trapTestVel);
			if(trapTestVel>velocityMax){
				//trapezoid, no triangle
				double trapSingleTriTime = velocityMax / acceleration;
				trapSingleTriTime = findTime(trapSingleTriTime);
				trapSingleTriLines = trapSingleTriTime/timeStep;
				double trapTriTestAcc = velocityMax / trapSingleTriTime;
				double trapDoubleTriDist = velocityMax * trapSingleTriTime;
				
				double trapRectDist = trapDist-trapDoubleTriDist;
				double trapRectTime = trapRectDist / velocityMax;
				//System.out.println("trapSingleTriTime: "+trapSingleTriTime+" trapTriTestAcc: "+trapTriTestAcc+" trapDoubleTriDist: "+trapDoubleTriDist+" trapRectDist: "+trapRectDist+" trapRectTime: "+trapRectTime);
				trapRectTime = findTime(trapRectTime);
				trapRectLines = trapRectTime/timeStep;
				double newMaxVel = trapDist/(trapRectTime+trapSingleTriTime);
				trapTriTestAcc = newMaxVel / trapSingleTriTime;
				trapDoubleTriDist = newMaxVel * trapSingleTriTime;
				trapRectDist = trapDist-trapDoubleTriDist;
				//System.out.println("ADJUSTED: trapRectTime: "+trapRectTime+" newMaxVel: "+newMaxVel+" trapTriTestAcc: "+trapTriTestAcc+" trapDoubleTriDist: "+trapDoubleTriDist+" trapRectDist: "+trapRectDist);

				acc1=trapTriTestAcc;
				acc2=-trapTriTestAcc;
				acc1Lines=Math.round(trapSingleTriLines*10);
				acc1Lines/=10;
				acc2Lines=Math.round(trapSingleTriLines*10);
				acc2Lines/=10;
				flatAccLines=trapRectLines;
				trapLineNum=trapRectLines+(trapSingleTriLines*2);
			}
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
				//System.out.println("totalDist: "+totalDistance+" trapDist: "+trapDist+" triDist: "+endTriDist+" rectDist: "+(trapTime*startVel)+" hexDist: "+hexDist);
				//ghostTestVel = endTriTestAcc*(totalLineNum-1)/2*timeStep;
				ghostTestVel=trapTestVel+startVel;
				double guessDist=guessTrapDist+endTriDist+(trapTime*startVel);
				//System.out.println("ghostTestVel: "+ghostTestVel+" guessDist: "+guessDist+" trapTime: "+trapTime+" topTrapDist: "+trapDist+" trapTestAcc: "+trapTestAcc+
					//	"\nguessTrapDist: "+guessTrapDist+" totalTime: "+totalTime+" triTime: "+endTriTime+" trapTestVel: "+trapTestVel);
				if(trapTestVel+startVel<=velocityMax){
					//System.out.println(trapTime);
					acc1=trapTestAcc;
					acc1Lines=Math.round(trapTime/2/0.02*10);
					acc1Lines/=10;
					//acc3=-Math.abs(trapTestAcc);
					acc2=-trapTestAcc;
					acc2Lines=Math.round(trapTime/2/0.02*10);
					acc2Lines/=10;
					trapLineNum=acc1Lines+acc2Lines;
				}else{
					//trapezoid path
					double trapSingleTriTime=(velocityMax-startVel)/acceleration;
					trapSingleTriTime=findTime(trapSingleTriTime);
					double trapDoubleTriDist=(velocityMax-startVel)*trapSingleTriTime;
					double trapSingleTriTestAcc=(velocityMax-startVel)/trapSingleTriTime;
					
					double trapDoubleTriRectDist=(trapSingleTriTime*2)*startVel;
					double bigRectDist=hexDist-trapDoubleTriRectDist-trapDoubleTriDist;
					//System.out.println(" bigRectDist: "+bigRectDist+" trapDoubleTriRectDist: "+trapDoubleTriRectDist+" trapDoubleTriDist: "+trapDoubleTriDist);
					double bigRectTime=bigRectDist/velocityMax;
					bigRectTime=findTime(bigRectTime);
					double newMaxVel=startVel+((bigRectDist-(bigRectTime*startVel))+trapDoubleTriDist)/(bigRectTime+trapSingleTriTime);
					trapDoubleTriDist=(newMaxVel-startVel)*trapSingleTriTime;
					trapSingleTriTestAcc=(newMaxVel-startVel)/trapSingleTriTime;
					bigRectDist=bigRectTime*newMaxVel;
					
					//System.out.println("trapSingleTriTime: "+trapSingleTriTime+" trapDoubleTriDist: "+trapDoubleTriDist+" trapSingleTriTestAcc: "+trapSingleTriTestAcc+" newMaxVel: "+newMaxVel+" bigRectDist: "
						//	+bigRectDist+" trapDoubleTriRectDist: "+trapDoubleTriRectDist+" trapDoubleTriDist: "+trapDoubleTriDist);
					acc1=trapSingleTriTestAcc;
					acc1Lines=Math.round(trapSingleTriTime/0.02*10);
					acc1Lines/=10;
					acc2=-trapSingleTriTestAcc;
					acc2Lines=Math.round(trapSingleTriTime/0.02*10);
					acc2Lines/=10;
					flatAccLines=Math.round(bigRectTime/0.02*10);
					flatAccLines/=10;
					trapLineNum=acc1Lines+acc2Lines+flatAccLines;
				}
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
					//System.out.println("left: "+trapDistLeftover+" tri: "+trapDoubleTriDist);
					double trapTimeAtMaxVel = trapDistLeftover/velocityMax;
					double trapTotalTime = (trapTriTime)+trapTimeAtMaxVel;
					double trapLineNum2 = trapTotalTime / timeStep;// + 1;// +1 = line of 0's
					//System.out.println("trapLineNum(unadjusted): "+trapLineNum2);
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
					//System.out.println("trapTime: "+trapTotalTime+" trapLineNum: "+trapLineNum2+" trapTestDist: "+trapTestDist+" trapRealDist: "+trapDist+" hexTestDist: "+hexTestDist+" hexOff: "+(hexDist-hexTestDist));
					//System.out.println("triTime: "+trapTriTime+" timeAtMax: "+trapTimeAtMaxVel);
					//change max velocity to get exact distance
					trapTestDist=(trapTimeAtMaxVel+(trapTriTime/2))*trapTestMaxVel;
					//System.out.println("adjustedMaxVel: "+trapTestMaxVel+" adjustedTrapDist: "+trapTestDist+" trapDoubleTriDist: "+trapDoubleTriDist+" trapTriTestAcc: "+trapTriTestAcc);
					trapDoubleTriDist = trapTriTime*(trapTestMaxVel-startVel);
					trapTriTestAcc = (trapDoubleTriDist)/(trapTriTime*trapTriTime);
					//System.out.println("trapDoubleTriDist: "+trapDoubleTriDist+" trapTriTestAcc: "+trapTriTestAcc+" "+(trapTimeAtMaxVel*trapTestMaxVel)+" = "+(trapTestDist-trapDoubleTriDist));
				}else{
					//triangle path
					double trapTriMaxVel = (trapDist)/(trapTime/2);
					double trapTriTestAcc = trapTriMaxVel/(trapTime/2);
					//System.out.println(trapTime+" "+trapTriMaxVel+" "+trapTriTestAcc);
				}
			}else if(endVel>startVel){
				//real startTri, ghostEndTri
				//trapDist=trapDist-(*endVel);
				ghostTestVel = startTriTestAcc*(totalLineNum-1)/2*timeStep;
				double guessDist=ghostTestVel*(totalTime-startTriTime);
				//System.out.println("guessDist: "+guessDist);
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
						//System.out.println("tAcc: "+trapTestAcc+" tVel: "+trapTestVel+" tTime: "+trapTime+" tDist: "+trapDist);
					}
					int secondLastI=0;
					while(backwards){
						i++;
						trapTime=totalTime-(startTriTime*2)-(lastI*timeStep)+(i*timeStep);
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
						double nowError = Math.abs(trapTestAcc-acceleration);
						if(nowError>lastError){
							forwards=false;
							backwards=false;
							secondLastI=i;
						}
						lastError=nowError;
						//System.out.println("tAcc: "+trapTestAcc+" tVel: "+trapTestVel+" tTime: "+trapTime+" tDist: "+trapDist);
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
					//System.out.println("tAcc: "+trapTestAcc+" tVel: "+trapTestVel+" tTime: "+trapTime+" tDist: "+trapDist);
				}
				double guessTrapDist=trapTestVel*trapTime;
				hexDist=trapDist+(trapTime*endVel);
				//System.out.println("totalDist: "+totalDistance+" trapDist: "+trapDist+" triDist: "+startTriDist+" rectDist: "+(trapTime*endVel)+" hexDist: "+hexDist);
				//ghostTestVel = endTriTestAcc*(totalLineNum-1)/2*timeStep;
				ghostTestVel=trapTestVel+endVel;
				guessDist=guessTrapDist+endTriDist+(trapTime*endVel);
				//System.out.println("ghostTestVel: "+ghostTestVel+" guessDist: "+guessDist+" trapTime: "+trapTime+" topTrapDist: "+trapDist+" trapTestAcc: "+trapTestAcc+
					//	"\nguessTrapDist: "+guessTrapDist+" totalTime: "+totalTime+" triTime: "+startTriTime+" trapTestVel: "+trapTestVel);
				//acc2=Math.abs(trapTestAcc);
				if(trapTestVel+endVel<=velocityMax){
					acc2=trapTestAcc;
					acc2Lines=Math.round(trapTime/2/0.02*10);
					acc2Lines/=10;
					//acc3=-Math.abs(trapTestAcc);
					acc3=-trapTestAcc;
					acc3Lines=Math.round(trapTime/2/0.02*10);
					acc3Lines/=10;
					trapLineNum=acc2Lines+acc3Lines;
				}else{
					//trapezoid path
					double trapSingleTriTime=(velocityMax-endVel)/acceleration;
					trapSingleTriTime=findTime(trapSingleTriTime);
					double trapDoubleTriDist=(velocityMax-endVel)*trapSingleTriTime;
					double trapSingleTriTestAcc=(velocityMax-endVel)/trapSingleTriTime;
					
					double trapDoubleTriRectDist=(trapSingleTriTime*2)*endVel;
					double bigRectDist=hexDist-trapDoubleTriRectDist-trapDoubleTriDist;
					//System.out.println(" bigRectDist: "+bigRectDist+" trapDoubleTriRectDist: "+trapDoubleTriRectDist+" trapDoubleTriDist: "+trapDoubleTriDist);
					double bigRectTime=bigRectDist/velocityMax;
					bigRectTime=findTime(bigRectTime);
					double newMaxVel=endVel+((bigRectDist-(bigRectTime*endVel))+trapDoubleTriDist)/(bigRectTime+trapSingleTriTime);
					trapDoubleTriDist=(newMaxVel-endVel)*trapSingleTriTime;
					trapSingleTriTestAcc=(newMaxVel-endVel)/trapSingleTriTime;
					bigRectDist=bigRectTime*newMaxVel;
					
					//System.out.println("trapSingleTriTime: "+trapSingleTriTime+" trapDoubleTriDist: "+trapDoubleTriDist+" trapSingleTriTestAcc: "+trapSingleTriTestAcc+" newMaxVel: "+newMaxVel+" bigRectDist: "
						//	+bigRectDist+" trapDoubleTriRectDist: "+trapDoubleTriRectDist+" trapDoubleTriDist: "+trapDoubleTriDist);
					acc2=trapSingleTriTestAcc;
					acc2Lines=Math.round(trapSingleTriTime/0.02*10);
					acc2Lines/=10;
					acc3=-trapSingleTriTestAcc;
					acc3Lines=Math.round(trapSingleTriTime/0.02*10);
					acc3Lines/=10;
					flatAccLines=Math.round(bigRectTime/0.02*10);
					flatAccLines/=10;
					trapLineNum=acc2Lines+acc3Lines+flatAccLines;
				}
				
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
					//System.out.println("left: "+trapDistLeftover+" tri: "+trapDoubleTriDist);
					double trapTimeAtMaxVel = trapDistLeftover/velocityMax;
					double trapTotalTime = (trapTriTime)+trapTimeAtMaxVel;
					double trapLineNum2 = trapTotalTime / timeStep;// + 1;// +1 = line of 0's
					//System.out.println("trapLineNum(unadjusted): "+trapLineNum2);
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
					//System.out.println("trapTime: "+trapTotalTime+" trapLineNum: "+trapLineNum2+" trapTestDist: "+trapTestDist+" trapRealDist: "+trapDist+" hexTestDist: "+hexTestDist+" hexOff: "+(hexDist-hexTestDist));
					//System.out.println("triTime: "+trapTriTime+" timeAtMax: "+trapTimeAtMaxVel);
					//change max velocity to get exact distance
					trapTestDist=(trapTimeAtMaxVel+(trapTriTime/2))*trapTestMaxVel;
					//System.out.println("adjustedMaxVel: "+trapTestMaxVel+" adjustedTrapDist: "+trapTestDist+" trapDoubleTriDist: "+trapDoubleTriDist+" trapTriTestAcc: "+trapTriTestAcc);
					trapDoubleTriDist = trapTriTime*(trapTestMaxVel-endVel);
					trapTriTestAcc = (trapDoubleTriDist)/(trapTriTime*trapTriTime);
					//System.out.println("trapDoubleTriDist: "+trapDoubleTriDist+" trapTriTestAcc: "+trapTriTestAcc+" "+(trapTimeAtMaxVel*trapTestMaxVel)+" = "+(trapTestDist-trapDoubleTriDist));
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
		lineNum = Math.round(lineNum);
		//System.out.println("endTLine: "+endTriLineNum+" startTLine: "+startTriLineNum+" trapLine: "+trapLineNum);
		double testAcc=0;
		//System.out.println("testVel: "+testVel+" overMaxVel? "+(testVel>velocityMax)+" steps: "+triSteps+" doubleTriDist: "+doubleTriDist+" timeAtMaxVel: "+timeAtMaxVel);
		//System.out.println("distLeftover: "+distLeftover);
		//System.out.println("dist: "+totalDistance+" time: "+totalTime+" timeOff: "+timeOff+" lineNum: "+lineNum+" testAcc: "+testAcc);

		double radians;
		//double lastRadians=0;
		double radDelta;
		//double lastRadDelta=0;
		double radiansList[]=new double[(int)lineNum];
	    //first side
	    y=-wheelbase/24;//-1.125;
	    x=0.0;
	    boolean hitMaxVel=false;
		for(int line = 0;line < lineNum;line++){
				acc = findAcc(line);
				velNow = velLast + acc * timeStep;
				posNow = posLast + (velLast + velNow)/2 * timeStep;
				if(totalDegrees<0){
					radians = -posNow/radius;
				}else{
					radians = posNow/radius;//360/(wheelbase*Math.PI)/4.77464829275769;//magic num??????
				}
				radiansList[line]=radians;
				if(line>1){
					radDelta=(radiansList[line-2]-radiansList[line-1])*-1;
				}else{
					radDelta=0;
				}
				Double[] accVel = {acc,velNow,radDelta,radius};
				if(totalDegrees<0){
					w.addRightAcc(accVel);
				}else{
					w.addLeftAcc(accVel);
				}

				velLast = velNow;
				posLast = posNow;
				if(line+1==lineNum){//last one
					System.out.println("---left side---");
					System.out.println("posNow: "+posNow);
					System.out.println("posError: "+(totalDistance-Math.abs(posNow)));
					String radList="";
					for(int i=0;i<=lineNum-1;i++){
						radList=radList+radiansList[i]+" ";
					}
					//System.out.println("radiansList: "+radList);
					//radDelta=(radiansList[line]-radiansList[line-1])*-1;
					//Double[] accVel2 = {accLast,velLast,radDelta,radius};
					//w.addLeftAcc(accVel2);
				}
				//radians = (180*posNow)/(Math.PI*radius)*(Math.PI/180);
				//x+=Math.sin(radians)*(posNow-posLast);
				//y+=Math.cos(radians)*(posNow-posLast);
				
				//http://rossum.sourceforge.net/papers/CalculationsForRobotics/CirclePath.htm
				//x=-wheelbase/24 - radius*Math.sin(startAngle)+radius*Math.sin((radians/(line*0.02-0.0))*(line*0.02-0.0)+startAngle);
				//y=0.0 + radius/24*Math.cos(startAngle)+radius/24*Math.cos((radians/(line*0.02-0.0))*(line*0.02-0.0)+startAngle);
				//x=Math.sin(radians)*radius;
			//	y=-wheelbase/24+radius-Math.cos(radians)*radius;
				//x = posNow;
				//y = -wheelbase/24;
				
			   // if(m_writer != null){try{
			//			m_writer.write(posNow + " "+velNow+" "+acc+" 0 "+radians+" "+timeStep+" "+x+" "+y+"\n");// jerk, x, y = 0
			//		}catch(Exception e){}}
	
			
		//		 if(m_writer != null){try{
		//				m_writer.write("0 0 0 0 0 "+timeStep+" "+x+" "+y+"\n");//first line 0 everything
		//			}catch(Exception e){}}
			
		}
		//second side
	    y=wheelbase/24;//1.125;
	    x=0.0;
		posLast =0.0;
		velLast=startVel;
		radius-=(wheelbase/12);
		for(int line = 0;line < lineNum;line++){
			//if(line != -1){
				radians = radiansList[line];
				if(totalDegrees<0){
					posNow =-radius*radians;
				}else{
					posNow =radius*radians;
				}
				velNow = ((posNow-posLast)/timeStep);//*2-velLast;
				//acc = findAcc(lineNum,line,triSteps,acceleration);
				acc = (velNow-velLast)/timeStep;
				if(line>1){
					radDelta=(radiansList[line-2]-radiansList[line-1])*-1;
				}else{
					radDelta=0;
				}
				Double[] accVel = {acc,velNow,radDelta,radius};
				if(totalDegrees<0){
					w.addLeftAcc(accVel);
				}else{
					w.addRightAcc(accVel);
				}

				
				//velNow = velLast + acc * timeStep;
				//posNow = posLast + (velLast + velNow)/2 * timeStep;
				//radians = posNow/radius;//360/(wheelbase*Math.PI)/4.77464829275769;//magic num??????
				
				//x+=Math.sin(radians)*(posNow-posLast);
				//y+=Math.cos(radians)*(posNow-posLast);
				
				//http://rossum.sourceforge.net/papers/CalculationsForRobotics/CirclePath.htm
				double startAngle = Math.PI/2;//Math.PI/2;
				//x=wheelbase/24 + radius/24*Math.sin(startAngle)+radius/24*Math.sin((radians/(line*0.02-0.0))*(line*0.02-0.0)+startAngle);
				//y=0.0 + radius/24*Math.cos(startAngle)+radius/24*Math.cos((radians/(line*0.02-0.0))*(line*0.02-0.0)+startAngle);
				//x = posNow;
				//y = wheelbase/24;
				x=Math.sin(radians)*radius;
				y=wheelbase/24+radius-Math.cos(radians)*radius;
				velLast = velNow;
				posLast = posNow;
				
			//    if(m_writer != null){try{
			//			m_writer.write(posNow + " "+velNow+" "+acc+" 0 "+radians+" "+timeStep+" "+x+" "+y+"\n");// jerk, x, y = 0
		//			}catch(Exception e){}}
	
				if(line+1==lineNum){//last one
					System.out.println("---right side---");
					System.out.println("posNow: "+posNow);
					System.out.println("posError: "+(totalDistance-Math.abs(posNow)));
					//radDelta=(radiansList[line]-radiansList[line-1])*-1;
					//Double[] accVel2 = {accLast,velLast,radDelta,radius};
					//w.addRightAcc(accVel2);
				}
			//}else{
		//		 if(m_writer != null){try{
		//				m_writer.write("0 0 0 0 0 "+timeStep+" "+x+" "+y+"\n");//first line 0 everything
	//				}catch(Exception e){}}
			//}
		}

		//try{m_writer.close();System.out.println("Wrote to "+filepath);}catch(Exception e){}
		System.out.println("acc1Lines: "+acc1Lines+" acc2Lines: "+acc2Lines+" acc3Lines: "+acc3Lines+" flatAccLines: "+flatAccLines);
		System.out.println("-------------------------------------------------------------");
	}
	
	private double findAcc(double line){
		if(line<=acc1Lines-1){
			return acc1;
		}else if(flatAccLines>0){
			if(acc1==-acc2){
				if(line<acc1Lines+flatAccLines){
					return 0.0;
				}else {
					if(line<acc1Lines+flatAccLines+acc2Lines){
						return acc2;
					}else{
						return acc3;
					}
				}
			}else{
				//(acc3==-acc2)
				if(line<acc1Lines+acc2Lines){
					return acc2;
				}else if(line<acc1Lines+acc2Lines+flatAccLines){
					return 0.0;
				}else{
					return acc3;
				}
			}
		}else{
			if(Math.abs(acc2Lines%1-0.5)<=0.1){//should be 0.5
				if(Math.abs(Math.abs(line-0.5)-acc1Lines+1)<=0.1){
					return 0.0;
				}else if(Math.abs(Math.abs(line-0.5)-(acc1Lines+acc2Lines-1))<=0.1){
					return 0.0;
				}else if(line<=acc1Lines+acc2Lines-1){
					//System.out.println("line: "+line+" acc1Lines+acc2Lines: "+(acc1Lines+acc2Lines)+" ? "+(line<=acc1Lines+acc2Lines));
					return acc2;
				}else{
					return acc3;
				}
			}else{
				//System.out.println("hi");
				if(line<acc1Lines+acc2Lines){
					return acc2;
				}else{
					return acc3;
				}
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
