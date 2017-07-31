/** Cars are the leading roles of this program.
 *	Thus, I designated a class to represent cars.
 *  Some of the properties of car are the speed, 
 *	the position, the decision which is made by the driver, etc.
 *	@author	Chien-chen Chen
 *	@version 1.0v	June 5, 2013
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

public class Car extends Thread{
	private int speed;
	private int speedFlag;
	private int mark;
	private boolean direction;
	private int state;	//0: well,	1: going down,	2:bump!!!
	private boolean isBump;

	private final int DELAY = 50;
	/** Constructor of object Car */
	public Car(int _mark, boolean _direction){
		//System.out.println("Creating a new car");
		mark = _mark;
		direction = _direction;

		int adjspeed = Highway.Check(mark, direction)/2;
		if(adjspeed > 0)	speed = adjspeed;
		else speed = 0;
		Highway.onLane(mark, direction);
		speedFlag = -1;
		state = 0;
		isBump = false;
	}
	public void run(){
		long beforeTime, timeDiff, sleep;

    	beforeTime = System.currentTimeMillis();
		while(true){
			/* show the information of car*/
			/*
			System.out.println("I am running...");
			System.out.println("My mark : "+mark);
			System.out.println("My speed : "+speed);
			*/
			if(isBump == true){
				Highway.offLane(mark, direction);
				break;
			}

			int dist = Highway.Check(mark,direction);
			if(speedFlag == -1){

				int adjspeed = dist/2;
				if(speed > dist){
					//System.out.println("Bump!!!");
					state = 2;
					Highway.offLane(mark, direction);
					break;
				}
			//if(adjspeed > 0){
				Highway.offLane(mark, direction);

				if(direction == true){
					for(int i = 0; i < speed; i++)
						mark += 1;
				}
				else{
					for(int i = 0; i < speed; i++)
						mark -= 1;
				}

				if(mark > CarSimulation.wayLen-1 || mark < 1){
					//System.out.println("I am going down the highway...");
					state = 1;
					break;
				}
				Highway.onLane(mark, direction);
				if(speed > adjspeed){
					speed = adjspeed;
				}
				else if(speed < adjspeed){
					speedFlag = adjspeed;
				}
				else{	//speed = adjspeed
				}
			//}
			}
			else{
				if(speed > dist){
					System.out.println("Bump!!!");
					state = 2;
					Highway.offLane(mark ,direction);
					break;
				}
				Highway.offLane(mark ,direction);

				if(direction == true){
					for(int i = 0; i < speed; i++)
						mark += 1;
				}
				else{
					for(int i = 0; i < speed; i++)
						mark -= 1;
				}

				if(mark > CarSimulation.wayLen-1){
					System.out.println("I am going down the highway...");
					state = 1;
					break;
				}
				Highway.onLane(mark ,direction);
				speed = speedFlag;
				speedFlag = -1;
			}
			timeDiff = System.currentTimeMillis() - beforeTime;
      		sleep = DELAY - timeDiff;

      		if (sleep < 0)
          		sleep = 20;
      		try {
          		Thread.sleep(sleep);
      		} catch (InterruptedException e) {
          		System.out.println("interrupted");
      		}

      		beforeTime = System.currentTimeMillis();
			if(state == 2){
				Highway.offLane(mark, direction);
				break;
			}
		}
		
    }
    

    public boolean getDirection(){
    	return direction;
    }

    public int getCarState(){
    	return state;
    }
    public void setIsBump(){
    	isBump = true;
    }

	/** Set the mark of car */
	public void setMark(int mark)	{this.mark = mark;}
	/** Get the mark of car */
	public int getMark()	{return mark;}
	/** Set the speed of car */
	public void setSpeed(int halfDist)	{speed = halfDist;}
	/** Get the speed of car */
	public int getSpeed()	{return speed;}

}