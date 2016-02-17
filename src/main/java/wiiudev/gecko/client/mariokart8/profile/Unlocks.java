package wiiudev.gecko.client.mariokart8.profile;

import wiiudev.gecko.client.connector.MemoryWriter;

import java.io.IOException;
import java.util.logging.Logger;

/**
 * A collection of methods that unlock certain things in Mario Kart 8
 */
public class Unlocks
{
	public static int unlockTracksAddress = PlayStatistics.totalCoinsAddress + 0x540;
	private static int value = 0x03030303;

	private static final Logger LOGGER = Logger.getLogger(Unlocks.class.getName());

	/**
	 * Unlocks all racing tracks
	 *
	 * @throws IOException
	 */
	public static void unlockAllTracks() throws IOException
	{
		LOGGER.info("Unlocking all tracks...");
		new MemoryWriter().serialWrite(unlockTracksAddress, value, 1);
	}

	/**
	 * Unlocks all playable characters
	 *
	 * @throws IOException
	 */
	public static void unlockAllCharacters() throws IOException
	{
		LOGGER.info("Unlocking all characters...");
		new MemoryWriter().serialWrite(unlockTracksAddress + 0x28, value, 7);
	}

	/**
	 * Unlocks all karts and bikes
	 *
	 * @throws IOException
	 */
	public static void unlockAllVehicles() throws IOException
	{
		LOGGER.info("Unlocking all vehicles...");
		new MemoryWriter().serialWrite(unlockTracksAddress + 0x68, value, 6);
	}

	/**
	 * Unlocks all vehicle tires
	 *
	 * @throws IOException
	 */
	public static void unlockAllTires() throws IOException
	{
		LOGGER.info("Unlocking all tires...");
		new MemoryWriter().serialWrite(unlockTracksAddress + 0xA8, value, 4);
	}

	/**
	 * Unlocks all glider parts
	 *
	 * @throws IOException
	 */
	public static void unlockAllGliders() throws IOException
	{
		LOGGER.info("Unlocking all gliders...");
		new MemoryWriter().serialWrite(unlockTracksAddress + 0xE8, value, 2);
	}

	/**
	 * Unlocks all character stickers
	 *
	 * @throws IOException
	 */
	public static void unlockAllStickers() throws IOException
	{
		LOGGER.info("Unlocking all stickers...");
		new MemoryWriter().serialWrite(unlockTracksAddress + 0x130, value, 22);
	}

	/**
	 * Unlocks all tracks, characters, vehicles, tires, gliders and stickers
	 *
	 * @throws IOException
	 */
	public static void unlockAll() throws IOException
	{
		unlockAllTracks();
		unlockAllCharacters();
		unlockAllVehicles();
		unlockAllTires();
		unlockAllGliders();
		unlockAllStickers();
	}
}