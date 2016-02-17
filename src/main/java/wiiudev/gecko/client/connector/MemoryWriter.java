package wiiudev.gecko.client.connector;

import wiiudev.gecko.client.connector.utilities.DataConversions;
import org.apache.commons.io.FileUtils;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * A class for writing data to the memory
 */
public class MemoryWriter extends SocketCommunication
{
	/**
	 * Sends <code>command</code> to the server followed by the <code>address</code> and <code>value</code> to write
	 *
	 * @param address The address to write to
	 * @param value   The value to write
	 * @param command The {@link ServerCommands} to execute
	 * @throws IOException
	 */
	private void sendWriteCommand(int address, int value, int command) throws IOException
	{
		assertValidAddress(address);

		DataOutputStream dataSender = Connector.getInstance().getDataSender();

		dataSender.writeByte(command);
		dataSender.writeInt(address);
		dataSender.writeInt(value);
	}

	/**
	 * Writes an 8-bit <code>value</code> to the memory starting at <code>address</code>
	 *
	 * @param address The address to write to
	 * @param value   The value to write
	 * @throws IOException
	 */
	public void write(int address, byte value) throws IOException
	{
		sendWriteCommand(address, value, ServerCommands.WRITE_BYTE);
	}

	/**
	 * Writes a 16-bit <code>value</code> to the memory starting at <code>address</code>
	 *
	 * @param address The address to write to
	 * @param value   The value to write
	 * @throws IOException
	 */
	public void writeShort(int address, short value) throws IOException
	{
		sendWriteCommand(address, value, ServerCommands.WRITE_SHORT);
	}

	/**
	 * Writes a 32-bit <code>value</code> to the memory starting at <code>address</code>
	 *
	 * @param address The address to write to
	 * @param value   The value to write
	 * @throws IOException
	 */
	public void writeInt(int address, int value) throws IOException
	{
		sendWriteCommand(address, value, ServerCommands.WRITE_INT);
	}

	/**
	 * Writes a 32-bit <code>value</code> to the memory starting at <code>address</code>
	 *
	 * @param address The address to write to
	 * @param value   The value to write
	 * @throws IOException
	 */
	public void writeBoolean(int address, boolean value) throws IOException
	{
		write(address, (byte) (value ? 1 : 0));
	}

	/**
	 * Writes a 32-bit <code>value</code> to the memory starting at <code>address</code>
	 *
	 * @param address The address to write to
	 * @param value   The value to write
	 * @throws IOException
	 */
	public void writeFloat(int address, float value) throws IOException
	{
		writeInt(address, DataConversions.toInteger(value));
	}

	/**
	 * Writes a null-terminated String <code>value</code> to the memory starting at <code>address</code>
	 *
	 * @param address The address to write to
	 * @param value   The value to write
	 * @throws IOException
	 */
	public void writeString(int address, String value) throws IOException
	{
		byte[] valueBytes = value.getBytes();
		byte[] valueBytesNullTerminated = new byte[valueBytes.length + 1];

		System.arraycopy(valueBytes, 0, valueBytesNullTerminated, 0, valueBytes.length);
		valueBytesNullTerminated[valueBytesNullTerminated.length - 1] = 0;

		writeBytes(address, valueBytesNullTerminated);
	}

	/**
	 * Writes a null-terminated String <code>value</code> to the memory starting at <code>address</code> with a maximum length of <code>maximumLength</code>
	 *
	 * @param address       The address to write to
	 * @param value         The value to write
	 * @param maximumLength The value's maximum allowed amount of characters
	 * @throws IOException
	 */
	public void writeString(int address, String value, int maximumLength) throws IOException
	{
		int valueLength = value.length();

		if (valueLength > maximumLength)
		{
			throw new IllegalArgumentException("The text's length is " + valueLength + " but may not exceed " + maximumLength + " characters!");
		}

		writeString(address, value);
	}

	/**
	 * Writes an entire <code>file</code> to the memory starting at <code>address</code>
	 *
	 * @param address The address to write to
	 * @param file    The file to write
	 * @throws IOException
	 */
	public void upload(int address, File file) throws IOException
	{
		byte[] bytes = FileUtils.readFileToByteArray(file);

		writeBytes(address, bytes);
	}

	/**
	 * Writes <code>bytes</code> to the memory starting at <code>address</code>
	 *
	 * @param address The address to write to
	 * @param bytes   The value to write
	 * @throws IOException
	 */
	public void writeBytes(int address, byte[] bytes) throws IOException
	{
		ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
		int bufferPointer = 0;

		while (byteBuffer.hasRemaining())
		{
			// We can read an int
			if (byteBuffer.remaining() > 3)
			{
				int intValue = byteBuffer.getInt();
				writeInt(address + bufferPointer, intValue);
				bufferPointer += 4;

				continue;
			}

			// We can read a short
			if (byteBuffer.remaining() > 1)
			{
				short shortValue = byteBuffer.getShort();
				writeShort(address + bufferPointer, shortValue);
				bufferPointer += 2;

				continue;
			}

			// We only have a byte left
			byte byteValue = byteBuffer.get();
			write(address + bufferPointer, byteValue);
			bufferPointer++;
		}
	}

	/**
	 * Pokes successive addresses with the same value
	 *
	 * @param startingAddress       The address to start poking memory at
	 * @param value                 The value to writeInt
	 * @param additionalWritesCount The amount of additional pokes to perform
	 * @throws IOException
	 */
	public void serialWrite(int startingAddress, int value, int additionalWritesCount) throws IOException
	{
		for (int writesIndex = 0; writesIndex < additionalWritesCount + 1; writesIndex++)
		{
			writeInt(startingAddress + writesIndex * 4, value);
		}
	}
}