package wiiudev.gecko.client.mariokart8.profile;

import wiiudev.gecko.client.connector.MemoryWriter;

import java.io.IOException;

/**
 * A collection of methods that unlock certain things in Mario Kart 8
 */
public class Unlocks
{
	public static int unlockTracksAddress = PlayStatistics.totalCoinsAddress + 0x540;
	private static int value = 0x03030303;

	/**
	 * Unlocks all racing tracks
	 */
	public static void unlockAllTracks() throws IOException
	{
		new MemoryWriter().serialWrite(unlockTracksAddress, value, 1);
	}

	/**
	 * Unlocks all playable characters
	 */
	public static void unlockAllCharacters() throws IOException
	{
		new MemoryWriter().serialWrite(unlockTracksAddress + 0x28, value, 7);
	}

	/**
	 * Unlocks all karts and bikes
	 */
	public static void unlockAllVehicles() throws IOException
	{
		new MemoryWriter().serialWrite(unlockTracksAddress + 0x68, value, 6);
	}

	/**
	 * Unlocks all vehicle tires
	 */
	public static void unlockAllTires() throws IOException
	{
		new MemoryWriter().serialWrite(unlockTracksAddress + 0xA8, value, 4);
	}

	/**
	 * Unlocks all glider parts
	 */
	public static void unlockAllGliders() throws IOException
	{
		new MemoryWriter().serialWrite(unlockTracksAddress + 0xE8, value, 2);
	}

	/**
	 * Unlocks all character stickers
	 */
	public static void unlockAllStickers() throws IOException
	{
		new MemoryWriter().serialWrite(unlockTracksAddress + 0x130, value, 22);
	}

	public static void allCups3Stars() throws IOException
	{
		new MemoryWriter().serialWrite(PlayStatistics.totalCoinsAddress - 0x101C, value, 33);
	}

	public static void allAmiiboSuits() throws IOException
	{
		new MemoryWriter().writeInt(PlayStatistics.totalCoinsAddress + 0x5100, 0x0071FEFF);
	}

	/**
	 * Unlocks all tracks, characters, vehicles, tires, gliders and stickers
	 */
	public static void unlockAll() throws IOException
	{
		allCups3Stars();
		unlockAllTracks();
		unlockAllCharacters();
		unlockAllVehicles();
		unlockAllTires();
		unlockAllGliders();
		unlockAllStickers();
		allAmiiboSuits();
	}
}