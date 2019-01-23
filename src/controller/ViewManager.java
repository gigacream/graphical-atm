package controller;

import java.awt.CardLayout;
import java.awt.Container;
import java.awt.Font;
import java.security.InvalidParameterException;
import java.text.NumberFormat;
import java.util.Locale;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import data.Database;
import model.BankAccount;
import view.ATM;
import view.HomeView;
import view.InformationView;
import view.LoginView;

public class ViewManager {
	
	private Container views;				// the collection of all views in the application
	private Database database;					// a reference to the database

	private BankAccount account;			// the user's bank account
	private BankAccount destination;		// an account to which the user can transfer funds
	
	public static final String NL = System.getProperty("line.separator");  
	/**
	 * Constructs an instance (or object) of the ViewManager class.
	 * 
	 * @param layout
	 * @param container
	 */
	
	public ViewManager(Container views) {
		this.views = views;
		this.database = new Database();
	}
	
	///////////////////// INSTANCE METHODS ////////////////////////////////////////////
	
	/**
	 * Routes a login request from the LoginView to the Database.
	 * 
	 * @param accountNumber
	 * @param pin
	 */
	public void login(String accountNumber, char[] pin) {
		LoginView lv = (LoginView) views.getComponents()[ATM.LOGIN_VIEW_INDEX];
		
		
		try {
			account = database.getAccount(Long.valueOf(accountNumber), Integer.valueOf(new String(pin)));
			
			if (account == null || account.getStatus() == 'N') {
				lv.updateErrorMessage("Invalid account number and/or PIN.");
			} else {
				switchTo(ATM.HOME_VIEW);
				showLabels();
			}
		} catch (NumberFormatException e) {
			lv.updateErrorMessage("Account numbers and PINs don't have letters.");
		}
	}
	
	/**
	 * Switches the active (or visible) view upon request.
	 * 
	 * @param view
	 */
	public void showLabels() {
		HomeView hv = (HomeView) views.getComponents()[ATM.HOME_VIEW_INDEX];
		hv.initWelcomeLabel();
		hv.initAccountNumberLabel();
		hv.initBalanceLabel();
	}
	
	public void initInformationAccount() {
		InformationView iv = (InformationView) views.getComponents()[ATM.INFORMATION_VIEW_INDEX];
		iv.initAccount(account);
		iv.initInformation();
	}
	
	public void insertAccount() {
		database.insertAccount(account);
	}
	
	public void switchTo(String view) {
		((CardLayout) views.getLayout()).show(views, view);
	}
	
	/**
	 * Routes a shutdown request to the database before exiting the application. This
	 * allows the database to clean up any open resources it used.
	 */
	
	public void logout() {
		LoginView lv = (LoginView) views.getComponents()[ATM.LOGIN_VIEW_INDEX];
		try {			
			int choice = JOptionPane.showConfirmDialog(
				views,
				"Are you sure?",
				"Log Out of ATM",
				JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE
			);
			
			if (choice == 0) {
				this.setAccount(null);
				this.setDestination(null);
				switchTo(ATM.LOGIN_VIEW);
				lv.removeAll();
				lv.initialize();
				lv.updateErrorMessage("");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void close() {
		LoginView lv = (LoginView) views.getComponents()[ATM.LOGIN_VIEW_INDEX];
		try {			
			int choice = JOptionPane.showConfirmDialog(
				views,
				"Are you sure you want to close your account?",
				"Close Account",
				JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE
			);
			
			if (choice == 0) {
				if (database.closeAccount(account)) {
					this.setAccount(null);
					this.setDestination(null);
					switchTo(ATM.LOGIN_VIEW);
					lv.removeAll();
					lv.initialize();
					lv.updateErrorMessage("");
				}
				else {
					throw new Exception();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void shutdown() {
		try {			
			int choice = JOptionPane.showConfirmDialog(
				views,
				"Are you sure?",
				"Shutdown ATM",
				JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE
			);
			
			if (choice == 0) {
				database.shutdown();
				System.exit(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public String welcome() {
		try {
			String firstName = account.getUser().getFirstName();
			String lastName = account.getUser().getLastName();
			String welcome =  "Welcome, " + firstName + " " + lastName + "!";
			
			return welcome;
		}
		catch (NullPointerException e) {
			return "Null Name";
		}
	}
	
	public String showAccountNumber() {
		try {
			return "Account Number: " + account.getAccountNumber();
		}
		catch (NullPointerException e) {
			return "Null Account Number";
		}
		
	}
	
	public String showBalance() {
		try {
			return "Balance: " + NumberFormat.getCurrencyInstance(Locale.US).format(account.getBalance());
		}
		catch (NullPointerException e) {
			return "Invalid Balance";
		}
	}
	
	public void transfer(long accountNumber, double amount) {
		try {
			this.destination = database.getAccount(accountNumber);
		}
		catch (NullPointerException e) {
			this.destination = null;
		}
		
		int status =  account.transfer(destination, amount);
		
		switch(status) {
			case ATM.ACCOUNT_NOT_FOUND:
				throw new InvalidParameterException("Account was not found");
			case ATM.INVALID_AMOUNT:
				throw new InvalidParameterException("Please enter a valid amount");
			case ATM.INSUFFICIENT_FUNDS:
				throw new InvalidParameterException("You have insufficient funds to complete this transfer");
			case ATM.SUCCESS:
				database.updateAccount(this.account);
				database.updateAccount(this.destination);
				break;
			default:
				throw new InvalidParameterException("Something went wrong. Please try again.");
		}
	}
	
	public void deposit(double amount) {
		int status = account.deposit(amount);
		
		switch(status) {
			case ATM.INVALID_AMOUNT:
				throw new InvalidParameterException("Please enter a valid amount");
			case ATM.SUCCESS:
				database.updateAccount(account);
				break;
			default:
				throw new InvalidParameterException("Something went wrong. Please try again.");
		}
	}
	
	public void withdraw(double amount) {
		int status = account.withdraw(amount);
		switch(status) {
			case ATM.INVALID_AMOUNT:
				throw new InvalidParameterException("Please enter a valid amount");
			case ATM.INSUFFICIENT_FUNDS:
				throw new InvalidParameterException("You have insufficient funds to complete this withdrawl");
			case ATM.SUCCESS:
				database.updateAccount(account);
				break;
			default:
				throw new InvalidParameterException("Something went wrong. Please try again.");
		}
	}
	
	public BankAccount getAccount() {
		return account;
	}

	public void setAccount(BankAccount account) {
		this.account = account;
	}

	public void setDestination(BankAccount destination) {
		this.destination = destination;
	}
	public Database getDatabase() {
		return database;
	}
}
