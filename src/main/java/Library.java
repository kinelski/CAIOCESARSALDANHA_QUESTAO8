
public class Library
{
	private UserDB _userDB;
	private BookDB _bookDB;
	
	public Library(UserDB userDB, BookDB bookDB)
	{
		_userDB = userDB;
		_bookDB = bookDB;
	}
	
	public int createBook(String title, String author)
	{
		return _bookDB.insertBook(new Book(title, author));
	}
	
	public void removeBook(int id)
	{
		_bookDB.removeBook(id);
	}
	
	public boolean createUser(String name)
	{
		if (_userDB.getUser(name) != null)
		{
			return false;
		}
		_userDB.insertUser(new User(name, _bookDB));
		return true;
	}
	
	public void removeUser(String name)
	{
		_userDB.removeUser(name);
	}
	
	public void blockUser(String name, int time)
	{
		User user = _userDB.getUser(name);
		user.block(time);
		_userDB.persistUser(user);
	}
	
	public boolean registerBorrow(String userName, int bookId, int returnDate)
	{
		User user = _userDB.getUser(userName);
		Book book = _bookDB.getBook(bookId);
		
		if (user.isBlocked() || !book.isAvailable())
		{
			return false;
		}
		
		user.borrow(bookId);
		book.borrow(userName, returnDate);
		
		_userDB.persistUser(user);
		_bookDB.persistBook(book);
		return true;
	}
	
	public void registerReturn(String userName, int bookId, int todayDate)
	{
		User user = _userDB.getUser(userName);
		Book book = _bookDB.getBook(bookId);
		
		int returnDate = book.getReturnDate();
		user.returnBook(bookId);
		book.returnBook();
		
		if (todayDate > returnDate)
		{
			// Bloquear durante (todayDate - returnDate) dias
			user.block(todayDate + (todayDate - returnDate));
		}
		
		_userDB.persistUser(user);
		_bookDB.persistBook(book);
	}
	
	public void setLostBook(int id)
	{
		Book book = _bookDB.getBook(id);
		book.setLost();
		_bookDB.persistBook(book);
	}
}
