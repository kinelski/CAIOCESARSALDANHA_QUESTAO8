import static org.junit.Assert.*;
import org.junit.Test;

public class UserTest extends TestDBSetup
{
	@Test
	public void MethodBookStatusReturnTheCorrectStatus()
	{
		library.createUser("Mirizas");
		int id = library.createBook("TDD", "Suga");
		User user = userDB.getUser("Mirizas");
		assertTrue(user.bookStatus(id) == "disponivel");
		
		library.registerBorrow("Mirizas", id, 0);
		assertTrue(user.bookStatus(id) == "retirado");
		
		library.setLostBook(id);
		assertTrue(user.bookStatus(id) == "extraviado");
	}
}
