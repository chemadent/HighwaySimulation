/** Simulating cars driving on the highway 
 *	The program simulates three conditions.
 *	The fisrt one is cars starting off from the beginning(the distance mark 1).
 *	The second one is the first driver getting a sudden headache and slowing down 
 *	her/his car down to half of the initial speed at time step 5. The driver then recovers
 *	immediately at time step 6.
 *	Goal:
 *	1.Re-design a HW1 - highway simulator
 *	2.Learn to use Java Applet for a GUI-based demo program
 *  3.Practice about multi-threading
 *
 *	@author Chien-chen Chen
 *	@version 1.0v	June 5,  2013
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;


public class CarSimulation extends JApplet implements Runnable{

	static final int speedLimit = 4;
	static final int wayLen = 920;

  private int count = 2;
  
	LinkedList<Car> link = new LinkedList<Car>();  //W2E
  LinkedList<Car> link2 = new LinkedList<Car>();  //E2W
	Container cp;
	Highway highway = new Highway(wayLen);
  Image img_highway;
  Image img_beginGo;
  Image img_endGo;


  JPanel totalPn = new JPanel(new BorderLayout(), true);
  JLabel ihighway;
	JButton beginGo;
	JButton endGo;
	JPanel  btnlist;

  private Thread animator;

  private Image offScreen;
  private Graphics gOffScreen;


  private final int DELAY = 50;

	public void init(){
		try{
			SwingUtilities.invokeAndWait(new Runnable(){
				public void run(){
					guiInit();
				}
			}
		);
		} catch(Exception exc){
			System.out.println("Error: "+exc);
		}
	}
	// Called second, after init().  Also called 
	// whenever the applet is restarted.  
	public void start() { 
  // Not used by this applet. 
  } 
 
  // Called when the applet is stopped. 
  public void stop() { 
  // Not used by this applet. 
  } 
 
  // Called when applet is terminated.  This is 
  // the last method executed. 
  public void destroy() { 
  // Not used by this applet. 
  } 


  private void guiInit(){

    img_highway = getImage(getClass().getResource("../image/shighway.png"));

    img_beginGo = getImage(getClass().getResource("../image/arrowW2E.png"));

    img_endGo = getImage(getClass().getResource("../image/arrowE2W.png"));

    ihighway = new JLabel(new ImageIcon(getClass().getResource("../image/shighway.png")));

    beginGo = new JButton(new ImageIcon(getClass().getResource("../image/arrowW2E.png")));

    endGo = new JButton(new ImageIcon(getClass().getResource("../image/arrowE2W.png")));

  	beginGo.addActionListener(getBeginGoActionListener());
    
  	endGo.addActionListener(getEndGoActionListener());
    
  	btnlist = new JPanel();
    btnlist.setLayout(new GridLayout(2,1));

    

    //beginGo.setBounds(new Rectangle(40,120,80,50));
    //endGo.setBounds(new Rectangle(40,40,80,50));
    /* it seems that the following codes which are commented could not work....
    ihighway.setBounds(140,40,1000,160);
    beginGo.setBounds(40,120,80,50);
    endGo.setBounds(40,40,80,50);

    totalPn.setLayout(null);
    */
    
    beginGo.setBorder(null);
    beginGo.setContentAreaFilled(false); 
    endGo.setBorder(null);
    endGo.setContentAreaFilled(false);
    
    //beginGo.setPreferredSize(new Dimension(80, 20));
    //endGo.setPreferredSize(new Dimension(80, 20));
    
    
  	btnlist.add(endGo);
  	btnlist.add(beginGo);

  	cp = getContentPane();
    //setBounds(0,0,1200,200);

    totalPn.setDoubleBuffered(true);

    totalPn.add(ihighway, BorderLayout.CENTER);
  	totalPn.add(btnlist,BorderLayout.WEST);
    cp.add(totalPn);

    offScreen = createImage(getWidth(), getHeight());
    gOffScreen = offScreen.getGraphics();
  }

  public void addNotify() {
      super.addNotify();
      animator = new Thread(this);
      animator.start();
  }

  public void update(Graphics g) {
    paint(g);
  }
  public void paint(Graphics g) {
    if(count > 0){
      super.paint(g);
      count--;
    }
    
    
    Graphics2D g2d = (Graphics2D)g;

    gOffScreen.clearRect(140, 0, 1000, 200);

    gOffScreen.drawImage(img_highway,140,20,this);
    //gOffScreen.drawImage(img_beginGo,0,120,this);
    //gOffScreen.drawImage(img_endGo,0,40,this);


    /*
      Check each car's state. If we find the following conditions, we remove those cars
      state = 1 : the cars are going down the highway
      state = 2 : the car bumped into the car in the immediate front
    */
    for(int i = 0; i < link.size(); i++){
      if(link.get(i).getCarState() == 1){
        link.remove(i);
      }
      else if(link.get(i).getCarState() == 2){
        int bumpMark = link.get(i).getMark();
        int yPos = 100;
        link.get(i-1).setIsBump();  //set the bumped car' state to 2
        
        link.remove(i-1);
        link.remove(i-1);
        /* draw the bump image */
        try{
          gOffScreen.drawImage(getImage(getClass().getResource("../image/bump.png")), bumpMark+140, yPos, this);
          Thread.sleep(100);
        }catch(InterruptedException e){}
      }
    }
    for(int i = 0; i < link2.size(); i++){
      if(link2.get(i).getCarState() == 1){
        link2.remove(i);
      }
      else if(link2.get(i).getCarState() == 2){
        int bumpMark = link2.get(i).getMark();
        int yPos = 40;

        link2.get(i-1).setIsBump(); //set the bumped car' state to 2
        
        link2.remove(i-1);
        link2.remove(i-1);
        /* draw the bump image */
        try{
          gOffScreen.drawImage(getImage(getClass().getResource("../image/bump.png")), bumpMark+140, yPos, this);
          Thread.sleep(100);
        }catch(InterruptedException e){}
      }
    }
    /*Draw the cars on the offscreen*/
    for(Car car : link){
        gOffScreen.drawImage(getImage(getClass().getResource("../image/scar.png")), car.getMark()+140, 120, this);
    }
    for(Car car : link2){
        gOffScreen.drawImage(getImage(getClass().getResource("../image/scar2.png")), car.getMark()+170, 60, this);
    }
    /*Draw the offscreen image on the Applet frame we see */
    g.drawImage(offScreen, 80, 0, this);
    Toolkit.getDefaultToolkit().sync();
    g.dispose();
  }

  public void run() {

    /* Keep a stable time for sleeping in order to control the times Applet frame being repainted per second */
    long beforeTime, timeDiff, sleep;

    beforeTime = System.currentTimeMillis();

    while (true) {
      /* If there is no car on the highway, then we do not call the repaint() */
      if(link.size() != 0 || link2.size() != 0){
        repaint();
      }
      else{
        //System.out.println("no car");
        try{Thread.sleep(1000);}
        catch(InterruptedException e){}
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
    }
  }

	private class BeginGoActionListener implements ActionListener{
		public void actionPerformed(ActionEvent e){
			
      if(highway.isCarOn(0,1) == true){
        System.out.println("could not create a car");
        return;
      }
      Car newcar = new Car(0, true);
			link.add(newcar);
      /* the new thread starts running */
      newcar.start();
      //System.out.println("link.add(new Car(0))");
      //System.out.println("link.size() = "+link.size());
		}
	}
	public BeginGoActionListener getBeginGoActionListener(){
		return new BeginGoActionListener();
	}
	private class EndGoActionListener implements ActionListener{
		public void actionPerformed(ActionEvent e){
			if(highway.isCarOn(wayLen-1,wayLen-2) == true){
        System.out.println("could not create a car");
        return;
      }
      Car newcar = new Car(wayLen-1, false);
      link2.add(newcar);
      /* the new thread starts running */
      newcar.start();
      //System.out.println("add new car!");
      //System.out.println("link2.size() = "+link2.size());
		}
	}
	public EndGoActionListener getEndGoActionListener(){
		return new EndGoActionListener();
	}
}
