
public class Book {
	@SuppressWarnings("unused")
	private String _title;
	@SuppressWarnings("unused")
	private String _author;
	
	private boolean _available;
	@SuppressWarnings("unused")
	private boolean _lost;
	private int _id;
	@SuppressWarnings("unused")
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
	
	public int getId()
	{
		return _id;
	}
	
	public int getReturnDate()
	{
		return _returnDate;
	}
}
