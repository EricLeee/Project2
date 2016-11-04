package edu.ncsu.csc216.tracker.command;

/**
 * Concrete class that encapsulates user interactions with the GUI for
 * processing by the ticket tracking FSM in TrackedTicket.
 * 
 * @author Jialang Li
 * @author Sayam Patel
 */
public class Command {

	/** Flag Duplicate constant */
	public static final String F_DUPLICATE = "Duplicate";

	/** Flag Inappropriate constant */
	public static final String F_INAPPROPRIATE = "Inappropriate";

	/** flag resolved */
	public static final String F_RESOLVED = "Resolved";

	/** ticket flag field */
	private Flag flag;

	/** Command enum value */
	private CommandValue c;

	/** owner of ticket */
	private String owner;

	/** note for the ticket */
	private String note;

	/** author of the note */
	private String noteAuthor;

	/**
	 * Constructs a command depending on the CommandValue c. Other parameters,
	 * flag, note author, and noteText are used according to the command value
	 * given, ignored if not needed. Will throw IllegalArgumentException if
	 * needed parameters are invalid/null
	 * 
	 * @param c
	 *            CommandValue, will initiate Command based on this enum value.
	 * @param owner
	 *            Owner's id string
	 * @param flag
	 *            Flag of the ticket
	 * @param noteAuthor
	 *            Author's id string
	 * @param noteText
	 *            note String for updating the ticket with
	 */
	public Command(CommandValue c, String owner, Flag flag, String noteAuthor, String noteText) {
		if (c == null) {
			throw new IllegalArgumentException();
		} else if (isNullOrEmpty(noteAuthor)) {
			throw new IllegalArgumentException("Invalid note author id.");
		} else if (isNullOrEmpty(noteText)) {
			throw new IllegalArgumentException("Invalid note.");
		} else if (c == CommandValue.POSSESSION && isNullOrEmpty(owner)) {
			throw new IllegalArgumentException();
		} else if (c == CommandValue.CLOSED && flag == null) {
			throw new IllegalArgumentException("Invalid flag.");
		}

		// setting three important things
		this.c = c;
		this.noteAuthor = noteAuthor;
		this.note = noteText;

		// for possession and closed
		if (c == CommandValue.POSSESSION) {
			this.owner = owner;
		} else if (c == CommandValue.CLOSED) {
			this.flag = flag;
		}

	}

	/**
	 * Checks if input string is null or empty
	 * 
	 * @param s
	 *            String to check
	 * @return true if s is null or empty
	 */
	private boolean isNullOrEmpty(String s) {
		if (s == null || s.equals("")) {
			return true;
		}
		return false;
	}

	/**
	 * Returns command value
	 * 
	 * @return command value
	 */
	public CommandValue getCommand() {
		return this.c;
	}

	/**
	 * returns owner of this command
	 * 
	 * @return owner of this command
	 */
	public String getOwner() {
		return this.owner;
	}

	/**
	 * Returns flag of ticket
	 * 
	 * @return flag Flag of ticket
	 */
	public Flag getFlag() {
		return this.flag;
	}

	/**
	 * returns note text
	 * 
	 * @return note text
	 */
	public String getNoteText() {
		return this.note;
	}

	/**
	 * returns author of the note
	 * 
	 * @return author of the note
	 */
	public String getNoteAuthor() {
		return this.noteAuthor;
	}

	/**
	 * CommandValue enum. Possesion assigns the ticket to a potential owner.
	 * Accepted when the owner it was assigned to, accepts the ticket. Closed
	 * when in closed state, sets the flag. Feedback in feedback state. Note
	 * author is the submitter and the current owner.
	 * 
	 * @author Jialang Li
	 *
	 */
	public static enum CommandValue {
		/** commands of tracked ticket */
		POSSESSION, ACCEPTED, CLOSED, PROGRESS, FEEDBACK
	}

	/**
	 * Flag enum with three states
	 * 
	 * @author Jialang Li
	 *
	 */
	public static enum Flag {
		/** three flag states for closing/resolving a ticket */
		DUPLICATE, INAPPROPRIATE, RESOLVED
	}

}
