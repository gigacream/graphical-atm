package view;
import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.security.InvalidParameterException;

import controller.ViewManager;
import model.BankAccount;
import model.User;

@SuppressWarnings("serial")
public class CreateView extends JPanel implements ActionListener {
	
	private ViewManager manager;		// manages interactions between the views, model, and database
	private JTextField firstNameField;
	private JTextField lastNameField;
	private JComboBox<Integer> monthField;
	private JComboBox<Integer> dayField;
	private JComboBox<Integer> yearField;
	private JTextField phoneNumber1Field;
	private JTextField phoneNumber2Field;
	private JTextField phoneNumber3Field;
	private JTextField streetAddressField;
	private JTextField cityField;
	private JComboBox<String> stateField;
	private JTextField postalField;
	private JPasswordField pinField;
	private JButton finalCreateButton;
	private JButton powerButton;
	private JButton cancelButton;
	private JLabel errorMessageLabel;
	
	private String firstName;
	private String lastName;
	private int dob;
	private long phone;
	String streetAddress;
	String city;
	String state;
	String postalCode;
	int pin;
	
	private BankAccount account;
	private User user;
	
	/**
	 * Constructs an instance (or object) of the CreateView class.
	 * 
	 * @param manager
	 */
	
	public CreateView(ViewManager manager) {
		super();
		
		this.manager = manager;
		this.errorMessageLabel = new JLabel("", SwingConstants.CENTER);
		initialize();
	}
	
	public void updateErrorMessage(String errorMessage) {
		errorMessageLabel.setText(errorMessage);
	}
	
	///////////////////// PRIVATE METHODS /////////////////////////////////////////////
	
	/*
	 * Initializes the CreateView components.
	 */
	
	private void initialize() {
		initPowerButton();
		this.setLayout(null);
		
		initFirstNameField();
		initLastNameField();
		initBirthDateField();
		initPhoneNumberInput();
		initAddressField();
		initPinField();
		initFinalCreateButton();
		initErrorMessageLabel();
		initCancelButton();
		
	}
	private void initFirstNameField() {
		
		JLabel label = new JLabel("First Name");
		label.setLayout(null);
		label.setBounds(50, 25, 100, 35);
		label.setLabelFor(firstNameField);
		label.setFont(new Font("DialogInput", Font.BOLD, 14));
		
		firstNameField = new JTextField(20);
		firstNameField.setBounds(160, 25, 240, 35);
		
		limitSize(firstNameField, 15);
		
		this.add(label);
		this.add(firstNameField);
		
	}
	
	private void initLastNameField() {
		JLabel label = new JLabel("Last Name");
		label.setBounds(50, 70, 100, 35);
		label.setLabelFor(lastNameField);
		label.setFont(new Font("DialogInput", Font.BOLD, 14));
		
		lastNameField = new JTextField(20);
		lastNameField.setBounds(160, 70, 240, 35);
		
		limitSize(lastNameField, 20);
		
		this.add(label);
		this.add(lastNameField);
	}
	
	private void initBirthDateField() {
		JLabel label = new JLabel("DOB (MM/DD/YYYY)");
		label.setBounds(50, 110, 200, 35);
		
		label.setFont(new Font("DialogInput", Font.BOLD, 14));
		/*String[] month = {"January", "February", "March", "April", "May", 
				"June", "July", "August", "September", "October", "November", "December"};*/
		Integer[] month = new Integer[12];
		for (int i = 0; i < 12; i++) {
			month[i] = i + 1;
		}
		
		monthField = new JComboBox<Integer>(month);
		monthField.setBounds(200, 110, 50, 35);
		
		this.add(label);
		this.add(monthField);
		
		Integer[] day = new Integer[31];
		for (int i = 0; i < 31; i++) {
			day[i] = i + 1;
		}
		
		dayField = new JComboBox<Integer>(day);
		dayField.setBounds(270, 110, 50, 35);
		this.add(dayField);
		
		Integer[] year = new Integer[120];
		
		for (int i = 0; i < 120; i++) {
			year[i] = 1900 + i;
		}
		
		yearField = new JComboBox<Integer>(year);
		yearField.setBounds(330, 110, 70, 35);
		this.add(yearField);
		
	}
	
