/**
 * 
 */
package edu.ncsu.csc216.tracker.ticket;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import edu.ncsu.csc216.ticket.xml.NoteItem;
import edu.ncsu.csc216.ticket.xml.NoteList;
import edu.ncsu.csc216.ticket.xml.Ticket;
import edu.ncsu.csc216.tracker.command.Command;
import edu.ncsu.csc216.tracker.command.Command.CommandValue;
import edu.ncsu.csc216.tracker.command.Command.Flag;

/**
 * Testing TicketTracker
 * 
 * @author Sayam Patel
 *
 */
public class TrackedTicketTest {

	/** state name */
	public static final String NEW_NAME = "New";

	/** state name */
	public static final String ASSIGNED_NAME = "Assigned";

	/** state name */
	public static final String WORKING_NAME = "Working";

	/** state name */
	public static final String FEEDBACK_NAME = "Feedback";

	/** closed state name */
	public static final String CLOSED_NAME = "Closed";

	/** submitter */
	private static final String SUBMITTER = "sdpate11";

	/** title */
	private static final String TITLE = "PLS HALP";

	/** note */
	private static final String NOTE = "FIX ME PROBLEM PLS DESU~";

	/** owner */
	private static final String EMPLOYEE = "ITECS Joe Mason";

	/** accepted note */
	private static final String ACCEPTED_NOTE = "On this like donkey on kong";

	/** TrackedTicket var */
	private TrackedTicket tt;

	/** Note Arr list */
	private ArrayList<Note> notes;

	/** single notes array */
	private String[] singleNoteArr;

	/** notesArr */
	private String[][] noteArr;

	/**
	 * Setup for testing
	 * 
	 * @throws java.lang.Exception
	 *             if setUp throws err
	 */
	@Before
	public void setUp() throws Exception {
		TrackedTicket.setCounter(1);

		// initially has a note entry with NOTE and note author as SUBMITTER
		tt = new TrackedTicket(TITLE, SUBMITTER, NOTE);

	}

	/**
	 * Testing ticket constructor
	 * 
	 * @throws Exception
	 *             if setup throws err
	 */
	@Test
	public void testTrackedTicket() throws Exception {
		setUp();

		// testing initial constructor fields
		assertEquals(1, tt.getTicketId());
		assertNull(tt.getOwner()); // no owner at first
		assertEquals(TITLE, tt.getTitle());
		assertEquals(SUBMITTER, tt.getSubmitter());
		assertNull(tt.getFlag());
		assertEquals(NEW_NAME, tt.getStateName());

		// should be length of 1
		notes = tt.getNotes();
		assertEquals(1, notes.size());

		singleNoteArr = notes.get(0).getNoteArray();
		assertEquals(SUBMITTER, singleNoteArr[0]);
		assertEquals(NOTE, singleNoteArr[1]);

		noteArr = tt.getNotesArray();
		assertEquals(1, noteArr.length);

		// second type of constructor with ticket parameter

		// making a new ticket
		Ticket ticket = new Ticket();
		ticket.setId(2);
		NoteList nl = new NoteList();
		NoteItem e = new NoteItem();
		e.setNoteAuthor(SUBMITTER);
		e.setNoteText(NOTE);
		nl.getNotes().add(e);

		ticket.setNoteList(nl);
		ticket.setState(NEW_NAME);
		ticket.setSubmitter(SUBMITTER);
		ticket.setTitle(TITLE);

		// changing to new tt
		tt = new TrackedTicket(ticket);
		// testing initial constructor fields
		assertEquals(2, tt.getTicketId());
		assertNull(tt.getOwner()); // no owner at first
		assertEquals(TITLE, tt.getTitle());
		assertEquals(SUBMITTER, tt.getSubmitter());
		assertNull(tt.getFlag());
		assertEquals(NEW_NAME, tt.getStateName());

		// should be length of 1
		notes = tt.getNotes();
		assertEquals(1, notes.size());

		singleNoteArr = notes.get(0).getNoteArray();
		assertEquals(SUBMITTER, singleNoteArr[0]);
		assertEquals(NOTE, singleNoteArr[1]);

		noteArr = tt.getNotesArray();
		assertEquals(1, noteArr.length);

		// creating a closed Ticket to pass into TrackedTicket()
		ticket = new Ticket();
		nl = new NoteList();
		e = new NoteItem();
		e.setNoteAuthor(SUBMITTER);
		e.setNoteText(NOTE);
		nl.getNotes().add(e);
		ticket.setId(3);
		ticket.setFlag(Command.F_RESOLVED);
		ticket.setNoteList(nl);
		ticket.setOwner(EMPLOYEE);
		ticket.setState(CLOSED_NAME);
		ticket.setSubmitter(SUBMITTER);
		ticket.setTitle(TITLE);

		tt = new TrackedTicket(ticket);

		// testing initial constructor fields
		assertEquals(3, tt.getTicketId());
		assertEquals(EMPLOYEE, tt.getOwner()); // no owner at first
		assertEquals(TITLE, tt.getTitle());
		assertEquals(SUBMITTER, tt.getSubmitter());

		// flag is resolved
		assertEquals(Flag.RESOLVED, tt.getFlag());
		assertEquals(Command.F_RESOLVED, tt.getFlagString());

		assertEquals(CLOSED_NAME, tt.getStateName());

		// should be length of 1
		notes = tt.getNotes();
		assertEquals(1, notes.size());

		singleNoteArr = notes.get(0).getNoteArray();
		assertEquals(SUBMITTER, singleNoteArr[0]);
		assertEquals(NOTE, singleNoteArr[1]);

		noteArr = tt.getNotesArray();
		assertEquals(1, noteArr.length);

	}

