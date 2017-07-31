/** A two-lane highway
 *	There are such properties as the length of highway,
 *	the number of cars driving on the highway, etc.
 *	@author Chien-chen Chen
 *	@version 1.0v	June 5,  2013
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

public class Highway extends JPanel{
	
    JLabel ihighway;
	private static int wayLen;
	private static final int carlength = 50;
	private int carNum;
	private static boolean[] lane;
	private static boolean[] lane2;

	private LinkedList<Car>	allcars = new LinkedList<Car>();
	/** Constructor of object Highway */
	public Highway(int len){
		
		wayLen = len;
		carNum = 0;
		lane = new boolean[wayLen];
		lane2 = new boolean[wayLen];
		for(int i = 0; i < wayLen; i++){
			lane[i] = false;
			lane2[i] = false;
		}
	}


	/** Report the info whether there is a car driving here */
	public boolean isCarOn(int start, int end){
		if(start < end){
			int bound = (end+carlength < wayLen-1)?(end+carlength):(wayLen-1);
			for(int i = start; i <= bound; i++){
				if(lane[i] == true)	return true;
				else continue;
			}
			return false;
		}
		else if(start > end){
			int bound = (end-carlength > 0)?(end-carlength):(0);
			for(int i = start; i >= bound; i--){
				if(lane2[i] == true)	return true;
				else continue;
			}
			return false;
		}
		else return true;	//mischief?
	}
	/** Record the information of specific position of the highway */
	public static synchronized void onLane(int index, boolean direction){
		if(direction == true)
			lane[index] = true;
		else lane2[index] = true;
	}
	/** Record the information of specific position of the highway */
	public static synchronized void offLane(int index, boolean direction){
		if(direction == true)
			lane[index] = false;
		else lane2[index] = false;
	}
	//

	/** Check the immediate 8 marks if there is a car driving there */
	public static synchronized int Check(int start, boolean direction){
		int Dist = 8;
		int bound;
		if(direction == true){
			bound = (start+7+carlength < wayLen-1)?(start+7+carlength):(wayLen-1);
			for(int i = start+carlength; i <= bound; i++){
				if(lane[i] == false) continue;
				else{
					Dist = i - (start+carlength);
					break;
				}
			}
		}
		else{
			bound = (start-7-carlength > 0)?(start-7-carlength):(0);
			for(int i = start-carlength; i >= bound; i--){
				if(lane2[i] == false) continue;
				else{
					Dist = (start-carlength) - i;
					break;
				}
			}
		}
		
		return Dist;
	}
}