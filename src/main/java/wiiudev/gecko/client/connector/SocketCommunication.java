package wiiudev.gecko.client.connector;

public abstract class SocketCommunication
{
	public SocketCommunication()
	{
		if (Connector.getInstance().getClientSocket() == null)
		{
			throw new IllegalStateException("Not connected");
		}
	}

	public void assertValidAddress(int address)
	{
		/*if (!MemoryRange.isValid(address))
		{
			// throw new IllegalArgumentException(new Hexadecimal(address) + " is not between " + new Hexadecimal(MemoryRange.STARTING_ADDRESS) + " and " + new Hexadecimal(MemoryRange.getLastAllowedAddress()) + "!");
		}*/
	}
}