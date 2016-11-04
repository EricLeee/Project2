/**
 * 
 */
package edu.ncsu.csc216.tracker.ticket_tracker;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import edu.ncsu.csc216.ticket.xml.NoteItem;
import edu.ncsu.csc216.ticket.xml.NoteList;
import edu.ncsu.csc216.ticket.xml.Ticket;
import edu.ncsu.csc216.tracker.ticket.TrackedTicket;

/**
 * Test TrackedTicketList
 * 
 * @author Sayam
 *
 */
public class TrackedTicketListTest {

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

	/** tracked ticket list var */
	TrackedTicketList ttl;

	/** tracked ticket List var */
	List<TrackedTicket> ttList;

	/** private ticket */
	private Ticket ticket;

	/** another ticket */
	private Ticket ticket2;

	/** ticket 3 */
	private Ticket ticket3;

	/**
	 * Setup before test. Sets the
	 * 
	 * @throws java.lang.Exception
	 *             if setup throws err
	 */
	@Before
	public void setUp() throws Exception {
		ttl = new TrackedTicketList();

		// a new ticket
		NoteList nl = new NoteList();
		NoteItem e = new NoteItem();
		e.setNoteAuthor(SUBMITTER);
		e.setNoteText(NOTE);
		nl.getNotes().add(e);

		ticket = makeTicket(1, nl, NEW_NAME, SUBMITTER, null, TITLE);

		// new ticket #2
		nl = new NoteList();
		e = new NoteItem();
		e.setNoteAuthor(EMPLOYEE);
		e.setNoteText("On it b0ss");
		nl.getNotes().add(e);
		ticket2 = makeTicket(2, nl, WORKING_NAME, SUBMITTER, EMPLOYEE, TITLE);

		// new ticket #3
		nl = new NoteList();
		e = new NoteItem();
		e.setNoteAuthor(EMPLOYEE + "2");
		e.setNoteText(ACCEPTED_NOTE);
		nl.getNotes().add(e);
		ticket3 = makeTicket(3, nl, WORKING_NAME, SUBMITTER + "2", EMPLOYEE + "2", TITLE);

		// ticket = new Ticket();
		// ticket.setId(1);
		// ticket.setNoteList(nl);
		// ticket.setState(NEW_NAME);
		// ticket.setSubmitter(SUBMITTER);
		// ticket.setTitle(TITLE);
	}

	/**
	 * Private class for making test tickets easily
	 * 
	 * @param id
	 *            of ticket
	 * @param nl
	 *            NoteList
	 * @param state
	 *            state of ticket
	 * @param submitter
	 *            submitter of ticket
	 * @param owner
	 *            owner of ticket
	 * @param title
	 *            title of the ticket
	 * @return ticket Ticket object
	 */
	private Ticket makeTicket(int id, NoteList nl, String state, String submitter, String owner, String title) {
		Ticket tickets = new Ticket();
		tickets.setId(id);
		tickets.setNoteList(nl);
		tickets.setState(state);
		tickets.setSubmitter(submitter);
		tickets.setOwner(owner);
		tickets.setTitle(title);

		return tickets;
	}

	/**
	 * Testing the constructor
	 */
	@Test
	public void testTrackedTicketList() {
		try {
			setUp();
		} catch (Exception e) {
			fail("Setup says: " + e.getMessage());
		}

		ttList = ttl.getTrackedTickets();

		assertEquals(0, ttList.size());

		ttList = ttl.getTicketsByOwner(EMPLOYEE);
		assertEquals(0, ttList.size());

		ttList = ttl.getTicketsBySubmitter(SUBMITTER);
		assertEquals(0, ttList.size());
	}

	/**
	 * Testing add tracked ticket
	 */
	@Test
	public void testAddTrackedTicket() {
		try {
			setUp();
		} catch (Exception e) {
			fail("Setup says: " + e.getMessage());
		}

		int num = ttl.addTrackedTicket(TITLE, SUBMITTER, NOTE);
		ttList = ttl.getTrackedTickets();
		assertEquals(1, ttList.size());
		assertEquals(1, ttList.get(0).getTicketId());
		assertEquals(1, num);

		num = ttl.addTrackedTicket(TITLE, SUBMITTER, NOTE);
		assertEquals(2, ttList.get(1).getTicketId());
		assertEquals(2, num);
	}

	/**
	 * Testing add XML ticket
	 */
	@Test
	public void testAddXMLTickets() {
		try {
			setUp();
		} catch (Exception e) {
			fail("Setup says: " + e.getMessage());
		}

		// making the xml ticket array
		ArrayList<Ticket> ticketList = new ArrayList<Ticket>(2);
		ticketList.add(ticket);
		ticketList.add(ticket2);

		// adding to ttl
		ttl.addXMLTickets(ticketList);

		ttList = ttl.getTrackedTickets();
		assertEquals(2, ttList.size());

	}

	/**
	 * Testing get tickets by owner
	 */
	@Test
	public void testGetTicketsByOwner() {
		try {
			setUp();
		} catch (Exception e) {
			fail("Setup says: " + e.getMessage());
		}

		// making the xml ticket array
		ArrayList<Ticket> ticketList = new ArrayList<Ticket>(3);
		ticketList.add(ticket);
		ticketList.add(ticket2);
		ticketList.add(ticket3);

		// adding tickets to ttl
		ttl.addXMLTickets(ticketList);

		List<TrackedTicket> byOwner = ttl.getTicketsByOwner(EMPLOYEE);

		// only 1 by employee
		assertEquals(1, byOwner.size());

		// only 1 by employee + "2"
		byOwner = ttl.getTicketsByOwner(EMPLOYEE + "2");
		assertEquals(1, byOwner.size());

		// search by null
		try {
			byOwner = ttl.getTicketsByOwner(null);
			fail();
		} catch (IllegalArgumentException e) {
			assertEquals(1, byOwner.size());
		}
	}

	/**
	 * Testing get tickets by submitter
	 */
	@Test
	public void testGetTicketsBySubmitter() {
		try {
			setUp();
		} catch (Exception e) {
			fail("Setup says: " + e.getMessage());
		}

		// making the xml ticket array
		ArrayList<Ticket> ticketList = new ArrayList<Ticket>(3);
		ticketList.add(ticket);
		ticketList.add(ticket2);
		ticketList.add(ticket3);

		ttl.addXMLTickets(ticketList);

		List<TrackedTicket> bySub = ttl.getTicketsBySubmitter(SUBMITTER);
		assertEquals(2, bySub.size());

		bySub = ttl.getTicketsBySubmitter(SUBMITTER + "2");
		assertEquals(1, bySub.size());

		try {
			bySub = ttl.getTicketsBySubmitter(null);
			fail();
		} catch (IllegalArgumentException e) {
			assertEquals(1, bySub.size());
		}

	}

	/**
	 * Testing get tickets by id
	 */
	@Test
	public void testGetTicketsById() {
		try {
			setUp();
		} catch (Exception e) {
			fail("Setup says: " + e.getMessage());
		}

		// making the xml ticket array
		ArrayList<Ticket> ticketList = new ArrayList<Ticket>(3);
		ticketList.add(ticket);
		ticketList.add(ticket2);
		ticketList.add(ticket3);

		ttl.addXMLTickets(ticketList);

		// TODO test by id
		TrackedTicket tt = ttl.getTicketById(1);
		assertEquals(1, tt.getTicketId());

		tt = ttl.getTicketById(2);
		assertEquals(2, tt.getTicketId());

		tt = ttl.getTicketById(3);
		assertEquals(3, tt.getTicketId());

	}
}
