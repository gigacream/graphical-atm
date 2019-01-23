package data;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import model.BankAccount;

public class Database {
	
	/*
	 * Field names for database table: accounts.
	 */
	
	public static final String ACCOUNT_NUMBER = "account_number";
	public static final String PIN = "pin";
	public static final String BALANCE = "balance";
	public static final String LAST_NAME = "last_name";
	public static final String FIRST_NAME = "first_name";
	public static final String DOB = "dob";
	public static final String PHONE = "phone";
	public static final String STREET_ADDRESS = "street_address";
	public static final String CITY = "city";
	public static final String STATE = "state";
	public static final String ZIP = "zip";
	public static final String STATUS = "status";
	
	private Connection conn;				// a connection to the database
	private Statement stmt;					// a SQL statement without parameters
	private PreparedStatement prepStmt;		// a SQL statement with parameters
	private ResultSet rs;					// a result set for all SQL queries
	private DatabaseMetaData meta;			// metadata about the database
	
	/**
	 * Constructs an instance (or object) of the Database class.
	 */
	
	public Database() {
		this.connect();
		this.setup();
	}
	
	///////////////////// INSTANCE METHODS ////////////////////////////////////////////
	
	/**
	 * Retrieves an existing account by account number and PIN.
	 * 
	 * @param accountNumber
	 * @param pin
	 * @return
	 */
	
