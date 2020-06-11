package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import objects.AccountManager;
import objects.User;

public class UsersWindow extends JFrame{

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 2602437861988302517L;
	
	/**
	 * Account manager for the program
	 */
	private AccountManager myManager;
	
	/**
	 * Main JFrame
	 */
	private UsersWindow myFrame;
	
	/**
	 * Currently selected user
	 */
	private User selectedUser;
	
	/**
	 * Constant delete account button
	 */
	final private JButton deleteAccountButton = new JButton("Delete");
	
	/**
	 * Constant give admin button
	 */
	final private JButton giveAdminButton = new JButton("Toggle admin status");
	/**
	 * List model
	 */
	DefaultListModel<User> listModel;
	/**
	 * Parameterless constructor
	 */
	public UsersWindow() {
		super("Modify Users");			
	}
	
	/**
	 * Start JFrame
	 * @throws IOEXception
	 * @author Romi Tshiorny
	 */
	public void start(AccountManager theManager) throws IOException
	{
		myManager = theManager;
		myFrame = new UsersWindow();
		myFrame.setLayout(new BorderLayout());	
		myFrame.add(createMainPanel(), BorderLayout.CENTER);
		myFrame.pack();
		myFrame.setVisible(true);
		myFrame.setLocationRelativeTo(null);
	}
	
	
	/**
	 * Create main panel
	 * @author Romi Tshiorny
	 */
	private JPanel createMainPanel() {
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		mainPanel.add(createUsersPanel(), BorderLayout.CENTER);
		mainPanel.add(generateButtons(), BorderLayout.SOUTH);
		mainPanel.add(createTopPanel(),BorderLayout.NORTH);
		return mainPanel;
	}
	
	private JPanel createTopPanel() {
		JPanel top = new JPanel();
		JLabel currentUser = new JLabel("Logged in as: " +
				myManager.getCurrentUser().toString());
		top.add(currentUser);
		top.add(Box.createRigidArea(new Dimension(10, 0)));
		return top;
	}
	
	/**
	 * Create panel of users
	 * @author Romi Tshiorny
	 */
	private JList<User> createUsersPanel() {
		listModel = new DefaultListModel<User>();
		JList<User> users = new JList<User>(listModel);
		users.setLayout((new BoxLayout(users, BoxLayout.Y_AXIS)));
		for(User u : myManager.getListOfUsers())
		{
				if(!u.equals(myManager.getCurrentUser()))
				{
					listModel.addElement(u);
				}
		}
		users.setMaximumSize(new Dimension(225,100));
		users.setPreferredSize(new Dimension(225,100));
		
		users.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (!e.getValueIsAdjusting()) {
						selectedUser = (User) users.getSelectedValue();
	                }	
				if(myManager.getCurrentUser().getAdminStat()) {
					deleteAccountButton.setEnabled(true);
					giveAdminButton.setEnabled(true);
				}
			}
        });
		return users;
		
	}
	
	private JPanel generateButtons() {
		JPanel bottom = new JPanel();
		bottom.setLayout(new FlowLayout());
		giveAdminButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				selectedUser.toggleAdmin();
				myFrame.repaint();
				myManager.serialize();
				
				
			}});
		deleteAccountButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				myManager.deleteUser(selectedUser);
				listModel.removeElement(selectedUser);	
			}});
		
		deleteAccountButton.setEnabled(false);
		giveAdminButton.setEnabled(false);
		bottom.add(deleteAccountButton);
		bottom.add(giveAdminButton);
		return bottom;
	}	

}