	private void initPhoneNumberInput() {
		JLabel label = new JLabel("Phone Number");
		label.setBounds(50, 150, 100, 35);
		label.setFont(new Font("DialogInput", Font.BOLD, 14));
		
		phoneNumber1Field = new JTextField(3);

		phoneNumber1Field.setBounds(160, 150, 80, 35);
		
		limitToIntegers(phoneNumber1Field);
		limitSize(phoneNumber1Field, 3);
		
		this.add(label);
		this.add(phoneNumber1Field);
		
		phoneNumber2Field = new JTextField(3);

		phoneNumber2Field.setBounds(240, 150, 80, 35);
		
		limitToIntegers(phoneNumber2Field);
		limitSize(phoneNumber2Field, 3);
		
		this.add(phoneNumber2Field);
		
		
		phoneNumber3Field = new JTextField(4);

		phoneNumber3Field.setBounds(320, 150, 80, 35);
		
		limitToIntegers(phoneNumber3Field);
		limitSize(phoneNumber3Field, 4);
		
		this.add(label);
		this.add(phoneNumber3Field);
	
	}
	
	private void initAddressField() {
		JLabel streetLabel = new JLabel("Address");
		streetLabel.setBounds(50, 195, 100, 35);
		streetLabel.setLabelFor(streetAddressField);
		streetLabel.setFont(new Font("DialogInput", Font.BOLD, 14));
		
		streetAddressField = new JTextField(20);
		streetAddressField.setBounds(160, 195, 240, 35);
		limitSize(streetAddressField, 30);
		
		this.add(streetLabel);
		this.add(streetAddressField);
		
		JLabel cityLabel = new JLabel("City");
		cityLabel.setBounds(50, 240, 100, 35);
		cityLabel.setLabelFor(cityField);
		cityLabel.setFont(new Font("DialogInput", Font.BOLD, 14));
		
		cityField = new JTextField(20);
		cityField.setBounds(100, 240, 150, 35);
		
		limitSize(cityField, 30);
		
		this.add(cityLabel);
		this.add(cityField);
		
		JLabel stateLabel = new JLabel("State");
		stateLabel.setBounds(270, 240, 100, 35);
		
		
		stateLabel.setFont(new Font("DialogInput", Font.BOLD, 14));
		
		String[] states = 
			{
				"AL", 
				"AK",
				"AZ", 
				"AR", 
				"CA",
				"CO", 
				"CT", 
				"DC", 
				"DE",
				"FL", 
				"GA", 
				"HI", 
				"ID", 
				"IL",
				"IN",
				"IA", 
				"KS", 
				"KY", 
				"LA", 
				"ME",
				"MD",
				"MA", 
				"MI", 
				"MN", 
				"MS",
				"MO",
				"MT", 
				"NE", 
				"NV",
				"NH",
				"NJ", 
				"NM",
				"NY",
				"NC",
				"ND",
				"OH",
				"OK",
				"OR",
				"PA",
				"PR",
				"RI", 
				"SC",
				"SD",
				"TN",
				"TX",
				"UT",
				"VT",
				"VA",
				"WA", 
				"WV", 
				"WI", 
				"WY"};
		stateField = new JComboBox<>(states);
		stateField.setBounds(320, 240, 60, 35);
		this.add(stateLabel);
		this.add(stateField);
		
		JLabel postalLabel = new JLabel("Postal Code");
		postalLabel.setBounds(50, 290, 100, 35);
		postalLabel.setLabelFor(postalField);
		postalLabel.setFont(new Font("DialogInput", Font.BOLD, 14));
		
		postalField = new JTextField(20);
		postalField.setBounds(160, 290, 100, 35);
		
		limitSize(postalField, 5);
		
		this.add(postalLabel);
		this.add(postalField);
	}
	
	private void initPinField(){
		JLabel label = new JLabel("PIN");
		label.setBounds(50, 340, 95, 35);
		label.setLabelFor(pinField);
		label.setFont(new Font("DialogInput", Font.BOLD, 14));
		
		pinField = new JPasswordField(20);
		pinField.setBounds(160, 340, 100, 35);
		
		limitToIntegers(pinField);
		limitSize(pinField, 4);
		
		this.add(label);
		this.add(pinField);
		
	}
	
	/*
	 * CreateView is not designed to be serialized, and attempts to serialize will throw an IOException.
	 * 
	 * @param oos
	 * @throws IOException
	 */
	private void writeObject(ObjectOutputStream oos) throws IOException {
		throw new IOException("ERROR: The CreateView class is not serializable.");
	}
	
	///////////////////// OVERRIDDEN METHODS //////////////////////////////////////////
	
	/*
	 * Responds to button clicks and other actions performed in the CreateView.
	 * 
	 * @param e
	 */
	
	private void initErrorMessageLabel() {
		errorMessageLabel.setBounds(0, 420, 500, 35);
		errorMessageLabel.setFont(new Font("DialogInput", Font.ITALIC, 14));
		errorMessageLabel.setForeground(Color.RED);
		
		this.add(errorMessageLabel);
	}
	
