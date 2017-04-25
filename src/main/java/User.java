import java.util.ArrayList;
import java.util.List;

public class User
{
	private String _name;
	private boolean _blocked;
	private boolean _levyBlocked;
	private int _unblockDate;
	
	private BookDB _bookDB;
	private List<Book> _books;
	
	public User(String name, BookDB bookDB)
	{
		_name = name;
		_blocked = false;
		_levyBlocked = false;
		_unblockDate = 0;
		_bookDB = bookDB;
		_books = new ArrayList<Book>();
	}
	
	public String getName()
	{
		return _name;
	}
	
	public void block(int date)
	{
		if (_blocked && date > _unblockDate)
		{
			_unblockDate = date;
		}
		_blocked = true;
	}
	
	public void levyBlock()
	{
		_blocked = true;
		_levyBlocked = true;
	}
	
	public boolean isBlocked()
	{
		return _blocked;
	}
	
	public boolean isLevyBlocked()
	{
		return _levyBlocked;
	}
	
	public boolean borrow(int bookId)
	{
		if (_blocked)
		{
			return false;
		}
		
		Book book = _bookDB.getBook(bookId);
		_books.add(book);
		return true;
	}
	
	public void returnBook(int bookId)
	{
		Book desiredBook = _bookDB.getBook(bookId);
		int id = desiredBook.getId();
		
		for (Book book : _books)
		{
			if (id == book.getId())
			{
				_books.remove(book);
				return;
			}
		}
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
	
	public String listBooks(int todayDate)
	{
		String bookList = "";
		
		for (Book book : _books)
		{
			bookList += book.getTitle() + ", " + book.getAuthor();
			if (book.getReturnDate() < todayDate)
			{
				bookList += " (PRAZO VENCIDO)\n";
			}
			else
			{
				bookList += " (NO PRAZO)\n";
			}
		}
		
		return bookList;
	}
	
	public String status()
	{
		if (!_blocked)
		{
			return "liberado";
		}
		if (_levyBlocked)
		{
			return "bloqueado por cobranca";
		}
		return "bloqueado por atraso";
	}
}
