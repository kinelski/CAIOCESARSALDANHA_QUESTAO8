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
	private Library library;

	@Before
	public void setup()
	{
		userDB = mock(UserDB.class);
		library = new Library(userDB);
		
		// Simula o banco de dados
		final List<User> userList = new ArrayList<User>();
		
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
		doAnswer(new Answer<User>()
		{
			public User answer(InvocationOnMock invocation)
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
}