	private void initCancelButton() {
		cancelButton = new JButton("Cancel");
		cancelButton.setBounds(50, 390, 120, 40);
		clearFields(cancelButton);
		
		cancelButton.addActionListener(this);
		this.add(cancelButton);
	}
	
	private void initFinalCreateButton() {
		
		finalCreateButton = new JButton("Create Account");
		finalCreateButton.setBounds(180, 390, 200, 40);
		clearFields(finalCreateButton);
		finalCreateButton.addActionListener(this);
		
		this.add(finalCreateButton);
	}
	
	private void initPowerButton() {
		powerButton = new JButton();
		powerButton.setBounds(420, 5, 50, 50);
		clearFields(powerButton);
		powerButton.addActionListener(this);
		
		try {
			Image image = ImageIO.read(new File("images/power-off.png"));
			powerButton.setIcon(new ImageIcon(image));
		} catch (Exception e) {
			powerButton.setText("OFF");
		}
		
		this.add(powerButton);
	}
	
	public void clearFields(JButton button) {
		button.addActionListener(new ActionListener(){
		    public void actionPerformed(ActionEvent e){
		        firstNameField.setText("");
		        lastNameField.setText("");
		        monthField.setSelectedIndex(0);
		        dayField.setSelectedIndex(0);
		        yearField.setSelectedIndex(0);
		        phoneNumber1Field.setText("");
		        phoneNumber2Field.setText("");
		        phoneNumber3Field.setText("");
		        streetAddressField.setText("");
		        cityField.setText("");
		        stateField.setSelectedIndex(0);
		        postalField.setText("");
		        pinField.setText("");
		        //textfield.setText(null); //or use this
		    }
		});
	}
	public void limitToIntegers(JTextField keyText) {
		keyText.addKeyListener(new KeyAdapter() {
		    public void keyTyped(KeyEvent e) {
		      char c = e.getKeyChar();
		      if (!((c >= '0') && (c <= '9') ||
		         (c == KeyEvent.VK_BACK_SPACE) ||
		         (c == KeyEvent.VK_DELETE))) {
		        getToolkit().beep();
		        e.consume();
		      }
		    }
		  });
	}
	public void limitSize(JTextField keyText, int n) {
		keyText.addKeyListener(new KeyAdapter() {
		    public void keyTyped(KeyEvent e) { 
		        if (keyText.getText().length() >= n ) // limit textfield to n characters
		            e.consume(); 
		    }  
		});
	}
	
	@SuppressWarnings("deprecation")
	private void setValues() throws NumberFormatException {
		
		this.pin = Integer.valueOf(pinField.getText());
		if (String.valueOf(this.pin).length() != 4) {
			throw new InvalidParameterException("Enter a valid PIN");
		}
		
		String month = monthField.getSelectedItem().toString();
		String day = dayField.getSelectedItem().toString();
		String year = yearField.getSelectedItem().toString();
		this.dob = Integer.valueOf(month + day + year);
		
		String phoneNumber1 = phoneNumber1Field.getText();
		String phoneNumber2 = phoneNumber2Field.getText();
		String phoneNumber3 = phoneNumber3Field.getText();
		this.phone = Long.valueOf(phoneNumber1 + phoneNumber2 + phoneNumber3);
		
		if (String.valueOf(this.phone).length() != 10) {
			throw new InvalidParameterException("Enter a valid Phone Number");
		}
		
		this.firstName = firstNameField.getText();
		this.lastName = lastNameField.getText();
		
		this.streetAddress = streetAddressField.getText();
		this.city = cityField.getText();
		this.state = stateField.getSelectedItem().toString();
		this.postalCode = postalField.getText();
		
		this.user = new User(this.pin, this.dob, this.phone, this.firstName, this.lastName, this.streetAddress,
				this.city, this.state, this.postalCode);
		
		this.account.setUser(this.user);
		// creates constructor for the account 
		
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		
		if (source.equals(finalCreateButton)) {
			try {
				setValues();
				manager.createAccount();
				manager.switchTo(ATM.HOME_VIEW);
			}
			catch (NumberFormatException e2){
				updateErrorMessage("ERROR");
			}
			catch(InvalidParameterException e3) {
				updateErrorMessage(e3.getMessage());
			}
		} else if (source.equals(cancelButton)) {
			manager.switchTo(ATM.LOGIN_VIEW);
		} else if (source.equals(powerButton)) {
			manager.shutdown();
		} else {
			System.err.println("ERROR: Action command not found (" + e.getActionCommand() + ")");
		}
		
	}
	
}