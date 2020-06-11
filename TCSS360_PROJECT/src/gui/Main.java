package gui;

import java.awt.EventQueue;
import java.io.IOException;

import javax.swing.UIManager;


/**
 * Homeowner's Manual PRO main class. Starts gui.
 * 
 * @author Team Quasars
 * @version %I%
 */
public class Main {

	private Main() {
		throw new IllegalStateException();
	}
	
	public static void main(final String[] theArgs) {
		try { 
			  
	            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); 
	        } catch (Exception e) { 
	            System.out.println("Look and Feel not set"); 
	        } 
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
					new Login().start();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        });
    }
	
}
