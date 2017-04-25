
public class Book {
	private String _title;
	private String _author;
	
	private boolean _available;
	private boolean _lost;
	private int _id;
	private String _holder;
	private int _returnDate;
	
	private static int idCounter = 0;
	
	public Book(String title, String author)
	{
		_title = title;
		_author = author;
		_available = true;
		_lost = false;
		_id = ++idCounter;
	}
	
	public boolean isAvailable()
	{
		return _available;
	}
	
	public boolean isLost()
	{
		return _lost;
	}
	
	public boolean borrow(String userName, int returnDate)
	{
		if (!_available)
		{
			return false;
		}
		_holder = userName;
		_available = false;
		_returnDate = returnDate;
		return true;
	}
	
	public void returnBook()
	{
		_available = true;
		_lost = false;
		_holder = null;
	}
	
	public String getTitle()
	{
		return _title;
	}
	
	public String getAuthor()
	{
		return _author;
	}
	
	public int getId()
	{
		return _id;
	}
	
	public String getHolder()
	{
		return _holder;
	}
	
	public int getReturnDate()
	{
		return _returnDate;
	}
	
	public void setLost()
	{
		_available = false;
		_lost = true;
	}
}
