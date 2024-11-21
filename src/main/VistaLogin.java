package main;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JButton;

public class VistaLogin extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtUsername;
	private JPasswordField pwdField;
	private JButton btnLogIn;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					VistaLogin frame = new VistaLogin();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public VistaLogin() {
		setTitle("AE02");
		initComponents();
	}

	public void initComponents() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);

		getContentPane().setLayout(null);

		JLabel lblLogIn = new JLabel("Log In");
		lblLogIn.setFont(new Font("Segoe UI", Font.PLAIN, 20));
		lblLogIn.setBounds(193, 22, 63, 32);
		getContentPane().add(lblLogIn);

		JLabel lblUsername = new JLabel("Nom d'usuari:");
		lblUsername.setFont(new Font("Segoe UI", Font.PLAIN, 16));
		lblUsername.setBounds(79, 75, 108, 26);
		getContentPane().add(lblUsername);

		txtUsername = new JTextField();
		txtUsername.setFont(new Font("Segoe UI", Font.PLAIN, 11));
		txtUsername.setBounds(193, 81, 173, 20);
		getContentPane().add(txtUsername);
		txtUsername.setColumns(10);

		JLabel lblPassword = new JLabel("Contrasenya:");
		lblPassword.setFont(new Font("Segoe UI", Font.PLAIN, 16));
		lblPassword.setBounds(79, 122, 108, 26);
		getContentPane().add(lblPassword);

		pwdField = new JPasswordField();
		pwdField.setFont(new Font("Segoe UI", Font.PLAIN, 11));
		pwdField.setBounds(193, 128, 173, 20);
		getContentPane().add(pwdField);

		btnLogIn = new JButton("Iniciar Sessió");
		btnLogIn.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		btnLogIn.setBounds(166, 174, 112, 32);
		getContentPane().add(btnLogIn);

		setVisible(true);
	}

	public JTextField getTxtUsername() {
		return txtUsername;
	}
	
	public JPasswordField getPwdField() {
		return pwdField;
	}
	
	public JButton getBtnLogIn() {
		return btnLogIn;
	}

}
