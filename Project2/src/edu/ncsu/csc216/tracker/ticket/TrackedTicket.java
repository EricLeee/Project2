/**
 * 
 */
package edu.ncsu.csc216.tracker.ticket;

import java.util.ArrayList;

import edu.ncsu.csc216.ticket.xml.NoteItem;
import edu.ncsu.csc216.ticket.xml.NoteList;
import edu.ncsu.csc216.ticket.xml.Ticket;
import edu.ncsu.csc216.tracker.command.Command;
import edu.ncsu.csc216.tracker.command.Command.CommandValue;
import edu.ncsu.csc216.tracker.command.Command.Flag;

/**
 * Sets up a Tracked ticket object
 * @author jli29
 * @author sdpate11
 *
 */
public class TrackedTicket {

	/** id for the ticket */
	private int ticketId;

	/** ticket title */
	private String title;
	
	/** user who submits the ticket */
	private String submitter;
	
	/** oner of the ticket */
	private String owner;
	
	/** ticket notes */
	private ArrayList<Note> notes;
	
	/** flag for the ticket */
	private Flag flag;
	
	/** state name */
	public static final String NEW_NAME = "New";
	
	/** state name */
	public static final String ASSIGNED_NAME = "Assigned";
	
	/** state name */
	public static final String WORKING_NAME = "Working";
	
	/** state name */
	public static final String FEEDBACK_NAME = "Feedback";
	
	/** close state name */
	public static final String CLOSED_NAME = "Closed";
	
	/** counter for id */
	private static int counter;
	
	/** new state */
	private final TicketState newState = new NewState();
	
	/** assigned state */
	private final TicketState assignedState = new AssignedState();
	
	/** working state */
	private final TicketState workingState =  new WorkingState();
	
	/** feedback state */
	private final TicketState feedbackState = new FeedBackState();
	
	/** close state */
	private final TicketState closedState = new ClosedState();

    /** ticket state */
    private TicketState state = newState;
	
    /**
     * Constructs a TrackedTicket object
     * @param title title of the ticket
     * @param submitter submitter of the ticket
     * @param note ticket notes
     */
	public TrackedTicket(String title, String submitter, String note) {
		if (title == null || title.equals("") || submitter == null) throw new IllegalArgumentException();
		this.ticketId = counter;
		incrementCounter();
		this.title = title;
		this.submitter = submitter;
		this.notes = new ArrayList<Note>();
		this.notes.add(new Note(submitter, note));
		this.setState(NEW_NAME);
	}
	
	/**
	 * Constructs a Tracked Ticket with an existing ticket
	 * @param ticket input ticket
	 */
	public TrackedTicket(Ticket ticket) {
		this.ticketId = ticket.getId();
		incrementCounter();
		this.title = ticket.getTitle();
		this.submitter = ticket.getSubmitter();
		this.owner = ticket.getOwner();
		if (ticket.getFlag() != null) {
			this.setFlag(ticket.getFlag());
		}
		this.notes = new ArrayList<Note>();
		for (int i = 0; i < ticket.getNoteList().getNotes().size(); i++) {
			
			this.notes.add(new Note(ticket.getNoteList().getNotes().get(i).getNoteAuthor(), 
					ticket.getNoteList().getNotes().get(i).getNoteText()));
		}
		this.setState(ticket.getState());
		
	}
	
	/**
	 * update counter by 1
	 */
	public static void incrementCounter() {
		counter++;
	}
	
	/**
	 * returns ticket's id
	 * @return ticket's id
	 */
	public int getTicketId() {
		return this.ticketId;
	}
	
	/**
	 * returns state's name
	 * @return state's name
	 */
	public String getStateName() {
		if (this.state == newState) {
			return newState.getStateName();
		} else if (this.state == assignedState) {
			return assignedState.getStateName();
		} else if (this.state == workingState) {
			return workingState.getStateName();
		} else if (this.state == feedbackState) {
			return feedbackState.getStateName();
		} else if (this.state == closedState) {
			return closedState.getStateName();
		}
		return null;
	}
	
	/**
	 * set state
	 * @param state state name to be set
	 */
	private void setState(String state) {
		if (state.equals(NEW_NAME)) {
		    this.state = newState;
		} else if (state.equals(ASSIGNED_NAME)) {
		    this.state = assignedState;
		} else if (state.equals(WORKING_NAME)) {
		    this.state = workingState;
		} else if (state.equals(FEEDBACK_NAME)) {
		    this.state = feedbackState;
		} else if (state.equals(CLOSED_NAME)) {
		    this.state = closedState;
		} else {
			throw new IllegalArgumentException();
		}
	}
	
