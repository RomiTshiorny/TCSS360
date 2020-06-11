package objects;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashSet;

/**
 * Class (Room object) to create a new file. Each file
 * has an import date, name, and if necessary notes.
 * All of these fields are retrievable via getter methods.
 * 
 * @author Romi Tshiorny
 * @author Istanbul Idris
 * @version 1.0
 *
 */
public class Room implements Serializable{

	
		/**
	 	* Location for list of currently existing users
	 	*/
		public final static File SAVE_FILE = new File("HomeOwnerFiles" + System.getProperty("file.separator")+"House.ser");
		/**
		* Auto Generated ID.
	 	*/
		private static final long serialVersionUID = -4137104956505567156L;

		/** Set to hold the subRooms. */
		private HashSet<Room> mySubRooms;
		
		/** Set to hold the files for current room. */
		private HashSet<HomeFile> myFiles;
		
		/** Current Room name (folder name). */
		private String myRoomName;
		
		/**
		 * Constructor to create the Room object.
		 * 
		 * @author Tshiorny Romi 
		 * @param theRoomName - Name for the room (folder name).
		 */
		public Room(String theRoomName) {
			myRoomName = theRoomName;
			mySubRooms = new HashSet<Room>();
			myFiles = new HashSet<HomeFile>();
		}
			
		
		/**
		 * Method that will add a room to the current room.
		 * 
		 * @author Idris Istanbul
		 * 
		 * @param theRoomName - The name of the room to be added.
		 * @return true if adding room was successful, false otherwise.
		 */
		public boolean addRoom(String theRoomName) {
			
			return mySubRooms.add(new Room(theRoomName));
		}
		
		/**
		 * Method for deleting a room using its name
		 * @author Romi Tshiorny
		 * @param theRoomName room to be deleted
		 */
		public void removeRoom(String theRoomName) {
			mySubRooms.remove(new Room(theRoomName));
		}
		
		/**
		 * Method for deleting a room using another Room
		 * @author Romi Tshiorny
		 * @param theRoomName room to be deleted
		 */
		public void removeRoom(Room theRoom) {
			mySubRooms.remove(theRoom);
		}
		
		/**
		 * Static method for saving a room
		 * @author Romi Tshiorny
		 */
		public static void saveRoom(Room theRoom) {
			try {
				FileOutputStream outFile = new FileOutputStream(SAVE_FILE);
				ObjectOutputStream writer = new ObjectOutputStream(outFile);
				writer.writeObject(theRoom);
				writer.close();
				outFile.close();
			}
			catch(IOException e){
				e.printStackTrace();
			}
		}
		
		/**
		 * Static method for loading the Room
		 * @author Romi Tshiorny
		 * @param roomName name for room loading in
		 * @return a Room object of the loaded in room
		 */
		public static Room loadRoom(String roomName) {
			try {
				
				if(SAVE_FILE.createNewFile()) {
					System.out.println(SAVE_FILE.toString() +" Creation Successful");
					Room firstRoom = new Room("Your First Room");
					saveRoom(firstRoom);
				}
				 
				FileInputStream inFile = new FileInputStream(SAVE_FILE);
				ObjectInputStream reader = new ObjectInputStream(inFile);	
				
				Room myRoom = (Room) reader.readObject();
				reader.close();
				inFile.close();
				return myRoom;
				
			}
			catch(IOException e) {
				e.printStackTrace();
				return null;
			}
			catch(ClassNotFoundException c) {
				c.printStackTrace();
				return null;
			}
		}
		
		/**
		 * Method will search thru the subRooms Set to find 
		 * a specific room.
		 * 
		 * @author Idris Istanbul
		 * 
		 * @param theRoomName - Name of the room to search for.
		 * @return r - Room that was specified to be searched.
		 */
		public Room findRoom(String theRoomName) {
			for(Room r : mySubRooms) {
				if(r.getRoomName().equals(theRoomName)) {
					return r;
				}
			}
			return null;
		}
		
		public HomeFile findFile(HomeFile file) {
			for(Room r : mySubRooms) {
				for(HomeFile f : r.getFiles()) {
					if(file.equals(f)) {
						return f;
					}
				}
			}
			return null;
			
		}
		
		/**
		 * Accessor method for the subRooms set.
		 * 
		 * @author Romi Tshiorny
		 * @return - SubRoom set.
		 */
		public HashSet<Room> getSubRooms() {
			return mySubRooms;
		}
		
		/**
		 * Accessor method for the files for current room.
		 * 
		 * @author Romi Tshiorny
		 * @return - Files for current room.
		 */
		public HashSet<HomeFile> getFiles() {
			return myFiles;
		}
		
		/**
		 * Accessor method for the room name.
		 * 
		 * @author Romi Tshiorny
		 * 
		 * @return - Current room name.
		 */
		public String getRoomName() {
			return myRoomName;
		}
		
		/**
		 * Method to find a file with a certain string in it.
		 * @author Romi Tshiorny
		 */
		public HashSet<HomeFile> filterFiles(String query) {
			return new SearchEngine(mySubRooms).searchMe(query);
		}

		/**
		 * Method to add files to current room.
		 * 
		 * @author Romi Tshiorny
		 * 
		 * @param theFile - The file to be added.
		 */
		public void addFile(HomeFile theFile) {
			myFiles.add(theFile);
		}
		
		/**
		 * Removes a file from the room.
		 * 
		 * @author Collin Nguyen
		 * @param theFile - The file to be removed.
		 */
		public void removeFile(HomeFile theFile) {
			myFiles.remove((HomeFile) theFile);
		}
		
		/**
		 * Return string
		 * 
		 * @author Collin Nguyen
		 */
		@Override
		public String toString() {
			return myRoomName;
		}
		
		/**
		 * equals method for room
		 * @author Romi Tshiorny
		 * @param other Room
		 * @return True if they have the same name (Since its a set no need to check for other equality)
		 */
		public boolean equals(Room other) {
			
			return myRoomName.equals(other.getRoomName());
		}
		
		/**
		 * Method for printing the structure of a room, mainly for dev use.
		 * @author Romi Tshiorny
		 */
		public void printRoom() {
			System.out.println(myRoomName +":");
			for(Room r : mySubRooms) {
				System.out.println(myRoomName +"->" + r.getRoomName() + ":");
				r.printRoom();
			}
			for(HomeFile f: myFiles) {
				System.out.println(myRoomName +"->" + f.getName());
			}
		}
		
		/**
		 * Method for renaming a room
		 * @author Romi Tshiorny
		 * @param theName new name
		 */
		public void rename(String theName) {
			myRoomName = theName;
		}
		
}
