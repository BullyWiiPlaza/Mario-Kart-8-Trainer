package wiiudev.gecko.client.connector;

/**
 * Commands for communicating with the code handler server
 */
public class ServerCommands
{
	public static final byte WRITE_BYTE = 1;
	public static final byte WRITE_SHORT = 2;
	public static final byte WRITE_INT = 3;
	public static final byte READ_BYTES = 4;
	public static final byte SEARCH_INT = 0x72;
}