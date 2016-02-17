package wiiudev.gecko.client.mariokart8.profile;

import wiiudev.gecko.client.connector.MemoryWriter;

import java.io.IOException;

import static wiiudev.gecko.client.mariokart8.profile.Unlocks.unlockTracksAddress;

/**
 * A collection of methods for performing play statistics modifications
 */
public class PlayStatistics
{
	public static int totalCoinsAddress = 0x2F748128;

	/**
	 * Sets the total amount of coins collected
	 *
	 * @param value The amount to set
	 * @throws IOException
	 */
	public static void setCoins(int value) throws IOException
	{
		new MemoryWriter().writeInt(totalCoinsAddress, value);
	}

	/**
	 * Sets the total amount of jump boosts performed
	 *
	 * @param value The amount to set
	 * @throws IOException
	 */
	public static void setJumpBoosts(int value) throws IOException
	{
		new MemoryWriter().writeInt(totalCoinsAddress + 0x8, value);
	}

	/**
	 * Sets the total amount of drifts performed
	 *
	 * @param value The amount to set
	 * @throws IOException
	 */
	public static void setDrifts(int value) throws IOException
	{
		new MemoryWriter().writeInt(totalCoinsAddress + 0xC, value);
	}

	/**
	 * Sets the total amount of mini turbos performed
	 *
	 * @param value The amount to set
	 * @throws IOException
	 */
	public static void setMiniTurbos(int value) throws IOException
	{
		new MemoryWriter().writeInt(totalCoinsAddress + 0x14, value);
	}

	/**
	 * Sets the amount of super mini turbos performed
	 *
	 * @param value The amount to set
	 * @throws IOException
	 */
	public static void setSuperMiniTurbos(int value) throws IOException
	{
		new MemoryWriter().writeInt(totalCoinsAddress + 0x18, value);
	}

	/**
	 * Sets the amount of times you popped someone else's balloon
	 *
	 * @param value The amount to set
	 * @throws IOException
	 */
	public static void setBalloonsPopped(int value) throws IOException
	{
		new MemoryWriter().writeInt(totalCoinsAddress + 0x1C, value);
	}

	/**
	 * Sets the amount of times your own balloons have been popped
	 *
	 * @param value The amount to set
	 * @throws IOException
	 */
	public static void setOwnBalloonsPopped(int value) throws IOException
	{
		new MemoryWriter().writeInt(totalCoinsAddress + 0x20, value);
	}

	public static void setRaceRating(int value) throws IOException
	{
		new MemoryWriter().writeInt(unlockTracksAddress - 0x8, value);
	}

	public static void setBattleRating(int value) throws IOException
	{
		new MemoryWriter().writeInt(unlockTracksAddress - 0x4, value);
	}
}