package gui;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import info.About;
import objects.AccountManager;
import objects.HomeFile;
import objects.Room;

/**
 * Homeowner's Manual PRO GUI class. Handles creation of GUI frame and elements.
 * 
 * @author Collin Nguyen
 * @version 1.0
 */
public class GUI extends JFrame{

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -4328079188596764858L;

	/**
	 * Account manager for the program
	 */
	AccountManager myManager;
	
	/**
	 * Main JFrame
	 */
	private GUI myFrame;	
	
	/**
	 * Selected file
	 */
	private HomeFile myFile;
	
	/**
	 * Set of files from selected room
	 */
	private Set<HomeFile> myFiles;
	
	/**
	 * Selected room
	 */
	private Room myRoom;
	
	/**
	 * List of files
	 */
	private DefaultListModel<HomeFile> listModel;
	
	/**
	 * List of files (visible list)
	 */
	private JList<HomeFile> fileList;
	
	/**
	 * Text area that holds the name of the file
	 */
	private JTextArea titleArea;
	
	/**
	 * Text area that holds the creation time of the file
	 */
	private JTextArea createdArea;
	
	/**
	 * Text area that holds the notes of the file
	 */
	private JTextArea notesArea;
	
	/**
	 * Panel that holds the big file buttons
	 */
	private JPanel filesArea;
	
	/**
	 * Indicates if a file is being deleted to prevent issues
	 */
	private boolean isDeleting;
	
	/**
	 * Drop down menu that holds the rooms
	 */
	private JComboBox<Room> roomBox;
	
	/**
	 * Room for the entire house (not visible)
	 */
	private Room House;
	
	/**
	 * Self
	 */
	private GUI Self = this;
	
	/**
	 * Parameterless constructor
	 * @author Collin Nguyen
	 */
	public GUI() {
		super("Homeowner's Manual PRO");
		House = new Room("House");
		/*** ENABLE THIS LINE TO WIPE ALL ROOMS UPON STARTUP ***/
		//Room.saveRoom(House);
		House = Room.loadRoom("House");
	}
	
	/**
	 * Start JFrame
	 * @throws IOException 
	 * @author Collin Nguyen
	 */
	public void start(AccountManager theManager) throws IOException {
		myManager = theManager;
		myFrame = new GUI();
		myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		myFrame.setLayout(new BorderLayout());
		
		myFrame.add(generateMenuBar(), BorderLayout.NORTH);
		myFrame.add(generateMainPanel(), BorderLayout.CENTER);
		
		myFrame.pack();
		myFrame.setVisible(true);
		myFrame.setLocationRelativeTo(null);
	}
	
