package wiiudev.gecko.client.mariokart8.profile.gui;

import wiiudev.gecko.client.mariokart8.profile.PlayStatistics;
import wiiudev.gecko.client.mariokart8.profile.Unlocks;
import wiiudev.gecko.client.connector.Connector;
import org.apache.commons.io.IOUtils;
import org.apache.commons.validator.routines.InetAddressValidator;
import wiiudev.gecko.client.connector.scanner.WiiUDetector;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.HyperlinkEvent;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

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

	public TrainerGUI() throws IOException
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

					if (JOptionPane.OK_OPTION == dialog.show())
					{
						int value = Integer.parseInt(newAmount.getText());

						if (modificationChooserComboBox.getSelectedIndex() == 0)
						{
							PlayStatistics.setCoins(value);
						} else if (modificationChooserComboBox.getSelectedIndex() == 1)
						{
							PlayStatistics.setJumpBoosts(value);
						} else if (modificationChooserComboBox.getSelectedIndex() == 2)
						{
							PlayStatistics.setDrifts(value);
						} else if (modificationChooserComboBox.getSelectedIndex() == 3)
						{
							PlayStatistics.setMiniTurbos(value);
						} else if (modificationChooserComboBox.getSelectedIndex() == 4)
						{
							PlayStatistics.setSuperMiniTurbos(value);
						} else if (modificationChooserComboBox.getSelectedIndex() == 5)
						{
							PlayStatistics.setBalloonsPopped(value);
						} else if (modificationChooserComboBox.getSelectedIndex() == 6)
						{
							PlayStatistics.setOwnBalloonsPopped(value);
						} else if (modificationChooserComboBox.getSelectedIndex() == 7)
						{
							PlayStatistics.setRaceRating(value);
						} else if (modificationChooserComboBox.getSelectedIndex() == 8)
						{
							PlayStatistics.setBattleRating(value);
						}

						JOptionPane.showMessageDialog(getRootPane(), playStatisticsList.get
								(selectedIndex) +
								" successfully" +
								" set to " +
								value + "!", "Success", JOptionPane
								.INFORMATION_MESSAGE);
					}
				} else if (unlocksRadioButton.isSelected())
				{
					if (modificationChooserComboBox.getSelectedIndex() == 0)
					{
						Unlocks.unlockAllTracks();
					} else if (modificationChooserComboBox.getSelectedIndex() == 1)
					{
						Unlocks.unlockAllCharacters();
					} else if (modificationChooserComboBox.getSelectedIndex() == 2)
					{
						Unlocks.unlockAllVehicles();
					} else if (modificationChooserComboBox.getSelectedIndex() == 3)
					{
						Unlocks.unlockAllTires();
					} else if (modificationChooserComboBox.getSelectedIndex() == 4)
					{
						Unlocks.unlockAllGliders();
					} else if (modificationChooserComboBox.getSelectedIndex() == 5)
					{
						Unlocks.unlockAllStickers();
					} else if (modificationChooserComboBox.getSelectedIndex() == 6)
					{
						Unlocks.allCups3Stars();
					} else if (modificationChooserComboBox.getSelectedIndex() == 7)
					{
						Unlocks.unlockAll();
					}

					JOptionPane.showMessageDialog(getRootPane(), unlocksList.get(modificationChooserComboBox
							.getSelectedIndex()
					) + " successfully unlocked!", "Success", JOptionPane.INFORMATION_MESSAGE);
				}
			} catch (Exception exception)
			{
				JOptionPane.showMessageDialog(getRootPane(), exception.getMessage(), "Error", JOptionPane
						.ERROR_MESSAGE);
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
			boolean autoDetect = autoDetectCheckBox
					.isSelected();

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
			protected String doInBackground() throws Exception
			{
				connectButton.setText("Connecting...");
				connectButton.setEnabled(false);

				try
				{
					String ipAddress;

					if (autoDetectCheckBox.isSelected())
					{
						ipAddress = WiiUDetector.getNintendoWiiUInternetProtocolAddress();
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
					JOptionPane.showMessageDialog(null, exception.getMessage(), "Connection Failed", JOptionPane
							.ERROR_MESSAGE);

					connectButton.setEnabled(true);
					connectButton.setText(connectButtonText);
				}

				return null;
			}
		}.execute());
	}

	private String resourceToString(String filePath) throws IOException, URISyntaxException
	{
		InputStream inputStream = TrainerGUI.class.getResourceAsStream(filePath);

		return IOUtils.toString(inputStream);
	}

	private void addAboutButtonListener()
	{
		aboutButton.addActionListener(e ->
		{
			try
			{
				String html = resourceToString("/About.html");

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

				JOptionPane.showMessageDialog(getRootPane(), aboutPane, "About", JOptionPane.INFORMATION_MESSAGE);
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

	private void setupFrameProperties() throws IOException
	{
		setContentPane(rootPanel);
		setTitle("Mario Kart 8 Trainer");
		setSize(300, 300);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/Icon.png")));
	}
}