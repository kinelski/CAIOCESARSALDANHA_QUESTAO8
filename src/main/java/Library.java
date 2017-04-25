
public class Library
{
	private UserDB _userDB;
	
	public Library(UserDB userDB)
	{
		_userDB = userDB;
	}
	
	public boolean createUser(String name)
	{
		if (_userDB.getUser(name) != null)
		{
			return false;
		}
		_userDB.insertUser(new User(name));
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
}
