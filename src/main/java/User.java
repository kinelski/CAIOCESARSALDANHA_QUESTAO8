
public class User
{
	private String _name;
	private boolean _blocked;
	@SuppressWarnings("unused")
	private int _unblockDate;
	
	private BookDB _bookDB;
	
	public User(String name, BookDB bookDB)
	{
		_name = name;
		_blocked = false;
		_unblockDate = 0;
		_bookDB = bookDB;
	}
	
	public String getName()
	{
		return _name;
	}
	
	public void block(int date)
	{
		_blocked = true;
		_unblockDate = date;
	}
	
	public boolean isBlocked()
	{
		return _blocked;
	}
	
	public boolean borrow(int bookId)
	{
		if (_blocked)
		{
			return false;
		}
		return true;
	}
	
	public void returnBook(int bookId)
	{
	}
	
	public String bookStatus(int bookId)
	{
		Book book = _bookDB.getBook(bookId);
		if (book.isAvailable())
		{
			return "disponivel";
		}
		if (book.isLost())
		{
			return "extraviado";
		}
		return "retirado";
	}
}