	/**
	 * Generates main panel.
	 * 
	 * @return the generated main panel
	 * @author Collin Nguyen
	 */
	private JPanel generateMainPanel() {
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new FlowLayout());
		mainPanel.add(generateLeftPanel());
		mainPanel.add(generateCenterPanel());
		mainPanel.add(generateRightPanel());
		return mainPanel;
	}
	
	/**
	 * Generates the menu bar.
	 * 
	 * @return the generated menu bar
	 * @throws IOException 
	 * @author Collin Nguyen
	 */
	private JMenuBar generateMenuBar() throws IOException {
		JMenuBar menuBar = new JMenuBar();
		JMenu fileMenu = new JMenu("File");
		JMenu roomMenu = new JMenu("Room");
		JMenu optionsMenu = new JMenu("Options");
		
		menuBar.add(fileMenu);
		menuBar.add(roomMenu);
		menuBar.add(optionsMenu);
		
		JMenuItem deleteFile = new JMenuItem("Delete");
		JMenuItem renameFile = new JMenuItem("Rename");
		JMenuItem deleteRoom = new JMenuItem("Delete");
		JMenuItem renameRoom = new JMenuItem("Rename");

		roomMenu.add(renameRoom);
		roomMenu.add(deleteRoom);
		
		deleteFile.addActionListener(new deleteFileListener());
		deleteRoom.addActionListener(new deleteRoomListener());
		
		renameFile.addActionListener(new renameFileButtonListener());
		renameRoom.addActionListener(new renameRoomButtonListener());
		
		
		JMenuItem importItem = new JMenuItem("Import");
		JMenuItem exportItem = new JMenuItem("Export");
		fileMenu.add(importItem);
		fileMenu.add(exportItem);
		fileMenu.addSeparator();
		fileMenu.add(renameFile);
		fileMenu.add(deleteFile);
		
		importItem.addActionListener(new ImportButtonListener());
		
		exportItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(myFile == null) {
					JOptionPane.showMessageDialog(myFrame,
						    "Please select a room before Importing a file",
						    "Error",
						    JOptionPane.ERROR_MESSAGE);
				}
				else {
					final JFileChooser chooser = new JFileChooser();
					int result = chooser.showSaveDialog(new JFrame());
					if(result == JFileChooser.APPROVE_OPTION) {	
						String extension = myFile.getExtension();
						HomeFile saveFile = new HomeFile(chooser.getSelectedFile().getAbsolutePath());
						if(!saveFile.getName().endsWith(extension)) {
							saveFile = new HomeFile(saveFile.getParentFile(), 
									saveFile.getName() + extension);
						}
						try {
							copyFile(myFile,saveFile);
						} catch (IOException e1) {
							e1.printStackTrace();
						}

					}
				}
			}
		});
		
		
		
		JMenuItem aboutItem = new JMenuItem("About");
		JMenuItem userItem = new JMenuItem("Users");
		optionsMenu.add(aboutItem);
		optionsMenu.add(userItem);
		
		About aboutBox = new About();
		
		aboutItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				aboutBox.show();
			}
			
        });
		
		userItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					new UsersWindow().start(myManager);
				} catch(IOException ex) {
					ex.printStackTrace();
				}
			}
			
        });
		
		return menuBar;
	}
	
	/**
	 * Generates the left side panel.
	 * 
	 * @return the generated panel
	 * @author Collin Nguyen
	 */
	private JPanel generateLeftPanel() {
		JPanel panel = new JPanel();
		JPanel top = new JPanel();
		JPanel middle = new JPanel();
		JPanel bottom = new JPanel();
		
		/**** This is how I'm getting the + button to the right of the roomBox - Romi  ****/
		JPanel selectorPanel = new JPanel();
		JPanel addPanel = new JPanel();
		
		selectorPanel.setMinimumSize(new Dimension(154,23));
		addPanel.setMinimumSize(new Dimension(40,25));
		
		selectorPanel.setMaximumSize(new Dimension(154,23));
		addPanel.setMaximumSize(new Dimension(40,25));
		
		selectorPanel.setLayout(new BorderLayout());
		addPanel.setLayout(new BorderLayout());
		/********/
		
		panel.setBorder(new EmptyBorder(0, 10, 0, 10));
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setPreferredSize(new Dimension(194, 600));

		top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));
		middle.setLayout(new BoxLayout(middle, BoxLayout.Y_AXIS));
		bottom.setLayout(new BoxLayout(bottom, BoxLayout.Y_AXIS));
		
		top.setMaximumSize(new Dimension(194, 50));
		
		roomBox = new JComboBox<Room>();
		JPanel roomPanel = new JPanel();
		JButton addRoomButton = new JButton("+");
		
		
		panel.setBorder(new EmptyBorder(0, 10, 0, 10));
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setPreferredSize(new Dimension(194, 600));

		top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));
		middle.setLayout(new BoxLayout(middle, BoxLayout.Y_AXIS));
		bottom.setLayout(new BoxLayout(bottom, BoxLayout.Y_AXIS));
		
		top.setMaximumSize(new Dimension(194, 50));	
		
		roomPanel.setLayout(new BoxLayout(roomPanel,BoxLayout.X_AXIS));
		roomPanel.setPreferredSize(new Dimension(194,25));
		roomPanel.setMinimumSize(new Dimension(194,25));
		roomPanel.setMaximumSize(new Dimension(194,25));
		roomPanel.add(selectorPanel);
		roomPanel.add(addPanel);
		
		selectorPanel.add(roomBox);
		addPanel.add(addRoomButton);
		
		addRoomButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				RoomName getNameGUI = new RoomName();
				getNameGUI.start(roomBox, House, Self);
			}
		
		});
		top.add(roomPanel);
		
		top.add(Box.createRigidArea(new Dimension(0, 10)));
		JTextField searchBar = new JTextField("Search");
		searchBar.setMaximumSize(new Dimension(194, 20));
		searchBar.setMinimumSize(new Dimension(194, 20));
		searchBar.setPreferredSize(new Dimension(194, 20));
		top.add(searchBar);
		
		searchBar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (searchBar.getText() == "") {
			        listModel.removeAllElements();
			        for (HomeFile h : myRoom.getFiles()) {
			        	listModel.addElement(h);
			        }
			        fileList.setSelectedIndex(0);
			        updateDisplay();
			        clearInfoArea();	
				} else {
					Set<HomeFile> s = House.filterFiles(searchBar.getText());
					listModel.removeAllElements();
			        for (HomeFile h : s) {
			        	listModel.addElement(h);
			        }
			        fileList.setSelectedIndex(0);
			        updateDisplay(s);
			        clearInfoArea();
				}
			}
		
		});
		
		searchBar.addFocusListener(new FocusAdapter() {
		    public void focusGained(FocusEvent e) {
		        JTextField source = (JTextField) e.getComponent();
		        if(source.getText().trim().equals("Search")) {
		        	source.setText("");
		    	}
		    }
		    public void focusLost(FocusEvent e) {
		    	JTextField source = (JTextField) e.getComponent();
		        if(source.getText().trim().equals("")) {
		        	source.setText("Search");
		    	}
		    }
		});
		
		listModel = new DefaultListModel<>();
		fileList = new JList<HomeFile>(listModel);
		fileList.setMaximumSize(new Dimension(194, 500));
		fileList.setMinimumSize(new Dimension(194, 0));
		fileList.setPreferredSize(new Dimension(194, 500));
		middle.add(fileList);
		
		if(House.getSubRooms().size() == 0) {
			roomBox.setEnabled(false);
		}
		
		for(Room r : House.getSubRooms())
		{
			roomBox.addItem(r);
		}	
		
		if(House.getSubRooms().size()>0) {		
			roomBox.setSelectedIndex(0);
			myRoom = (Room) roomBox.getSelectedItem();
			
		} else {
			myRoom = House;
		}
		      
        myFiles = myRoom.getFiles();
        if(myFiles.size()>0) {
        	 fileList.setSelectedIndex(0);
        }
        myFile = (HomeFile) fileList.getSelectedValue();
        for (HomeFile h : myRoom.getFiles()) {
        	listModel.addElement(h);
        }
		
		// End
		
        /**SWITCHING ROOMS ACTION**/
		roomBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(House.getSubRooms().size() > 0) {
					JComboBox<?> box = (JComboBox<?>) e.getSource();
			        myRoom = (Room) box.getSelectedItem();
			        myFiles = myRoom.getFiles();
			        myFile = null;
			        updateDisplay();
			        listModel.removeAllElements();
			        for (HomeFile h : myRoom.getFiles()) {
			        	listModel.addElement(h);
			        }
			        clearInfoArea();	
				}
			}
        });
		
		fileList.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (!e.getValueIsAdjusting() && !roomBox.isFocusOwner() && !isDeleting) {
						myFile = (HomeFile) fileList.getSelectedValue();
						if(myFile != null) {
							titleArea.setText(myFile.toString());
							createdArea.setText(myFile.getImportDate());
							notesArea.setText(myFile.getFileNotes());
						} else {
							clearInfoArea();
						}
	                }
				
			}
        });
		
		JButton addFileButton = new JButton("New File");
		addFileButton.setMaximumSize(new Dimension(194, 40));
		addFileButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		addFileButton.addActionListener(new ImportButtonListener());
		
		JButton deleteButton = new JButton("Delete");
		deleteButton.setMaximumSize(new Dimension(194, 20));
		deleteButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		deleteButton.addActionListener(new deleteFileListener());
		
		bottom.add(addFileButton);
		bottom.add(Box.createRigidArea(new Dimension(0, 10)));
		bottom.add(deleteButton);
		
		panel.add(top);
		panel.add(Box.createRigidArea(new Dimension(0, 10)));
		panel.add(middle);
		panel.add(Box.createRigidArea(new Dimension(0, 10)));
		panel.add(bottom);
		return panel;
	}
	
	/**
	 * Generates the center panel.
	 * 
	 * @return the generated panel
	 * @author Collin Nguyen
	 */
	private JPanel generateCenterPanel() {
		JPanel panel = new JPanel();
		panel.setBorder(new EmptyBorder(10, 0, 10, 0));
		panel.setLayout(new BorderLayout());
		//panel.add(generateInfoPanel(), BorderLayout.NORTH);
		panel.add(generateDisplayPanel(), BorderLayout.CENTER);
		return panel;
	}
	
	/**
	 * Generates the right side panel.
	 * 
	 * @return the generated panel
	 * @author Collin Nguyen
	 */
	private JPanel generateRightPanel() {
		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension(294, 600));
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		notesArea = new JTextArea();
		//notesArea.setPreferredSize(new Dimension(225, 400));
		JButton saveButton = new JButton("Save");
		saveButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		saveButton.setMaximumSize(new Dimension(294, 20));
		
		saveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(myFile != null) {
					myFile.updateNote(notesArea.getText());
				} else {
					JOptionPane.showMessageDialog(myFrame,
						    "No file selected",
						    "Error",
						    JOptionPane.ERROR_MESSAGE);
				}
			}
        });
		
		JButton openButton = new JButton("Open");
		openButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		openButton.setPreferredSize(new Dimension(294, 100));
		openButton.setMinimumSize(new Dimension(294, 100));
		openButton.setMaximumSize(new Dimension(294, 100));
		
		openButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(myFile != null) {
					try {
						if(myFile.canOpen()) {
							myFile.open();
						} else {
							JOptionPane.showMessageDialog(myFrame,
								    "FILE NOT FOUND",
								    "Error",
								    JOptionPane.ERROR_MESSAGE);
						}
					} catch(Exception ex) {
						ex.printStackTrace();
					}
				}
			}
        });
		
		JPanel notePanel = new JPanel();
		notePanel.setLayout(new BoxLayout(notePanel, BoxLayout.Y_AXIS));
		notePanel.setBorder(BorderFactory.createCompoundBorder(new TitledBorder("Notes"), new EmptyBorder(2, 2, 2, 2)));
		notePanel.add(notesArea);
		notePanel.add(Box.createRigidArea(new Dimension(0, 5)));
		notePanel.add(saveButton);
		
		panel.setBorder(new EmptyBorder(0, 10, 0, 10));
		panel.add(openButton);
		panel.add(Box.createRigidArea(new Dimension(0, 10)));
		panel.add(generateInfoPanel());
		panel.add(Box.createRigidArea(new Dimension(0, 10)));
		panel.add(notePanel);
		return panel;
	}
	
	/**
	 * Generates the file display.
	 * 
	 * @return the generated panel
	 * @author Collin Nguyen
	 */
	private JPanel generateDisplayPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		FlowLayout layout = new FlowLayout(FlowLayout.LEFT, 0, 0);
		filesArea = new JPanel();
		filesArea.setOpaque(true);
		filesArea.setBackground(Color.WHITE);
		filesArea.setLayout(layout);
		updateDisplay();
		JScrollPane scrollPane = new JScrollPane(filesArea, 
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.getVerticalScrollBar().setUnitIncrement(12);
		panel.add(scrollPane, BorderLayout.CENTER);
		panel.setPreferredSize(new Dimension(600, 600));
		return panel;
	}
	
	/**
	 * Generates the panel that holds file information.
	 * 
	 * @return the generated panel
	 * @author Collin Nguyen
	 */
	private JPanel generateInfoPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setMaximumSize(new Dimension(294, 180));
		JLabel title = new JLabel("Name:");
		title.setAlignmentX(Component.CENTER_ALIGNMENT);
		titleArea = new JTextArea();
		titleArea.setEditable(false);
		JLabel created = new JLabel("Created on:");
		created.setAlignmentX(Component.CENTER_ALIGNMENT);
		createdArea = new JTextArea();
		createdArea.setEditable(false);
		
		JLabel permissions = new JLabel("Editing Permissions:");
		permissions.setAlignmentX(Component.CENTER_ALIGNMENT);
		String[] perms = {"Only Me", "Everyone"};
		JComboBox<String> permissionsBox = new JComboBox<>(perms);
		
		JPanel p1 = new JPanel();
		JPanel p2 = new JPanel();
		JPanel p3 = new JPanel();
		p1.setLayout(new BoxLayout(p1, BoxLayout.Y_AXIS));
		p2.setLayout(new BoxLayout(p2, BoxLayout.Y_AXIS));
		p3.setLayout(new BoxLayout(p3, BoxLayout.Y_AXIS));
		p1.setBorder(BorderFactory.createCompoundBorder(new TitledBorder("Name"), new EmptyBorder(2, 2, 2, 2)));
		p2.setBorder(BorderFactory.createCompoundBorder(new TitledBorder("Created On"), new EmptyBorder(2, 2, 2, 2)));
		p3.setBorder(BorderFactory.createCompoundBorder(new TitledBorder("Editing Permissions"), new EmptyBorder(2, 2, 2, 2)));
		
		p1.add(titleArea);
		p2.add(createdArea);
		p3.add(permissionsBox);
		
		panel.add(p1);
		panel.add(p2);
		panel.add(p3);
		
		return panel;
	}
	
	/**
	 * Updates the file display
	 * @author Collin Nguyen
	 */
	public void updateDisplay() {
		filesArea.removeAll();
		filesArea.revalidate();
		filesArea.repaint();
		for(HomeFile h : myFiles) {
			filesArea.add(generateFileButton(h));
		}
		filesArea.revalidate();
		filesArea.repaint();
		filesArea.setPreferredSize(new Dimension(600, 193*(myFiles.size()+2)/2));
	}
	
	/**
	 * Updates the file display with a given set
	 * @author Collin Nguyen
	 */
	public void updateDisplay(Set<HomeFile> theSet) {
		filesArea.removeAll();
		filesArea.revalidate();
		filesArea.repaint();
		for(HomeFile h : theSet) {
			filesArea.add(generateFileButton(h));
		}
		filesArea.revalidate();
		filesArea.repaint();
		filesArea.setPreferredSize(new Dimension(600, 193*(myFiles.size()+2)/2));
	}
	
	
	
	/**
	 * Generates big file buttons for the display
	 * 
	 * @return the generated button
	 * @author Collin Nguyen
	 */
	private JButton generateFileButton(HomeFile theFile) {
		JButton button = new JButton(theFile.toString());
		button.setPreferredSize(new Dimension(193, 193));
		button.setBackground(Color.white);
		button.setOpaque(true);
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				 myFile = theFile;
				 fileList.setSelectedValue(myFile, true);
			}			
		});
		return button;
	}
	
	/**
	 * Method for clearing the info area
	 * @author Romi Tshiorny
	 */
	private void clearInfoArea() {
		titleArea.setText("");
		createdArea.setText("");
		notesArea.setText("");
	}
	
	/**
	 * Private method for copying a file from one to another
	 * @author Romi Tshiorny
	 * @author Pankaj from https://www.journaldev.com/861/java-copy-file
	 * @param source file
	 * @param dest file
	 * @throws IOException
	 */
	private static void copyFile(HomeFile source, HomeFile dest) throws IOException {
	    Files.copy(source.toPath(), dest.toPath(),StandardCopyOption.REPLACE_EXISTING);
	}
	
	/**
	 * Private action listener for importing files
	 * @author Romi Tshiorny
	 *
	 */
	private class ImportButtonListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			if(House.getSubRooms().size() == 0) {
				JOptionPane.showMessageDialog(myFrame,
					    "Please select a room before Importing a file",
					    "Error",
					    JOptionPane.ERROR_MESSAGE);
			}
			else {
				final JFileChooser chooser = new JFileChooser();
				int result = chooser.showOpenDialog(new JFrame());
				if(result == JFileChooser.APPROVE_OPTION) {	
					HomeFile newFile = new HomeFile(chooser.getSelectedFile().getAbsolutePath());
					listModel.addElement(newFile);
					myRoom.addFile(newFile);
					myRoom = House.findRoom(myRoom.getRoomName());
					myRoom.addFile(newFile);
					Room.saveRoom(House);
					updateDisplay();
					//House.printRoom();
				}
			}
		}
		
	}
	
	/**
	 * Private action listener for deleting files
	 * @author Collin Nguyen
	 *
	 */
	private class deleteFileListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			if(myFile != null) {
				isDeleting = true;
				String choiceButtons[] = {"Yes","No"};
				int result = JOptionPane.showOptionDialog(null,"Are you sure you want to delete this file?","Confirm Deletion",
			        		JOptionPane.DEFAULT_OPTION,JOptionPane.WARNING_MESSAGE,null, choiceButtons, choiceButtons[1]);
			    if (result == 0) {
			    	myRoom.removeFile((HomeFile) fileList.getSelectedValue());
					listModel.removeElement(fileList.getSelectedValue());
				    updateDisplay();
				    clearInfoArea();
				    myFile = null;
				    Room.saveRoom(House);
			    }
			    isDeleting = false;
			} 
			else {
				JOptionPane.showMessageDialog(myFrame,
					    "No file selected.",
					    "Error",
					    JOptionPane.ERROR_MESSAGE);
			}
		}			
	}
	
	/**
	 * Private action listener for deleting rooms
	 * @author Collin Nguyen
	 *
	 */
	private class deleteRoomListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			if(myRoom != null) {
				String choiceButtons[] = {"Yes","No"};
				int result = JOptionPane.showOptionDialog(null,"Are you sure you want to delete this Room?","Confirm Deletion",
			        		JOptionPane.DEFAULT_OPTION,JOptionPane.WARNING_MESSAGE,null, choiceButtons, choiceButtons[1]);
			    if (result == 0) {
					//System.out.println("Removing " + ((Room)roomBox.getSelectedItem()).getRoomName());
					House.removeRoom(((Room)roomBox.getSelectedItem()));
					roomBox.removeItemAt(roomBox.getSelectedIndex());
					myRoom = null;
					if(House.getSubRooms().size() == 0) {
						roomBox.setEnabled(false);
					}
					updateDisplay();
					Room.saveRoom(House);
					//House.printRoom();
			    }
			} 
			else {
				JOptionPane.showMessageDialog(myFrame,
					    "No room selected.",
					    "Error",
					    JOptionPane.ERROR_MESSAGE);
			}
		}			
	}
	
	/**
	 * Private action listener for renaming files
	 * @author Collin Nguyen
	 *
	 */
	private class renameFileButtonListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			if(myFile != null) {
				FileName renameFile = new FileName();
				renameFile.start(roomBox, House, fileList, listModel, Self);
				updateDisplay();
				
			} 
			else {
				JOptionPane.showMessageDialog(myFrame,
					    "No file selected.",
					    "Error",
					    JOptionPane.ERROR_MESSAGE);
			}
		}			
	}
	
	/**
	 * Method for closing the GUI
	 * @throws IOException 
	 */
	public void close() throws IOException {
		myFrame.dispatchEvent(new WindowEvent(myFrame, WindowEvent.WINDOW_CLOSING));
	}
	
	/**
	 * Private action listener for renaming Rooms
	 * @author Romi Tshiorny
	 *
	 */
	private class renameRoomButtonListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			if(myRoom != null) {
				RoomRename renameUI = new RoomRename();
				renameUI.start(roomBox, House);
			} 
			else {
				JOptionPane.showMessageDialog(myFrame,
					    "No Room Selected.",
					    "Error",
					    JOptionPane.ERROR_MESSAGE);
			}
		}			
	}
	
}

