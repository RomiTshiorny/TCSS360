package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import objects.Room;

/**
 * Jframe for Room name selection
 * @author Romi Tshiorny
 *
 */
public class RoomName extends JFrame{

	/**
	 * Auto generated UID
	 */
	private static final long serialVersionUID = 6114087212474971239L;

	/**
	 * Main frame displayed
	 */
	private RoomName myFrame;
	
	
	/**
	 * Main GUI
	 */
	private GUI gui;
	
	/**
	 * the Name that the room will be named
	 */
	private String myName;
	
	public RoomName() {
		super("Set your Room Name");
	}
	
	/**
	 * Method for starting the GUI
	 */
	public void start(JComboBox<Room> roomBox, Room House, GUI mygui){
		gui = mygui;
		myFrame = new RoomName();
		myFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		myFrame.setLayout(new BorderLayout());
		myFrame.setMinimumSize(new Dimension(300,150));
		
		
		myFrame.add(createMainPanel(roomBox, House));
		
		myFrame.pack();
		myFrame.setVisible(true);
		myFrame.setLocationRelativeTo(null);
	}

	/**
	 * Creates the main panel
	 * @author Romi Tshiorny
	 * @return the Panel
	 */
	private JPanel createMainPanel(JComboBox<Room> roomBox, Room House) {
		JPanel mainPanel = new JPanel();
		JPanel fieldPanel = new JPanel();
		JPanel buttonPanel = new JPanel();
		JPanel label = new JPanel();
		JTextField roomName = new JTextField();
		JButton setButton = new JButton("Set Name");
		JButton cancelButton = new JButton("Cancel");
		
		JLabel note = new JLabel("NOTICE: PLEASE RESTART AFTER CREATING ROOM");
		label.add(note);
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
				addRoomWithName(roomBox,House);
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
		mainPanel.add(label);
		
		return mainPanel;
	}
	
	/**
	 * Gets the set name.
	 * @author Romi Tshiorny
	 */
	private void addRoomWithName(JComboBox<Room> roomBox, Room House) {
		Room newRoom = new Room(myName);
		roomBox.addItem(newRoom);
		roomBox.setSelectedItem(newRoom);
		House.addRoom(newRoom.getRoomName());
		roomBox.setEnabled(true);
		Room.saveRoom(House);
		try {
			gui.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//House.printRoom();
	}
}
