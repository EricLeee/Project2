package edu.ncsu.csc216.tracker.ticket;

/**
 * Set up a note object
 * @author jialangLi
 *
 */
public class Note {

	/** author of the note */
	private String noteAuthor;
	
	/** note text */
	private String noteText;
	
	/**
	 * Note Constructor
	 * @param author author of the note
	 * @param text note text
	 */
	public Note(String author, String text) {
		this.setNoteAuthor(author);
		this.setNoteText(text);
	}
	
	/**
	 * returns the note author
	 * @return the note author
	 */
	public String getNoteAuthor() {
		return this.noteAuthor;
	}
	
	/**
	 * set the author of the note
	 * @param author author of the note
	 */
	private void setNoteAuthor(String author) {
		if (author == null || author.length() < 1) 
			throw new IllegalArgumentException("Invalid note author id."); 
		this.noteAuthor = author;
	}
	
	/**
	 * returns the note text
	 * @return the note text
	 */
	public String getNoteText() {
		return this.noteText;
	}
	
	/**
	 * set note text
	 * @param text note text
	 */
	private void setNoteText(String text) {
		if (text == null || text.length() < 1) 
			throw new IllegalArgumentException("Invalid note text."); 
		this.noteText = text;
	}
	
	/**
	 * returns an array of the note message
	 * @return an array of the note message
	 */
	public String[] getNoteArray() {
		String[] note = new String[2];
		note[0] = this.noteAuthor;
		note[1] = this.noteText;
		return note;
	}
}
