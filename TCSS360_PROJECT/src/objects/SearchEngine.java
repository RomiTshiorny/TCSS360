package objects;

import java.util.HashSet;
import java.io.Serializable;

/**
 * A class to perform simple searches of Room objects, including their subrooms, for relevant File objects.  
 * 
 * @author Evan McAllister
 * @version 1.0
 */
public class SearchEngine implements Serializable
{
	private static final long serialVersionUID = 5410866876698048600L;
	HashSet<Room> mySet;
	int dataSize;
	
	/*
	 * The class constructor.
	 * 
	 *  @param theSet A set of room objects for this SearchEngine to search. 
	 */
	public SearchEngine(HashSet<Room> theSet)
	{
		mySet = theSet;
	}
	
	/*
	 *  Method to update the set this SearchEngine uses for searching.   
	 * 
	 *  @param newSet A new set of room objects to search.  
	 */
	public void changeSet(HashSet<Room> newSet)
	{
		mySet = newSet;
	}
	
	/*
	 *  Method to get the set this SearchEngine uses for searching.   
	 * 
	 *  @return A HashSet of type <Room>, used by this object for searching.   
	 */
	public HashSet<Room> getSet()
	{
		return mySet;
	}
	
	/*
	 *  Method to get a shallow copy of the set this SearchEngine uses for searching.   
	 * 
	 *  @return A HashSet of type <Room>, a copy of the one used by this object for searching.   
	 */
	public HashSet<Room> getSetCopy()
	{
		return new HashSet<Room>(mySet);
	}
	
	/*
	 *  Method to get the size of the data set this SearchEngine currently operates on.  
	 * 
	 *  @return Number of rooms searched by this SearchEngine.    
	 */
	public int getDataSize()
	{
		return mySet.size();
	}
	
	/*
	 * Method to get list of HomeFile objects related to String search. 
	 * 
	 * Searches currently ignore case and look for containment. 
	 * 
	 * @param query String object used to search this SearchEngine's set for matches. 
	 * @return ArrayList of matching HomeFile objects. 
	 */
	public HashSet<HomeFile> searchMe(String query)
	{
		HashSet<HomeFile> matches = new HashSet<HomeFile>();
		matches.clear();
		
		String reg = query; //Could just leave as query, but this allows for easier editing later. 
		
		//Iterate through all rooms in list. 
		for (Room r: mySet)
		{
			//Iterate through all HomeFiles in current Room.
			for (HomeFile hf: r.getFiles())
			{
				if (hf.getFileName().toLowerCase().contains(reg.toLowerCase()))
				{
					matches.add(hf);
				}
			}
			
			//Iterate through all sub-Rooms in current Room. 
			for (Room sub: r.getSubRooms())
			{
				//Iterate through all HomeFiles in current sub-Room.
				for (HomeFile shf: sub.getFiles())
				{
					if (shf.getFileName().toLowerCase().contains(reg.toLowerCase()))
					{
						matches.add(shf);
					}
				}
			}
		}
		
		return matches;
	}
	
	//TODO: Allow search of notes for keyword too.
}
