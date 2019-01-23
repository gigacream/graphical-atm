package view;

import java.awt.CardLayout;
import java.awt.EventQueue;
import java.io.IOException;
import java.io.ObjectOutputStream;
import javax.swing.UIManager.*;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;

import controller.ViewManager;

@SuppressWarnings("serial")
public class ATM extends JFrame {
	
	/*
	 * Status codes for deposits, withdrawals, and transfers.
	 */
	
	public final static int INVALID_AMOUNT = 0;			// invalid amounts (i.e., less than $0.01)
	public final static int INSUFFICIENT_FUNDS = 1;		// insufficient funds to perform withdrawal or transfer
	public final static int ACCOUNT_NOT_FOUND = 2;		// destination account (for transfers) not found
	public final static int SUCCESS = 3;				// transaction completed successfully
	
	/*
	 * Named identifiers for views in CardLayout.
	 */
	
	public final static String LOGIN_VIEW = "LOGIN_VIEW";
	public final static String CREATE_VIEW = "CREATE_VIEW";
	public final static String HOME_VIEW = "HOME_VIEW";
	public final static String DEPOSIT_VIEW = "DEPOSIT_VIEW";
	public final static String WITHDRAW_VIEW = "WITHDRAW_VIEW";
	public final static String TRANSFER_VIEW = "TRANSFER_VIEW";
	public final static String INFORMATION_VIEW = "INFORMATION_VIEW";
	
	/*
	 * Indexes for views as they are stored in CardLayout.
	 */
	
	public final static int LOGIN_VIEW_INDEX = 0;
	public final static int CREATE_VIEW_INDEX = 1;
	public final static int HOME_VIEW_INDEX = 2;
	public final static int DEPOSIT_VIEW_INDEX = 3;
	public final static int WITHDRAW_VIEW_INDEX = 4;
	public final static int TRANSFER_VIEW_INDEX = 5;
	public final static int INFORMATION_VIEW_INDEX = 6;
		
	/**
	 * Constructs an instance (or object) of the ATM class.
	 */
	
	public ATM() {
		super("Graphical ATM");
	}
	
	///////////////////// PRIVATE METHODS /////////////////////////////////////////////

	/*
	 * Initializes the ATM components.
	 */
	
	private void initialize() {		
		JPanel views = new JPanel(new CardLayout());
		ViewManager manager = new ViewManager(views);
		
		// add child views to the parent container

		views.add(new LoginView(manager), LOGIN_VIEW);
		views.add(new CreateView(manager), CREATE_VIEW);
		views.add(new HomeView(manager), HOME_VIEW);
		views.add(new DepositView(manager), DEPOSIT_VIEW);
		views.add(new WithdrawView(manager), WITHDRAW_VIEW);
		views.add(new TransferView(manager), TRANSFER_VIEW);
		views.add(new InformationView(manager), INFORMATION_VIEW);
		
		// configure the application frame
		
		this.add(views);
		this.setSize(500, 500);
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.setVisible(true);
	}
	
	/*
	 * ATM is not designed to be serialized, and attempts to serialize will throw an IOException.
	 * 
	 * @param oos
	 * @throws IOException
	 */
	
	private void writeObject(ObjectOutputStream oos) throws IOException {
		throw new IOException("ERROR: The ATM class is not serializable.");
	}
	
	///////////////////// MAIN METHOD /////////////////////////////////////////////////

	/**
	 * Program execution starts here.
	 * 
	 * @param args
	 */
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				        if ("Nimbus".equals(info.getName())) {
				            UIManager.setLookAndFeel(info.getClassName());
				            break;
				        }
				    }
					/*
					 * Uncomment this to check out a different look and feel (i.e., style)
					 * for your application. Feel to free to experiment with this and others.
					 * You'll need to add the required import statements for it to compile.
					 */
					
					new ATM().initialize();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
