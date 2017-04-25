import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class TestDBSetup {
	protected UserDB userDB;
	protected BookDB bookDB;
	protected Library library;

	@Before
	public void setup()
	{
		userDB = mock(UserDB.class);
		bookDB = mock(BookDB.class);
		library = new Library(userDB, bookDB);
		
		// Simulam os bancos de dados
		final List<User> userList = new ArrayList<User>();
		final List<Book> bookList = new ArrayList<Book>();
		
		// Seta comportamento de insertUser
		doAnswer(new Answer<Void>()
		{
			public Void answer(InvocationOnMock invocation)
			{
				User user = invocation.getArgumentAt(0, User.class);
				userList.add(user);
				return null;
			}
		}).when(userDB).insertUser(any(User.class));
		
		// Seta comportamento de getUser
		doAnswer(new Answer<User>()
		{
			public User answer(InvocationOnMock invocation)
			{
				String name = invocation.getArgumentAt(0, String.class);
				for (User user : userList)
				{
					if (name == user.getName())
					{
						return user;
					}
				}
				return null;
			}
		}).when(userDB).getUser(any(String.class));
		
		// Seta comportamento de removeUser
		doAnswer(new Answer<Void>()
		{
			public Void answer(InvocationOnMock invocation)
			{
				String name = invocation.getArgumentAt(0, String.class);
				for (User user : userList)
				{
					if (name == user.getName())
					{
						userList.remove(user);
						return null;
					}
				}
				return null;
			}
		}).when(userDB).removeUser(any(String.class));
		
		// Seta comportamento de persistUser
		doAnswer(new Answer<Void>()
		{
			public Void answer(InvocationOnMock invocation)
			{
				User updateUser = invocation.getArgumentAt(0, User.class);
				String name = updateUser.getName();
				for (User user : userList)
				{
					if (name == user.getName())
					{
						userList.remove(user);
						userList.add(updateUser);
						return null;
					}
				}
				return null;
			}
		}).when(userDB).persistUser(any(User.class));
		
		// Seta comportamento de insertBook
		doAnswer(new Answer<Integer>()
		{
			public Integer answer(InvocationOnMock invocation)
			{
				Book book = invocation.getArgumentAt(0, Book.class);
				bookList.add(book);
				return book.getId();
			}
		}).when(bookDB).insertBook(any(Book.class));
		
		// Seta comportamento de getBook
		doAnswer(new Answer<Book>()
		{
			public Book answer(InvocationOnMock invocation)
			{
				int id = invocation.getArgumentAt(0, Integer.class);
				for (Book book : bookList)
				{
					if (id == book.getId())
					{
						return book;
					}
				}
				return null;
			}
		}).when(bookDB).getBook(any(Integer.class));
		
		// Seta comportamento de removeBook
		doAnswer(new Answer<Void>()
		{
			public Void answer(InvocationOnMock invocation)
			{
				int id = invocation.getArgumentAt(0, Integer.class);
				for (Book book : bookList)
				{
					if (id == book.getId())
					{
						bookList.remove(book);
						return null;
					}
				}
				return null;
			}
		}).when(bookDB).removeBook(any(Integer.class));
		
		// Seta comportamento de persistBook
		doAnswer(new Answer<Void>()
		{
			public Void answer(InvocationOnMock invocation)
			{
				Book updateBook = invocation.getArgumentAt(0, Book.class);
				int id = updateBook.getId();
				for (Book book : bookList)
				{
					if (id == book.getId())
					{
						bookList.remove(book);
						bookList.add(updateBook);
						return null;
					}
				}
				return null;
			}
		}).when(bookDB).persistBook(any(Book.class));
	}
}