	/**
	 * returns flag
	 * @return current flag
	 */
	public Flag getFlag() {
	    return this.flag;
	}
	
	/**
	 * returns flag string
	 * @return flag string
	 */
	public String getFlagString() {
		if (this.flag == Flag.DUPLICATE) {
			return Command.F_DUPLICATE;
		} else if (this.flag == Flag.INAPPROPRIATE) {
			return Command.F_INAPPROPRIATE;
		} else if (this.flag == Flag.RESOLVED) {
			return Command.F_RESOLVED;
		} 
		return null;
	}
	
	/**
	 * set flag
	 * @param flag flag to be set
	 */
	private void setFlag(String flag) {
		if (flag.equals(Command.F_DUPLICATE)) {
			this.flag = Flag.DUPLICATE;
		} else if (flag.equals(Command.F_INAPPROPRIATE)) {
			this.flag = Flag.INAPPROPRIATE;
		} else if (flag.equals(Command.F_RESOLVED)) {
			this.flag = Flag.RESOLVED;
		} 
	}
	
	/**
	 * returns owner
	 * @return owner
	 */
	public String getOwner() {
		return this.owner;
	}
	
	/**
	 * returns ticket title
	 * @return ticket's title
	 */
	public String getTitle() {
		return this.title;
	}
	
	/**
	 * returns submitter
	 * @return submitter
	 */
	public String getSubmitter() {
		return this.submitter;
	}
	
	/**
	 * returns all notes
	 * @return all notes
	 */
	public ArrayList<Note> getNotes() {
		return this.notes;
	}
	
	/**
	 * updates state
	 * @param c passed command
	 */
	public void update(Command c) {
		state.updateState(c);
	}
	
	/**
	 * returns the ticket on a XMLTicket format
	 * @return the ticket on a XMLTicket format
	 */
	public Ticket getXMLTicket() {
		Ticket ticket = new Ticket();
		ticket.setFlag(this.getFlagString());
		ticket.setId(this.getTicketId());
		NoteList nl = new NoteList();
		for (int i = 0; i < this.notes.size(); i++) {
			NoteItem n = new NoteItem();
			n.setNoteAuthor(notes.get(i).getNoteAuthor());
			n.setNoteText(notes.get(i).getNoteText());
			nl.getNotes().add(n);
		}
		ticket.setNoteList(nl);
		ticket.setOwner(this.owner);
		ticket.setState(this.getStateName());
		ticket.setSubmitter(this.submitter);
		ticket.setTitle(this.title);
		return ticket;
		
	}
	
	/**
	 * set counter
	 * @param count set counter
	 */
	public static void setCounter(int count) {
		if (count <= 0) {
			throw new IllegalArgumentException();
		}
		counter = count;
	}
	
	/**
	 * returns notes array
	 * @return notes array
	 */
	public String[][] getNotesArray() {
		String[][] note = new String[this.notes.size()][2];
		for (int i = 0; i < this.notes.size(); i++) {
			note[i][0] = this.notes.get(i).getNoteAuthor();
			note[i][1] = this.notes.get(i).getNoteText();
		}
		return note;
	}

    /**
     * Ticket State interface
     * @author jialangLi
     *
     */
    private interface TicketState {
    	
    	/**
    	 * Update the {@link TrackedTicket} based on the given {@link Command}.
    	 * An {@link UnsupportedOperationException} is throw if the {@link CommandValue}
    	 * is not a valid action for the given state.  
    	 * @param c {@link Command} describing the action that will update the {@link TrackedTicket}'s
    	 * state.
    	 * @throws UnsupportedOperationException if the {@link CommandValue} is not a valid action
    	 * for the given state.
    	 */
    	void updateState(Command c);
    	
    	/**
    	 * Returns the name of the current state as a String.
    	 * @return the name of the current state as a String.
    	 */
    	String getStateName();
    
    }

    /**
     * NewA. Transition from New to Assigned. 
     * The owner's user id is associated with the ticket 
     * and identifies the person responsible for resolving the ticket.
     * The ticket retains that same owner unless the ticket is reassigned 
     * to another owner through the Working state.
     * 
     * @author jialangLi
     *
     */
    private class NewState implements TicketState {
    
