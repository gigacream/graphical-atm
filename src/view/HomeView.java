package view;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import controller.ViewManager;
import data.Database;
import model.BankAccount;

@SuppressWarnings("serial")
public class HomeView extends JPanel implements ActionListener {
	
	private ViewManager manager;		// manages interactions between the views, model, and database
	private JButton powerButton;
	private JButton logoutButton;
	private JButton depositButton;
	private JButton withdrawButton;
	private JButton transferButton;
	private JButton informationButton;
	private JButton closeButton;
	private JLabel welcomeLabel;
	private JLabel accountNumberLabel;
	private JLabel balanceLabel;
	
	/**
	 * Constructs an instance (or objects) of the HomeView class.
	 * 
	 * @param manager
	 */
	
	public HomeView(ViewManager manager) {
		super();
		
		this.manager = manager;
		this.welcomeLabel = new JLabel("", SwingConstants.CENTER);
		this.accountNumberLabel = new JLabel("", SwingConstants.CENTER);
		this.balanceLabel = new JLabel("", SwingConstants.CENTER);
		initialize();
	}
	
	///////////////////// PRIVATE METHODS /////////////////////////////////////////////
	
	/*
	 * Initializes the HomeView components.
	 */
	
	
	private void initialize() {
		this.setLayout(null);
		
		this.add(new javax.swing.JLabel("HomeView", javax.swing.SwingConstants.CENTER));
		
		initPowerButton();
		initWelcomeLabel();
		initLogoutButton();
		initDepositButton();
		initWithdrawButton();
		initTransferButton();
		initAccountNumberLabel();
		initBalanceLabel();
		initInformationButton();
		initCloseButton();
	}
	public void initWelcomeLabel() {
		welcomeLabel.setBounds(150, 5, 200, 35);
		welcomeLabel.setFont(new Font("DialogInput", Font.ITALIC, 14));
		welcomeLabel.setText(manager.welcome());
		
		
		this.add(welcomeLabel);
	}
	
	public void initAccountNumberLabel() {
		accountNumberLabel.setBounds(150, 20, 200, 35);
		accountNumberLabel.setFont(new Font("DialogInput", Font.ITALIC, 14));
		accountNumberLabel.setText(manager.showAccountNumber());
		
		
		this.add(accountNumberLabel);
	}
	
	public void initBalanceLabel() {
		balanceLabel.setBounds(150, 35, 200, 35);
		balanceLabel.setFont(new Font("DialogInput", Font.ITALIC, 14));
		balanceLabel.setText(manager.showBalance());
		
		
		this.add(balanceLabel);
	}
	
	private void initPowerButton() {
		powerButton = new JButton();
		powerButton.setBounds(5, 5, 50, 50);
		powerButton.addActionListener(this);
		
		try {
			Image image = ImageIO.read(new File("images/power-off.png"));
			powerButton.setIcon(new ImageIcon(image));
		} catch (Exception e) {
			powerButton.setText("OFF");
		}
		
		this.add(powerButton);
	}
	
	private void initLogoutButton() {
		logoutButton = new JButton("Log Out");
		logoutButton.setBounds(150, 340, 200, 40);
		logoutButton.addActionListener(this);
		
		this.add(logoutButton);
	}
	
	private void initCloseButton() {
		closeButton = new JButton("Close Account");
		closeButton.setBounds(150, 400, 200, 40);
		closeButton.addActionListener(this);
		
		this.add(closeButton);
	}
	
	private void initDepositButton() {
		depositButton = new JButton("Deposit");
		depositButton.setBounds(150, 70, 200, 40);
		depositButton.addActionListener(this);
		this.add(depositButton);
	}
	
	private void initWithdrawButton() {
		withdrawButton = new JButton("Withdraw");
		withdrawButton.setBounds(150, 140, 200, 40);
		withdrawButton.addActionListener(this);
		this.add(withdrawButton);
	}
	
	private void initTransferButton() {
		transferButton = new JButton("Transfer");
		transferButton.setBounds(150, 210, 200, 40);
		transferButton.addActionListener(this);
		this.add(transferButton);
	}
	
	private void initInformationButton() {
		informationButton = new JButton("View / Edit Information");
		informationButton.setBounds(150, 280, 200, 40);
		informationButton.addActionListener(this);
		this.add(informationButton);
	}
	
	/*
	 * HomeView is not designed to be serialized, and attempts to serialize will throw an IOException.
	 * 
	 * @param oos
	 * @throws IOException
	 */
	
	private void writeObject(ObjectOutputStream oos) throws IOException {
		throw new IOException("ERROR: The HomeView class is not serializable.");
	}
	
	///////////////////// OVERRIDDEN METHODS //////////////////////////////////////////
	
	/*
	 * Responds to button clicks and other actions performed in the HomeView.
	 * 
	 * @param e
	 */
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		if (source.equals(logoutButton)) {
			manager.logout();
		}
		else if (source.equals(closeButton)) {
			try {
				manager.close();
			}
			catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		else if (source.equals(depositButton)) {
			manager.switchTo(ATM.DEPOSIT_VIEW);
		}
		else if (source.equals(withdrawButton)) {
			manager.switchTo(ATM.WITHDRAW_VIEW);
		}
		else if (source.equals(transferButton)) {
			manager.switchTo(ATM.TRANSFER_VIEW);
		}
		else if (source.equals(informationButton)) {
			manager.switchTo(ATM.INFORMATION_VIEW);
			manager.initInformationAccount();
		}
		else if (source.equals(powerButton)) {
			manager.shutdown();
		}
		else {
			System.err.println("ERROR: Action command not found (" + e.getActionCommand() + ")");
		}
	}
}
