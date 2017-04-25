
public class User
{
	private String _name;
	private boolean _blocked;
	@SuppressWarnings("unused")
	private int _blockedTime;
	
	public User(String name)
	{
		_name = name;
		_blocked = false;
		_blockedTime = 0;
	}
	
	public String getName()
	{
		return _name;
	}
	
	public void block(int time)
	{
		_blocked = true;
		_blockedTime = time;
	}
	
	public boolean isBlocked()
	{
		return _blocked;
	}
}
