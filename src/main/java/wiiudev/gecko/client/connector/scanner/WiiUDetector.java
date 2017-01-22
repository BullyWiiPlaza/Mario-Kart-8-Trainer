package wiiudev.gecko.client.connector.scanner;

import wiiudev.gecko.client.connector.Connector;
import wiiudev.gecko.client.connector.MemoryReader;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Scans the network for a possible Wii U console in order to provide an auto-detection functionality
 */
public class WiiUDetector
{
	/**
	 * Gets all addresses from the computer's sub network and checks them to find the Wii U console
	 *
	 * @return The Wii U console's local IP address
	 */
	public static String getNintendoWiiUIPAddress() throws IOException, InterruptedException,
			ExecutionException
	{
		String localIPAddress = NetworkUtils.getLocalInternetProtocolAddress();
		int networkMaskLength = NetworkUtils.getLocalNetworkMaskLength();
		List<String> subNetworkAddresses = NetworkUtils.getAllSubNetworkAddresses(localIPAddress, networkMaskLength);
		subNetworkAddresses.remove(localIPAddress); // Remove the local IP address

		return getWiiUIPAddress(subNetworkAddresses);
	}

	/**
	 * Pings all available IP addresses in the current sub network in order to find the Wii U console
	 */
	private static String getWiiUIPAddress(List<String> subNetworkAddresses) throws InterruptedException, ExecutionException
	{
		int subNetStartingIndex = 1;
		int subNetUpperBound = 256;
		int threadPoolSize = subNetUpperBound - subNetStartingIndex;
		ExecutorService pool = Executors.newFixedThreadPool(threadPoolSize);
		ExecutorCompletionService<String> completionService = new ExecutorCompletionService<>(pool);

		for (String subNetworkAddress : subNetworkAddresses)
		{
			completionService.submit(() ->
			{
				try
				{
					if (PingUtils.isReachable(subNetworkAddress))
					{
						Connector.getInstance().connect(subNetworkAddress);
						MemoryReader memoryReader = new MemoryReader();
						int readValue = memoryReader.readInt(0x10000000);
						int expected = 0x3E8;

						if (readValue != expected)
						{
							throw new IllegalStateException("Read value was " + readValue + " but expected " + expected + "!");
						}

						pool.shutdownNow();

						return subNetworkAddress;
					}
				} catch (Exception ignored)
				{

				}

				return null;
			});
		}

		for (int tasksIndex = 0; tasksIndex < threadPoolSize; tasksIndex++)
		{
			// Retrieve results as they become available
			String result = completionService.take().get();

			if (result != null)
			{
				return result;
			}
		}

		pool.shutdown();

		return null;
	}
}