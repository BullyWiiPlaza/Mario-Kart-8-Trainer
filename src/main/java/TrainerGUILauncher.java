import wiiudev.gecko.client.mariokart8.profile.gui.TrainerGUI;

import static javax.swing.SwingUtilities.invokeLater;
import static javax.swing.UIManager.getSystemLookAndFeelClassName;
import static javax.swing.UIManager.setLookAndFeel;

public class TrainerGUILauncher
{
	// TODO: Auto-detect currently loaded profile
	public static void main(String[] arguments) throws Exception
	{
		setLookAndFeel(getSystemLookAndFeelClassName());

		invokeLater(() ->
		{
			TrainerGUI trainerGUI = new TrainerGUI();
			trainerGUI.setVisible(true);
		});
	}
}
