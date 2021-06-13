package wiiudev.gecko.client.mariokart8.profile.gui;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

import static javax.swing.JOptionPane.*;

public class CustomDialog
{
	private final List<JComponent> components;

	private String title;
	private int messageType;
	private JRootPane rootPane;
	private String[] options;
	private int optionIndex;

	CustomDialog()
	{
		components = new ArrayList<>();

		setTitle("Custom dialog");
		setMessageType();
		setRootPane();
		setOptions(new String[] { "OK", "Cancel" });
		setOptionSelection();
	}

	void setTitle(String title)
	{
		this.title = title;
	}

	private void setMessageType()
	{
		this.messageType = PLAIN_MESSAGE;
	}

	void addComponent(JComponent component)
	{
		components.add(component);
	}

	public void addMessageText(String messageText)
	{
		JLabel label = new JLabel("<html>" + messageText + "</html>");

		components.add(label);
	}

	private void setRootPane()
	{
		this.rootPane = null;
	}

	private void setOptions(String[] options)
	{
		this.options = options;
	}

	private void setOptionSelection()
	{
		this.optionIndex = 0;
	}

	int show()
	{
		int optionType = OK_CANCEL_OPTION;
		Object optionSelection = null;

		if(options.length != 0)
		{
			optionSelection = options[optionIndex];
		}

		return showOptionDialog(rootPane,
				components.toArray(), title, optionType, messageType, null,
				options, optionSelection);
	}
}