    	@Override
    	public void updateState(Command c) {
    		if (c.getCommand() != CommandValue.POSSESSION) 
    			throw new UnsupportedOperationException();
    		if (c.getCommand() == CommandValue.POSSESSION) {
    			owner = c.getOwner();
    			notes.add(new Note(c.getNoteAuthor(), c.getNoteText()));
    			setState(ASSIGNED_NAME);
    		}
    	}
    
    	@Override
    	public String getStateName() {
    		return TrackedTicket.NEW_NAME;
    	}
    	
    }

    /**
     * AssignedA. Transition from Assigned to Working. 
     * The owner has accepted the ticket.
     * AssignedB. Transition from Assigned to Closed. 
     * The owner closed the ticket with a flag of Duplicate or Inappropriate.
     * 
     * @author jialangLi
     *
     */
    private class AssignedState implements TicketState {
    
    	@Override
    	public void updateState(Command c) {
    		if (c.getCommand() != CommandValue.ACCEPTED &&
    				c.getCommand() != CommandValue.CLOSED) 
    			throw new UnsupportedOperationException();
    		if (c.getCommand() == CommandValue.ACCEPTED) {
    			notes.add(new Note(c.getNoteAuthor(), c.getNoteText()));
    			state = workingState;
    		} else if (c.getCommand() == CommandValue.CLOSED) {
    			if (c.getFlag() == Flag.DUPLICATE
    				|| c.getFlag() == Flag.INAPPROPRIATE) {
    				notes.add(new Note(c.getNoteAuthor(), c.getNoteText()));
    			flag = c.getFlag();
    			state = closedState;
    			} else {
    				throw new IllegalArgumentException("Invalid flag.");
    			}
    		} 
    	}
    
    	@Override
    	public String getStateName() {
    		return TrackedTicket.ASSIGNED_NAME;
    	}
    	
    }

    /**
     * Working state
     * @author jialangLi
     *
     */
    private class WorkingState implements TicketState {
    
    	@Override
    	public void updateState(Command c) {
    		if (c.getCommand() != CommandValue.PROGRESS &&
    				c.getCommand() != CommandValue.CLOSED &&
    				c.getCommand() != CommandValue.FEEDBACK &&
    				c.getCommand() != CommandValue.POSSESSION) 
    			throw new UnsupportedOperationException();
    		notes.add(new Note(c.getNoteAuthor(), c.getNoteText()));
    		if (c.getCommand() == CommandValue.PROGRESS) {
    			state = workingState;
    		} else if (c.getCommand() == CommandValue.FEEDBACK) {
    			state = feedbackState;
    		} else if (c.getCommand() == CommandValue.CLOSED) {
    			flag = c.getFlag();
    			state = closedState;
    		} else if (c.getCommand() == CommandValue.POSSESSION) {
    			owner = c.getOwner();
    			state = assignedState;
    		}
    	}
    
    	@Override
    	public String getStateName() {
    		return TrackedTicket.WORKING_NAME;
    	}
    	
    }

    /**
     * Feedback state
     * @author jialangLi
     *
     */
    private class FeedBackState implements TicketState {
    
    	@Override
    	public void updateState(Command c) {
    		if (c.getCommand() != CommandValue.FEEDBACK) 
    			throw new UnsupportedOperationException();
    		notes.add(new Note(c.getNoteAuthor(), c.getNoteText()));
    		if (c.getCommand() == CommandValue.FEEDBACK) {
    			state = workingState;
    		}
    	    
    	}
    
    	@Override
    	public String getStateName() {
    		return TrackedTicket.FEEDBACK_NAME;
    	}
    	
    }

    /**
     * Close state
     * @author jialangLi
     *
     */
    private class ClosedState implements TicketState {
    
    	@Override
    	public void updateState(Command c) {
    		if (c.getCommand() != CommandValue.PROGRESS &&
    				c.getCommand() != CommandValue.POSSESSION) 
    			throw new UnsupportedOperationException();
    		notes.add(new Note(c.getNoteAuthor(), c.getNoteText()));
    		if (c.getCommand() == CommandValue.PROGRESS) {
    			flag = null;
    			state = workingState;
    		} else if (c.getCommand() == CommandValue.POSSESSION) {
    			owner = c.getOwner();
    			flag = null;
    			state = assignedState;
    		}
    	}
    
    	@Override
    	public String getStateName() {
    		return TrackedTicket.CLOSED_NAME;
    	}
    	
    }
}
