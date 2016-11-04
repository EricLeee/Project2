package edu.ncsu.csc216.tracker.ticket;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Tests the note class
 * 
 * @author Sayam Patel
 *
 */
public class NoteTest {

	/** test author id */
	private static final String AUTHOR = "jli29";
	
	/** test note text */
	private static final String NOTE = "note";
	/**
	 * Testing the new note test
	 */
	@Test
	public void testNote() {
		Note n = null;
		
		//invalid author
		try {
			n = new Note(null, NOTE);
		} catch (IllegalArgumentException e) {
			assertEquals("Invalid note author id.", e.getMessage());
		}
		
		try {
			n = new Note("", NOTE);
		} catch (IllegalArgumentException e) {
			assertEquals("Invalid note author id.", e.getMessage());
		}
		
		//invalid note text
		try {
			n = new Note(AUTHOR, "");
		} catch (IllegalArgumentException e) {
			assertEquals("Invalid note text.", e.getMessage());
		}
		try {
			n = new Note(AUTHOR, null);
		} catch (IllegalArgumentException e) {
			assertEquals("Invalid note text.", e.getMessage());
		}
		
		n = new Note(AUTHOR, NOTE);
		assertEquals(AUTHOR, n.getNoteAuthor());
		assertEquals(NOTE, n.getNoteText());
		
		String[] nArr = n.getNoteArray();
		assertEquals(2, nArr.length);
		assertEquals(AUTHOR, nArr[0]);
		assertEquals(NOTE, nArr[1]);
	}

}
