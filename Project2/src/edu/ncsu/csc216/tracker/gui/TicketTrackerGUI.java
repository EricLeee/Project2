package edu.ncsu.csc216.tracker.gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.AbstractTableModel;

import edu.ncsu.csc216.tracker.command.Command;
import edu.ncsu.csc216.tracker.command.Command.CommandValue;
import edu.ncsu.csc216.tracker.command.Command.Flag;
import edu.ncsu.csc216.tracker.ticket.TrackedTicket;
import edu.ncsu.csc216.tracker.ticket_tracker.TicketTrackerModel;

/**
 * Container for the TicketTracker that has the menu options for new ticket 
 * tracking files, loading existing files, saving files and quitting.
 * Depending on user actions, other {@link JPanel}s are loaded for the
 * different ways users interact with the UI.
 * 
 * @author Dr. Sarah Heckman (heckman@csc.ncsu.edu)
 */
public class TicketTrackerGUI extends JFrame implements ActionListener {
	
	/** ID number used for object serialization. */
	private static final long serialVersionUID = 1L;
	/** Title for top of GUI. */
	private static final String APP_TITLE = "Ticket Tracker";
	/** Text for the File Menu. */
	private static final String FILE_MENU_TITLE = "File";
	/** Text for the New Ticket XML menu item. */
	private static final String NEW_XML_TITLE = "New";
	/** Text for the Load Ticket XML menu item. */
	private static final String LOAD_XML_TITLE = "Load";
	/** Text for the Save menu item. */
	private static final String SAVE_XML_TITLE = "Save";
	/** Text for the Quit menu item. */
	private static final String QUIT_TITLE = "Quit";
	/** Menu bar for the GUI that contains Menus. */
	private JMenuBar menuBar;
	/** Menu for the GUI. */
	private JMenu menu;
	/** Menu item for creating a new file containing {@link TrackedTicket}s. */
	private JMenuItem itemNewTicketXML;
	/** Menu item for loading a file containing {@link TrackedTicket}s. */
	private JMenuItem itemLoadTicketXML;
	/** Menu item for saving the ticket list. */
	private JMenuItem itemSaveTicketXML;
	/** Menu item for quitting the program. */
	private JMenuItem itemQuit;
	/** Panel that will contain different views for the application. */
	private JPanel panel;
	/** Constant to identify TicketListPanel for {@link CardLayout}. */
	private static final String TICKET_LIST_PANEL = "TicketListPanel";
	/** Constant to identify NewPanel for {@link CardLayout}. */
	private static final String NEW_PANEL = "NewPanel";
	/** Constant to identify AssignedPanel for {@link CardLayout}. */
	private static final String ASSIGNED_PANEL = "AssignedPanel";
	/** Constant to identify WorkingPanel for {@link CardLayout}. */
	private static final String WORKING_PANEL = "WorkingPanel";
	/** Constant to identify FeedbackPanel for {@link CardLayout}. */
	private static final String FEEDBACK_PANEL = "FeedbackPanel";
	/** Constant to identify ClosedPanel for {@link CardLayout}. */
	private static final String CLOSED_PANEL = "ClosedPanel";
	/** Constant to identify CreateTicketPanel for {@link CardLayout}. */
	private static final String CREATE_TICKET_PANEL = "CreateTicketPanel";
	/** Ticket List panel - we only need one instance, so it's final. */
	private final TicketListPanel pnlTicketList = new TicketListPanel();
	/** New panel - we only need one instance, so it's final. */
	private final NewPanel pnlNew = new NewPanel();
	/** Assigned panel - we only need one instance, so it's final. */
	private final AssignedPanel pnlAssigned = new AssignedPanel();
	/** Working panel - we only need one instance, so it's final. */
	private final WorkingPanel pnlWorking = new WorkingPanel();
	/** Feedback panel - we only need one instance, so it's final. */
	private final FeedbackPanel pnlFeedback = new FeedbackPanel();
	/** Closed panel - we only need one instance, so it's final. */
	private final ClosedPanel pnlClosed = new ClosedPanel();
	/** Add Ticket panel - we only need one instance, so it's final. */
	private final CreateTicketPanel pnlCreateTicket = new CreateTicketPanel();
	/** Reference to {@link CardLayout} for panel.  Stacks all of the panels. */
	private CardLayout cardLayout;
	
	
	/**
	 * Constructs a {@link TicketTrackerGUI} object that will contain a {@link JMenuBar} and a
	 * {@link JPanel} that will hold different possible views of the data in
	 * the {@link TicketTrackerModel}.
	 */
	public TicketTrackerGUI() {
		super();
		
		//Set up general GUI info
		setSize(500, 700);
		setLocation(50, 50);
		setTitle(APP_TITLE);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setUpMenuBar();
		
		//Create JPanel that will hold rest of GUI information.
		//The JPanel utilizes a CardLayout, which stack several different
		//JPanels.  User actions lead to switching which "Card" is visible.
		panel = new JPanel();
		cardLayout = new CardLayout();
		panel.setLayout(cardLayout);
		panel.add(pnlTicketList, TICKET_LIST_PANEL);
		panel.add(pnlNew, NEW_PANEL);
		panel.add(pnlAssigned, ASSIGNED_PANEL);
		panel.add(pnlWorking, WORKING_PANEL);
		panel.add(pnlFeedback, FEEDBACK_PANEL);
		panel.add(pnlClosed, CLOSED_PANEL);
		panel.add(pnlCreateTicket, CREATE_TICKET_PANEL);
		cardLayout.show(panel, TICKET_LIST_PANEL);
		
		//Add panel to the container
		Container c = getContentPane();
		c.add(panel, BorderLayout.CENTER);
		
		//Set the GUI visible
		setVisible(true);
	}
	
	/**
	 * Makes the GUI Menu bar that contains options for loading a file
	 * containing tickets or for quitting the application.
	 */
	private void setUpMenuBar() {
		//Construct Menu items
		menuBar = new JMenuBar();
		menu = new JMenu(FILE_MENU_TITLE);
		itemNewTicketXML = new JMenuItem(NEW_XML_TITLE);
		itemLoadTicketXML = new JMenuItem(LOAD_XML_TITLE);
		itemSaveTicketXML = new JMenuItem(SAVE_XML_TITLE);
		itemQuit = new JMenuItem(QUIT_TITLE);
		itemNewTicketXML.addActionListener(this);
		itemLoadTicketXML.addActionListener(this);
		itemSaveTicketXML.addActionListener(this);
		itemQuit.addActionListener(this);
		
		//Start with save button disabled
		itemSaveTicketXML.setEnabled(false);
		
		//Build Menu and add to GUI
		menu.add(itemNewTicketXML);
		menu.add(itemLoadTicketXML);
		menu.add(itemSaveTicketXML);
		menu.add(itemQuit);
		menuBar.add(menu);
		this.setJMenuBar(menuBar);
	}
	
	/**
	 * Performs an action based on the given {@link ActionEvent}.
	 * @param e user event that triggers an action.
	 */
	public void actionPerformed(ActionEvent e) {
		//Use TicketTrackerModel's singleton to create/get the sole instance.
		TicketTrackerModel model = TicketTrackerModel.getInstance();
		if (e.getSource() == itemNewTicketXML) {
			//Create a new ticket list
			model.createNewTicketList();
			itemSaveTicketXML.setEnabled(true);
			pnlTicketList.updateTable(null, null);
			cardLayout.show(panel, TICKET_LIST_PANEL);
			validate();
			repaint();			
		} else if (e.getSource() == itemLoadTicketXML) {
			//Load an existing ticket list
			try {
				model.loadTicketsFromFile(getFileName(true));
				itemSaveTicketXML.setEnabled(true);
				pnlTicketList.updateTable(null, null);
				cardLayout.show(panel, TICKET_LIST_PANEL);
				validate();
				repaint();
			} catch (IllegalArgumentException exp) {
				JOptionPane.showMessageDialog(this, "Unable to load ticket file.");
			} catch (IllegalStateException exp) {
				//Don't do anything - user canceled (or error)
			}
		} else if (e.getSource() == itemSaveTicketXML) {
			//Save current ticket list
			try {
				model.saveTicketsToFile(getFileName(false));
			} catch (IllegalArgumentException exp) {
				JOptionPane.showMessageDialog(this, "Unable to save ticket file.");
			} catch (IllegalStateException exp) {
				//Don't do anything - user canceled (or error)
			}
		} else if (e.getSource() == itemQuit) {
			//Quit the program
			try {
				model.saveTicketsToFile(getFileName(false));
				System.exit(0);  //Ignore FindBugs warning here - this is the only place to quit the program!
			} catch (IllegalArgumentException exp) {
				JOptionPane.showMessageDialog(this, "Unable to save ticket file.");
			} catch (IllegalStateException exp) {
				//Don't do anything - user canceled (or error)
			}
		}
	}
	