	/**
	 * Testing update()
	 * 
	 * @throws Exception
	 *             if setUp throws err
	 */
	@Test
	public void testUpdate() throws Exception {
		setUp();

		// updating tracked ticket with possesion
		try {
			tt.update(new Command(CommandValue.PROGRESS, EMPLOYEE, null, "jli29", NOTE));
			fail();
		} catch (UnsupportedOperationException e) {
			assertEquals(TrackedTicket.NEW_NAME, tt.getStateName());
		}
		Command c = new Command(CommandValue.POSSESSION, EMPLOYEE, null, "manager", "assigning to employee.");
		tt.update(c);

		// owner is EMPLOYEE now
		assertEquals(EMPLOYEE, tt.getOwner());
		assertEquals(TITLE, tt.getTitle()); // same as before
		assertEquals(SUBMITTER, tt.getSubmitter()); // same as before

		// state is assigned
		
		try {
			tt.update(new Command(CommandValue.CLOSED, "jli29", Flag.RESOLVED, EMPLOYEE, NOTE));
			fail();
		} catch (IllegalArgumentException e) {
			assertEquals("Invalid flag.", e.getMessage());
		}
		
		try {
			tt.update(new Command(CommandValue.PROGRESS, EMPLOYEE, null, "jli29", "nothing"));
			fail();
		} catch (UnsupportedOperationException e) {
			assertEquals(ASSIGNED_NAME, tt.getStateName());
		}

		// should be length of 2
		notes = tt.getNotes();
		assertEquals(2, notes.size());

		singleNoteArr = notes.get(0).getNoteArray(); // first note
		assertEquals(SUBMITTER, singleNoteArr[0]);
		assertEquals(NOTE, singleNoteArr[1]);

		// second note
		singleNoteArr = notes.get(1).getNoteArray();
		assertEquals("manager", singleNoteArr[0]);
		assertEquals("assigning to employee.", singleNoteArr[1]);

		// updating ticked with accepted
		c = new Command(CommandValue.ACCEPTED, null, null, EMPLOYEE, ACCEPTED_NOTE);
		tt.update(c);

		// state is working
		assertEquals(WORKING_NAME, tt.getStateName());

		// EMPLOYEE is owner
		assertEquals(EMPLOYEE, tt.getOwner());
		assertEquals(TITLE, tt.getTitle());
		assertEquals(SUBMITTER, tt.getSubmitter()); // same as before

		// should be length of 3
		notes = tt.getNotes();
		assertEquals(3, notes.size());

		singleNoteArr = notes.get(0).getNoteArray(); // first note
		assertEquals(SUBMITTER, singleNoteArr[0]);
		assertEquals(NOTE, singleNoteArr[1]);

		singleNoteArr = notes.get(2).getNoteArray(); // third note
		assertEquals(EMPLOYEE, singleNoteArr[0]);
		assertEquals(ACCEPTED_NOTE, singleNoteArr[1]);

		// updating with progress
		c = new Command(CommandValue.PROGRESS, null, null, EMPLOYEE, "working on it beep boop.");
		tt.update(c);

		// state is still working
		assertEquals(WORKING_NAME, tt.getStateName());

		// EMPLOYEE is owner
		assertEquals(EMPLOYEE, tt.getOwner());
		assertEquals(TITLE, tt.getTitle());
		assertEquals(SUBMITTER, tt.getSubmitter()); // same as before

		// should be length of 4
		notes = tt.getNotes();
		assertEquals(4, notes.size());

		singleNoteArr = notes.get(3).getNoteArray(); // fourth note
		assertEquals(EMPLOYEE, singleNoteArr[0]);
		assertEquals("working on it beep boop.", singleNoteArr[1]);

		// updating ticket with feedback
		c = new Command(CommandValue.FEEDBACK, null, null, EMPLOYEE, "Can you provide more info pls?");
		tt.update(c);

		// state is feedback
		assertEquals(FEEDBACK_NAME, tt.getStateName());
		
		try {
			tt.update(new Command(CommandValue.ACCEPTED, null, null, EMPLOYEE, NOTE));
			fail();
		} catch (UnsupportedOperationException e) {
			assertEquals(FEEDBACK_NAME, tt.getStateName());
		}

		// EMPLOYEE is owner in feedback state
		assertEquals(EMPLOYEE, tt.getOwner());
		assertEquals(TITLE, tt.getTitle());
		assertEquals(SUBMITTER, tt.getSubmitter()); // same as before

		// should be length of 5
		notes = tt.getNotes();
		assertEquals(5, notes.size());

		noteArr = tt.getNotesArray();

		// testing single note arr
		singleNoteArr = noteArr[4]; // 5th note

		assertEquals(EMPLOYEE, singleNoteArr[0]);
		assertEquals("Can you provide more info pls?", singleNoteArr[1]);

		// updating with customer feedback
		c = new Command(CommandValue.FEEDBACK, null, null, SUBMITTER, "Here is more info.");
		tt.update(c);

		// state is working
		assertEquals(WORKING_NAME, tt.getStateName());

		// EMPLOYEE is owner in feedback state
		assertEquals(EMPLOYEE, tt.getOwner());
		assertEquals(TITLE, tt.getTitle());
		assertEquals(SUBMITTER, tt.getSubmitter()); // same as before

		// should be length of 6
		notes = tt.getNotes();
		assertEquals(6, notes.size());

		singleNoteArr = notes.get(5).getNoteArray(); // 6th note
		assertEquals(SUBMITTER, singleNoteArr[0]);
		assertEquals("Here is more info.", singleNoteArr[1]);

		// updating with closed
		c = new Command(CommandValue.CLOSED, null, Flag.RESOLVED, EMPLOYEE, "Fixed it! $$$");
		tt.update(c);

		// state is closed
		assertEquals(CLOSED_NAME, tt.getStateName());

		// flag is resolved
		assertEquals(Flag.RESOLVED, tt.getFlag());
		assertEquals(Command.F_RESOLVED, tt.getFlagString());

		// EMPLOYEE is owner in closed
		assertEquals(EMPLOYEE, tt.getOwner());
		assertEquals(TITLE, tt.getTitle());
		assertEquals(SUBMITTER, tt.getSubmitter()); // same as before

		// should be length of 7
		notes = tt.getNotes();
		assertEquals(7, notes.size());

		singleNoteArr = notes.get(6).getNoteArray(); // 7th note
		assertEquals(EMPLOYEE, singleNoteArr[0]);
		assertEquals("Fixed it! $$$", singleNoteArr[1]);

		// another employee reopening ticket
		c = new Command(CommandValue.POSSESSION, "another employee", null, "another employee", "reopening ticket");
		tt.update(c);

		// state is assigned again
		assertEquals(ASSIGNED_NAME, tt.getStateName());

		// flag should be null
		assertNull(tt.getFlag());

		// another employee is owner now
		assertEquals("another employee", tt.getOwner());
		assertEquals(TITLE, tt.getTitle());
		assertEquals(SUBMITTER, tt.getSubmitter()); // same as before

		// should be length of 8
		notes = tt.getNotes();
		assertEquals(8, notes.size());

		singleNoteArr = notes.get(7).getNoteArray(); // 8th note
		assertEquals("another employee", singleNoteArr[0]);
		assertEquals("reopening ticket", singleNoteArr[1]);

		// another employee marking it duplicate
		c = new Command(CommandValue.CLOSED, null, Flag.DUPLICATE, "another employee",
				"was accidentally assigned to me.");
		tt.update(c);

		// state is closed again
		assertEquals(CLOSED_NAME, tt.getStateName());

		// flag is duplicate now
		assertEquals(Flag.DUPLICATE, tt.getFlag());
		assertEquals(Command.F_DUPLICATE, tt.getFlagString());

		// another employee is still owner
		assertEquals("another employee", tt.getOwner());
		assertEquals(TITLE, tt.getTitle());
		assertEquals(SUBMITTER, tt.getSubmitter()); // same as before

		// should be length of 9
		notes = tt.getNotes();
		assertEquals(9, notes.size());

		singleNoteArr = notes.get(8).getNoteArray(); // 9th note
		assertEquals("another employee", singleNoteArr[0]);
		assertEquals("was accidentally assigned to me.", singleNoteArr[1]);

		// "another employee" reopening the ticket
		c = new Command(CommandValue.PROGRESS, null, null, "another employee", "Forgot something, reopening ticket.");
		tt.update(c);

		// state is working
		assertEquals(WORKING_NAME, tt.getStateName());

		// flag is null 
		assertNull(tt.getFlag());

		// another employee is still owner
		assertEquals("another employee", tt.getOwner());
		assertEquals(TITLE, tt.getTitle());
		assertEquals(SUBMITTER, tt.getSubmitter()); // same as before

		// should be length of 10
		notes = tt.getNotes();
		assertEquals(10, notes.size());

		singleNoteArr = notes.get(9).getNoteArray(); // 10th note
		assertEquals("another employee", singleNoteArr[0]);
		assertEquals("Forgot something, reopening ticket.", singleNoteArr[1]);

		// "another employee" assigning to original EMPLOYEE
		c = new Command(CommandValue.POSSESSION, EMPLOYEE, null, "another employee", "assigning to Joe Mason");
		tt.update(c);

		// state is assigned
		assertEquals(ASSIGNED_NAME, tt.getStateName());

		// flag is null
		assertNull(tt.getFlag());

		// EMPLOYEE is owner now
		assertEquals(EMPLOYEE, tt.getOwner());
		assertEquals(TITLE, tt.getTitle());
		assertEquals(SUBMITTER, tt.getSubmitter()); // same as before

		// should be length of 11
		notes = tt.getNotes();
		assertEquals(11, notes.size());

		noteArr = tt.getNotesArray();

		// testing single note arr
		singleNoteArr = noteArr[10]; // 11th note
		assertEquals("another employee", singleNoteArr[0]);
		assertEquals("assigning to Joe Mason", singleNoteArr[1]);
	}
	
	/**
	 * testing getXMLTicket() in tracked ticket
	 * @throws Exception if setup throws err
	 */
	@Test
	public void getXMLTicketTest() throws Exception {
		setUp();
		
		Ticket ticket = tt.getXMLTicket();
		
		NoteList nl = new NoteList();
		NoteItem e = new NoteItem();
		e.setNoteAuthor(SUBMITTER);
		e.setNoteText(NOTE);
		nl.getNotes().add(e);
		
		//checking fields on XML of tt
		assertNull(ticket.getFlag());
		assertEquals(1, ticket.getId());
		
		List<NoteItem> ticketNoteList = ticket.getNoteList().getNotes();
		
		assertEquals(1, ticketNoteList.size());
		assertEquals(SUBMITTER, ticketNoteList.get(0).getNoteAuthor());
		assertEquals(NOTE, ticketNoteList.get(0).getNoteText());
		
		assertNull(ticket.getOwner());
		assertEquals(NEW_NAME, ticket.getState());
		assertEquals(SUBMITTER, ticket.getSubmitter());
		assertEquals(TITLE, ticket.getTitle());
	}

}
