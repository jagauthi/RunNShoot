package View;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import Controller.AudioManager;
import Controller.Dao;
import sun.audio.*;

public class Launcher {

	final int WIDTH = 600;
	final int HEIGHT = 400;

	// UI Stuff
	public static final Color TRANSPARENT = new Color(1.0f, 1.0f, 1.0f, 0.0f);
	public static final Color FAKETRANS = new Color(128, 128, 128);
	public static final Color FIELDGRAY = new Color(200, 200, 200);
	public static Font smallFont;
	public static Font normalFont;
	public static Font bigFont;
	public static Border emptyBorder;
	private Image closeIcon;
	static Image plusIcon;
	static Image minusIcon;
	public static Image magePort, roguePort, warriorPort;
	private Image newPort;
	private Image unknownPort;
	public static BufferedImage npBackground, npBasic_50_7;

	private JFrame frame;

	// Panels
	private JPanel cards;
	private BackgroundedPanel connectPanel;
	private BackgroundedPanel loginPanel;
	private BackgroundedPanel createAccountPanel;
	private BackgroundedPanel charSelectPanel;
	private BackgroundedPanel createNewCharacterPanel;
	private BackgroundedPanel modifyAccountPanel;

	// Info that is collected in the Login Panel
	private JTextField loginNameText;
	private JPasswordField loginPasswordText;
	private JRadioButton rememberLoginName;

	// Info that is collected in the Create Account Panel
	private JTextField createNameText;
	private JTextField createEmailText;
	private JPasswordField createPasswordText;
	private JPasswordField createVerifyPasswordText;
	private JTextField createSecAnswer1Text;
	private JTextField createSecAnswer2Text;
	JComboBox secQuestions1;
	JComboBox secQuestions2;
	JButton createButton;
	JButton modifyButton;

	private JTextField modifyNameText;
	private JTextField modifyEmailText;
	private JPasswordField modifyPasswordText;
	private JPasswordField modifyVerifyPasswordText;
	private JTextField modifySecAnswer1Text;
	private JTextField modifySecAnswer2Text;

	private String username = "";
	String[] acctInfo;
	int charSelected = 0;
	JButton[] charSelectButtons;
	JLabel[] charLabels;
	String[] characterNames = new String[5];

	// Info that is collected in the Create New Character Panel
	int[] statValues;
	JTextField newCharacterNameText;
	static JTextField pointsLeft;
	static int pointsRemaining;
	JRadioButton male;
	JRadioButton female;
	JRadioButton warrior;
	JRadioButton rogue;
	JRadioButton mage;
	StatField strengthField;
	StatField dexterityField;
	StatField constitutionField;
	StatField intelligenceField;
	StatField willpowerField;
	StatField luckField;

	// Each of the elements holds a string with
	// info about each individual character. Each
	// of the strings hold values for the character's
	// stats and stuff, and they're each separated
	// by a space (or some other delimiter)
	ArrayList<String> characters = new ArrayList<String>();

	String accountID;

	AudioManager audioManager;

	Dao dao;

