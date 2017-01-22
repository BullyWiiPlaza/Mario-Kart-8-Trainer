import wiiudev.gecko.client.mariokart8.profile.gui.TrainerGUI;

import javax.swing.*;
import java.io.IOException;

public class TrainerGUILauncher
{
	public static void main(String[] arguments) throws Exception
	{
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

		SwingUtilities.invokeLater(() ->
		{
			try
			{
				TrainerGUI trainerGUI = new TrainerGUI();
				trainerGUI.setVisible(true);
			} catch (IOException exception)
			{
				exception.printStackTrace();
			}
		});
	}
}