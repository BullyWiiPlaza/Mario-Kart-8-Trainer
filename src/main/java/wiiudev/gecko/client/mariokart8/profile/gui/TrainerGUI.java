package wiiudev.gecko.client.mariokart8.profile.gui;

import org.apache.commons.io.IOUtils;
import org.apache.commons.validator.routines.InetAddressValidator;
import wiiudev.gecko.client.connector.Connector;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.HyperlinkEvent;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Integer.parseInt;
import static javax.swing.JOptionPane.*;
import static wiiudev.gecko.client.connector.scanner.WiiUDetector.getNintendoWiiUIPAddress;
import static wiiudev.gecko.client.mariokart8.profile.PlayStatistics.*;
import static wiiudev.gecko.client.mariokart8.profile.Unlocks.*;

public class TrainerGUI extends JFrame
{
	private JTextField wiiUIPAddressField;
	private JCheckBox autoDetectCheckBox;
	private JButton injectButton;
	private JComboBox<String> modificationChooserComboBox;
	private JRadioButton unlocksRadioButton;
	private List<String> unlocksList;
	private JRadioButton playStatisticsRadioButton;
	private List<String> playStatisticsList;
	private JButton aboutButton;
	private JButton connectButton;
	private JPanel rootPanel;
	private boolean connected;

	public TrainerGUI()
	{
		setPlayStatisticsList();

		setUnlocksList();

		setupFrameProperties();

		setInjectButtonListener();

		addConnectButtonListener();

		addAboutButtonListener();

		addAutoDetectListener();

		autoDetectCheckBox.setSelected(true);
		wiiUIPAddressField.setEnabled(false);

		wiiUIPAddressField.getDocument().addDocumentListener(new DocumentListener()
		{
			@Override
			public void insertUpdate(DocumentEvent event)
			{
				validateIPAddress();
			}

			@Override
			public void removeUpdate(DocumentEvent event)
			{
				validateIPAddress();
			}

			@Override
			public void changedUpdate(DocumentEvent event)
			{
				validateIPAddress();
			}
		});

		playStatisticsRadioButton.addActionListener(actionEvent -> updateModificationChooserList());

		playStatisticsRadioButton.setSelected(true);
		updateModificationChooserList();

		unlocksRadioButton.addActionListener(actionEvent -> updateModificationChooserList());

		groupSelections();

		addConnectionTerminationShutdownHook();
	}

	private void validateIPAddress()
	{
		boolean isValid = new InetAddressValidator().isValidInet4Address(wiiUIPAddressField.getText());
		connectButton.setEnabled(isValid && !connected);
	}

	private void updateModificationChooserList()
	{
		if (playStatisticsRadioButton.isSelected())
		{
			modificationChooserComboBox.removeAllItems();

			playStatisticsList.forEach(modificationChooserComboBox::addItem);
		} else if (unlocksRadioButton.isSelected())
		{
			modificationChooserComboBox.removeAllItems();
			unlocksList.forEach(modificationChooserComboBox::addItem);
		}
	}

	private void setPlayStatisticsList()
	{
		playStatisticsList = new ArrayList<>();
		playStatisticsList.add("Coins");
		playStatisticsList.add("Jump Boosts");
		playStatisticsList.add("Drifts");
		playStatisticsList.add("Mini-Turbos");
		playStatisticsList.add("Super Mini-Turbos");
		playStatisticsList.add("Balloons Popped");
		playStatisticsList.add("Own Balloons Popped");
		playStatisticsList.add("Race Rating");
		playStatisticsList.add("Battle Rating");
	}

	private void setUnlocksList()
	{
		unlocksList = new ArrayList<>();
		unlocksList.add("All Tracks");
		unlocksList.add("All Characters");
		unlocksList.add("All Vehicles");
		unlocksList.add("All Tires");
		unlocksList.add("All Gliders");
		unlocksList.add("All Stickers");
		unlocksList.add("All Cups 3 Stars");
		unlocksList.add("Amiibo Suits");
		unlocksList.add("Everything");
	}