	public Launcher() {
		try {
			// Load Fonts
			File fontIn = new File("resources/GUI/Fonts/8Bit.ttf");
			smallFont = Font.createFont(Font.TRUETYPE_FONT, fontIn).deriveFont(20f);
			normalFont = Font.createFont(Font.TRUETYPE_FONT, fontIn).deriveFont(30f);
			bigFont = Font.createFont(Font.TRUETYPE_FONT, fontIn).deriveFont(60f);
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			// register the font
			ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, fontIn));
			// Load Images
			closeIcon = ImageIO.read(new File("resources/GUI/Icons/CloseIcon.png"));
			plusIcon = ImageIO.read(new File("resources/GUI/Icons/PlusIcon.png"));
			minusIcon = ImageIO.read(new File("resources/GUI/Icons/MinusIcon.png"));
			magePort = ImageIO.read(new File("resources/GUI/Portraits/mage.png"));
			roguePort = ImageIO.read(new File("resources/GUI/Portraits/rogue.png"));
			warriorPort = ImageIO.read(new File("resources/GUI/Portraits/warrior.png"));
			newPort = ImageIO.read(new File("resources/GUI/Portraits/new.png"));
			unknownPort = ImageIO.read(new File("resources/GUI/Portraits/unknown.png"));
			npBackground = ImageIO.read(new File("resources/GUI/NinePatches/large_200x200_18x18.png"));
			npBasic_50_7 = ImageIO.read(new File("resources/GUI/NinePatches/basic_50x50_7x7.png"));
		} catch (IOException e) {
			e.printStackTrace();
		} catch (FontFormatException e) {
			e.printStackTrace();
		}
		// Set Fonts as default for buttons, labels, and text fields
		UIManager.put("Label.font", normalFont);
		UIManager.put("Button.font", normalFont);
		UIManager.put("Button.background", FIELDGRAY);
		UIManager.put("TextField.font", normalFont);
		UIManager.put("TextField.background", FIELDGRAY);
		NinePatchImage np = new NinePatchImage(WIDTH, HEIGHT, 23, 23, npBackground);
		emptyBorder = BorderFactory.createEmptyBorder();

		frame = new JFrame();
		frame.setUndecorated(true);
		frame.setBackground(TRANSPARENT);

		cards = new JPanel(new CardLayout());
		connectPanel = new BackgroundedPanel(np);
		loginPanel = new BackgroundedPanel(np);
		createAccountPanel = new BackgroundedPanel(np);
		charSelectPanel = new BackgroundedPanel(np);
		createNewCharacterPanel = new BackgroundedPanel(np);
		modifyAccountPanel = new BackgroundedPanel(np);

		initConnectPanel();
		initLoginPanel();
		initCreateAccountPanel();
		// initCreateNewCharacterPanel();
		connectToServer();

		cards.add(connectPanel, "Connect Panel");
		cards.add(loginPanel, "Login Panel");
		cards.add(createAccountPanel, "Create Account Panel");
		cards.add(modifyAccountPanel, "Modify Account Panel");
		cards.add(charSelectPanel, "Char Select Panel");
		cards.add(createNewCharacterPanel, "Create New Character Panel");

		frame.add(cards, BorderLayout.CENTER);

		frame.setTitle("Launcher");
		frame.setSize(WIDTH, HEIGHT);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);

		audioManager = new AudioManager();

		// switchCards("Create New Character Panel");
	}

	public void initConnectPanel() {
		connectPanel.setLayout(null);

		NinePatchImage np = new NinePatchImage(400, 200, 23, 23, npBackground);

		JButton closeButton = new JButton(new ImageIcon(closeIcon));
		closeButton.setMargin(new Insets(0, 0, 0, 0));
		closeButton.setBackground(TRANSPARENT);
		closeButton.setBorder(emptyBorder);
		closeButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				System.exit(0);
			}
		});
		Dimension size = closeButton.getPreferredSize();
		closeButton.setBounds(WIDTH - size.width - connectPanel.getNinePatch().getCornerWidth() + 5,
				connectPanel.getNinePatch().getCornerHeight() - 5, size.width, size.height);

		JButton connectButton = new JButton();
		connectButton.setPreferredSize(new Dimension(400, 200));
		connectButton.setBackground(TRANSPARENT);
		connectButton.setBorder(emptyBorder);
		connectButton.setIcon(new ImageIcon(np.getScaledImage(400, 200)));
		connectButton.setText("Play!");
		connectButton.setFont(bigFont);
		connectButton.setHorizontalTextPosition(JButton.CENTER);
		connectButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				goToLogin(evt);
			}
		});
		size = connectButton.getPreferredSize();
		connectButton.setBounds((WIDTH / 2) - (size.width / 2), (HEIGHT / 2) - (size.height / 2), size.width,
				size.height);

		connectPanel.add(connectButton);
		connectPanel.add(closeButton);
	}

	public void initLoginPanel() {
		JLabel loginNameLabel = new JLabel();
		JLabel loginPasswordLabel = new JLabel();
		loginNameText = new JTextField();
		loginPasswordText = new JPasswordField();
		rememberLoginName = new JRadioButton();
		JButton loginButton = new JButton();
		JButton createAccountButton = new JButton();
		JButton forgotPasswordButton = new JButton();

		NinePatchImage np = new NinePatchImage(200, 400, 7, 7, npBasic_50_7);
		Dimension size = null;

		loginNameLabel.setText("Username:");
		loginNameLabel.setAlignmentX(JLabel.CENTER);
		size = loginNameLabel.getPreferredSize();
		loginNameLabel.setBounds((WIDTH / 5) - (size.width / 2) + loginPanel.getNinePatch().getCornerWidth(),
				(HEIGHT / 4) - (size.height) + loginPanel.getNinePatch().getCornerHeight(), size.width, size.height);

		loginNameText.setPreferredSize(new Dimension(
				WIDTH - (WIDTH / 5) - size.width - (loginPanel.getNinePatch().getCornerWidth() * 2), size.height));
		loginNameText.setBorder(BorderFactory.createLineBorder(Color.black));
		size = loginNameText.getPreferredSize();
		loginNameText.setBounds((WIDTH * 3 / 5) - (size.width / 2) + loginPanel.getNinePatch().getCornerWidth(),
				(HEIGHT / 4) - (size.height) + loginPanel.getNinePatch().getCornerHeight(), size.width, size.height);

		loginPasswordLabel.setText("Password:");
		loginPasswordLabel.setAlignmentX(JLabel.LEFT);
		size = loginPasswordLabel.getPreferredSize();
		loginPasswordLabel.setBounds(loginNameLabel.getBounds().x,
				loginNameLabel.getBounds().y + (loginNameLabel.getHeight() * 3 / 2), loginNameLabel.getWidth(),
				size.height);

		loginPasswordText.setBounds(loginNameText.getBounds().x,
				loginNameText.getBounds().y + (loginNameText.getHeight() * 3 / 2), loginNameText.getWidth(),
				loginNameText.getHeight());
		loginPasswordText.setBackground(FIELDGRAY);
		loginPasswordText.setBorder(BorderFactory.createLineBorder(Color.black));

		rememberLoginName.setText("Remember Login Name");
		rememberLoginName.setFont(smallFont);
		rememberLoginName.setBackground(TRANSPARENT);
		rememberLoginName.setBorder(emptyBorder);
		rememberLoginName.setFocusPainted(false);
		size = rememberLoginName.getPreferredSize();
		rememberLoginName.setBounds(loginNameLabel.getBounds().x,
				loginPasswordLabel.getBounds().y + (loginPasswordLabel.getHeight() * 3 / 2), size.width, size.height);

		JButton closeButton = new JButton(new ImageIcon(closeIcon));
		closeButton.setMargin(new Insets(0, 0, 0, 0));
		closeButton.setBackground(TRANSPARENT);
		closeButton.setBorder(emptyBorder);
		closeButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				System.exit(0);
			}
		});
		size = closeButton.getPreferredSize();
		closeButton.setBounds(WIDTH - size.width - connectPanel.getNinePatch().getCornerWidth() + 5,
				connectPanel.getNinePatch().getCornerHeight() - 5, size.width, size.height);

		createAccountButton.setText("Create Account");
		createAccountButton.setFont(smallFont);
		createAccountButton.setBorder(emptyBorder);
		createAccountButton.setBackground(TRANSPARENT);
		createAccountButton.setHorizontalTextPosition(JButton.CENTER);
		createAccountButton.setPreferredSize(new Dimension(WIDTH / 3, HEIGHT / 5));
		size = createAccountButton.getPreferredSize();
		createAccountButton.setBounds(loginPanel.getNinePatch().getCornerWidth(),
				HEIGHT - loginPanel.getNinePatch().getCornerHeight() - size.height, size.width, size.height);
		createAccountButton.setIcon(new ImageIcon(np.getScaledImage(size.width, size.height)));
		createAccountButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				goToCreateAccount(evt);
			}
		});

		loginButton.setText("Login");
		loginButton.setHorizontalTextPosition(JButton.CENTER);
		loginButton.setFont(normalFont);
		loginButton.setBackground(TRANSPARENT);
		loginButton.setBorder(emptyBorder);
		loginButton.setIcon(new ImageIcon(np.getScaledImage((int) size.getWidth(), (int) size.getHeight())));
		loginButton.setBounds(WIDTH - loginPanel.getNinePatch().getCornerWidth() - size.width,
				HEIGHT - loginPanel.getNinePatch().getCornerHeight() - size.height, size.width, size.height);
		loginButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				logIn(evt);
			}
		});

		forgotPasswordButton.setText("Forgot password?");
		forgotPasswordButton.setHorizontalTextPosition(JButton.CENTER);
		forgotPasswordButton.setFont(smallFont);
		forgotPasswordButton.setBackground(TRANSPARENT);
		forgotPasswordButton.setBorder(emptyBorder);
		size = forgotPasswordButton.getPreferredSize();
		forgotPasswordButton.setBounds(WIDTH - loginPanel.getNinePatch().getCornerWidth() - size.width,
				loginPasswordLabel.getBounds().y + (loginPasswordLabel.getHeight() * 3 / 2), size.width, size.height);
		forgotPasswordButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				forgotPassword(evt);
			}
		});

		loginPanel.setLayout(null);

		loginPanel.add(closeButton);
		loginPanel.add(loginNameLabel);
		loginPanel.add(loginNameText);
		loginPanel.add(loginPasswordLabel);
		loginPanel.add(loginPasswordText);
		loginPanel.add(rememberLoginName);
		loginPanel.add(forgotPasswordButton);
		loginPanel.add(createAccountButton);
		loginPanel.add(loginButton);
	}

	public void initCreateAccountPanel() {
		int cornerW = createAccountPanel.getNinePatch().getCornerWidth();
		int cornerH = createAccountPanel.getNinePatch().getCornerHeight();

		JButton closeButton = new JButton();
		JButton createAccountBackButton = new JButton();
		JLabel createNameLabel = new JLabel();
		JLabel createEmailLabel = new JLabel();
		JLabel createPasswordLabel = new JLabel();
		JLabel createVerifyPasswordLabel = new JLabel();

		createNameText = new JTextField();
		createEmailText = new JTextField();
		createPasswordText = new JPasswordField();
		createVerifyPasswordText = new JPasswordField();
		createSecAnswer1Text = new JTextField();
		createSecAnswer2Text = new JTextField();
		createButton = new JButton();

		String[] questions1 = { "What is your mothers maiden name", "Sec Question 2", "Sec Question 3" };
		String[] questions2 = { "Name of your first pet", "Sec Question 2", "Sec Question 3" };
		secQuestions1 = new JComboBox(questions1);
		secQuestions1.setSelectedIndex(0);
		secQuestions2 = new JComboBox(questions2);
		secQuestions2.setSelectedIndex(0);

		Dimension size = null;
		NinePatchImage np = new NinePatchImage(50, 50, 7, 7, npBasic_50_7);

		closeButton.setIcon(new ImageIcon(closeIcon));
		closeButton.setMargin(new Insets(0, 0, 0, 0));
		closeButton.setBackground(TRANSPARENT);
		closeButton.setBorder(emptyBorder);
		size = closeButton.getPreferredSize();
		closeButton.setBounds(WIDTH - size.width - cornerW + 5, cornerH - 5, size.width, size.height);
		closeButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				System.exit(0);
			}
		});

		createAccountBackButton.setText("<< Back");
		createAccountBackButton.setHorizontalTextPosition(JButton.CENTER);
		createAccountBackButton.setBackground(TRANSPARENT);
		createAccountBackButton.setBorder(emptyBorder);
		size = createAccountBackButton.getPreferredSize();
		createAccountBackButton.setBounds(cornerW - 5, cornerH - 5, size.width + 10, size.height + 10);
		createAccountBackButton.setIcon(new ImageIcon(np.getScaledImage(size.width + 10, size.height + 10)));
		createAccountBackButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				createAccountGoBack(evt);
			}
		});

		createEmailLabel.setText("Email: ");
		createEmailLabel.setHorizontalAlignment(JLabel.LEFT);
		size = createEmailLabel.getPreferredSize();
		createEmailLabel.setBounds(cornerW, cornerH + (createAccountBackButton.getHeight()) + 5, size.width,
				size.height);
		createEmailText.setBorder(BorderFactory.createLineBorder(Color.black));
		createEmailText.setBounds(cornerW + size.width, createEmailLabel.getBounds().y,
				WIDTH - (cornerW * 2) - size.width, size.height);

		createNameLabel.setText("Username: ");
		createNameLabel.setHorizontalAlignment(JLabel.LEFT);
		size = createNameLabel.getPreferredSize();
		createNameLabel.setBounds(cornerW, createEmailLabel.getBounds().y + (createEmailLabel.getHeight()) + 5,
				size.width, size.height);
		createNameText.setBorder(BorderFactory.createLineBorder(Color.black));
		createNameText.setBounds(cornerW + size.width, createNameLabel.getBounds().y,
				WIDTH - (cornerW * 2) - size.width, size.height);

		createPasswordLabel.setText("Password: ");
		createPasswordLabel.setHorizontalAlignment(JLabel.LEFT);
		size = createPasswordLabel.getPreferredSize();
		createPasswordLabel.setBounds(cornerW, createNameLabel.getBounds().y + (createNameLabel.getHeight()) + 5,
				size.width, size.height);
		createPasswordText.setBackground(FIELDGRAY);
		createPasswordText.setBorder(BorderFactory.createLineBorder(Color.black));
		createPasswordText.setBounds(cornerW + size.width, createPasswordLabel.getBounds().y,
				WIDTH - (cornerW * 2) - size.width, size.height);

		createVerifyPasswordLabel.setText("<html>Re-enter<br>Password</html>");
		createVerifyPasswordLabel.setFont(smallFont);
		createVerifyPasswordLabel.setHorizontalAlignment(JLabel.CENTER);
		createVerifyPasswordLabel.setBounds(cornerW,
				createPasswordLabel.getBounds().y + (createPasswordLabel.getHeight()) + 2, size.width, size.height + 6);
		createVerifyPasswordText.setBackground(FIELDGRAY);
		createVerifyPasswordText.setBorder(BorderFactory.createLineBorder(Color.black));
		createVerifyPasswordText.setBounds(cornerW + size.width, createVerifyPasswordLabel.getBounds().y + 5,
				WIDTH - (cornerW * 2) - size.width, size.height);

		secQuestions1.setBackground(FIELDGRAY);
		secQuestions1.setBorder(BorderFactory.createLineBorder(Color.black));
		secQuestions1.setBounds(cornerW,
				createVerifyPasswordLabel.getBounds().y + (createVerifyPasswordLabel.getHeight()) + 5,
				WIDTH / 2 - cornerW - 10, size.height);
		createSecAnswer1Text.setBorder(BorderFactory.createLineBorder(Color.black));
		createSecAnswer1Text.setBounds(WIDTH / 2 + 10, secQuestions1.getBounds().y, WIDTH / 2 - cornerW - 10,
				size.height);

		secQuestions2.setBackground(FIELDGRAY);
		secQuestions2.setBorder(BorderFactory.createLineBorder(Color.black));
		secQuestions2.setBounds(cornerW, secQuestions1.getBounds().y + (secQuestions1.getHeight()),
				WIDTH / 2 - cornerW - 10, size.height);
		createSecAnswer2Text.setBorder(BorderFactory.createLineBorder(Color.black));
		createSecAnswer2Text.setBounds(WIDTH / 2 + 10, secQuestions2.getBounds().y, WIDTH / 2 - cornerW - 10,
				size.height);

		createButton.setText("Create!");
		createButton.setHorizontalTextPosition(JButton.CENTER);
		createButton.setBackground(TRANSPARENT);
		createButton.setBorder(emptyBorder);
		size = createButton.getPreferredSize();
		createButton.setBounds(WIDTH - cornerW - size.width - 15, HEIGHT - cornerH - size.height - 5, size.width + 15,
				size.height + 10);
		createButton.setIcon(new ImageIcon(np.getScaledImage(size.width + 15, size.height + 10)));
		createButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				createAccount(evt);
			}
		});

		createAccountPanel.setLayout(null);

		createAccountPanel.add(closeButton);
		createAccountPanel.add(createAccountBackButton);
		createAccountPanel.add(createNameLabel);
		createAccountPanel.add(createNameText);
		createAccountPanel.add(createEmailLabel);
		createAccountPanel.add(createEmailText);
		createAccountPanel.add(createPasswordLabel);
		createAccountPanel.add(createPasswordText);
		createAccountPanel.add(createVerifyPasswordLabel);
		createAccountPanel.add(createVerifyPasswordText);
		createAccountPanel.add(secQuestions1);
		createAccountPanel.add(createSecAnswer1Text);
		createAccountPanel.add(secQuestions2);
		createAccountPanel.add(createSecAnswer2Text);
		createAccountPanel.add(createButton);

	}

	public void initModifyAccountPanel() {
		modifyAccountPanel.removeAll();

		int cornerW = createAccountPanel.getNinePatch().getCornerWidth();
		int cornerH = createAccountPanel.getNinePatch().getCornerHeight();

		JLabel modifyNameLabel = new JLabel();
		JLabel modifyEmailLabel = new JLabel();
		JLabel modifyPasswordLabel = new JLabel();
		JLabel modifyVerifyPasswordLabel = new JLabel();

		modifyNameText = new JTextField();
		modifyEmailText = new JTextField();
		modifyPasswordText = new JPasswordField();
		modifyVerifyPasswordText = new JPasswordField();
		modifySecAnswer1Text = new JTextField();
		modifySecAnswer2Text = new JTextField();
		modifyButton = new JButton();
		JButton modifyAccountBackButton = new JButton();
		JButton closeButton = new JButton();

		String[] questions1 = { "What is your mothers maiden name", "Sec Question 2", "Sec Question 3" };
		String[] questions2 = { "Name of your first pet", "Sec Question 2", "Sec Question 3" };
		secQuestions1 = new JComboBox(questions1);
		secQuestions1.setSelectedIndex(0);
		secQuestions2 = new JComboBox(questions2);
		secQuestions2.setSelectedIndex(0);

		Dimension size = null;
		NinePatchImage np = new NinePatchImage(50, 50, 7, 7, npBasic_50_7);

		closeButton.setIcon(new ImageIcon(closeIcon));
		closeButton.setMargin(new Insets(0, 0, 0, 0));
		closeButton.setBackground(TRANSPARENT);
		closeButton.setBorder(emptyBorder);
		size = closeButton.getPreferredSize();
		closeButton.setBounds(WIDTH - size.width - cornerW + 5, cornerH - 5, size.width, size.height);
		closeButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				System.exit(0);
			}
		});

		modifyAccountBackButton.setText("<< Back");
		modifyAccountBackButton.setHorizontalTextPosition(JButton.CENTER);
		modifyAccountBackButton.setBackground(TRANSPARENT);
		modifyAccountBackButton.setBorder(emptyBorder);
		size = modifyAccountBackButton.getPreferredSize();
		modifyAccountBackButton.setBounds(cornerW - 5, cornerH - 5, size.width + 10, size.height + 10);
		modifyAccountBackButton.setIcon(new ImageIcon(np.getScaledImage(size.width + 10, size.height + 10)));
		modifyAccountBackButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				modifyAccountGoBack(evt);
			}
		});

		modifyNameLabel.setText("Username: ");
		modifyEmailLabel.setText("Email: ");
		modifyPasswordLabel.setText("Password: ");
		modifyVerifyPasswordLabel.setText("Re-enter password: ");
		modifyNameText.setPreferredSize(new Dimension(200, 40));
		modifyEmailText.setPreferredSize(new Dimension(200, 40));
		modifyPasswordText.setPreferredSize(new Dimension(200, 40));
		modifyVerifyPasswordText.setPreferredSize(new Dimension(200, 40));
		modifySecAnswer1Text.setPreferredSize(new Dimension(200, 40));
		modifySecAnswer2Text.setPreferredSize(new Dimension(200, 40));
		secQuestions1.setPreferredSize(new Dimension(300, 40));
		secQuestions2.setPreferredSize(new Dimension(300, 40));

		modifyEmailLabel.setText("Email: ");
		modifyEmailLabel.setHorizontalAlignment(JLabel.LEFT);
		size = modifyEmailLabel.getPreferredSize();
		modifyEmailLabel.setBounds(cornerW, cornerH + (modifyAccountBackButton.getHeight()) + 5, size.width,
				size.height);
		modifyEmailText.setBorder(BorderFactory.createLineBorder(Color.black));
		modifyEmailText.setBounds(cornerW + size.width, modifyEmailLabel.getBounds().y,
				WIDTH - (cornerW * 2) - size.width, size.height);

		modifyNameLabel.setText("Username: ");
		modifyNameLabel.setHorizontalAlignment(JLabel.LEFT);
		size = modifyNameLabel.getPreferredSize();
		modifyNameLabel.setBounds(cornerW, modifyEmailLabel.getBounds().y + (modifyEmailLabel.getHeight()) + 5,
				size.width, size.height);
		modifyNameText.setBorder(BorderFactory.createLineBorder(Color.black));
		modifyNameText.setBounds(cornerW + size.width, modifyNameLabel.getBounds().y,
				WIDTH - (cornerW * 2) - size.width, size.height);

		modifyPasswordLabel.setText("Password: ");
		modifyPasswordLabel.setHorizontalAlignment(JLabel.LEFT);
		size = modifyPasswordLabel.getPreferredSize();
		modifyPasswordLabel.setBounds(cornerW, modifyNameLabel.getBounds().y + (modifyNameLabel.getHeight()) + 5,
				size.width, size.height);
		modifyPasswordText.setBackground(FIELDGRAY);
		modifyPasswordText.setBorder(BorderFactory.createLineBorder(Color.black));
		modifyPasswordText.setBounds(cornerW + size.width, modifyPasswordLabel.getBounds().y,
				WIDTH - (cornerW * 2) - size.width, size.height);

		modifyVerifyPasswordLabel.setText("<html>Re-enter<br>Password</html>");
		modifyVerifyPasswordLabel.setFont(smallFont);
		modifyVerifyPasswordLabel.setHorizontalAlignment(JLabel.CENTER);
		modifyVerifyPasswordLabel.setBounds(cornerW,
				modifyPasswordLabel.getBounds().y + (modifyPasswordLabel.getHeight()) + 2, size.width, size.height + 6);
		modifyVerifyPasswordText.setBackground(FIELDGRAY);
		modifyVerifyPasswordText.setBorder(BorderFactory.createLineBorder(Color.black));
		modifyVerifyPasswordText.setBounds(cornerW + size.width, modifyVerifyPasswordLabel.getBounds().y + 5,
				WIDTH - (cornerW * 2) - size.width, size.height);

		secQuestions1.setBackground(FIELDGRAY);
		secQuestions1.setBorder(BorderFactory.createLineBorder(Color.black));
		secQuestions1.setBounds(cornerW,
				modifyVerifyPasswordLabel.getBounds().y + (modifyVerifyPasswordLabel.getHeight()) + 5,
				WIDTH / 2 - cornerW - 10, size.height);
		modifySecAnswer1Text.setBorder(BorderFactory.createLineBorder(Color.black));
		modifySecAnswer1Text.setBounds(WIDTH / 2 + 10, secQuestions1.getBounds().y, WIDTH / 2 - cornerW - 10,
				size.height);

		secQuestions2.setBackground(FIELDGRAY);
		secQuestions2.setBorder(BorderFactory.createLineBorder(Color.black));
		secQuestions2.setBounds(cornerW, secQuestions1.getBounds().y + (secQuestions1.getHeight()),
				WIDTH / 2 - cornerW - 10, size.height);
		modifySecAnswer2Text.setBorder(BorderFactory.createLineBorder(Color.black));
		modifySecAnswer2Text.setBounds(WIDTH / 2 + 10, secQuestions2.getBounds().y, WIDTH / 2 - cornerW - 10,
				size.height);

		modifyButton.setText("Modify");
		modifyButton.setActionCommand("modify");
		modifyButton.setHorizontalTextPosition(JButton.CENTER);
		modifyButton.setBackground(TRANSPARENT);
		modifyButton.setBorder(emptyBorder);
		size = modifyButton.getPreferredSize();
		modifyButton.setBounds(WIDTH - cornerW - size.width - 15, HEIGHT - cornerH - size.height - 5, size.width + 15,
				size.height + 10);
		modifyButton.setIcon(new ImageIcon(np.getScaledImage(size.width + 15, size.height + 10)));
		modifyButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				modifyAccount(evt);
			}
		});

		modifyAccountPanel.setLayout(null);

		modifyAccountPanel.add(closeButton);
		modifyAccountPanel.add(modifyAccountBackButton);
		modifyAccountPanel.add(modifyNameLabel);
		modifyAccountPanel.add(modifyNameText);
		modifyAccountPanel.add(modifyEmailLabel);
		modifyAccountPanel.add(modifyEmailText);
		modifyAccountPanel.add(modifyPasswordLabel);
		modifyAccountPanel.add(modifyPasswordText);
		modifyAccountPanel.add(modifyVerifyPasswordLabel);
		modifyAccountPanel.add(modifyVerifyPasswordText);
		modifyAccountPanel.add(secQuestions1);
		modifyAccountPanel.add(modifySecAnswer1Text);
		modifyAccountPanel.add(secQuestions2);
		modifyAccountPanel.add(modifySecAnswer2Text);
		modifyAccountPanel.add(modifyButton);
	}

	public void initCharSelectPanel() {
		int cornerW = createAccountPanel.getNinePatch().getCornerWidth();
		int cornerH = createAccountPanel.getNinePatch().getCornerHeight();

		loginNameText.setText("");
		loginPasswordText.setText("");
		charSelectPanel.setLayout(null);
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.NONE;

		charSelectButtons = new JButton[5];
		charLabels = new JLabel[5];
		JButton closeButton = new JButton();
		JButton[] deleteButtons = new JButton[5];
		Dimension size = null;
		NinePatchImage np = new NinePatchImage(50, 50, 7, 7, npBasic_50_7);

		for (int x = 0; x < characters.size(); x++) {
			final int selectVariable = x;
			String[] charInfo = characters.get(x).split(" ");
			characterNames[x] = charInfo[0];

			charSelectButtons[x] = new JButton();
			charSelectButtons[x].setBackground(TRANSPARENT);
			charSelectButtons[x].setBorder(emptyBorder);
			if (charInfo[1].equals("Mage")) {
				charSelectButtons[x].setIcon(new ImageIcon(magePort));
			} else if (charInfo[1].equals("Rogue")) {
				charSelectButtons[x].setIcon(new ImageIcon(roguePort));
			} else if (charInfo[1].equals("Warrior")) {
				charSelectButtons[x].setIcon(new ImageIcon(warriorPort));
			} else {
				charSelectButtons[x].setIcon(new ImageIcon(unknownPort));
			}
			charSelectButtons[x].addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent evt) {
					selectChar(selectVariable);
				}
			});
			size = charSelectButtons[x].getPreferredSize();
			charSelectButtons[x].setBounds(cornerW, cornerH + 40 + ((size.height + 3) * x), size.width + 10,
					size.height + 10);

			deleteButtons[x] = new JButton("Delete");
			deleteButtons[x].setHorizontalTextPosition(JButton.CENTER);
			deleteButtons[x].setBackground(TRANSPARENT);
			deleteButtons[x].setBorder(emptyBorder);
			size = deleteButtons[x].getPreferredSize();
			deleteButtons[x].setBounds(charSelectPanel.getWidth() - cornerW - size.width - 20,
					charSelectButtons[x].getBounds().y, size.width + 20, charSelectButtons[x].getBounds().height);
			deleteButtons[x].setIcon(new ImageIcon(np.getScaledImage(size.width + 20, size.height + 10)));
			final int selected = x;
			deleteButtons[x].addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent evt) {
					deleteChar(selected);
				}
			});

			charLabels[x] = new JLabel();
			charLabels[x].setText(charInfo[0] + ", a level " + charInfo[2] + " " + charInfo[1]);
			size = charLabels[x].getPreferredSize();
			charLabels[x].setBounds(charSelectButtons[x].getX() + charSelectButtons[x].getWidth(),
					charSelectButtons[x].getY(),
					deleteButtons[x].getX() - charSelectButtons[x].getX() - charSelectButtons[x].getWidth(),
					charSelectButtons[x].getBounds().height);

			charSelectPanel.add(charSelectButtons[x]);

			charSelectPanel.add(charLabels[x]);

			charSelectPanel.add(deleteButtons[x]);
		}

		closeButton.setIcon(new ImageIcon(closeIcon));
		closeButton.setMargin(new Insets(0, 0, 0, 0));
		closeButton.setBackground(TRANSPARENT);
		closeButton.setBorder(emptyBorder);
		size = closeButton.getPreferredSize();
		closeButton.setBounds(WIDTH - size.width - cornerW + 5, cornerH - 5, size.width, size.height);
		closeButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				System.exit(0);
			}
		});

		JButton logoutButton = new JButton();
		logoutButton.setText("Logout");
		logoutButton.setFont(smallFont);
		logoutButton.setHorizontalTextPosition(JButton.CENTER);
		logoutButton.setBackground(TRANSPARENT);
		logoutButton.setBorder(emptyBorder);
		size = logoutButton.getPreferredSize();
		logoutButton.setBounds(cornerW - 5, cornerH - 5, size.width + 20, size.height + 10);
		logoutButton.setIcon(new ImageIcon(np.getScaledImage(size.width + 20, size.height + 10)));
		logoutButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				logout(evt);
			}
		});

		JButton accountManagementButton = new JButton("Manage Account");
		accountManagementButton.setFont(smallFont);
		accountManagementButton.setHorizontalTextPosition(JButton.CENTER);
		accountManagementButton.setBackground(TRANSPARENT);
		accountManagementButton.setBorder(emptyBorder);
		size = accountManagementButton.getPreferredSize();
		accountManagementButton.setBounds(logoutButton.getBounds().x + logoutButton.getBounds().width + 10, cornerH - 5,
				size.width + 20, size.height + 10);
		accountManagementButton.setIcon(new ImageIcon(np.getScaledImage(size.width + 20, size.height + 10)));
		accountManagementButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				getSecurityInfo(evt);
			}
		});

		JButton playButton = new JButton();
		playButton.setText("Play!");
		playButton.setHorizontalTextPosition(JButton.CENTER);
		playButton.setBackground(TRANSPARENT);
		playButton.setBorder(emptyBorder);
		size = playButton.getPreferredSize();
		playButton.setBounds(WIDTH - cornerW - size.width - 20, HEIGHT - cornerH - size.height - 5, size.width + 20,
				size.height + 10);
		playButton.setIcon(new ImageIcon(np.getScaledImage(size.width + 20, size.height + 10)));
		playButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				playGame();
			}
		});

		charSelectPanel.add(closeButton);
		charSelectPanel.add(logoutButton);
		charSelectPanel.add(accountManagementButton);
		charSelectPanel.add(playButton);

		JButton[] createNewCharButtons = new JButton[5 - characters.size()];
		if (characters.size() < 5) {
			for (int x = 0; x < (5 - characters.size()); x++) {
				createNewCharButtons[x] = new JButton();
				createNewCharButtons[x].setBackground(TRANSPARENT);
				createNewCharButtons[x].setBorder(emptyBorder);
				createNewCharButtons[x].setIcon(new ImageIcon(newPort));
				createNewCharButtons[x].addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent evt) {
						createNewCharacter(evt);
					}
				});
				size = createNewCharButtons[x].getPreferredSize();
				createNewCharButtons[x].setBounds(cornerW, cornerH + 40 + ((size.height + 3) * (x + characters.size())),
						size.width + 10, size.height + 10);

				charSelectPanel.add(createNewCharButtons[x]);
			}
		}
	}

	public void initCreateNewCharacterPanel() {
		int cornerW = createAccountPanel.getNinePatch().getCornerWidth();
		int cornerH = createAccountPanel.getNinePatch().getCornerHeight();

		JPanel sexPanel = new JPanel();
		JPanel classPanel = new JPanel();

		Dimension size = null;
		NinePatchImage np = new NinePatchImage(50, 50, 7, 7, npBasic_50_7);

		//////// Name Panel
		JLabel newCharacterNameLabel = new JLabel();
		newCharacterNameText = new JTextField();
		JLabel dontUseYourAccountNameLabel = new JLabel();
		JButton createNewCharacterButton = new JButton();

		newCharacterNameText.setPreferredSize(new Dimension(300, 30));

		JButton backButton = new JButton();
		backButton.setText("<< Back");
		backButton.setHorizontalTextPosition(JButton.CENTER);
		backButton.setBackground(TRANSPARENT);
		backButton.setBorder(emptyBorder);
		size = backButton.getPreferredSize();
		backButton.setBounds(cornerW - 5, cornerH - 5, size.width + 10, size.height + 10);
		backButton.setIcon(new ImageIcon(np.getScaledImage(size.width + 10, size.height + 10)));
		backButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				backToCharSelect();
			}
		});

		newCharacterNameLabel.setText("Name: ");
		newCharacterNameLabel.setHorizontalAlignment(JLabel.LEFT);
		size = newCharacterNameLabel.getPreferredSize();
		newCharacterNameLabel.setBounds(cornerW + backButton.getBounds().width + 10, cornerH, size.width, size.height);
		newCharacterNameText.setBorder(BorderFactory.createLineBorder(Color.black));
		newCharacterNameText.setBounds(cornerW + size.width + backButton.getBounds().width + 10,
				newCharacterNameLabel.getBounds().y,
				WIDTH - (cornerW * 2) - size.width - +backButton.getBounds().width - 10, size.height);

		dontUseYourAccountNameLabel.setText("Do not use your account name");
		dontUseYourAccountNameLabel.setFont(smallFont);
		size = dontUseYourAccountNameLabel.getPreferredSize();
		dontUseYourAccountNameLabel.setBounds(newCharacterNameText.getBounds().x,
				newCharacterNameText.getBounds().y + newCharacterNameText.getBounds().height,
				newCharacterNameText.getBounds().width, size.height);

		createNewCharacterButton.setText("Create!");
		backButton.setText("<< Back");
		createNewCharacterButton.setHorizontalTextPosition(JButton.CENTER);
		createNewCharacterButton.setBackground(TRANSPARENT);
		createNewCharacterButton.setBorder(emptyBorder);
		size = createNewCharacterButton.getPreferredSize();
		createNewCharacterButton.setBounds(WIDTH - cornerW - size.width - 10, HEIGHT - cornerH - size.height - 10,
				size.width + 20, size.height + 20);
		createNewCharacterButton.setIcon(new ImageIcon(np.getScaledImage(size.width + 20, size.height + 20)));
		createNewCharacterButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				createCharacter(evt);
			}
		});

		//////// Sex Panel
		male = new JRadioButton("Male", true);
		male.setFont(smallFont);
		male.setBackground(FAKETRANS);
		female = new JRadioButton("Female");
		female.setFont(smallFont);
		female.setBackground(FAKETRANS);

		ButtonGroup sexGroup = new ButtonGroup();
		sexGroup.add(male);
		sexGroup.add(female);
		Border sexBorder = BorderFactory.createLineBorder(Color.BLACK);
		sexPanel.setBorder(BorderFactory.createTitledBorder(sexBorder, "Gender", TitledBorder.CENTER, TitledBorder.TOP,
				smallFont, Color.BLACK));
		sexPanel.setBackground(FAKETRANS);
		sexPanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.anchor = GridBagConstraints.WEST;

		c.gridx = 0;
		c.gridy = 0;
		sexPanel.add(male, c);

		c.anchor = GridBagConstraints.CENTER;
		c.gridx = 0;
		c.gridy = 1;
		sexPanel.add(female, c);
		size = sexPanel.getPreferredSize();
		sexPanel.setBounds(cornerW, cornerH + backButton.getHeight() + 10, backButton.getWidth(), size.height);

		///////// Class Panel
		warrior = new JRadioButton("Warrior", true);
		warrior.setFont(smallFont);
		warrior.setBackground(FAKETRANS);
		rogue = new JRadioButton("Rogue");
		rogue.setFont(smallFont);
		rogue.setBackground(FAKETRANS);
		mage = new JRadioButton("Mage");
		mage.setFont(smallFont);
		mage.setBackground(FAKETRANS);

		ButtonGroup classGroup = new ButtonGroup();
		classGroup.add(warrior);
		classGroup.add(rogue);
		classGroup.add(mage);

		Border classBorder = BorderFactory.createLineBorder(Color.BLACK);
		classPanel.setBorder(BorderFactory.createTitledBorder(classBorder, "Class", TitledBorder.CENTER,
				TitledBorder.TOP, smallFont, Color.BLACK));
		classPanel.setBackground(FAKETRANS);

		classPanel.setLayout(new GridBagLayout());
		c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.anchor = GridBagConstraints.WEST;

		c.gridx = 0;
		c.gridy = 0;
		classPanel.add(warrior, c);

		c.gridx = 0;
		c.gridy = 1;
		classPanel.add(rogue, c);

		c.gridx = 0;
		c.gridy = 2;
		classPanel.add(mage, c);

		size = classPanel.getPreferredSize();
		classPanel.setBounds(sexPanel.getX(), sexPanel.getY() + sexPanel.getHeight() + 10, sexPanel.getWidth(),
				size.height);

		///////// Stat Panel
		// Stat Values {pointsRemaining, Str, Dex, Con, Int, Will, Luck}
		statValues = new int[] { 10, 5, 5, 5, 5, 5, 5 };
		StatField[] statFieldArray = new StatField[6];

		pointsLeft = new JTextField(2);
		pointsLeft.setHorizontalAlignment(JTextField.CENTER);
		pointsLeft.setBackground(FAKETRANS);
		pointsLeft.setText(String.valueOf(statValues[0]));
		pointsLeft.setFont(bigFont);
		pointsLeft.setEditable(false);
		Border pointsLeftBorder = BorderFactory.createLineBorder(Color.BLACK);
		pointsLeft.setBorder(BorderFactory.createTitledBorder(pointsLeftBorder, "<html>Points<br>Left :</html>",
				TitledBorder.CENTER, TitledBorder.TOP, smallFont, Color.BLACK));
		size = pointsLeft.getPreferredSize();
		pointsLeft.setBounds(WIDTH - cornerW - size.width, sexPanel.getY() + 15, size.width, size.height);

		// int statGap = WIDTH - cornerW - newCharacterNameLabel.getX() - (WIDTH
		// -pointsLeft.getX()) - 30;
		// strengthField.panel.setBounds(newCharacterNameLabel.getX()+(statGap-size.width),
		// sexPanel.getY()+20, size.width+30, size.height-10);

		strengthField = new StatField("Strength", pointsLeft, statValues, statFieldArray, 1, 5, StatField.NEW);
		size = strengthField.panel.getPreferredSize();
		strengthField.panel.setBounds(newCharacterNameLabel.getX(), sexPanel.getY() + 20, size.width + 30,
				size.height - 10);
		statFieldArray[0] = strengthField;

		dexterityField = new StatField("Dexterity", pointsLeft, statValues, statFieldArray, 2, 5, StatField.NEW);
		size = dexterityField.panel.getPreferredSize();
		dexterityField.panel.setBounds(newCharacterNameLabel.getX(),
				strengthField.panel.getY() + strengthField.panel.getHeight(), size.width + 30, size.height - 10);
		statFieldArray[1] = dexterityField;

		constitutionField = new StatField("Constitution", pointsLeft, statValues, statFieldArray, 3, 5, StatField.NEW);
		size = constitutionField.panel.getPreferredSize();
		constitutionField.panel.setBounds(newCharacterNameLabel.getX(),
				dexterityField.panel.getY() + dexterityField.panel.getHeight(), size.width + 30, size.height - 10);
		statFieldArray[2] = constitutionField;

		intelligenceField = new StatField("Intelligence", pointsLeft, statValues, statFieldArray, 4, 5, StatField.NEW);
		size = intelligenceField.panel.getPreferredSize();
		intelligenceField.panel.setBounds(newCharacterNameLabel.getX(),
				constitutionField.panel.getY() + constitutionField.panel.getHeight(), size.width + 30,
				size.height - 10);
		statFieldArray[3] = intelligenceField;

		willpowerField = new StatField("Willpower", pointsLeft, statValues, statFieldArray, 5, 5, StatField.NEW);
		size = willpowerField.panel.getPreferredSize();
		willpowerField.panel.setBounds(newCharacterNameLabel.getX(),
				intelligenceField.panel.getY() + intelligenceField.panel.getHeight(), size.width + 30,
				size.height - 10);
		statFieldArray[4] = willpowerField;

		luckField = new StatField("Luck", pointsLeft, statValues, statFieldArray, 6, 5, StatField.NEW);
		size = luckField.panel.getPreferredSize();
		luckField.panel.setBounds(newCharacterNameLabel.getX(),
				willpowerField.panel.getY() + willpowerField.panel.getHeight(), size.width + 30, size.height - 10);
		statFieldArray[5] = luckField;

		///////// Create New Character Panel
		createNewCharacterPanel.setLayout(null);
		createNewCharacterPanel.add(backButton);
		createNewCharacterPanel.add(newCharacterNameLabel);
		createNewCharacterPanel.add(newCharacterNameText);
		createNewCharacterPanel.add(dontUseYourAccountNameLabel);
		createNewCharacterPanel.add(sexPanel);
		createNewCharacterPanel.add(classPanel);
		createNewCharacterPanel.add(pointsLeft);
		createNewCharacterPanel.add(strengthField.panel);
		createNewCharacterPanel.add(dexterityField.panel);
		createNewCharacterPanel.add(constitutionField.panel);
		createNewCharacterPanel.add(intelligenceField.panel);
		createNewCharacterPanel.add(willpowerField.panel);
		createNewCharacterPanel.add(luckField.panel);
		createNewCharacterPanel.add(createNewCharacterButton);
	}

	public void switchCards(String cardName) {
		CardLayout cl = (CardLayout) (cards.getLayout());
		cl.show(cards, cardName);
	}

	private void goToLogin(ActionEvent evt) {
		// Maybe check to make sure we're connected to server first, before
		// switching over to the login screen??
		switchCards("Login Panel");
		audioManager.playSong("resources/Sounds/LauncherMusic.wav");
	}

	private void logIn(ActionEvent evt) {
		this.username = loginNameText.getText();
		String password = String.valueOf(loginPasswordText.getPassword());
		if (username.equals("")) {
			loginPasswordText.setText("");
			JOptionPane.showMessageDialog(null, "Username field is empty.", "ERROR", JOptionPane.INFORMATION_MESSAGE);
		} else if (password.equals("")) {
			loginNameText.setText("");
			JOptionPane.showMessageDialog(null, "Password field is empty.", "ERROR", JOptionPane.INFORMATION_MESSAGE);
		} else {
			loadCharacterInfo(username);
			initCharSelectPanel();
			switchCards("Char Select Panel");
		}
	}

	private void createAccount(ActionEvent evt) {
		String username = createNameText.getText();
		String email = createEmailText.getText();
		String password = String.valueOf(createPasswordText.getPassword());
		String passwordVerify = String.valueOf(createVerifyPasswordText.getPassword());
		String securityQuestion1 = (String) secQuestions1.getSelectedItem();
		String securityQuestion2 = (String) secQuestions2.getSelectedItem();
		String securityAnswer1 = createSecAnswer1Text.getText();
		String securityAnswer2 = createSecAnswer2Text.getText();
		/*
		 * System.out.println("Creating account...");
		 * System.out.println("Username: " + username);
		 * System.out.println("Email: " + email);
		 * System.out.println("Password: " + password);
		 * System.out.println("Verified Password: " + passwordVerify);
		 * System.out.println("Security question: " + securityQuestion1);
		 * System.out.println("Security answer: " + securityAnswer1);
		 */

		if (!password.equals(passwordVerify)) {
			System.out.println("Passwords do not match");
		}
		if (!checkUsername(username)) {
			createNameText.setText("");
			JOptionPane.showMessageDialog(null, "Please enter a username with only letters.", "ERROR",
					JOptionPane.INFORMATION_MESSAGE);
		} else if (username.equals("")) {
			createNameText.setText("");
			createEmailText.setText("");
			createPasswordText.setText("");
			createVerifyPasswordText.setText("");
			createSecAnswer1Text.setText("");
			createSecAnswer2Text.setText("");

			JOptionPane.showMessageDialog(null, "Please enter a username.", "ERROR", JOptionPane.INFORMATION_MESSAGE);
		} else if (email.equals("")) {
			createNameText.setText("");
			createEmailText.setText("");
			createPasswordText.setText("");
			createVerifyPasswordText.setText("");
			createSecAnswer1Text.setText("");
			createSecAnswer2Text.setText("");

			JOptionPane.showMessageDialog(null, "Please enter an email.", "ERROR", JOptionPane.INFORMATION_MESSAGE);
		} else if (password.equals("")) {
			createNameText.setText("");
			createEmailText.setText("");
			createPasswordText.setText("");
			createVerifyPasswordText.setText("");
			createSecAnswer1Text.setText("");
			createSecAnswer2Text.setText("");

			JOptionPane.showMessageDialog(null, "Please enter a password.", "ERROR", JOptionPane.INFORMATION_MESSAGE);
		} else if (passwordVerify.equals("")) {
			createNameText.setText("");
			createEmailText.setText("");
			createPasswordText.setText("");
			createVerifyPasswordText.setText("");
			createSecAnswer1Text.setText("");
			createSecAnswer2Text.setText("");

			JOptionPane.showMessageDialog(null, "Please verify password.", "ERROR", JOptionPane.INFORMATION_MESSAGE);
		} else if (!password.equals(passwordVerify)) {
			createPasswordText.setText("");
			createVerifyPasswordText.setText("");

			JOptionPane.showMessageDialog(null, "Passwords do not match.", "ERROR", JOptionPane.INFORMATION_MESSAGE);
		} else if (securityAnswer1.equals("")) {
			JOptionPane.showMessageDialog(null, "Please enter a security answer.", "ERROR",
					JOptionPane.INFORMATION_MESSAGE);
		} else if (securityAnswer2.equals("")) {
			JOptionPane.showMessageDialog(null, "Please enter a security answer.", "ERROR",
					JOptionPane.INFORMATION_MESSAGE);
		} else {
			System.out.println("CREATE ACCOUNT WITH: " + username + "#" + password + "#" + email + "#"
					+ securityQuestion1 + "#" + securityAnswer1 + "#" + securityQuestion2 + "#" + securityAnswer2);
		}
	}

	private void modifyAccount(ActionEvent evt) {
		if (modifyButton.getActionCommand().equals("modify")) {
			String username = modifyNameText.getText();
			String email = modifyEmailText.getText();
			String password = String.valueOf(modifyPasswordText.getPassword());
			String passwordVerify = String.valueOf(modifyVerifyPasswordText.getPassword());
			String securityQuestion1 = (String) secQuestions1.getSelectedItem();
			String securityQuestion2 = (String) secQuestions2.getSelectedItem();
			String securityAnswer1 = modifySecAnswer1Text.getText();
			String securityAnswer2 = modifySecAnswer2Text.getText();

			if (!password.equals(passwordVerify)) {
				System.out.println("Passwords do not match");
			}
			if (email.equals("") || email.indexOf("@") < 0 || email.indexOf(".") < 0) {
				// modifyNameText.setText("");
				// modifyEmailText.setText("");
				// modifyPasswordText.setText("");
				// modifyVerifyPasswordText.setText("");
				// modifySecAnswer1Text.setText("");
				// modifySecAnswer2Text.setText("");

				JOptionPane.showMessageDialog(null, "Email must contain '@' and '.'.", "ERROR",
						JOptionPane.INFORMATION_MESSAGE);
			} else if (password.equals("")) {
				modifyNameText.setText("");
				modifyEmailText.setText("");
				modifyPasswordText.setText("");
				modifyVerifyPasswordText.setText("");
				modifySecAnswer1Text.setText("");
				modifySecAnswer2Text.setText("");

				JOptionPane.showMessageDialog(null, "Please enter a password.", "ERROR",
						JOptionPane.INFORMATION_MESSAGE);
			} else if (passwordVerify.equals("")) {
				modifyNameText.setText("");
				modifyEmailText.setText("");
				modifyPasswordText.setText("");
				modifyVerifyPasswordText.setText("");
				modifySecAnswer1Text.setText("");
				modifySecAnswer2Text.setText("");

				JOptionPane.showMessageDialog(null, "Please verify password.", "ERROR",
						JOptionPane.INFORMATION_MESSAGE);
			} else if (!password.equals(passwordVerify)) {
				modifyPasswordText.setText("");
				modifyVerifyPasswordText.setText("");

				JOptionPane.showMessageDialog(null, "Passwords do not match.", "ERROR",
						JOptionPane.INFORMATION_MESSAGE);
			} else if (securityAnswer1.equals("")) {
				JOptionPane.showMessageDialog(null, "Please enter a security answer.", "ERROR",
						JOptionPane.INFORMATION_MESSAGE);
			} else if (securityAnswer2.equals("")) {
				JOptionPane.showMessageDialog(null, "Please enter a security answer.", "ERROR",
						JOptionPane.INFORMATION_MESSAGE);
			} else {
				System.out.println("Modify ACCOUNT WITH: " + acctInfo[0] + "#"
						+ String.valueOf(modifyPasswordText.getPassword()) + "#" + modifyEmailText.getText() + "#"
						+ acctInfo[3] + "#" + modifySecAnswer1Text.getText() + "#" + acctInfo[5] + "#"
						+ modifySecAnswer2Text.getText());
			}
		}
	}

	private void createAccountGoBack(ActionEvent evt) {
		createNameText.setText("");
		createEmailText.setText("");
		createPasswordText.setText("");
		createVerifyPasswordText.setText("");
		createSecAnswer1Text.setText("");
		createSecAnswer2Text.setText("");
		switchCards("Login Panel");
	}

	private void modifyAccountGoBack(ActionEvent evt) {
		modifyAccountPanel.removeAll();
		switchCards("Char Select Panel");
	}

	private void goToCreateAccount(ActionEvent evt) {
		loginNameText.setText("");
		loginPasswordText.setText("");
		switchCards("Create Account Panel");
	}

	public void getSecurityInfo(ActionEvent evt) {
		System.out.println("GETSECURITYINFO#" + accountID);
	}

	public void manageAccount(String[] securityInfo) {
		// for(int i = 1; i < securityInfo.length; i++)
		// System.out.println(securityInfo[i]);
		// "securityInfo", username, password, email, secQuestion1, secAnswer1,
		// secQuestion2, secAnswer2, macAddress, lastLoginTime
		acctInfo = new String[9];
		acctInfo[0] = securityInfo[1];
		acctInfo[1] = securityInfo[2];
		acctInfo[2] = securityInfo[3];
		acctInfo[3] = securityInfo[4];
		acctInfo[4] = securityInfo[5];
		acctInfo[5] = securityInfo[6];
		acctInfo[6] = securityInfo[7];
		acctInfo[7] = securityInfo[8];
		acctInfo[8] = securityInfo[9];

		Boolean correctSecAnswer = false;
		int count = 0;
		while (!correctSecAnswer && count < 4) {
			String secAnswer = JOptionPane.showInputDialog(null, acctInfo[3], "Security Question",
					JOptionPane.INFORMATION_MESSAGE);
			if (secAnswer == null)
				break;

			if (secAnswer.equals(acctInfo[4])) {
				correctSecAnswer = true;
				updateModifyAccountPanel(acctInfo);
			} else {
				secAnswer = JOptionPane.showInputDialog(null, acctInfo[5], "Security Question",
						JOptionPane.INFORMATION_MESSAGE);
				if (secAnswer.equals(acctInfo[6])) {
					correctSecAnswer = true;
					updateModifyAccountPanel(acctInfo);
				}
				count++;
			}
		}

		if (count == 4) {
			System.out.println("LOCKACCOUNT#" + accountID);
			switchCards("Login Panel");
		}
	}

	public void updateModifyAccountPanel(String[] accountInfo) {
		initModifyAccountPanel();
		switchCards("Modify Account Panel");
		modifyNameText.setEditable(false);
		modifyNameText.setText(accountInfo[0]);
		modifyPasswordText.setText("");
		modifyVerifyPasswordText.setText("");
		modifyEmailText.setText(accountInfo[2]);
		secQuestions1.setSelectedIndex(0);
		modifySecAnswer1Text.setText(accountInfo[4]);
		secQuestions2.setSelectedIndex(1);
		modifySecAnswer2Text.setText(accountInfo[6]);

	}

	private void selectChar(int selecter) {
		System.out.println("Selecting " + characterNames[selecter]);
		charSelected = selecter;
	}

	public void createNewCharacter(ActionEvent evt) {
		createNewCharacterPanel.removeAll();
		initCreateNewCharacterPanel();
		switchCards("Create New Character Panel");
	}

	public void createCharacter(ActionEvent evt) {
		String charName = newCharacterNameText.getText();
		String chosenSex = "";
		String chosenClass = "";

		if (male.isSelected())
			chosenSex = "Male";
		else
			chosenSex = "Female";

		if (rogue.isSelected())
			chosenClass = "Rogue";
		else if (mage.isSelected())
			chosenClass = "Mage";
		else
			chosenClass = "Warrior";

		String str = strengthField.getStringValue();
		String dex = dexterityField.getStringValue();
		String con = constitutionField.getStringValue();
		String intel = intelligenceField.getStringValue();

		// name, accountID, class, gender, str, dex, con, int, wil, lck
		if (charName.equals("")) {
			newCharacterNameText.setText("");
			JOptionPane.showMessageDialog(null, "Please enter a character name.", "ERROR",
					JOptionPane.INFORMATION_MESSAGE);
		} else if (!checkUsername(charName)) {
			newCharacterNameText.setText("");
			JOptionPane.showMessageDialog(null, "Please enter a character name with only letters.", "ERROR",
					JOptionPane.INFORMATION_MESSAGE);
		} else {
			String pointsToSpend = pointsLeft.getText();
			dao.createCharacter(username, charName, chosenClass, pointsToSpend, str, dex, con, intel);
		}
	}

	public void logout(ActionEvent evt) {
		charSelectPanel.removeAll();
		for (int i = 0; i < characterNames.length; i++)
			characterNames[i] = "";
		switchCards("Login Panel");
		loginNameText.setText("");
		loginPasswordText.setText("");
	}

	public void usernameNotFound() {
		loginNameText.setText("");
		loginPasswordText.setText("");
		JOptionPane.showMessageDialog(null, "Username not found.", "ERROR", JOptionPane.INFORMATION_MESSAGE);
	}

	public void incorrectPassword() {
		loginPasswordText.setText("");
		JOptionPane.showMessageDialog(null, "Incorrect password.", "ERROR", JOptionPane.INFORMATION_MESSAGE);
	}

	public void accountLocked() {
		loginNameText.setText("");
		loginPasswordText.setText("");
		JOptionPane.showMessageDialog(null, "Account has been locked. Please try 'Forgot Password'", "ERROR",
				JOptionPane.INFORMATION_MESSAGE);
	}

	public void accountBanned() {
		loginNameText.setText("");
		loginPasswordText.setText("");
		JOptionPane.showMessageDialog(null, "Account has been banned.", "ERROR", JOptionPane.INFORMATION_MESSAGE);
	}

	public void moreThanOneAccountFound() {
		loginNameText.setText("");
		loginPasswordText.setText("");
		JOptionPane.showMessageDialog(null, "More than one account with that username, contact admins.", "ERROR",
				JOptionPane.INFORMATION_MESSAGE);
	}

	public void accountAlreadyExists() {
		JOptionPane.showMessageDialog(null, "Account already exists.", "ERROR", JOptionPane.INFORMATION_MESSAGE);
	}

	public void accountCreated() {
		switchCards("Login Panel");
		loginNameText.setText("");
		loginPasswordText.setText("");
	}

	public void loadCharacterInfo(String user) {
		charSelectPanel.removeAll();
		if (characters.size() > 0) {
			characters.clear();
		}
		characters = dao.getCharactersForUser(user);
	}

	public void updateAcctInfo(ActionEvent evt) {

	}

	public void backToCharSelect() {
		switchCards("Char Select Panel");
	}

	public void deleteChar(int x) {
		String delete = JOptionPane.showInputDialog(null, "Type \"DELETE\" in all caps to confirm deletion.",
				"Confirm Delete", JOptionPane.INFORMATION_MESSAGE);
		if (delete != null && delete.equals("DELETE"))
			dao.deleteCharacter(username, characterNames[x]);
	}

	public void playGame() {
		String playerInfo = dao.getCharacterInfo(characterNames[charSelected]);
		intoGame(playerInfo);
	}

	public void intoGame(String playerInfo) {
		audioManager.stopSong();
		JFrame window = new JFrame("SWE Game");
		window.setContentPane(new GamePanel(playerInfo));
		window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		window.setResizable(false);
		window.pack();
		window.setVisible(true);
	}

	public void connectToServer() {
		dao = new Dao();
		System.out.println("Connected!");
		connectPanel.requestFocus();
	}

	public boolean checkUsername(String name) {
		char[] charArray = name.toCharArray();
		for (char c : charArray) {
			if (!Character.isLetter(c)) {
				return false;
			}
		}
		return true;
	}

	public void forgotPassword(ActionEvent e) {
		String username = JOptionPane.showInputDialog(null, "What is your username?", "Forgot Password",
				JOptionPane.INFORMATION_MESSAGE);
		if (username != null)
			System.out.println("FORGOTPASSWORD#" + username);
	}

	public void close() {
		System.exit(0);
	}
}