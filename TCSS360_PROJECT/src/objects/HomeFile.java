package objects;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.awt.Desktop;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Class (File object) to create a new file. Each file
 * has an import date, name, and if necessary notes.
 * All of these fields are retrievable via getter methods.
 * 
 * @author Istanbul Idris
 * @version 1.0
 *
 */
public class HomeFile extends File implements Serializable
{ 
	
	/**
	 * Location for list of currently existing users
	 */
	public final static File SAVE_FILE = new File(System.getProperty("user.dir") 
			+ System.getProperty("file.separator") + "client files" + System.getProperty("file.separator") );
	/**
	 * Auto generated serial ID
	 */
	private static final long serialVersionUID = 3891327390707397371L;
	/** Fixture to store the file name. */
	private String myFileName;
	/** Fixture to store the file notes. */
	private String myFileNotes;
	/** Fixture to store the file import date. */
	private final String myImportDate;
	/** Desktop for opening files */
	private static final Desktop DESKTOP = Desktop.getDesktop();
	/**
	 * Constructor for creating a File from a file path
	 * @author Romi Tshiorny
	 * 
	 * @param theFileName The name of the new file.
	 */
	public HomeFile(String path) {
		super(path);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
		LocalDateTime now = LocalDateTime.now();
		myImportDate = formatter.format(now);
		myFileName = super.getName();
		myFileNotes = "";
	}
	
	/**
	 * Constructor for creating a File from a file and name
	 * @author Romi Tshiorny
	 * 
	 * @param parentfile The name of the parent file.
	 * @param name The name of the file
	 */
	public HomeFile(File parentfile, String name) {
		super(parentfile, name);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
		LocalDateTime now = LocalDateTime.now();
		myImportDate = formatter.format(now);
		myFileName = super.getName();
		myFileNotes = "";
	}
	
	/**
	 * Constructor for making a blank file w/ nothing in it
	 */
	public HomeFile(String theName, String theNotes) {
		super(SAVE_FILE.getPath());
		myFileName = theName;
		myFileNotes = theNotes;
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
		LocalDateTime now = LocalDateTime.now();
		myImportDate = formatter.format(now);
	}
	
	

	/**
	 * Method to append notes to the current file notes fixture.
	 * @author Idris Istanbul
	 * 
	 * @param theNote - The note to add.
	 */
	public void addNote(final String theNote)
	{
		myFileNotes = new StringBuilder().append(myFileNotes).append("\n").
										 append(theNote).toString();
	}
	
	/**
	 * Method to clear out all notes for the file.
	 * @author Idris Istanbul
	 */
	public void clearNotes()
	{
		myFileNotes = "";
	}
	
	/**
	 * Getter for file name.
	 * @author Idris Istanbul
	 * 
	 * @return myFileName - The file name fixture.
	 */
	public String getFileName()
	{
		return getName();
	}
	
	/**
	 * Getter for file notes.
	 * @author Idris Istanbul
	 * 
	 * @return myFileNotes - The file notes fixture.
	 */
	public String getFileNotes()
	{
		return myFileNotes;
	}
	
	/**
	 * Getter for file import date.
	 * @author Idris Istanbul
	 * 
	 * @return myImportDate - The file import date.
	 */
	public String getImportDate()
	{
		return myImportDate;
	}
	
	/**
	 * This will replace the file name with a new one.
	 * @author Idris Istanbul
	 * 
	 * @param theNewFileName - The new file name.
	 */
	public void setNewFileName(final String theNewFileName)
	{
		myFileName = theNewFileName;
	}
	
	/**
	 * Method to get name of file
	 * @return file name
	 */
	public String getName() {
		return toString();
	}

	
	/**
	 * Method to return the extension of a file
	 * @return the file exension
	 */
	public String getExtension() {
		String extension = "";
		char[] filename = getName().toCharArray();
		for(int i = filename.length-1; i >= 0; i--) {
			System.out.println(filename[i]);
			extension = filename[i] + extension;
			if(filename[i] == '.') {
				break;
			}
		}
		return extension;
	}
	
	/**
	 * String representation of HomeFile
	 * @author Idris Istanbul
	 */
	@Override
	public String toString() {
		return myFileName;
	}
	
	@Override
	public boolean equals(Object theObject) {
		HomeFile h = (HomeFile) theObject;
		if(theObject == null) {
			return false;
		}
		return h.getFileName() == this.getFileName() &&
				h.getFileNotes() == this.getFileNotes() &&
				h.getImportDate() == this.getImportDate();
	}
	
	/**
	 * Method for seeing if u can open the file
	 * @author Romi Tshiorny
	 * @return true if the file can be opened
	 */
	public boolean canOpen() {
		return super.exists();
	}
	
	/**
	 * Opens the file in the users desktop
	 * @throws IOException
	 */
	public void open() throws IOException {
		DESKTOP.open(super.getAbsoluteFile());
	}
	
	/**
	 * Method for updating notes
	 * @author Collin Nguyen
	 */
	public void updateNote(String theNote) {
		myFileNotes = theNote;
	}
	
	/**
	 * Method for renaming a file
	 * @author Romi Tshiorny
	 * @param theName new name
	 */
	public void rename(String theName) {
		myFileName = theName;
	}
	
	
}