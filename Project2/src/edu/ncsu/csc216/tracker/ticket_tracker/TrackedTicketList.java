/**
 * 
 */
package edu.ncsu.csc216.tracker.ticket_tracker;

import java.util.ArrayList;
import java.util.List;

import edu.ncsu.csc216.ticket.xml.Ticket;
import edu.ncsu.csc216.tracker.command.Command;
import edu.ncsu.csc216.tracker.ticket.*;

/**
 * Maintains the current list of TrackedTickets in the TicketTracker system.
 * @author jli29
 * @author Sayam Patel
 * 
 */
public class TrackedTicketList {

	/** initial counter */
	private static final int INITIAL_COUNTER_VALUE = 1;
	
	/** ticket list */
	private ArrayList<TrackedTicket> tickets;
	
	/**
	 *  Constructs the ticket list
	 */
	public TrackedTicketList() {
		this.tickets = new ArrayList<TrackedTicket>();
		TrackedTicket.setCounter(INITIAL_COUNTER_VALUE);
	}
	
	/**
	 * add new Ticket to the list
	 * @param title new ticket title
	 * @param submitter new ticket submitter
	 * @param note new ticket note text
	 * @return id of the new ticket
	 */
	public int addTrackedTicket(String title, String submitter, String note) {
		TrackedTicket tt = new TrackedTicket(title, submitter, note);
		this.tickets.add(tt);
		return tt.getTicketId();
	}
	
	/**
	 * Adds ticket list in XML format
	 * @param ticketList ticket list to added in
	 */
	public void addXMLTickets(List<Ticket> ticketList) {
		for (int i = 0; i < ticketList.size(); i++) {
			TrackedTicket tt = new TrackedTicket(ticketList.get(i));
			this.tickets.add(tt);
			TrackedTicket.setCounter(tt.getTicketId() + 1);
		}
	}
	
	/**
	 * Gets ticket in list
	 * @return List of tracked Ticket
	 */
	public List<TrackedTicket> getTrackedTickets() {
		return this.tickets;
	}
	
	/**
	 * Gets ticket in list by owner
	 * @param owner owner to search
	 * @return List of tracked Ticket of the specific owner
	 */
	public List<TrackedTicket> getTicketsByOwner(String owner) {
		if (owner == null || owner.equals("")) throw new IllegalArgumentException();
		ArrayList<TrackedTicket> tl = new ArrayList<TrackedTicket>();
		for (int i = 0; i < this.tickets.size(); i++) {
			if (this.tickets.get(i).getOwner() != null && 
					this.tickets.get(i).getOwner().equals(owner)) {
				tl.add(this.tickets.get(i));
			}
		}
		return tl;
	}
	
	/**
	 * Gets ticket in list by submitter
	 * @param submitter the target submitter
	 * @return List of tracked Ticket of the specific submitter
	 */
	public List<TrackedTicket> getTicketsBySubmitter(String submitter) {
		if (submitter == null || submitter.equals("")) throw new IllegalArgumentException();
		ArrayList<TrackedTicket> tl = new ArrayList<TrackedTicket>();
		for (int i = 0; i < this.tickets.size(); i++) {
			if (this.tickets.get(i).getSubmitter().equals(submitter)) {
				tl.add(this.tickets.get(i));
			}
		}
		return tl;
	}
	
	/**
	 * Gets ticket by input id
	 * @param id id of the ticket to get
	 * @return Ticket with id
	 */
	public TrackedTicket getTicketById(int id) {
		for (int i = 0; i < this.tickets.size(); i++) {
			if (this.tickets.get(i).getTicketId() == id) {
				return this.tickets.get(i);
			}
		}
		return null;
	}
	
	/**
	 * Executes command on an ticket
	 * @param id id of ticket
	 * @param c Command to perform on ticket with id id
	 */
	public void executeCommand(int id, Command c) {
		for (int i = 0; i < this.tickets.size(); i++) {
			if (this.tickets.get(i).getTicketId() == id) {
				this.tickets.get(i).update(c);
			}
		}
	}
	
	/**
	 * Deletes ticket by id
	 * @param id ticket id
	 */
	public void deleteTicketById(int id) {
		for (int i = 0; i < this.tickets.size(); i++) {
			if (this.tickets.get(i).getTicketId() == id) {
				this.tickets.remove(this.tickets.get(i));
			}
		}
	}
}
