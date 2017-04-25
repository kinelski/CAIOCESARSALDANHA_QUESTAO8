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
	
	@Test
	public void MethodListBooksListsAllBooksThatWereNotReturned()
	{
		int today = 10;
		
		int id1 = library.createBook("A Hora da Estrela", "Lispector");
		int id2 = library.createBook("Dom Casmurro", "Machado");
		int id3 = library.createBook("O Guarani", "Alencar");
		
		library.createUser("Leitor");
		User user = userDB.getUser("Leitor");
		
		assertEquals("", user.listBooks(today));
		
		library.registerBorrow("Leitor", id1, 13);
		library.registerBorrow("Leitor", id2, 15);
		library.registerBorrow("Leitor", id3, 7);
		
		String expected = "A Hora da Estrela, Lispector (NO PRAZO)\n"
						+ "Dom Casmurro, Machado (NO PRAZO)\n"
						+ "O Guarani, Alencar (PRAZO VENCIDO)\n";
		
		assertEquals(expected, user.listBooks(today));
		
		library.registerReturn("Leitor", id2, today);
		
		expected = "A Hora da Estrela, Lispector (NO PRAZO)\n"
				 + "O Guarani, Alencar (PRAZO VENCIDO)\n";
		
		assertEquals(expected, user.listBooks(today));
	}
	
	@Test
	public void MethodStatusReturnsCorrectUserStatus()
	{
		library.createUser("Laty");
		assertEquals(userDB.getUser("Laty").status(), "liberado");
		
		library.blockUser("Laty", 5);
		assertEquals(userDB.getUser("Laty").status(), "bloqueado por atraso");
		
		library.levyBlockUser("Laty");
		assertEquals(userDB.getUser("Laty").status(), "bloqueado por cobranca");
	}
}
