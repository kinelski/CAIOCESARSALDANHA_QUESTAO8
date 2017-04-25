
public interface BookDB
{
	public int insertBook(Book book);
	public void removeBook(int id);
	public void persistBook(Book book);
	public Book getBook(int id);
}
