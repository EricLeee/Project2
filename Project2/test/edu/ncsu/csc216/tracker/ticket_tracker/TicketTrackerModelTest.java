package edu.ncsu.csc216.tracker.ticket_tracker;

import static org.junit.Assert.*;

import org.junit.Test;

import edu.ncsu.csc216.tracker.ticket.TrackedTicket;

/**
 * Test TicketTrackerModel
 * 
 * @author jialangLi
 * @author sdpate11
 */
public class TicketTrackerModelTest {
	
	/** TicketTrackerModel */
	private TicketTrackerModel model = TicketTrackerModel.getInstance();
	
	/** test ticket title */
	private static final String TITLE = "test title";
	
	/** test ticket submitter */
	private static final String SUBMITTER = "jli29";
	
	/** test note text */
	private static final String NOTE = "note text";
	
	/** target file to save */
	private static final String ACTUAL_SAVE = "test_files/Actual_save.xml";
	
	/** file to import tickets */
	private static final String LOAD = "test_files/ticket1.xml";
	
	/**
	 * Test TicketTrackerModel.loadTicketsFromFiles()
	 */
	@Test
	public void testLoadTicketsFromFiles() {
		model.createNewTicketList();
		try {
			model.loadTicketsFromFile(LOAD);
			assertEquals(5, TicketTrackerModel.getInstance().getTicketListAsArray().length);
		} catch (IllegalArgumentException e) {
			fail();
			
		}
		try {
			model.loadTicketsFromFile("test_files/ticket2.xml");
			fail();
		} catch (IllegalArgumentException e) {
			assertEquals(null, e.getMessage());
		}
	}

	/**
	 * Test TicketTrackerModel.saveTicketToFile()
	 */
	@Test
	public void testSaveTicketsToFile() {
		model.createNewTicketList();
		
		try {
			model.saveTicketsToFile(ACTUAL_SAVE);
			fail();
		} catch (IllegalArgumentException e) {
			assertEquals(0, model.getTicketListAsArray().length);
		}
		model.addTicketToList(TITLE, SUBMITTER, NOTE);
		try {
			model.saveTicketsToFile(ACTUAL_SAVE);
			assertEquals(1, model.getTicketListAsArray().length);
		} catch (IllegalArgumentException e) {
			fail();
			
		}
		
	}
	
	/**
	 * Test TicketTrackerModel.getTicketListBySubmitterAsArray()
	 */
	@Test
	public void testGetTicketListBySubmitterAsArray() {
		model.createNewTicketList();
		model.addTicketToList(TITLE, SUBMITTER, NOTE);
		try {
			model.getTicketListBySubmitterAsArray(null);
			fail();
		} catch (IllegalArgumentException e) {
			assertEquals(1, model.getTicketListAsArray().length);
		}
		try {
			model.getTicketListBySubmitterAsArray("jli29");
		} catch (IllegalArgumentException e) {
			fail();
			assertEquals(1, model.getTicketListAsArray().length);
		}
	}
	
	/**
	 * Test TicketTrackerModel.getTicketListByOwnerAsArray()
	 */
	@Test
	public void testGetTicketListByOwnerAsArray() {
		model.createNewTicketList();
		model.loadTicketsFromFile(LOAD);
		try {
			model.getTicketListByOwnerAsArray(null);
			fail();
		} catch (IllegalArgumentException e) {
			assertEquals(5, model.getTicketListAsArray().length);
		}
		
		try {
			Object[][] tl = model.getTicketListByOwnerAsArray("jep");
			assertEquals(4, tl.length);
		} catch (IllegalArgumentException e) {
			fail();
			assertEquals(5, model.getTicketListAsArray().length);
		}
		
		TrackedTicket tt = model.getTicketById(1);
		assertEquals(1, tt.getNotes().size());
		
		model.deleteTicketById(1);
		assertEquals(4, model.getTicketListAsArray().length);
	}
}
