package wiiudev.gecko.client.connector;

import wiiudev.gecko.client.connector.utilities.DataConversions;
import wiiudev.gecko.client.connector.utilities.MemoryPointer;
import wiiudev.gecko.client.connector.utilities.MemoryRange;
import org.apache.commons.io.FileUtils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * A class for reading data from the memory based on the {@link Connector} class
 */
public class MemoryReader extends SocketCommunication
{
	/**
	 * Reads an 8-bit value from the memory at the given <code>address</code>
	 *
	 * @param address The address to read from
	 * @return The read byte
	 */
	public byte read(int address) throws IOException
	{
		byte[] readBytes = readBytes(address, 1);

		return readBytes[0];
	}

	/**
	 * Reads a 16-bit short value from the memory at the given <code>address</code>
	 *
	 * @param address The address to read from
	 * @return The read short
	 */
	public short readShort(int address) throws IOException
	{
		byte[] readBytes = readBytes(address, 2);

		return ByteBuffer.wrap(readBytes).getShort();
	}

	/**
	 * Reads a 32-bit integer value from the memory at the given <code>address</code>
	 *
	 * @param address The address to read from
	 * @return The read value
	 */
	public int readInt(int address) throws IOException
	{
		byte[] readBytes = readBytes(address, 4);

		return ByteBuffer.wrap(readBytes).getInt();
	}

	/**
	 * Reads a 32-bit floating point value from the memory at the given <code>address</code>
	 *
	 * @param address The address to read from
	 * @return The read float
	 */
	public float readFloat(int address) throws IOException
	{
		int readInt = readInt(address);

		return DataConversions.toFloat((long) readInt);
	}

	/**
	 * Reads a null-terminated String from the memory at the given <code>address</code>
	 *
	 * @param address The address the String starts at
	 * @return The retrieved String
	 */
	public String readString(int address) throws IOException
	{
		byte[] bytesRead;
		StringBuilder stringBuilder = new StringBuilder();
		int byteBufferSize = 100; // The amount of bytes to read and inspect at once

		while (true)
		{
			bytesRead = readBytes(address, byteBufferSize);

			for (byte byteRead : bytesRead)
			{
				if (byteRead == 0)
				{
					// The String has ended
					return stringBuilder.toString();
				}

				char letter = DataConversions.toCharacter(byteRead);
				stringBuilder.append(letter);
			}

			address += byteBufferSize;
		}
	}

	/**
	 * Reads a boolean value from the memory at the given <code>address</code>
	 *
	 * @param address The address to start reading from
	 * @return The read boolean
	 */
	public boolean readBoolean(int address) throws IOException
	{
		byte readByte = read(address);

		return readByte == 1;
	}

	/**
	 * Dumps a block of memory with length <code>bytes</code> to a local <code>file</code> starting at <code>address</code>
	 *
	 * @param address The address to start dumping memory at
	 * @param bytes   The amount of bytes to dump
	 * @param file    The file to store the data to
	 */
	public void dump(int address, int bytes, File file) throws IOException
	{
		List<byte[]> readByteChunks = new ArrayList<>();
		int maximumChunkSize = 0x400;

		// Read in chunks
		while (bytes > maximumChunkSize)
		{
			byte[] readBytes = readBytes(address, maximumChunkSize);
			readByteChunks.add(readBytes);

			bytes -= maximumChunkSize;
			address += maximumChunkSize;
		}

		// Read the rest
		readByteChunks.add(readBytes(address, bytes));

		FileUtils.deleteQuietly(file);

		// Write all chunks to the file
		for (byte[] readByteChunk : readByteChunks)
		{
			FileUtils.writeByteArrayToFile(file, readByteChunk, true);
		}
	}

	/**
	 * Searches the server's memory starting at <code>address</code> for <code>value</code> for <code>length</code> amount of bytes
	 *
	 * @param address The address to start searching at
	 * @param value   The value to search for
	 * @param length  The maximum range of the search
	 * @return The address where <code>value</code> occurred the first time or 0 if not found
	 */
	public int search(int address, int value, int length) throws IOException
	{
		assertValidAddress(address);

		DataOutputStream dataSender = Connector.getInstance().getDataSender();

		dataSender.writeByte(ServerCommands.SEARCH_INT);
		dataSender.writeInt(address);
		dataSender.writeInt(value);
		dataSender.writeInt(length);

		DataInputStream dataReceiver = Connector.getInstance().getDataReceiver();

		return dataReceiver.readInt();
	}

	/**
	 * Follows the given <code>memoryPointer</code> till the address it points to. Returns {@value MemoryRange#INVALID_ADDRESS} if not possible
	 *
	 * @param memoryPointer The pointer to follow
	 * @return The destination address the <code>memoryPointer</code> is pointing to
	 */
	public int getDestinationAddress(MemoryPointer memoryPointer) throws IOException
	{
		int destinationAddress = memoryPointer.getBaseAddress();

		for (int offset : memoryPointer.getOffsets())
		{
			int pointerValue = readInt(destinationAddress);

			destinationAddress = pointerValue + offset;

			if (!MemoryRange.isValid(destinationAddress))
			{
				// This pointer wasn't working
				return MemoryRange.INVALID_ADDRESS;
			}
		}

		return destinationAddress;
	}

	/**
	 * Reads <code>length</code> bytes from the memory starting at <code>address</code>
	 *
	 * @param address The address to start reading memory from
	 * @param length  The amount of bytes to read
	 * @return A byte array containing all read bytes
	 */
	public byte[] readBytes(int address, int length) throws IOException
	{
		assertValidAddress(address);

		byte[] received = new byte[length];

		DataOutputStream dataSender = Connector.getInstance().getDataSender();

		dataSender.writeByte(ServerCommands.READ_BYTES);
		dataSender.writeInt(address);
		dataSender.writeInt(address + length);

		DataInputStream dataReceiver = Connector.getInstance().getDataReceiver();
		byte serverStatus = dataReceiver.readByte();

		if (serverStatus == StatusCodes.NON_ZERO_VALUE_READ)
		{
			dataReceiver.readFully(received);
		}

		return received;
	}
}