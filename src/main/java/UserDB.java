
public interface UserDB
{
	public void insertUser(User user);
	public void removeUser(String name);
	public void persistUser(User user);
	public User getUser(String name);
}
