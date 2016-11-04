/**
 * 
 */
package edu.ncsu.csc216.tracker.ticket_tracker;

import edu.ncsu.csc216.ticket.xml.TicketIOException;
import edu.ncsu.csc216.ticket.xml.TicketReader;
import edu.ncsu.csc216.ticket.xml.TicketWriter;
import edu.ncsu.csc216.tracker.command.Command;
import edu.ncsu.csc216.tracker.ticket.TrackedTicket;

/**
 * Maintains the TrackedTicketList and handles Commands from the GUI.
 * @author jialangLi
 * @author Sayam
 *
 */
public class TicketTrackerModel {
	
	/** Single instance of this */
	private static TicketTrackerModel singleton = new TicketTrackerModel();
	
	/** tracked ticket list */
	private TrackedTicketList trackedTicketList; 
	
	/**
	 * Constructor for TicketTrackerModel 
	 */
	private TicketTrackerModel() {
		this.createNewTicketList();
	}
	
	/**
	 * Returns TicketTrackerModel instance
	 * 
	 * @return TicketTrackerModel instance
	 */
	public static TicketTrackerModel getInstance() {
		return singleton;
	}
	
	/**
	 * Writes ticket to file in XML format
	 * 
	 * @param fileName to save file to 
	 */
	public void saveTicketsToFile(String fileName) {
		if (this.trackedTicketList.getTrackedTickets().isEmpty()) throw new IllegalArgumentException();
		TicketWriter tw = new TicketWriter(fileName);
		for (int i = 0; i < this.trackedTicketList.getTrackedTickets().size(); i++) {
			tw.addItem(this.trackedTicketList.getTrackedTickets().get(i).getXMLTicket());
		}
		try {
			tw.marshal();
		} catch (TicketIOException e) {
			throw new IllegalArgumentException("Unable to save ticket file.");
		}
	}
	
	/**
	 * Loads ticket from XML file
	 * @param fileName file from which to load the tickets
	 */
	public void loadTicketsFromFile(String fileName) {
		TicketReader tr = null;
		try {
			tr = new TicketReader(fileName);
		} catch (TicketIOException e) {
			throw new IllegalArgumentException();
		}
		this.trackedTicketList.addXMLTickets(tr.getTickets());
	}
	
	/**
	 * Creates new ticket list
	 * 
	 */
	public void createNewTicketList() {
		this.trackedTicketList = new TrackedTicketList();
	}
	
	/**
	 * Returns ticket list as array
	 * @return 2D object array
	 */
	public Object[][] getTicketListAsArray() {
		Object[][] tl = new Object[this.trackedTicketList.getTrackedTickets().size()][3];
		for (int i = 0; i < tl.length; i++) {
			tl[i][0] = this.trackedTicketList.getTrackedTickets().get(i).getTicketId();
			tl[i][1] = this.trackedTicketList.getTrackedTickets().get(i).getStateName();
			tl[i][2] = this.trackedTicketList.getTrackedTickets().get(i).getTitle();
		}
		return tl;
	}
	
	/**
	 * getTicketListByOwnerAsArray
	 * 
	 * @param owner which owner to get tickets for?
	 * @return 2D ticket array
	 */
	public Object[][] getTicketListByOwnerAsArray(String owner) {
		if (owner == null) throw new IllegalArgumentException();
		Object[][] tl = new Object[this.trackedTicketList.getTicketsByOwner(owner).size()][3];
		for (int i = 0; i < tl.length; i++) {
			tl[i][0] = this.trackedTicketList.getTicketsByOwner(owner).get(i).getTicketId();
			tl[i][1] = this.trackedTicketList.getTicketsByOwner(owner).get(i).getStateName();
			tl[i][2] = this.trackedTicketList.getTicketsByOwner(owner).get(i).getTitle();
		}
		return tl;
	}
	
	/**
	 * getTicketListBySubmitterAsArray
	 * 
	 * @param submitter which submitter to get tickets for?
	 * @return 2D ticket array
	 */
	public Object[][] getTicketListBySubmitterAsArray(String submitter) {
		if (submitter == null) throw new IllegalArgumentException();
		Object[][] tl = new Object[this.trackedTicketList.getTicketsBySubmitter(submitter).size()][3];
		for (int i = 0; i < tl.length; i++) {
			tl[i][0] = this.trackedTicketList.getTicketsBySubmitter(submitter).get(i).getTicketId();
			tl[i][1] = this.trackedTicketList.getTicketsBySubmitter(submitter).get(i).getStateName();
			tl[i][2] = this.trackedTicketList.getTicketsBySubmitter(submitter).get(i).getTitle();
		}
		return tl;
	}
	
	/**
	 * Gets ticket by id number
	 * @param id id number of the ticket?
	 * @return TrackedTicket ticket with input id 
	 */
	public TrackedTicket getTicketById(int id) {
		return this.trackedTicketList.getTicketById(id);
	}
	
	/**
	 * Executes command on an ticket
	 * @param id id of ticket
	 * @param c Command to perform on ticket with id id
	 */
	public void executeCommand(int id, Command c) {
		this.getTicketById(id).update(c);
	}
	
	/**
	 * Deletes ticket by id
	 * @param id ticket id
	 */
	public void deleteTicketById(int id) {
		this.trackedTicketList.deleteTicketById(id);
	}
	
	/**
	 * Add tickets to list
	 * @param title new ticket title
	 * @param submitter new ticket submitter
	 * @param note new ticket note text
	 */
	public void addTicketToList(String title, String submitter, String note) {
		this.trackedTicketList.addTrackedTicket(title, submitter, note);
	}
}
