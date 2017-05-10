
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

import lejos.pc.comm.NXTConnector;


public class NXTremoteControl_TA extends JFrame
{
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static JButton quit, connect, disconnect;
	private static JButton forward;
	private static ButtonHandler bh = new ButtonHandler();
	private static DataOutputStream outData;
	private static DataInputStream inData;
	private static NXTConnector link;
  
  public NXTremoteControl_TA()
  { 
    setTitle ("Control");
    setBounds(650,350,500,500);
    setLayout(new GridLayout(1,4));
   
    forward = new JButton("Action");
    forward.addActionListener(bh);
    forward.addKeyListener(bh);
    add(forward);

    connect = new JButton(" Connect ");
    connect.addActionListener(bh);
    connect.addKeyListener(bh);
    add(connect);

    disconnect = new JButton(" Disconnect ");
    disconnect.addActionListener(bh);
    disconnect.addKeyListener(bh);
    add(disconnect);
    
    quit = new JButton("Quit");
    quit.addActionListener(bh);
    add(quit);

  }
  
  public static void main(String[] args)
  {
     NXTremoteControl_TA NXTrc = new NXTremoteControl_TA();
     NXTrc.setVisible(true);
     NXTrc.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
  }//End main
  
  private static class ButtonHandler implements ActionListener, KeyListener
  {

    public void actionPerformed(ActionEvent ae)
    {
      if(ae.getSource() == quit)  {System.exit(0);}
      if(ae.getSource() == connect) {connect();}
      if(ae.getSource() == disconnect) {disconnect();}
      
      if (outData != null)
	      try{
	         if(ae.getSource() == forward) {outData.write(1);} 
	         outData.flush();
	        } 
	         catch (IOException ioe) {
	        System.out.println("\nIO Exception writeInt");
	         }
      
     }//End ActionEvent(for buttons)

   public void keyPressed(KeyEvent ke) {}
  

   public void keyTyped(KeyEvent ke)  {
	   if (outData != null) {
		   int key = ke.getKeyChar();
		   final int callbackMethodNo = 1;
		   
		   try {
			   
		       outData.write(callbackMethodNo);
		       outData.writeFloat((float) key);
		       outData.flush();
		       
		   } catch (IOException ioe) {
			   System.out.println("IO Exception write.");
		   }
		   
	   }
		      
		        
	}//End keyTyped
   
   public void keyReleased(KeyEvent ke) {}

  }//End ButtonHandler
  
  public static void connect()
  {
     link = new NXTConnector();
    
     if (!link.connectTo("btspp://"))
     {
        System.out.println("\nNo NXT find using USB");
     }
     
     outData = new DataOutputStream(link.getOutputStream());
     inData = new DataInputStream(link.getInputStream());
     System.out.println("\nNXT is Connected");   
     
  }//End connect
  
  public static void disconnect()
  {
     try{
    	if (outData != null)
    		outData.close();
    	if (link != null)
    		link.close();
        } 
     catch (IOException ioe) {
        System.out.println("\nIO Exception writing bytes");
     }
     System.out.println("\nClosed data streams");
     
  }//End disconnect
  
}//End ControlWindow class