	/**
	 * Returns a file name generated through interactions with a {@link JFileChooser}
	 * object.
	 * @return the file name selected through {@link JFileChooser}
	 */
	private String getFileName(boolean chooserType) {
		JFileChooser fc = new JFileChooser("./");  //Open JFileChoose to current working directory
		fc.setApproveButtonText("Select");
		int returnVal = Integer.MIN_VALUE;
		if (chooserType) {
			fc.setDialogTitle("Load Course Catalog");
			returnVal = fc.showOpenDialog(this);
		} else {
			fc.setDialogTitle("Save Schedule");
			returnVal = fc.showSaveDialog(this);
		}
		if (returnVal != JFileChooser.APPROVE_OPTION) {
			//Error or user canceled, either way no file name.
			throw new IllegalStateException();
		}
		File file = fc.getSelectedFile();
		return file.getAbsolutePath();
	}

	/**
	 * Starts the GUI for the TicketTracker application.
	 * @param args command line arguments
	 */
	public static void main(String [] args) {
		new TicketTrackerGUI();
	}
	
	/**
	 * Inner class that creates the look and behavior for the {@link JPanel} that 
	 * shows the list of tickets.
	 * 
	 * @author Dr. Sarah Heckman (heckman@csc.ncsu.edu)
	 */
	private class TicketListPanel extends JPanel implements ActionListener {
		/** ID number used for object serialization. */
		private static final long serialVersionUID = 1L;
		/** Button for creating a new Ticket */
		private JButton btnAddTicket;
		/** Button for deleting the selected ticket in the list */
		private JButton btnDeleteTicket;
		/** Button for editing the selected ticket in the list */
		private JButton btnEditTicket;
		/** Text field for a user to enter an owner name to filter ticket list */
		private JTextField txtFilterByOwner;
		/** Button for starting filter of list by owner */
		private JButton btnFilterByOwner;
		/** Text field for a user to enter an submitter name to filter ticket list */
		private JTextField txtFilterBySubmitter;
		/** Button for starting filter of list by submitter */
		private JButton btnFilterBySubmitter;
		/** Button that will show all ticket that are currently tracked */
		private JButton btnShowAllTickets;
		/** JTable for displaying the list of ticket */
		private JTable table;
		/** TableModel for tickets */
		private TicketTableModel ticketTableModel;
		
		/**
		 * Creates the ticket list.
		 */
		public TicketListPanel() {
			super(new GridBagLayout());
			
			//Set up the JPanel that will hold action buttons
			JPanel pnlActions = new JPanel();
			btnAddTicket = new JButton("Add New Ticket");
			btnAddTicket.addActionListener(this);
			btnDeleteTicket = new JButton("Delete Selected Ticket");
			btnDeleteTicket.addActionListener(this);
			btnEditTicket = new JButton("Edit Selected Ticket");
			btnEditTicket.addActionListener(this);
			
			Border lowerEtched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
			TitledBorder border = BorderFactory.createTitledBorder(lowerEtched, "Ticket Actions");
			pnlActions.setBorder(border);
			pnlActions.setToolTipText("Ticket Actions");
			
			pnlActions.setLayout(new GridBagLayout());
			
			GridBagConstraints c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 0;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.CENTER;
			c.fill = GridBagConstraints.HORIZONTAL;
			pnlActions.add(btnAddTicket, c);
			c = new GridBagConstraints();
			c.gridx = 1;
			c.gridy = 0;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.HORIZONTAL;
			pnlActions.add(btnDeleteTicket, c);
			c = new GridBagConstraints();
			c.gridx = 2;
			c.gridy = 0;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.HORIZONTAL;
			pnlActions.add(btnEditTicket, c);
			
			//Set up the JPanel that handles search
			JPanel pnlSearch = new JPanel();
			txtFilterByOwner = new JTextField(10);
			btnFilterByOwner = new JButton("Filter List by Owner");
			btnFilterByOwner.addActionListener(this);
			txtFilterBySubmitter = new JTextField(10);
			btnFilterBySubmitter = new JButton("Filter List by Submitter");
			btnFilterBySubmitter.addActionListener(this);
			btnShowAllTickets = new JButton("Show All Tickets");
			btnShowAllTickets.addActionListener(this);
			
			border = BorderFactory.createTitledBorder(lowerEtched, "Ticket Search");
			pnlSearch.setBorder(border);
			pnlSearch.setToolTipText("Ticket Search");
			
			pnlSearch.setLayout(new GridBagLayout());
			c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 0;
			c.gridheight = 1;
			c.gridwidth = 2;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.HORIZONTAL;
			pnlSearch.add(txtFilterByOwner, c);
			c = new GridBagConstraints();
			c.gridx = 2;
			c.gridy = 0;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.HORIZONTAL;
			pnlSearch.add(btnFilterByOwner, c);
			c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 1;
			c.gridheight = 1;
			c.gridwidth = 2;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.HORIZONTAL;
			pnlSearch.add(txtFilterBySubmitter, c);
			c = new GridBagConstraints();
			c.gridx = 2;
			c.gridy = 1;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.HORIZONTAL;
			pnlSearch.add(btnFilterBySubmitter, c);
			c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 2;
			c.gridheight = 1;
			c.gridwidth = 3;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.HORIZONTAL;
			pnlSearch.add(btnShowAllTickets, c);
						
			//Set up table
			ticketTableModel = new TicketTableModel();
			table = new JTable(ticketTableModel);
			table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			table.setPreferredScrollableViewportSize(new Dimension(500, 500));
			table.setFillsViewportHeight(true);
			
			JScrollPane listScrollPane = new JScrollPane(table);
			
			border = BorderFactory.createTitledBorder(lowerEtched, "Ticket List");
			listScrollPane.setBorder(border);
			listScrollPane.setToolTipText("Ticket List");
			
			c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 0;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = .1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			add(pnlActions, c);
			c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 1;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = .1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			add(pnlSearch, c);
			c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 2;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.ipady = 400;
			c.weightx = 1;
			c.weighty = .8;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			add(listScrollPane, c);
		}

