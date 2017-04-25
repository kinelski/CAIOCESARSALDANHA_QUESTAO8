import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.List;

public class LibraryTest
{
	private UserDB userDB;
	private BookDB bookDB;
	private Library library;

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
	
	@Test
	public void MethodCreateUserSuccessfullyInsertUserInDataBase()
	{
		library.createUser("Foo");
		assertTrue(userDB.getUser("Foo") != null);
	}
	
	@Test
	public void MethodCreateUserAlwaysSucceedsWhenInsertingDifferentUsers()
	{	
		assertTrue(library.createUser("Hello"));
		assertTrue(library.createUser("Darkness"));
	}
	
	@Test
	public void MethodCreateUserFailsWhenUserNameIsAlreadyBeingUsed()
	{
		assertTrue(library.createUser("VouRepetir"));
		assertFalse(library.createUser("VouRepetir"));
	}
	
	@Test
	public void MethodRemoveUserSuccessfullyRemovesUserFromDataBase()
	{
		library.createUser("DiscoVoador");
		library.removeUser("DiscoVoador");
		assertTrue(userDB.getUser("DiscoVoador") == null);
	}
	
	@Test
	public void MethodBlockUserSuccessfullyBlocksUserInDataBase()
	{
		library.createUser("Blocky");
		library.blockUser("Blocky", 5);
		assertTrue(userDB.getUser("Blocky").isBlocked());
	}
	
	@Test
	public void MethodCreateBookSuccessfullyInsertBookInDataBase()
	{
		int id = library.createBook("CES33", "ITA");
		assertTrue(bookDB.getBook(id) != null);
	}
	
	@Test
	public void MethodRemoveBookSuccessfullyRemovesBookFromDataBase()
	{
		int id = library.createBook("Bola Quadrada", "Quico");
		library.removeBook(id);
		assertTrue(bookDB.getBook(id) == null);
	}
	
	@Test
	public void UnblockedUsersCanBorrowAvailableBooks()
	{
		library.createUser("Covarde");
		int id = library.createBook("Cachorro", "Covarde");
		assertTrue(library.registerBorrow("Covarde", id, 0));
	}
	
	@Test
	public void BlockedUsersCantBorrowAvailableBooks()
	{
		library.createUser("Covarde");
		int id = library.createBook("Cachorro", "Covarde");
		library.blockUser("Covarde", 4);
		assertFalse(library.registerBorrow("Covarde", id, 0));
	}
	
	@Test
	public void UnblockedUsersCantBorrowUnavailableBooks()
	{
		library.createUser("Covarde");
		library.createUser("Muriel");
		int id = library.createBook("Cachorro", "Covarde");
		library.registerBorrow("Covarde", id, 0);
		assertFalse(library.registerBorrow("Muriel", id, 0));
	}
	
	@Test
	public void UserIsNotBlockedIfBookIsReturnedBeforeDeadline()
	{
		library.createUser("Covarde");
		int id = library.createBook("Cachorro", "Covarde");
		library.registerBorrow("Covarde", id, 5);
		library.registerReturn("Covarde", id, 4);
		assertFalse(userDB.getUser("Covarde").isBlocked());
	}
	
	@Test
	public void UserIsNotBlockedIfBookIsReturnedOnDeadline()
	{
		library.createUser("Covarde");
		int id = library.createBook("Cachorro", "Covarde");
		library.registerBorrow("Covarde", id, 5);
		library.registerReturn("Covarde", id, 5);
		assertFalse(userDB.getUser("Covarde").isBlocked());
	}
	
	@Test
	public void UserIsBlockedIfBookIsReturnedAfterDeadline()
	{
		library.createUser("Covarde");
		int id = library.createBook("Cachorro", "Covarde");
		library.registerBorrow("Covarde", id, 5);
		library.registerReturn("Covarde", id, 6);
		assertTrue(userDB.getUser("Covarde").isBlocked());
	}
}
