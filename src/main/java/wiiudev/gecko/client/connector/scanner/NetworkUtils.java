package wiiudev.gecko.client.connector.scanner;

import org.apache.commons.net.util.SubnetUtils;

import java.net.*;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;

public class NetworkUtils
{
	/**
	 * @return The local Internet protocol address of this machine
	 */
	public static String getLocalInternetProtocolAddress() throws SocketException
	{
		Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();

		while (networkInterfaces.hasMoreElements())
		{
			for (InterfaceAddress interfaceAddress : networkInterfaces.nextElement().getInterfaceAddresses())
			{
				if (interfaceAddress.getAddress().isSiteLocalAddress())
				{
					return interfaceAddress.getAddress().toString().replace("/", "");
				}
			}
		}

		throw new IllegalStateException("Expected the computer's local IP address but didn't get one!");
	}

	/**
	 * @return The network mask length is bytes the computer is currently in
	 */
	public static int getLocalNetworkMaskLength() throws UnknownHostException, SocketException
	{
		InetAddress localHost = Inet4Address.getLocalHost();
		NetworkInterface networkInterface = NetworkInterface.getByInetAddress(localHost);

		for (InterfaceAddress address : networkInterface.getInterfaceAddresses())
		{
			int networkPrefixLength = address.getNetworkPrefixLength();

			// This should be an IPv4 address
			if (networkPrefixLength <= 32)
			{
				return address.getNetworkPrefixLength();
			}
		}

		throw new IllegalStateException("Failed to find IPv4 network prefix length!");
	}

	/**
	 * @param subNetworkAddress An IP address which is part of the sub network
	 * @param networkMaskLength The length in bytes of the sub network mask
	 * @return A list of IP addresses contained in the sub network
	 */
	public static List<String> getAllSubNetworkAddresses(String subNetworkAddress, int networkMaskLength)
	{
		SubnetUtils subnetUtils = new SubnetUtils(subNetworkAddress + "/" + networkMaskLength);
		SubnetUtils.SubnetInfo subnetInfo = subnetUtils.getInfo();
		return new LinkedList<>(Arrays.asList(subnetInfo.getAllAddresses()));
	}
}