		/**
		 * Performs an action based on the given {@link ActionEvent}.
		 * @param e user event that triggers an action.
		 */
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == btnAddTicket) {
				//If the add button is pressed switch to the createTicketPanel
				cardLayout.show(panel,  CREATE_TICKET_PANEL);
			} else if (e.getSource() == btnDeleteTicket) {
				//If the delete button is pressed, delete the ticket
				int row = table.getSelectedRow();
				if (row == -1) {
					JOptionPane.showMessageDialog(TicketTrackerGUI.this, "No ticket selected");
				} else {
					try {
						int ticketId = Integer.parseInt(ticketTableModel.getValueAt(row, 0).toString());
						TicketTrackerModel.getInstance().deleteTicketById(ticketId);
					} catch (NumberFormatException nfe ) {
						JOptionPane.showMessageDialog(TicketTrackerGUI.this, "Invalid ticket id");
					}
				}
				updateTable(null, null);
			} else if (e.getSource() == btnEditTicket) {
				//If the edit button is pressed, switch panel based on state
				int row = table.getSelectedRow();
				if (row == -1) {
					JOptionPane.showMessageDialog(TicketTrackerGUI.this, "No ticket selected");
				} else {
					try {
						int ticketId = Integer.parseInt(ticketTableModel.getValueAt(row, 0).toString());
						String stateName = TicketTrackerModel.getInstance().getTicketById(ticketId).getStateName();
						 
						if (stateName.equals(TrackedTicket.NEW_NAME)) {
							cardLayout.show(panel, NEW_PANEL);
							pnlNew.setTicketInfo(ticketId);
						} 
						if (stateName.equals(TrackedTicket.ASSIGNED_NAME)) {
							cardLayout.show(panel, ASSIGNED_PANEL);
							pnlAssigned.setTicketInfo(ticketId);
						} 
						if (stateName.equals(TrackedTicket.WORKING_NAME)) {
							cardLayout.show(panel, WORKING_PANEL);
							pnlWorking.setTicketInfo(ticketId);
						} 
						if (stateName.equals(TrackedTicket.FEEDBACK_NAME)) {
							cardLayout.show(panel, FEEDBACK_PANEL);
							pnlFeedback.setTicketInfo(ticketId);
						}
						if (stateName.equals(TrackedTicket.CLOSED_NAME)) {
							cardLayout.show(panel, CLOSED_PANEL);
							pnlClosed.setTicketInfo(ticketId);
						} 
					} catch (NumberFormatException nfe) {
						JOptionPane.showMessageDialog(TicketTrackerGUI.this, "Invalid ticket id");
					} catch (NullPointerException npe) {
						JOptionPane.showMessageDialog(TicketTrackerGUI.this, "Invalid ticket id");
					}
				}
			} else if (e.getSource() == btnFilterByOwner) {
				String owner = txtFilterByOwner.getText();
				txtFilterByOwner.setText("");
				updateTable(owner, null);
			} else if (e.getSource() == btnFilterBySubmitter) {
				String submitter = txtFilterBySubmitter.getText();
				txtFilterBySubmitter.setText("");
				updateTable(null, submitter);
			} else if (e.getSource() == btnShowAllTickets) {
				updateTable(null, null);
			}
			TicketTrackerGUI.this.repaint();
			TicketTrackerGUI.this.validate();
		}
		
		public void updateTable(String owner, String submitter) {
			if (owner == null && submitter == null) {
				ticketTableModel.updateData();
			} else if (owner != null) {
				ticketTableModel.updateDataWithOwner(owner);
			} else if (submitter != null) {
				ticketTableModel.updateDataWithSubmitter(submitter);
			}
		}
		
		/**
		 * {@link TicketTableModel} is the object underlying the {@link JTable} object that displays
		 * the list of {@link TrackedTicket}s to the user.
		 * @author Dr. Sarah Heckman (heckman@csc.ncsu.edu)
		 */
		private class TicketTableModel extends AbstractTableModel {
			
			/** ID number used for object serialization. */
			private static final long serialVersionUID = 1L;
			/** Column names for the table */
			private String [] columnNames = {"Ticket ID", "Ticket State", "Ticket Title"};
			/** Data stored in the table */
			private Object [][] data;
			
			/**
			 * Constructs the {@link TicketTableModel} by requesting the latest information
			 * from the {@link TicketTrackerModel}.
			 */
			public TicketTableModel() {
				updateData();
			}

			/**
			 * Returns the number of columns in the table.
			 * @return the number of columns in the table.
			 */
			public int getColumnCount() {
				return columnNames.length;
			}

			/**
			 * Returns the number of rows in the table.
			 * @return the number of rows in the table.
			 */
			public int getRowCount() {
				if (data == null) 
					return 0;
				return data.length;
			}
			
			/**
			 * Returns the column name at the given index.
			 * @return the column name at the given column.
			 */
			public String getColumnName(int col) {
				return columnNames[col];
			}

			/**
			 * Returns the data at the given {row, col} index.
			 * @return the data at the given location.
			 */
			public Object getValueAt(int row, int col) {
				if (data == null)
					return null;
				return data[row][col];
			}
			
			/**
			 * Sets the given value to the given {row, col} location.
			 * @param value Object to modify in the data.
			 * @param row location to modify the data.
			 * @param column location to modify the data.
			 */
			public void setValueAt(Object value, int row, int col) {
				data[row][col] = value;
				fireTableCellUpdated(row, col);
			}
			
			/**
			 * Updates the given model with {@link TrackedTicket} information from the {@link TicketTrackerModel}.
			 */
			private void updateData() {
				TicketTrackerModel m = TicketTrackerModel.getInstance();
				data = m.getTicketListAsArray();
			}
			
			/**
			 * Updates the given model with {@link TrackedTicket} information for the 
			 * given owner from the {@link TicketTrackerModel}.
			 * @param owner developer id to search for.
			 */
			private void updateDataWithOwner(String owner) {
				try {
					TicketTrackerModel m = TicketTrackerModel.getInstance();
					data = m.getTicketListByOwnerAsArray(owner);
				} catch (IllegalArgumentException e) {
					JOptionPane.showMessageDialog(TicketTrackerGUI.this, "Invalid owner");
				}
			}
			
			/**
			 * Updates the given model with {@link TrackedTicket} information for the 
			 * given submitter from the {@link TicketTrackerModel}.
			 * @param submitter submitter id to search for.
			 */
			private void updateDataWithSubmitter(String submitter) {
				try {
					TicketTrackerModel m = TicketTrackerModel.getInstance();
					data = m.getTicketListBySubmitterAsArray(submitter);
				} catch (IllegalArgumentException e) {
					JOptionPane.showMessageDialog(TicketTrackerGUI.this, "Invalid submitter");
				}
			}
		}
	}
	
	/**
	 * Inner class that creates the look and behavior for the {@link JPanel} that 
	 * interacts with an new ticket.
	 * 
	 * @author Dr. Sarah Heckman (heckman@csc.ncsu.edu)
	 */
	private class NewPanel extends JPanel implements ActionListener {
		/** ID number used for object serialization. */
		private static final long serialVersionUID = 1L;
		/** {@link TicketInfoPanel} that presents the {@TrackedTicket}'s information to the user */
		private TicketInfoPanel pnlTicketInfo;
		/** Note author label for the state update */
		private JLabel lblNoteAuthor;
		/** Note author for the state update */
		private JTextField txtNoteAuthor;
		/** Note label for the state update */
		private JLabel lblNote;
		/** Note for the state update */
		private JTextArea txtNote;
		/** Label for owner id field */
		private JLabel lblOwner;
		/** Text field for owner id */
		private JTextField txtOwner;
		/** Assign action */
		private JButton btnAssign;
		/** Cancel action */
		private JButton btnCancel;
		/** Current {@link TrackedTicket}'s id */
		private int ticketId;
		
		/**
		 * Constructs the JPanel for editing a {@link TrackedTicket} in the NewState.
		 */
		public NewPanel() {
			pnlTicketInfo = new TicketInfoPanel();
			lblNoteAuthor = new JLabel("Note Author");
			txtNoteAuthor = new JTextField(30);
			lblNote = new JLabel("Note Text");
			txtNote = new JTextArea(30, 5);
			lblOwner = new JLabel("Owner");
			txtOwner = new JTextField(30);
			btnAssign = new JButton("Assign");
			btnCancel = new JButton("Cancel");
			
			btnAssign.addActionListener(this);
			btnCancel.addActionListener(this);
			
			setLayout(new GridBagLayout());
			GridBagConstraints c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 0;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.PAGE_START;
			c.fill = GridBagConstraints.BOTH;
			add(pnlTicketInfo, c);
			
			JPanel pnlNewInfo = new JPanel();
			pnlNewInfo.setLayout(new GridBagLayout());
			
			Border lowerEtched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
			TitledBorder border = BorderFactory.createTitledBorder(lowerEtched, "Assign Ticket Owner");
			pnlNewInfo.setBorder(border);
			pnlNewInfo.setToolTipText("Assign Ticket Owner");
			
			c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 0;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.HORIZONTAL;
			pnlNewInfo.add(lblOwner, c);
			c = new GridBagConstraints();
			c.gridx = 1;
			c.gridy = 0;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.HORIZONTAL;
			pnlNewInfo.add(txtOwner, c);
			c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 1;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.HORIZONTAL;
			pnlNewInfo.add(lblNoteAuthor, c);
			c = new GridBagConstraints();
			c.gridx = 1;
			c.gridy = 1;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.HORIZONTAL;
			pnlNewInfo.add(txtNoteAuthor, c);
			c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 2;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.HORIZONTAL;
			pnlNewInfo.add(lblNote, c);
			c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 3;
			c.gridheight = 1;
			c.gridwidth = 2;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			pnlNewInfo.add(txtNote, c);
			
			c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 1;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.CENTER;
			c.fill = GridBagConstraints.BOTH;
			add(pnlNewInfo, c);
			
			JPanel pnlButtons = new JPanel();
			pnlButtons.setLayout(new GridBagLayout());
			c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 0;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			pnlButtons.add(btnAssign, c);
			c = new GridBagConstraints();
			c.gridx = 1;
			c.gridy = 0;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			pnlButtons.add(btnCancel, c);
			
			c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 5;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.PAGE_END;
			c.fill = GridBagConstraints.HORIZONTAL;
			add(pnlButtons, c);
		}
		
		/**
		 * Set the {@link TicketInfoPanel} with the given ticket data.
		 * @param tickedId id of the ticket
		 */
		public void setTicketInfo(int tickedId) {
			this.ticketId = tickedId;
			pnlTicketInfo.setTicketInfo(this.ticketId);
		}

		/**
		 * Performs an action based on the given {@link ActionEvent}.
		 * @param e user event that triggers an action.
		 */
		public void actionPerformed(ActionEvent e) {
			boolean reset = true;
			if (e.getSource() == btnAssign) {
				//Take care of note.
				String note = txtNote.getText();
				if (note.equals("")) {
					note = null;
				}
				String noteAuthor = txtNoteAuthor.getText();
				if (noteAuthor.equals("")) {
					noteAuthor = null;
				}
				String ownerId = txtOwner.getText();
				if (ownerId == null || ownerId.equals("")) {
					//If developer id is invalid, show an error message
					JOptionPane.showMessageDialog(TicketTrackerGUI.this, "Invalid developer id");
					reset = false;
				} else {
					//Otherwise, try a Command.  If command fails, go back to ticket list
					try {
						Command c = new Command(Command.CommandValue.POSSESSION, ownerId, null, noteAuthor, note);
						TicketTrackerModel.getInstance().executeCommand(ticketId, c);
					} catch (IllegalArgumentException iae) {
						JOptionPane.showMessageDialog(TicketTrackerGUI.this, "Invalid command");
						reset = false;
					} catch (UnsupportedOperationException uoe) {
						JOptionPane.showMessageDialog(TicketTrackerGUI.this, "Invalid state transition");
						reset = false;
					}
					
				}
			}
			if (reset) {
				//All buttons lead to back ticket list if valid info for owner
				cardLayout.show(panel, TICKET_LIST_PANEL);
				pnlTicketList.updateTable(null, null);
				TicketTrackerGUI.this.repaint();
				TicketTrackerGUI.this.validate();
				//Reset fields
				txtOwner.setText("");
				txtNote.setText("");
				txtNoteAuthor.setText("");
			}
			//Otherwise, do not refresh the GUI panel and wait for correct developer input.
		}
		
	}
	
	/**
	 * Inner class that creates the look and behavior for the {@link JPanel} that 
	 * interacts with an assigned ticket.
	 * 
	 * @author Dr. Sarah Heckman (heckman@csc.ncsu.edu)
	 */
	private class AssignedPanel extends JPanel implements ActionListener {
		/** ID number used for object serialization. */
		private static final long serialVersionUID = 1L;
		/** {@link TicketInfoPanel} that presents the {@TrackedTicket}'s information to the user */
		private TicketInfoPanel pnlTicketInfo;
		/** Note author label for the state update */
		private JLabel lblNoteAuthor;
		/** Note author for the state update */
		private JTextField txtNoteAuthor;
		/** Note label for the state update */
		private JLabel lblNote;
		/** Note for the state update */
		private JTextArea txtNote;
		/** Label for selecting a flag */
		private JLabel lblFlag;
		/** ComboBox for flags */
		private JComboBox<String> comboFlags;
		/** Accept action */
		private JButton btnAccept;
		/** Close action */
		private JButton btnClose;
		/** Cancel action */
		private JButton btnCancel;
		/** Current ticket's id */
		private int ticketId;
		/** Possible flags to list in the combo box */
		private String [] flags = {"Duplicate", "Inappropriate"}; 

		/**
		 * Constructs a JPanel for editing a {@link TrackedTicket} in the AssignedState.
		 */
		public AssignedPanel() {
			pnlTicketInfo = new TicketInfoPanel();
			lblNoteAuthor = new JLabel("Note Author");
			txtNoteAuthor = new JTextField(30);
			lblNote = new JLabel("Note");
			txtNote = new JTextArea(30, 5);
			lblFlag = new JLabel("Flag");
			comboFlags = new JComboBox<String>(flags);
			comboFlags.setSelectedIndex(0);
			
			btnAccept = new JButton("Accept");
			btnClose = new JButton("Close");
			btnCancel = new JButton("Cancel");
			
			btnAccept.addActionListener(this);
			btnClose.addActionListener(this);
			btnCancel.addActionListener(this);
			
			setLayout(new GridBagLayout());
			GridBagConstraints c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 0;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.PAGE_START;
			c.fill = GridBagConstraints.BOTH;
			add(pnlTicketInfo, c);
			
			JPanel pnlCloseAction = new JPanel();
			pnlCloseAction.setLayout(new GridBagLayout());
			c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 0;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.HORIZONTAL;
			pnlCloseAction.add(lblFlag, c);
			c = new GridBagConstraints();
			c.gridx = 1;
			c.gridy = 0;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_END;
			c.fill = GridBagConstraints.HORIZONTAL;
			pnlCloseAction.add(comboFlags, c);
			
			Border lowerEtched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
			TitledBorder border = BorderFactory.createTitledBorder(lowerEtched, "Close Ticket");
			pnlCloseAction.setBorder(border);
			pnlCloseAction.setToolTipText("Close Ticket");	
			
			JPanel pnlNote = new JPanel();
			pnlNote.setLayout(new GridBagLayout());
			
			border = BorderFactory.createTitledBorder(lowerEtched, "Ticket Note");
			pnlNote.setBorder(border);
			pnlNote.setToolTipText("TicketNote");
			
			c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 0;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.HORIZONTAL;
			pnlNote.add(lblNoteAuthor, c);
			c = new GridBagConstraints();
			c.gridx = 1;
			c.gridy = 0;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_END;
			c.fill = GridBagConstraints.HORIZONTAL;
			pnlNote.add(txtNoteAuthor, c);
			c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 1;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.HORIZONTAL;
			pnlNote.add(lblNote, c);
			c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 2;
			c.gridheight = 1;
			c.gridwidth = 2;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_END;
			c.fill = GridBagConstraints.BOTH;
			pnlNote.add(txtNote, c);
			
			c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 1;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.CENTER;
			c.fill = GridBagConstraints.BOTH;
			add(pnlCloseAction, c);
			c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 2;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.CENTER;
			c.fill = GridBagConstraints.BOTH;
			add(pnlNote, c);
			
			JPanel pnlButtons = new JPanel();
			pnlButtons.setLayout(new GridLayout(1, 3));
			c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 0;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.HORIZONTAL;
			pnlButtons.add(btnAccept, c);
			c = new GridBagConstraints();
			c.gridx = 1;
			c.gridy = 0;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.CENTER;
			c.fill = GridBagConstraints.HORIZONTAL;
			pnlButtons.add(btnClose, c);
			c = new GridBagConstraints();
			c.gridx = 2;
			c.gridy = 0;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_END;
			c.fill = GridBagConstraints.HORIZONTAL;
			pnlButtons.add(btnCancel, c);
			
			c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 5;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.PAGE_END;
			c.fill = GridBagConstraints.HORIZONTAL;
			add(pnlButtons, c);
		}
		
		/**
		 * Set the {@link TicketInfoPanel} with the given ticket data.
		 * @param ticketId id of the ticket
		 */
		public void setTicketInfo(int ticketId) {
			this.ticketId = ticketId;
			pnlTicketInfo.setTicketInfo(this.ticketId);
		}

		/**
		 * Performs an action based on the given {@link ActionEvent}.
		 * @param e user event that triggers an action.
		 */
		public void actionPerformed(ActionEvent e) {
			boolean reset = true;
			if (e.getSource() == btnAccept) {
				//Handle note
				String note = txtNote.getText();
				if (note.equals("")) {
					note = null;
				}
				String noteAuthor = txtNoteAuthor.getText();
				if (noteAuthor.equals("")) {
					noteAuthor = null;
				}
				//Try a command.  If problem, go back to ticket list.
				try {
					Command c = new Command(Command.CommandValue.ACCEPTED, null, null, noteAuthor, note);
					TicketTrackerModel.getInstance().executeCommand(ticketId, c);
				} catch (IllegalArgumentException iae) {
					JOptionPane.showMessageDialog(TicketTrackerGUI.this, "Invalid command");
					reset = false;
				} catch (UnsupportedOperationException uoe) {
					JOptionPane.showMessageDialog(TicketTrackerGUI.this, "Invalid state transition");
					reset = false;
				}
			}
			if (e.getSource() == btnClose) {
				//Handle note
				String note = txtNote.getText();
				if (note.equals("")) {
					note = null;
				}
				String noteAuthor = txtNoteAuthor.getText();
				if (noteAuthor.equals("")) {
					noteAuthor = null;
				}
				//Get flag
				int idx = comboFlags.getSelectedIndex();
				if (idx < 0 || idx >= flags.length) {
					//If problem, show error and remain on page.
					JOptionPane.showMessageDialog(TicketTrackerGUI.this, "Invalid flag");
					reset = false;
				} else {
					Flag r = null;
					switch (idx) {
					case 0: 
						r = Flag.DUPLICATE;
						break;
					case 1:
						r = Flag.INAPPROPRIATE;
						break;
					case 2:
						r = Flag.RESOLVED;
						break;
					default:
						r = null;
					}
					//Try a command.  If problem, go back to ticket list.
					try {
						Command c = new Command(Command.CommandValue.CLOSED, null, r, noteAuthor, note);
						TicketTrackerModel.getInstance().executeCommand(ticketId, c);
					} catch (IllegalArgumentException iae) {
						JOptionPane.showMessageDialog(TicketTrackerGUI.this, "Invalid command");
						reset = false;
					} catch (UnsupportedOperationException uoe) {
						JOptionPane.showMessageDialog(TicketTrackerGUI.this, "Invalid state transition");
						reset = false;
					}
				}
			} 
			if (reset) {
				//All buttons lead to back ticket list
				cardLayout.show(panel, TICKET_LIST_PANEL);
				pnlTicketList.updateTable(null, null);
				TicketTrackerGUI.this.repaint();
				TicketTrackerGUI.this.validate();
				//Reset fields
				comboFlags.setSelectedIndex(0);
				txtNote.setText("");
				txtNoteAuthor.setText("");
			}
			//Otherwise, stay on panel
		}
		
	}
	
	/**
	 * Inner class that creates the look and behavior for the {@link JPanel} that 
	 * interacts with a working ticket.
	 * 
	 * @author Dr. Sarah Heckman (heckman@csc.ncsu.edu)
	 */
	private class WorkingPanel extends JPanel implements ActionListener {
		/** ID number used for object serialization. */
		private static final long serialVersionUID = 1L;
		/** {@link TicketInfoPanel} that presents the {@TrackedTicket}'s information to the user */
		private TicketInfoPanel pnlTicketInfo;
		/** Note author label for the state update */
		private JLabel lblNoteAuthor;
		/** Note author for the state update */
		private JTextField txtNoteAuthor;
		/** Note label for the state update */
		private JLabel lblNote;
		/** Note for the state update */
		private JTextArea txtNote;
		/** Label for selecting a flag */
		private JLabel lblFlag;
		/** ComboBox for flags */
		private JComboBox<String> comboFlags;
		/** Label for owner id field */
		private JLabel lblOwner;
		/** Text field for owner id */
		private JTextField txtOwner;
		/** Add Note action */
		private JButton btnAddNote;
		/** Feedback action */
		private JButton btnFeedback;
		/** Close action */
		private JButton btnClose;
		/** Assign action */
		private JButton btnAssign;
		/** Cancel action */
		private JButton btnCancel;
		/** Current ticket's id */
		private int ticketId;
		/** Possible flags to list in the combo box */
		private String [] flags = {"Duplicate", "Inappropriate", "Resolved"};

		/**
		 * Constructs a JFrame for editing a {@link TrackedTicket} in the WorkingState.
		 */
		public WorkingPanel() {
			pnlTicketInfo = new TicketInfoPanel();
			lblNoteAuthor = new JLabel("Note Author");
			txtNoteAuthor = new JTextField(30);
			lblNote = new JLabel("Note");
			txtNote = new JTextArea(30, 5);
			lblFlag = new JLabel("Flag");
			comboFlags = new JComboBox<String>(flags);
			comboFlags.setSelectedIndex(0);
			lblOwner = new JLabel("Owner");
			txtOwner = new JTextField(30);
			
			btnAddNote = new JButton("Add Note");
			btnFeedback = new JButton("Request Feedback");
			btnClose = new JButton("Close Ticket");
			btnAssign = new JButton("Assign to new Owner");
			btnCancel = new JButton("Cancel");
			
			btnAddNote.addActionListener(this);
			btnFeedback.addActionListener(this);
			btnClose.addActionListener(this);
			btnAssign.addActionListener(this);
			btnCancel.addActionListener(this);
			
			setLayout(new GridBagLayout());
			GridBagConstraints c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 0;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.PAGE_START;
			c.fill = GridBagConstraints.BOTH;
			add(pnlTicketInfo, c);
			
			JPanel pnlCloseAction = new JPanel();
			pnlCloseAction.setLayout(new GridBagLayout());
			
			Border lowerEtched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
			TitledBorder border = BorderFactory.createTitledBorder(lowerEtched, "Close Ticket");
			pnlCloseAction.setBorder(border);
			pnlCloseAction.setToolTipText("Close Ticket");	
			
			c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 0;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.HORIZONTAL;
			pnlCloseAction.add(lblFlag, c);
			c = new GridBagConstraints();
			c.gridx = 1;
			c.gridy = 0;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_END;
			c.fill = GridBagConstraints.HORIZONTAL;
			pnlCloseAction.add(comboFlags, c);
			
			JPanel pnlOwnerAction = new JPanel();
			pnlOwnerAction.setLayout(new GridBagLayout());
			
			border = BorderFactory.createTitledBorder(lowerEtched, "Assign Ticket Owner");
			pnlOwnerAction.setBorder(border);
			pnlOwnerAction.setToolTipText("Assign Ticket Owner");
			
			c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 0;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.HORIZONTAL;
			pnlOwnerAction.add(lblOwner, c);
			c = new GridBagConstraints();
			c.gridx = 1;
			c.gridy = 0;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.HORIZONTAL;
			pnlOwnerAction.add(txtOwner, c);
			
			JPanel pnlNote = new JPanel();
			pnlNote.setLayout(new GridBagLayout());
			
			border = BorderFactory.createTitledBorder(lowerEtched, "Ticket Note");
			pnlNote.setBorder(border);
			pnlNote.setToolTipText("TicketNote");
			
			c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 0;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.HORIZONTAL;
			pnlNote.add(lblNoteAuthor, c);
			c = new GridBagConstraints();
			c.gridx = 1;
			c.gridy = 0;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_END;
			c.fill = GridBagConstraints.HORIZONTAL;
			pnlNote.add(txtNoteAuthor, c);
			c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 1;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.HORIZONTAL;
			pnlNote.add(lblNote, c);
			c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 2;
			c.gridheight = 1;
			c.gridwidth = 2;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_END;
			c.fill = GridBagConstraints.BOTH;
			pnlNote.add(txtNote, c);
			
			c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 1;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.CENTER;
			c.fill = GridBagConstraints.BOTH;
			add(pnlCloseAction, c);
			c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 2;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.CENTER;
			c.fill = GridBagConstraints.BOTH;
			add(pnlOwnerAction, c);
			c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 3;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.CENTER;
			c.fill = GridBagConstraints.BOTH;
			add(pnlNote, c);
			
			JPanel pnlButtons = new JPanel();
			pnlButtons.setLayout(new GridBagLayout());
			c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 0;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.HORIZONTAL;
			pnlButtons.add(btnAddNote, c);
			c = new GridBagConstraints();
			c.gridx = 1;
			c.gridy = 0;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_END;
			c.fill = GridBagConstraints.HORIZONTAL;
			pnlButtons.add(btnFeedback, c);
			c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 1;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.HORIZONTAL;
			pnlButtons.add(btnClose, c);
			c = new GridBagConstraints();
			c.gridx = 1;
			c.gridy = 1;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_END;
			c.fill = GridBagConstraints.HORIZONTAL;
			pnlButtons.add(btnAssign, c);
			c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 2;
			c.gridheight = 1;
			c.gridwidth = 2;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.HORIZONTAL;
			pnlButtons.add(btnCancel, c);
			
			c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 5;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.PAGE_END;
			c.fill = GridBagConstraints.HORIZONTAL;
			add(pnlButtons, c);
		}
		
		/**
		 * Set the {@link TicketInfoPanel} with the given ticket data.
		 * @param ticketId id of the ticket
		 */
		public void setTicketInfo(int ticketId) {
			this.ticketId = ticketId;
			pnlTicketInfo.setTicketInfo(this.ticketId);
		}

		/**
		 * Performs an action based on the given {@link ActionEvent}.
		 * @param e user event that triggers an action.
		 */
		public void actionPerformed(ActionEvent e) {
			//Handle note
			String note = txtNote.getText();
			if (note.equals("")) {
				note = null;
			}
			String noteAuthor = txtNoteAuthor.getText();
			if (noteAuthor.equals("")) {
				noteAuthor = null;
			}
			boolean reset = true;
			if (e.getSource() == btnAddNote) {
				//Try command.  If problem, go to ticket list.
				try {
					Command c = new Command(Command.CommandValue.PROGRESS, null, null, noteAuthor, note);
					TicketTrackerModel.getInstance().executeCommand(ticketId, c);
				} catch (IllegalArgumentException iae) {
					JOptionPane.showMessageDialog(TicketTrackerGUI.this, "Invalid command");
					reset = false;
				} catch (UnsupportedOperationException uoe) {
					JOptionPane.showMessageDialog(TicketTrackerGUI.this, "Invalid state transition");
					reset = false;
				}
			} else if (e.getSource() == btnFeedback) {
				//Try command.  If problem, go to ticket list.
				try {
					Command c = new Command(Command.CommandValue.FEEDBACK, null, null, noteAuthor, note);
					TicketTrackerModel.getInstance().executeCommand(ticketId, c);
				} catch (IllegalArgumentException iae) {
					JOptionPane.showMessageDialog(TicketTrackerGUI.this, "Invalid command");
					reset = false;
				} catch (UnsupportedOperationException uoe) {
					JOptionPane.showMessageDialog(TicketTrackerGUI.this, "Invalid state transition");
					reset = false;
				}
			} if (e.getSource() == btnClose) {
				//Get flag
				int idx = comboFlags.getSelectedIndex();
				if (idx < 0 || idx >= flags.length) {
					//If problem, show error and remain on page.
					JOptionPane.showMessageDialog(TicketTrackerGUI.this, "Invalid flag");
					reset = false;
				} else {
					Flag f = null;
					switch (idx) {
					case 0: 
						f = Flag.DUPLICATE;
						break;
					case 1:
						f = Flag.INAPPROPRIATE;
						break;
					case 2:
						f = Flag.RESOLVED;
						break;
					default:
						f = null;
					}
					//Try a command.  If problem, go back to ticket list.
					try {
						Command c = new Command(Command.CommandValue.CLOSED, null, f, noteAuthor, note);
						TicketTrackerModel.getInstance().executeCommand(ticketId, c);
					} catch (IllegalArgumentException iae) {
						JOptionPane.showMessageDialog(TicketTrackerGUI.this, "Invalid command");
						reset = false;
					} catch (UnsupportedOperationException uoe) {
						JOptionPane.showMessageDialog(TicketTrackerGUI.this, "Invalid state transition");
						reset = false;
					}
				}
			} else if (e.getSource() == btnAssign) {
				String ownerId = txtOwner.getText();
				if (ownerId == null || ownerId.equals("")) {
					//If developer id is invalid, show an error message
					JOptionPane.showMessageDialog(TicketTrackerGUI.this, "Invalid developer id");
					reset = false;
				} else {
					//Otherwise, try a Command.  If command fails, go back to ticket list
					try {
						Command c = new Command(Command.CommandValue.POSSESSION, ownerId, null, noteAuthor, note);
						TicketTrackerModel.getInstance().executeCommand(ticketId, c);
					} catch (IllegalArgumentException iae) {
						JOptionPane.showMessageDialog(TicketTrackerGUI.this, "Invalid command");
						reset = false;
					} catch (UnsupportedOperationException uoe) {
						JOptionPane.showMessageDialog(TicketTrackerGUI.this, "Invalid state transition");
						reset = false;
					}
					
				}
			}
			if (reset) {
				//All buttons lead to back ticket list
				cardLayout.show(panel, TICKET_LIST_PANEL);
				pnlTicketList.updateTable(null, null);
				TicketTrackerGUI.this.repaint();
				TicketTrackerGUI.this.validate();
				//Reset fields
				comboFlags.setSelectedIndex(0);
				txtNote.setText("");
				txtNoteAuthor.setText("");
				txtOwner.setText("");
			}
			//Otherwise, stay on panel
		}
	}
	
	/**
	 * Inner class that creates the look and behavior for the {@link JPanel} that 
	 * interacts with a feedback ticket.
	 * 
	 * @author Dr. Sarah Heckman (heckman@csc.ncsu.edu)
	 */
	private class FeedbackPanel extends JPanel implements ActionListener {
		/** ID number used for object serialization. */
		private static final long serialVersionUID = 1L;
		/** {@link TicketInfoPanel} that presents the {@TrackedTicket}'s information to the user */
		private TicketInfoPanel pnlTicketInfo;
		/** Note author label for the state update */
		private JLabel lblNoteAuthor;
		/** Note author for the state update */
		private JTextField txtNoteAuthor;
		/** Note label for the state update */
		private JLabel lblNote;
		/** Note for the state update */
		private JTextArea txtNote;
		/** Feedback action */
		private JButton btnFeedback;
		/** Cancel action */
		private JButton btnCancel;
		/** Current ticket's id */
		private int ticketId;

		/**
		 * Constructs a JPanel for editing a {@link TrackedTicket} in the FeedbackState.
		 */
		public FeedbackPanel() {
			pnlTicketInfo = new TicketInfoPanel();
			lblNoteAuthor = new JLabel("Note Author");
			txtNoteAuthor = new JTextField(30);
			lblNote = new JLabel("Note Text");
			txtNote = new JTextArea(30, 5);
			btnFeedback = new JButton("Provide Feedback");
			btnCancel = new JButton("Cancel");
			
			btnFeedback.addActionListener(this);
			btnCancel.addActionListener(this);
			
			setLayout(new GridBagLayout());
			GridBagConstraints c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 0;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.PAGE_START;
			c.fill = GridBagConstraints.BOTH;
			add(pnlTicketInfo, c);
			
			JPanel pnlNewInfo = new JPanel();
			pnlNewInfo.setLayout(new GridBagLayout());
			
			Border lowerEtched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
			TitledBorder border = BorderFactory.createTitledBorder(lowerEtched, "Ticket Note");
			pnlNewInfo.setBorder(border);
			pnlNewInfo.setToolTipText("TicketNote");

			c.gridx = 0;
			c.gridy = 1;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.HORIZONTAL;
			pnlNewInfo.add(lblNoteAuthor, c);
			c = new GridBagConstraints();
			c.gridx = 1;
			c.gridy = 1;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.HORIZONTAL;
			pnlNewInfo.add(txtNoteAuthor, c);
			c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 2;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.HORIZONTAL;
			pnlNewInfo.add(lblNote, c);
			c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 3;
			c.gridheight = 1;
			c.gridwidth = 2;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			pnlNewInfo.add(txtNote, c);
			
			c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 1;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.CENTER;
			c.fill = GridBagConstraints.BOTH;
			add(pnlNewInfo, c);
			
			JPanel pnlButtons = new JPanel();
			pnlButtons.setLayout(new GridBagLayout());
			c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 0;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			pnlButtons.add(btnFeedback, c);
			c = new GridBagConstraints();
			c.gridx = 1;
			c.gridy = 0;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			pnlButtons.add(btnCancel, c);
			
			c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 5;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.PAGE_END;
			c.fill = GridBagConstraints.HORIZONTAL;
			add(pnlButtons, c);
		}
		
		/**
		 * Set the {@link TicketInfoPanel} with the given ticket data.
		 * @param ticketId id of the ticket
		 */
		public void setTicketInfo(int ticketId) {
			this.ticketId = ticketId;
			pnlTicketInfo.setTicketInfo(this.ticketId);
		}

		/**
		 * Performs an action based on the given {@link ActionEvent}.
		 * @param e user event that triggers an action.
		 */
		public void actionPerformed(ActionEvent e) {
			//Handle note
			String note = txtNote.getText();
			if (note.equals("")) {
				note = null;
			}
			String noteAuthor = txtNoteAuthor.getText();
			if (noteAuthor.equals("")) {
				noteAuthor = null;
			}
			boolean reset = true;
			if (e.getSource() == btnFeedback) {
				//Try command.  If problem, return to ticket list.
				try {
					Command c = new Command(Command.CommandValue.FEEDBACK, null, null, noteAuthor, note);
					TicketTrackerModel.getInstance().executeCommand(ticketId, c);
				} catch (IllegalArgumentException iae) {
					JOptionPane.showMessageDialog(TicketTrackerGUI.this, "Invalid command");
					reset = false;
				} catch (UnsupportedOperationException uoe) {
					JOptionPane.showMessageDialog(TicketTrackerGUI.this, "Invalid state transition");
					reset = false;
				}
			} 
			if (reset) {
				//All buttons lead to back ticket list
				cardLayout.show(panel, TICKET_LIST_PANEL);
				pnlTicketList.updateTable(null, null);
				TicketTrackerGUI.this.repaint();
				TicketTrackerGUI.this.validate();
				//Reset fields
				txtNoteAuthor.setText("");
				txtNote.setText("");
			}
		}
		
	}
	
	/**
	 * Inner class that creates the look and behavior for the {@link JPanel} that 
	 * interacts with a closed ticket.
	 * 
	 * @author Dr. Sarah Heckman (heckman@csc.ncsu.edu)
	 */
	private class ClosedPanel extends JPanel implements ActionListener {
		/** ID number used for object serialization. */
		private static final long serialVersionUID = 1L;
		/** {@link TicketInfoPanel} that presents the {@TrackedTicket}'s information to the user */
		private TicketInfoPanel pnlTicketInfo;
		/** Note author label for the state update */
		private JLabel lblNoteAuthor;
		/** Note author for the state update */
		private JTextField txtNoteAuthor;
		/** Note label for the state update */
		private JLabel lblNote;
		/** Note for the state update */
		private JTextArea txtNote;
		/** Note author label for the state update */
		private JLabel lblOwner;
		/** Note author for the state update */
		private JTextField txtOwner;
		/** Move to working with same owner action */
		private JButton btnReopen;
		/** Move to assigned with new owner action */
		private JButton btnAssign;
		/** Cancel action */
		private JButton btnCancel;
		/** Current ticket's id */
		private int ticketId;

		/**
		 * Constructs a JPanel for editing a {@link TrackedTicket} in the ClosedState.
		 */
		public ClosedPanel() {
			pnlTicketInfo = new TicketInfoPanel();
			lblNoteAuthor = new JLabel("Note Author");
			txtNoteAuthor = new JTextField(30);
			lblNote = new JLabel("Note");
			txtNote = new JTextArea(30, 5);
			lblOwner = new JLabel("Owner");
			txtOwner = new JTextField(30);
			
			btnReopen = new JButton("Reopen");
			btnAssign = new JButton("Assign to new Owner");
			btnCancel = new JButton("Cancel");
			
			btnReopen.addActionListener(this);
			btnAssign.addActionListener(this);
			btnCancel.addActionListener(this);
			
			setLayout(new GridBagLayout());
			GridBagConstraints c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 0;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.PAGE_START;
			c.fill = GridBagConstraints.BOTH;
			add(pnlTicketInfo, c);

			
			JPanel pnlOwnerAction = new JPanel();
			pnlOwnerAction.setLayout(new GridBagLayout());
			
			Border lowerEtched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
			TitledBorder border = BorderFactory.createTitledBorder(lowerEtched, "Assign Ticket Owner");
			pnlOwnerAction.setBorder(border);
			pnlOwnerAction.setToolTipText("Assign Ticket Owner");
			
			c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 0;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.HORIZONTAL;
			pnlOwnerAction.add(lblOwner, c);
			c = new GridBagConstraints();
			c.gridx = 1;
			c.gridy = 0;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.HORIZONTAL;
			pnlOwnerAction.add(txtOwner, c);
			
			JPanel pnlNote = new JPanel();
			pnlNote.setLayout(new GridBagLayout());
			
			border = BorderFactory.createTitledBorder(lowerEtched, "Ticket Note");
			pnlNote.setBorder(border);
			pnlNote.setToolTipText("TicketNote");
			
			c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 0;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.HORIZONTAL;
			pnlNote.add(lblNoteAuthor, c);
			c = new GridBagConstraints();
			c.gridx = 1;
			c.gridy = 0;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_END;
			c.fill = GridBagConstraints.HORIZONTAL;
			pnlNote.add(txtNoteAuthor, c);
			c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 1;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.HORIZONTAL;
			pnlNote.add(lblNote, c);
			c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 2;
			c.gridheight = 1;
			c.gridwidth = 2;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_END;
			c.fill = GridBagConstraints.BOTH;
			pnlNote.add(txtNote, c);

			c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 1;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.CENTER;
			c.fill = GridBagConstraints.BOTH;
			add(pnlOwnerAction, c);
			c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 2;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.CENTER;
			c.fill = GridBagConstraints.BOTH;
			add(pnlNote, c);
			
			JPanel pnlButtons = new JPanel();
			pnlButtons.setLayout(new GridBagLayout());
			c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 0;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.HORIZONTAL;
			pnlButtons.add(btnReopen, c);
			c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 1;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_END;
			c.fill = GridBagConstraints.HORIZONTAL;
			pnlButtons.add(btnAssign, c);
			c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 2;
			c.gridheight = 1;
			c.gridwidth = 2;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.HORIZONTAL;
			pnlButtons.add(btnCancel, c);
			
			c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 5;
			c.gridheight = 1;
			c.gridwidth = 2;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.PAGE_END;
			c.fill = GridBagConstraints.HORIZONTAL;
			add(pnlButtons, c);
		}
		
		/**
		 * Set the {@link TicketInfoPanel} with the given ticket data.
		 * @param ticketId id of the ticket
		 */
		public void setTicketInfo(int ticketId) {
			this.ticketId = ticketId;
			pnlTicketInfo.setTicketInfo(this.ticketId);
		}

		/**
		 * Performs an action based on the given {@link ActionEvent}.
		 * @param e user event that triggers an action.
		 */
		public void actionPerformed(ActionEvent e) {
			String note = txtNote.getText();
			if (note.equals("")) {
				note = null;
			}
			String noteAuthor = txtNoteAuthor.getText(); 
			if (noteAuthor.equals("")) {
				noteAuthor = null;
			}
			boolean reset = true;
			if (e.getSource() == btnReopen) {
				//Try command.  If problem, go back to ticket list
				try {
					Command c = new Command(CommandValue.PROGRESS, null, null, noteAuthor, note);
					TicketTrackerModel.getInstance().executeCommand(ticketId, c);
				} catch (IllegalArgumentException iae) {
					JOptionPane.showMessageDialog(TicketTrackerGUI.this, "Invalid command");
					reset = false;
				} catch (UnsupportedOperationException uoe) {
					JOptionPane.showMessageDialog(TicketTrackerGUI.this, "Invalid state transition");
					reset = false;
				}
			}
			if (e.getSource() == btnAssign) {
				String owner = txtOwner.getText();
				if (owner == null || owner.equals("")) {
					//If owner is invalid, show an error message
					JOptionPane.showMessageDialog(TicketTrackerGUI.this, "Invalid owner");
					reset = false;
				} else {
					//Otherwise, try a Command.  If command fails, go back to ticket list
					try {
						Command c = new Command(Command.CommandValue.POSSESSION, owner, null, noteAuthor, note);
						TicketTrackerModel.getInstance().executeCommand(ticketId, c);
					} catch (IllegalArgumentException iae) {
						JOptionPane.showMessageDialog(TicketTrackerGUI.this, "Invalid command");
						reset = false;
					} catch (UnsupportedOperationException uoe) {
						JOptionPane.showMessageDialog(TicketTrackerGUI.this, "Invalid state transition");
						reset = false;
					}
					
				}
			}
			if (reset) {
				//All buttons lead to back ticket list if valid info for owner
				cardLayout.show(panel, TICKET_LIST_PANEL);
				pnlTicketList.updateTable(null, null);
				TicketTrackerGUI.this.repaint();
				TicketTrackerGUI.this.validate();
				//Reset fields
				txtOwner.setText("");
				txtNote.setText("");
				txtNoteAuthor.setText("");
			}
			//Otherwise, do not refresh the GUI panel and wait for correct developer input.
		}
	}
	
	/**
	 * Inner class that creates the look and behavior for the {@link JPanel} that 
	 * shows information about the ticket.
	 * 
	 * @author Dr. Sarah Heckman (heckman@csc.ncsu.edu)
	 */
	private class TicketInfoPanel extends JPanel {
		/** ID number used for object serialization. */
		private static final long serialVersionUID = 1L;
		/** Label for id */
		private JLabel lblId;
		/** Field for id */
		private JTextField txtId;
		/** Label for state */
		private JLabel lblState;
		/** Field for state */
		private JTextField txtState;
		/** Label for title */
		private JLabel lblTitle;
		/** Field for title */
		private JTextField txtTitle;
		/** Label for submitter */
		private JLabel lblSubmitter;
		/** Field for submitter */
		private JTextField txtSubmitter;
		/** Label for owner */
		private JLabel lblOwner;
		/** Field for owner */
		private JTextField txtOwner;
		/** Label for flag */
		private JLabel lblFlag;
		/** Field for flag */
		private JTextField txtFlag;
		/** Label for notes */
		private JLabel lblNotes;
		/** JTable for displaying notes */
		private JTable tableNotes;
		/** Scroll pane for table */
		private JScrollPane scrollNotes;
		/** TableModel for Notes */
		private NotesTableModel notesTableModel;
		
		/** 
		 * Construct the panel for the ticket information.
		 */
		public TicketInfoPanel() {
			super(new GridBagLayout());
			
			lblId = new JLabel("Ticket Id");
			lblState = new JLabel("Ticket State");
			lblTitle = new JLabel("Ticket Summary");
			lblSubmitter = new JLabel("Submitter");
			lblOwner = new JLabel("Owner");
			lblFlag = new JLabel("Flag");
			lblNotes = new JLabel("Notes");
			
			txtId = new JTextField(15);
			txtState = new JTextField(15);
			txtTitle = new JTextField(30);
			txtSubmitter = new JTextField(15);
			txtOwner = new JTextField(15);
			txtFlag = new JTextField(15);
			
			txtId.setEditable(false);
			txtState.setEditable(false);
			txtTitle.setEditable(false);
			txtSubmitter.setEditable(false);
			txtOwner.setEditable(false);
			txtFlag.setEditable(false);
			
			notesTableModel = new NotesTableModel();
			tableNotes = new JTable(notesTableModel);
			tableNotes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			tableNotes.setFillsViewportHeight(true);
			
			scrollNotes = new JScrollPane(tableNotes, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			
			Border lowerEtched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
			TitledBorder border = BorderFactory.createTitledBorder(lowerEtched, "Ticket Information");
			this.setBorder(border);
			this.setToolTipText("Ticket Information");
			
			GridBagConstraints c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 0;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.FIRST_LINE_START;
			c.fill = GridBagConstraints.HORIZONTAL;
			add(lblTitle, c);
			c = new GridBagConstraints();
			c.gridx = 1;
			c.gridy = 0;
			c.gridheight = 1;
			c.gridwidth = 3;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.HORIZONTAL;
			add(txtTitle, c);
			c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 1;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.HORIZONTAL;
			add(lblId, c);
			c = new GridBagConstraints();
			c.gridx = 1;
			c.gridy = 1;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.HORIZONTAL;
			add(txtId, c);
			c = new GridBagConstraints();
			c.gridx = 2;
			c.gridy = 1;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.HORIZONTAL;
			add(lblState, c);
			c = new GridBagConstraints();
			c.gridx = 3;
			c.gridy = 1;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.HORIZONTAL;
			add(txtState, c);
			c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 2;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.HORIZONTAL;
			add(lblSubmitter, c);
			c = new GridBagConstraints();
			c.gridx = 1;
			c.gridy = 2;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.HORIZONTAL;
			add(txtSubmitter, c);
			c = new GridBagConstraints();
			c.gridx = 2;
			c.gridy = 2;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.HORIZONTAL;
			add(lblOwner, c);
			c = new GridBagConstraints();
			c.gridx = 3;
			c.gridy = 2;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.HORIZONTAL;
			add(txtOwner, c);
			c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 3;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.HORIZONTAL;
			add(lblFlag, c);
			c = new GridBagConstraints();
			c.gridx = 1;
			c.gridy = 3;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.HORIZONTAL;
			add(txtFlag, c);
			c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 4;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			add(lblNotes, c);
			c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 5;
			c.ipady = 50;
			c.gridheight = 30;
			c.gridwidth = 4;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			add(scrollNotes, c);
		}
		
		/**
		 * Adds information about the ticket to the display.  
		 * @param ticketId the id for the ticket to display information about.
		 */
		public void setTicketInfo(int ticketId) {
			//Get the ticket from the model
			TrackedTicket t = TicketTrackerModel.getInstance().getTicketById(ticketId);
			if (t == null) {
				//If the ticket doesn't exist for the given id, show an error message
				JOptionPane.showMessageDialog(TicketTrackerGUI.this, "Invalid ticket id");
				cardLayout.show(panel, TICKET_LIST_PANEL);
				TicketTrackerGUI.this.repaint();
				TicketTrackerGUI.this.validate();
			} else {
				//Otherwise, set all of the fields with the information
				txtId.setText("" + t.getTicketId());
				txtState.setText(t.getStateName());
				txtTitle.setText(t.getTitle());
				txtSubmitter.setText(t.getSubmitter());
				txtOwner.setText(t.getOwner());
				String flagString = t.getFlagString();
				if (flagString == null) {
					txtFlag.setText("");
				} else {
					txtFlag.setText("" + t.getFlagString());
				}
				notesTableModel.updateData(t.getNotesArray());
			}
		}
		
		/**
		 * {@link NotesTableModel} is the object underlying the {@link JTable} object that displays
		 * the list of Notes to the user.
		 * @author Sarah Heckman
		 */
		private class NotesTableModel extends AbstractTableModel {
			
			/** ID number used for object serialization. */
			private static final long serialVersionUID = 1L;
			/** Column names for the table */
			private String [] columnNames = {"Note Author", "Note Text"};
			/** Data stored in the table */
			private Object [][] data;

			/**
			 * Returns the number of columns in the table.
			 * @return the number of columns in the table.
			 */
			public int getColumnCount() {
				return columnNames.length;
			}

			/**
			 * Returns the number of rows in the table.
			 * @return the number of rows in the table.
			 */
			public int getRowCount() {
				if (data == null) 
					return 0;
				return data.length;
			}
			
			/**
			 * Returns the column name at the given index.
			 * @return the column name at the given column.
			 */
			public String getColumnName(int col) {
				return columnNames[col];
			}

			/**
			 * Returns the data at the given {row, col} index.
			 * @return the data at the given location.
			 */
			public Object getValueAt(int row, int col) {
				if (data == null)
					return null;
				return data[row][col];
			}
			
			/**
			 * Sets the given value to the given {row, col} location.
			 * @param value Object to modify in the data.
			 * @param row location to modify the data.
			 * @param column location to modify the data.
			 */
			public void setValueAt(Object value, int row, int col) {
				data[row][col] = value;
				fireTableCellUpdated(row, col);
			}
			
			/**
			 * Updates the given model with {@link Course} information from the {@link CourseCatalog}.
			 */
			public void updateData(String [][] notes) {
				data = notes;
			}
		}
	}
	
	/**
	 * Inner class that creates the look and behavior for the {@link JPanel} that 
	 * allows for creation of a new ticket.
	 * 
	 * @author Dr. Sarah Heckman (heckman@csc.ncsu.edu)
	 */
	private class CreateTicketPanel extends JPanel implements ActionListener {
		/** ID number used for object serialization. */
		private static final long serialVersionUID = 1L;
		/** Label for identifying summary text field */
		private JLabel lblSummary;
		/** Text field for entering summary information */
		private JTextField txtTitle;
		/** Label for identifying submitter text field */
		private JLabel lblSubmitter;
		/** Text field for entering submitter id */
		private JTextField txtSubmitter;
		/** Label for note text field */
		private JLabel lblNote;
		/** Text field for note information*/
		private JTextArea txtNote;
		/** Button to add a ticket */
		private JButton btnAdd;
		/** Button for canceling add action */
		private JButton btnCancel;
		
		/**
		 * Creates the {@link JPanel} for adding new ticket to the 
		 * tracker.
		 */
		public CreateTicketPanel() {
			super(new GridBagLayout());  
			
			//Construct widgets
			lblSummary = new JLabel("Ticket Title");
			txtTitle = new JTextField(30);
			lblSubmitter = new JLabel("Ticket Submitter");
			txtSubmitter = new JTextField(30);
			lblNote = new JLabel("Ticket Notes");
			txtNote = new JTextArea(10, 30);
			btnAdd = new JButton("Add Ticket to List");
			btnCancel = new JButton("Cancel");
			
			//Adds action listeners
			btnAdd.addActionListener(this);
			btnCancel.addActionListener(this);
			
			//Builds summary panel, which is a 2 row, 1 col grid
			JPanel pnlInfo = new JPanel();
			pnlInfo.setLayout(new GridBagLayout());
			
			GridBagConstraints c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 0;
			c.gridheight = 1;
			c.gridwidth = 2;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.CENTER;
			c.fill = GridBagConstraints.HORIZONTAL;
			pnlInfo.add(lblSummary, c);
			c = new GridBagConstraints();
			c.gridx = 1;
			c.gridy = 0;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.CENTER;
			c.fill = GridBagConstraints.HORIZONTAL;
			pnlInfo.add(txtTitle, c);
			c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 2;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.CENTER;
			c.fill = GridBagConstraints.HORIZONTAL;
			pnlInfo.add(lblSubmitter, c);
			c = new GridBagConstraints();
			c.gridx = 1;
			c.gridy = 2;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.CENTER;
			c.fill = GridBagConstraints.HORIZONTAL;
			pnlInfo.add(txtSubmitter, c);
			c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 3;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.CENTER;
			c.fill = GridBagConstraints.HORIZONTAL;
			pnlInfo.add(lblNote, c);
			c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 4;
			c.gridheight = 1;
			c.gridwidth = 2;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.CENTER;
			c.fill = GridBagConstraints.BOTH;
			pnlInfo.add(txtNote, c);
			
			Border lowerEtched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
			TitledBorder border = BorderFactory.createTitledBorder(lowerEtched, "New Ticket Information");
			pnlInfo.setBorder(border);
			pnlInfo.setToolTipText("New Ticket Information");
			
			//Build button panel, which is a 1 row, 2 col grid
			JPanel pnlButtons = new JPanel();
			pnlButtons.setLayout(new GridBagLayout());
			c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 0;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.CENTER;
			c.fill = GridBagConstraints.HORIZONTAL;
			pnlButtons.add(btnAdd, c);
			c = new GridBagConstraints();
			c.gridx = 1;
			c.gridy = 0;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.CENTER;
			c.fill = GridBagConstraints.HORIZONTAL;
			pnlButtons.add(btnCancel, c);
			
			border = BorderFactory.createTitledBorder(lowerEtched, "New Ticket Actions");
			pnlButtons.setBorder(border);
			pnlButtons.setToolTipText("New Ticket Actions");
			
			//Adds all panels to main panel
			c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 0;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.PAGE_START;
			c.fill = GridBagConstraints.HORIZONTAL;
			add(pnlInfo, c);
			c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 1;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.PAGE_END;
			c.fill = GridBagConstraints.HORIZONTAL;
			add(pnlButtons, c);
		}

		/**
		 * Performs an action based on the given {@link ActionEvent}.
		 * @param e user event that triggers an action.
		 */
		public void actionPerformed(ActionEvent e) {
			boolean done = true; //Assume done unless error
			if (e.getSource() == btnAdd) {
				//Add ticket to the list
				String summary = txtTitle.getText();
				String submitter = txtSubmitter.getText();
				String note = txtNote.getText();
				//Get instance of model and add ticket
				try {
					TicketTrackerModel.getInstance().addTicketToList(summary, submitter, note);
				} catch (IllegalArgumentException exp) {
					done = false;
					JOptionPane.showMessageDialog(TicketTrackerGUI.this, "Invalid ticket information.");
				}
			} 
			if (done) {
				//All buttons lead to back ticket list
				cardLayout.show(panel, TICKET_LIST_PANEL);
				pnlTicketList.updateTable(null, null);
				TicketTrackerGUI.this.repaint();
				TicketTrackerGUI.this.validate();
				//Reset fields
				txtTitle.setText("");
				txtSubmitter.setText("");
			}
		}
	}
}