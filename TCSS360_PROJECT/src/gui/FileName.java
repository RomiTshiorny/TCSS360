package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import objects.HomeFile;
import objects.Room;

/**
 * Jframe for Room name selection
 * @author Romi Tshiorny
 * @author Collin Nguyen
 *
 */
public class FileName extends JFrame{

	/**
	 * Auto generated UID
	 */
	private static final long serialVersionUID = 6114087212474971239L;

	/**
	 * Main frame displayed
	 */
	private FileName myFrame;
	
	/**
	 * the Name that the room will be named
	 */
	private String myName;
	
	/**
	 * Main GUI
	 */
	private GUI parentGUI;
	
	public FileName() {
		super("Set your File Name");
	}
	
	/**
	 * Method for starting the GUI
	 * @param listModel 
	 */
	public void start(JComboBox<Room> roomBox, Room House, JList<HomeFile> fileList, DefaultListModel<HomeFile> listModel, GUI parent){
		parentGUI = parent;
		myFrame = new FileName();
		myFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		myFrame.setLayout(new BorderLayout());
		myFrame.setMinimumSize(new Dimension(300,150));
		
		
		myFrame.add(createMainPanel(roomBox, House, fileList, listModel));
		
		myFrame.pack();
		myFrame.setVisible(true);
		myFrame.setLocationRelativeTo(null);
	}

	/**
	 * Creates the main panel
	 * @author Romi Tshiorny
	 * @author Collin Nguyen
	 * @return the Panel
	 */
	private JPanel createMainPanel(JComboBox<Room> roomBox, Room House, JList<HomeFile> fileList,  DefaultListModel<HomeFile> listModel) {
		JPanel mainPanel = new JPanel();
		JPanel fieldPanel = new JPanel();
		JPanel buttonPanel = new JPanel();
		
		JTextField roomName = new JTextField();
		JButton setButton = new JButton("Set Name");
		JButton cancelButton = new JButton("Cancel");
		
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		fieldPanel.setLayout(new BoxLayout(fieldPanel, BoxLayout.Y_AXIS));
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
				
		mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		fieldPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		buttonPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

		roomName.setMargin(new Insets(2,2,2,2));
		
		setButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				myName = roomName.getText();
				renameFile(roomBox, House, fileList, listModel);
				myFrame.dispatchEvent(new WindowEvent(myFrame, WindowEvent.WINDOW_CLOSING));
			}
        });
		
        cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				myFrame.dispatchEvent(new WindowEvent(myFrame, WindowEvent.WINDOW_CLOSING));
			}
        });
		
		fieldPanel.add(roomName);
		
		buttonPanel.add(Box.createRigidArea(new Dimension(10, 0)));
		buttonPanel.add(setButton);
		buttonPanel.add(Box.createRigidArea(new Dimension(10, 0)));
		buttonPanel.add(cancelButton);
		
		mainPanel.add(fieldPanel);
		mainPanel.add(buttonPanel);
		
		return mainPanel;
	}
	
	/**
	 * Renames file
	 * @author Collin Nguyen
	 */
	private void renameFile(JComboBox<Room> roomBox,Room House, JList<HomeFile> fileList,  DefaultListModel<HomeFile> listModel) {
		// This will need to be implemented somehow
		 fileList.getSelectedValue().rename(myName);
		 House.findFile(fileList.getSelectedValue()).rename(myName);
		 Room myRoom = (Room) roomBox.getSelectedItem();
		 listModel.removeAllElements();
	        for (HomeFile h : myRoom.getFiles()) {
	        	listModel.addElement(h);
	        }
		 Room.saveRoom(House);
		 //House.printRoom();
		 parentGUI.updateDisplay();
	}
	
}
