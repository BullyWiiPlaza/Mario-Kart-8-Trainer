import wiiudev.gecko.client.mariokart8.profile.gui.TrainerGUI;

import javax.swing.*;

public class TrainerGUILauncher
{
	public static void main(String[] arguments) throws Exception
	{
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

		new TrainerGUI().setVisible(true);
	}
}