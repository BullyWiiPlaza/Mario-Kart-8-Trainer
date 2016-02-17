import wiiudev.gecko.client.connector.Connector;
import wiiudev.gecko.client.connector.MemoryWriter;
import wiiudev.gecko.client.mariokart8.profile.gui.TrainerGUI;

import javax.swing.*;

public class TrainerGUILauncher
{
	public static void main(String[] arguments) throws Exception
	{
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		new TrainerGUI().setVisible(true);

		/*Connector.getInstance().connect("192.168.178.35");



		Connector.getInstance().closeConnection();*/
	}
}