	private void setInjectButtonListener()
	{
		injectButton.setEnabled(false);

		injectButton.addActionListener(e ->
		{
			try
			{
				int selectedIndex = modificationChooserComboBox.getSelectedIndex();

				if (playStatisticsRadioButton.isSelected())
				{
					CustomDialog dialog = new CustomDialog();
					JTextField newAmount = new JTextField();
					dialog.setTitle("Please enter!");
					dialog.addComponent(new JLabel("Please enter the new amount in decimal for \"" +
							playStatisticsList.get
									(selectedIndex) + "\"!"));
					dialog.addComponent(newAmount);

					if (dialog.show() == OK_OPTION)
					{
						int value = parseInt(newAmount.getText());

						if (modificationChooserComboBox.getSelectedIndex() == 0)
						{
							setCoins(value);
						} else if (modificationChooserComboBox.getSelectedIndex() == 1)
						{
							setJumpBoosts(value);
						} else if (modificationChooserComboBox.getSelectedIndex() == 2)
						{
							setDrifts(value);
						} else if (modificationChooserComboBox.getSelectedIndex() == 3)
						{
							setMiniTurbos(value);
						} else if (modificationChooserComboBox.getSelectedIndex() == 4)
						{
							setSuperMiniTurbos(value);
						} else if (modificationChooserComboBox.getSelectedIndex() == 5)
						{
							setBalloonsPopped(value);
						} else if (modificationChooserComboBox.getSelectedIndex() == 6)
						{
							setOwnBalloonsPopped(value);
						} else if (modificationChooserComboBox.getSelectedIndex() == 7)
						{
							setRaceRating(value);
						} else if (modificationChooserComboBox.getSelectedIndex() == 8)
						{
							setBattleRating(value);
						}

						showMessageDialog(getRootPane(), playStatisticsList.get
								(selectedIndex) +
								" successfully" +
								" set to " +
								value + "!", "Success", INFORMATION_MESSAGE);
					}
				} else if (unlocksRadioButton.isSelected())
				{
					if (modificationChooserComboBox.getSelectedIndex() == 0)
					{
						unlockAllTracks();
					} else if (modificationChooserComboBox.getSelectedIndex() == 1)
					{
						unlockAllCharacters();
					} else if (modificationChooserComboBox.getSelectedIndex() == 2)
					{
						unlockAllVehicles();
					} else if (modificationChooserComboBox.getSelectedIndex() == 3)
					{
						unlockAllTires();
					} else if (modificationChooserComboBox.getSelectedIndex() == 4)
					{
						unlockAllGliders();
					} else if (modificationChooserComboBox.getSelectedIndex() == 5)
					{
						unlockAllStickers();
					} else if (modificationChooserComboBox.getSelectedIndex() == 6)
					{
						allCups3Stars();
					} else if (modificationChooserComboBox.getSelectedIndex() == 7)
					{
						allAmiiboSuits();
					} else if (modificationChooserComboBox.getSelectedIndex() == 8)
					{
						unlockAll();
					}

					showMessageDialog(getRootPane(), unlocksList.get(modificationChooserComboBox
							.getSelectedIndex()
					) + " successfully unlocked!", "Success", INFORMATION_MESSAGE);
				}
			} catch (Exception exception)
			{
				showMessageDialog(getRootPane(), exception.getMessage(), "Error", ERROR_MESSAGE);
			}
		});
	}

	private void groupSelections()
	{
		ButtonGroup buttonGroup = new ButtonGroup();
		buttonGroup.add(unlocksRadioButton);
		buttonGroup.add(playStatisticsRadioButton);
	}

	private void addAutoDetectListener()
	{
		autoDetectCheckBox.addActionListener(actionEvent ->
		{
			boolean autoDetect = autoDetectCheckBox.isSelected();

			wiiUIPAddressField.setEnabled(!autoDetect);

			if (autoDetect)
			{
				if (!connected)
				{
					connectButton.setEnabled(true);
				}
			} else
			{
				validateIPAddress();
			}
		});
	}

	private void addConnectButtonListener()
	{
		String connectButtonText = connectButton.getText();

		connectButton.addActionListener(actionEvent -> new SwingWorker<String, String>()
		{
			@Override
			protected String doInBackground()
			{
				connectButton.setText("Connecting...");
				connectButton.setEnabled(false);

				try
				{
					String ipAddress;

					if (autoDetectCheckBox.isSelected())
					{
						ipAddress = getNintendoWiiUIPAddress();
					} else
					{
						ipAddress = wiiUIPAddressField.getText();
					}

					Connector.getInstance().connect(ipAddress);

					connectButton.setText("Connected");
					connectButton.setEnabled(false);
					injectButton.setEnabled(true);
					connected = true;
				} catch (Exception exception)
				{
					showMessageDialog(null, exception.getMessage(),
							"Connection Failed", ERROR_MESSAGE);

					connectButton.setEnabled(true);
					connectButton.setText(connectButtonText);
				}

				return null;
			}
		}.execute());
	}

	private static String resourceToString() throws IOException
	{
		InputStream inputStream = TrainerGUI.class.getResourceAsStream("/About.html");
		return IOUtils.toString(inputStream, StandardCharsets.UTF_8);
	}

	private void addAboutButtonListener()
	{
		aboutButton.addActionListener(actionEvent ->
		{
			try
			{
				String html = resourceToString();

				JEditorPane aboutPane = new JEditorPane();
				aboutPane.setContentType("text/html");
				aboutPane.setText(html);

				aboutPane.setEditable(false);
				aboutPane.setOpaque(false);

				aboutPane.addHyperlinkListener(hyperLinkEvent ->
				{
					if (HyperlinkEvent.EventType.ACTIVATED.equals(hyperLinkEvent.getEventType()))
					{
						Desktop desktop = Desktop.getDesktop();

						try
						{
							desktop.browse(hyperLinkEvent.getURL().toURI());
						} catch (Exception exception)
						{
							exception.printStackTrace();
						}
					}
				});

				showMessageDialog(getRootPane(), aboutPane, "About", INFORMATION_MESSAGE);
			} catch (Exception exception)
			{
				exception.printStackTrace();
			}
		});
	}

	private void addConnectionTerminationShutdownHook()
	{
		Runtime.getRuntime().addShutdownHook(new Thread(() ->
		{
			try
			{
				if (Connector.getInstance().isConnected())
				{
					Connector.getInstance().closeConnection();
				}
			} catch (IOException exception)
			{
				exception.printStackTrace();
			}
		}));
	}

	private void setupFrameProperties()
	{
		setContentPane(rootPanel);
		setTitle("Mario Kart 8 Trainer");
		setSize(300, 260);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/Icon.png")));
	}
}
