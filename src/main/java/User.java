
public class User
{
	private String _name;
	private boolean _blocked;
	@SuppressWarnings("unused")
	private int _unblockDate;
	
	public User(String name)
	{
		_name = name;
		_blocked = false;
		_unblockDate = 0;
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
}
