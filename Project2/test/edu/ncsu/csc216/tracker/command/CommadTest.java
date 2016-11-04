/**
 * 
 */
package edu.ncsu.csc216.tracker.command;

import static org.junit.Assert.*;

import org.junit.Test;

import edu.ncsu.csc216.tracker.command.Command.CommandValue;
import edu.ncsu.csc216.tracker.command.Command.Flag;

/**
 * Testing Command class
 * 
 * @author Sayam
 *
 */
public class CommadTest {

	/** owner */
	private static final String OWNER = "youngMoney";
	/** author */
	private static final String AUTHOR = "authorId";

	/** note text */
	private static final String NOTE = "Always be resolving.";

	/**
	 * Testing command command constructor
	 */
	@Test
	public void testCommand() {
		Command c = null;

		// testing for command value being null
		try {
			c = new Command(null, OWNER, Flag.DUPLICATE, AUTHOR, NOTE);
			fail();
		} catch (IllegalArgumentException e) {
			assertNull(c);
		}

		try {
			c = new Command(null, OWNER, Flag.INAPPROPRIATE, AUTHOR, NOTE);
			fail();
		} catch (IllegalArgumentException e) {
			assertNull(c);
		}

		try {
			c = new Command(null, OWNER, Flag.RESOLVED, AUTHOR, NOTE);
			fail();
		} catch (IllegalArgumentException e) {
			assertNull(c);
		}

		// testing for note author being null
		try {
			c = new Command(CommandValue.ACCEPTED, OWNER, Flag.DUPLICATE, null, NOTE);
			fail();
		} catch (IllegalArgumentException e) {
			assertNull(c);
		}

		try {
			c = new Command(CommandValue.FEEDBACK, OWNER, Flag.DUPLICATE, null, NOTE);
			fail();
		} catch (IllegalArgumentException e) {
			assertNull(c);
		}

		try {
			c = new Command(CommandValue.CLOSED, OWNER, Flag.DUPLICATE, null, NOTE);
			fail();
		} catch (IllegalArgumentException e) {
			assertNull(c);
		}

		try {
			c = new Command(CommandValue.PROGRESS, OWNER, Flag.DUPLICATE, null, NOTE);
			fail();
		} catch (IllegalArgumentException e) {
			assertNull(c);
		}

		// testing note text being null

		try {
			c = new Command(CommandValue.PROGRESS, OWNER, Flag.DUPLICATE, AUTHOR, null);
			fail();
		} catch (IllegalArgumentException e) {
			assertNull(c);
		}

		try {
			c = new Command(CommandValue.FEEDBACK, OWNER, Flag.RESOLVED, AUTHOR, null);
			fail();
		} catch (IllegalArgumentException e) {
			assertNull(c);
		}

		try {
			c = new Command(CommandValue.POSSESSION, OWNER, Flag.INAPPROPRIATE, AUTHOR, null);
			fail();
		} catch (IllegalArgumentException e) {
			assertNull(c);
		}

		// possession tests
		try {
			c = new Command(CommandValue.POSSESSION, null, Flag.RESOLVED, AUTHOR, NOTE);
			fail();
		} catch (IllegalArgumentException e) {
			assertNull(c);
		}

		// possession with "" for owner
		try {
			c = new Command(CommandValue.POSSESSION, "", Flag.RESOLVED, AUTHOR, NOTE);
			fail();
		} catch (IllegalArgumentException e) {
			assertNull(c);
		}
		// valid possession
		try {
			c = new Command(CommandValue.POSSESSION, OWNER, null, AUTHOR, NOTE);
			assertEquals(CommandValue.POSSESSION, c.getCommand());
			assertEquals(AUTHOR, c.getNoteAuthor());
			assertEquals(NOTE, c.getNoteText());

			assertEquals(OWNER, c.getOwner());
		} catch (IllegalArgumentException e) {
			fail();
		}

		// invalid flag tests
		Command c1 = null;
		try {
			c1 = new Command(CommandValue.CLOSED, OWNER, null, AUTHOR, NOTE);
			fail();
		} catch (IllegalArgumentException e) {
			assertNull(c1);
		}

		// valid flag with owner ""
		try {
			c1 = new Command(CommandValue.CLOSED, "", Flag.INAPPROPRIATE, AUTHOR, NOTE);
			assertEquals(CommandValue.CLOSED, c1.getCommand());
			assertEquals(AUTHOR, c1.getNoteAuthor());
			assertEquals(NOTE, c1.getNoteText());

			assertEquals(Flag.INAPPROPRIATE, c1.getFlag());
		} catch (IllegalArgumentException e) {
			fail();
		}
		
		//valid flag with owner null
		try {
			c1 = new Command(CommandValue.CLOSED, null, Flag.RESOLVED, AUTHOR, NOTE);
			assertEquals(CommandValue.CLOSED, c1.getCommand());
			assertEquals(AUTHOR, c1.getNoteAuthor());
			assertEquals(NOTE, c1.getNoteText());

			assertEquals(Flag.RESOLVED, c1.getFlag());
		} catch (IllegalArgumentException e) {
			fail();
		}
		
		//tests for VALID Commands 
		try {
			c1 = new Command(CommandValue.ACCEPTED, null, null, AUTHOR, NOTE);
			assertEquals(CommandValue.ACCEPTED, c1.getCommand());
			assertEquals(AUTHOR, c1.getNoteAuthor());
			assertEquals(NOTE, c1.getNoteText());
		} catch (IllegalArgumentException e) {
			fail();
		}
		
		try {
			c1 = new Command(CommandValue.FEEDBACK, null, null, AUTHOR, NOTE);
			assertEquals(CommandValue.FEEDBACK, c1.getCommand());
			assertEquals(AUTHOR, c1.getNoteAuthor());
			assertEquals(NOTE, c1.getNoteText());
		} catch (IllegalArgumentException e) {
			fail();
		}
		
		try {
			c1 = new Command(CommandValue.PROGRESS, null, null, AUTHOR, NOTE);
			assertEquals(CommandValue.PROGRESS, c1.getCommand());
			assertEquals(AUTHOR, c1.getNoteAuthor());
			assertEquals(NOTE, c1.getNoteText());
		} catch (IllegalArgumentException e) {
			fail();
		}
		
	}
	
	

}
