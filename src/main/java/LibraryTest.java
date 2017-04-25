import static org.junit.Assert.*;
import org.junit.Test;

public class LibraryTest extends TestDBSetup
{	
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