	public BankAccount getAccount(long accountNumber, int pin) {
		try {
			prepStmt = conn.prepareStatement("SELECT * FROM accounts WHERE account_number = ? AND pin = ?");
			prepStmt.setLong(1, accountNumber);
			prepStmt.setInt(2, pin);
			
			rs = prepStmt.executeQuery();
			if (rs.next()) {
				return new BankAccount(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * Retrieves an existing account by account number.
	 * 
	 * @param accountNumber
	 * @return
	 */
	
	public BankAccount getAccount(long accountNumber) {
		try {			
			prepStmt = conn.prepareStatement("SELECT * FROM accounts WHERE account_number = ?");
			prepStmt.setLong(1, accountNumber);
			
			rs = prepStmt.executeQuery();
			if (rs.next()) {
				return new BankAccount(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * Inserts an account into the database.
	 * 
	 * @param account
	 * @return true if the insert is successful; false otherwise.
	 */
	
	public boolean insertAccount(BankAccount account) {		
		try {
			prepStmt = conn.prepareStatement("INSERT INTO accounts VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");		
			prepStmt.setLong(1, account.getAccountNumber());
			prepStmt.setInt(2, account.getUser().getPin());
			prepStmt.setDouble(3, account.getBalance());
			prepStmt.setString(4, account.getUser().getLastName());
			prepStmt.setString(5, account.getUser().getFirstName());
			prepStmt.setInt(6, account.getUser().getDob());
			prepStmt.setLong(7, account.getUser().getPhone());
			prepStmt.setString(8, account.getUser().getStreetAddress());
			prepStmt.setString(9, account.getUser().getCity());
			prepStmt.setString(10, account.getUser().getState());
			prepStmt.setString(11, account.getUser().getZip());
			prepStmt.setString(12, String.valueOf(account.getStatus()));
			
			prepStmt.executeUpdate();
			
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	/**
	 * Performs a soft delete of an account by setting the status to closed.
	 * 
	 * @param account
	 * @return true if the transaction is successful; false otherwise.
	 */
	
	public boolean closeAccount(BankAccount account) {
		try {
			prepStmt = conn.prepareStatement("UPDATE accounts SET status = ? WHERE account_number = ?");		
			prepStmt.setString(1, "N");
			prepStmt.setLong(2, account.getAccountNumber());
			
			prepStmt.executeUpdate();
			prepStmt.close();
			
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	/**
	 * Updates all potentially edited fields (i.e., PIN, account balance, phone number,
	 * street address, city, state, and zip code).
	 * 
	 * @param account
	 * @return true if the transaction is successful; false otherwise.
	 */
	
	public boolean updateAccount(BankAccount account) {
		try {
			prepStmt = conn.prepareStatement(		// all editable fields included in update statement
				"UPDATE accounts SET " +
					"pin = ?, " +
					"balance = ?, " +
					"phone = ?, " +
					"street_address = ?, " +
					"city = ?, " +
					"state = ?, " +
					"zip = ? " +
				"WHERE account_number = ?"
			);
			
			prepStmt.setInt(1, account.getUser().getPin());
			prepStmt.setDouble(2, account.getBalance());
			prepStmt.setLong(3, account.getUser().getPhone());
			prepStmt.setString(4, account.getUser().getStreetAddress());
			prepStmt.setString(5, account.getUser().getCity());
			prepStmt.setString(6, account.getUser().getState());
			prepStmt.setString(7, account.getUser().getZip());
			prepStmt.setLong(8, account.getAccountNumber());
			
			prepStmt.executeUpdate();
			prepStmt.close();
			
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	/**
	 * Retrieves the largest account number that exists in the database.
	 * 
	 * @return
	 */
	
	public long getMaxAccountNumber() {
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery("SELECT MAX(account_number) FROM accounts");
			
			if (rs.next()) {
				return rs.getLong(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return -1;
	}
	
	/**
	 * Shuts down the database, releasing all allocated resources.
	 */
	
	public void shutdown() {
		try {
			if (rs != null) rs.close();
			if (stmt != null) stmt.close();
			if (prepStmt != null) prepStmt.close();
			if (conn != null) conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	///////////////////// PRIVATE METHODS /////////////////////////////////////////////
	
	/*
	 * Establishes a connection to the database.
	 */
	
	private void connect() {
		Properties props = new Properties();
        props.put("user", "user1");
        props.put("password", "user1");

        try {
			conn = DriverManager.getConnection("jdbc:derby:atm;create=true", props);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * Performs initial database setup.
	 */
	
	private void setup() {		
		createAccountsTable();
		insertDefaultAccount();
	}
	
	/*
	 * Creates the initial accounts table. This will only be done once during initial setup.
	 */
	
	private void createAccountsTable() {
		try {
			meta = conn.getMetaData();
			rs = meta.getTables(null, "USER1", "ACCOUNTS", null);
			
			if (!rs.next()) {
				stmt = conn.createStatement();
				
				stmt.execute(
					"CREATE TABLE accounts (" +
						"account_number BIGINT PRIMARY KEY, " +
						"pin INT, " +
						"balance FLOAT, " +
						"last_name VARCHAR(20), " +
						"first_name VARCHAR(15), " +
						"dob INT, " +
						"phone BIGINT, " +
						"street_address VARCHAR(30), " +
						"city VARCHAR(30), " +
						"state VARCHAR(2), " +
						"zip VARCHAR(5), " +
						"status CHAR(1)" +
					")"
				);
			}			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * Inserts a default account into the database. This will only be done once during initial setup.
	 */
	
	private void insertDefaultAccount() {
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery("SELECT COUNT(*) FROM accounts");
			
			if (rs.next() && rs.getInt(1) == 0) {
				PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO accounts VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
				
				insertStmt.setLong(1, 100000001L);
				insertStmt.setInt(2, 1234);
				insertStmt.setDouble(3, 0.00);
				insertStmt.setString(4, "Wilson");
				insertStmt.setString(5, "Ryan");
				insertStmt.setInt(6, 19700707);
				insertStmt.setLong(7, 55555555555L);
				insertStmt.setString(8, "1776 Raritan Road");
				insertStmt.setString(9, "Scotch Plains");
				insertStmt.setString(10, "NJ");
				insertStmt.setString(11, "07065");
				insertStmt.setString(12, "Y");
				
				insertStmt.executeUpdate();
				insertStmt.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
