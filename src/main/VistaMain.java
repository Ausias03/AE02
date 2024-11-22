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

public class VistaMain extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtUsername;
	private JPasswordField pwdField;
	private JPasswordField pwdRepField;
	private JButton btnAlta;
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					VistaMain frame = new VistaMain();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public VistaMain() {
		setTitle("AE02");
		initComponents();
	}

	public void initComponents() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 636, 443);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblRegistrar = new JLabel("Registrar Usuari:");
		lblRegistrar.setFont(new Font("Segoe UI", Font.BOLD, 14));
		lblRegistrar.setBounds(35, 23, 148, 20);
		contentPane.add(lblRegistrar);
		
		txtUsername = new JTextField();
		txtUsername.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		txtUsername.setBounds(35, 76, 125, 25);
		contentPane.add(txtUsername);
		txtUsername.setColumns(10);
		
		JLabel lblUsername = new JLabel("Nom usuari:");
		lblUsername.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		lblUsername.setBounds(35, 54, 108, 20);
		contentPane.add(lblUsername);
		
		JLabel lblContrasenya = new JLabel("Contrasenya:");
		lblContrasenya.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		lblContrasenya.setBounds(173, 54, 108, 20);
		contentPane.add(lblContrasenya);
		
		JLabel lblRepContrasenya = new JLabel("Repetir contrasenya:");
		lblRepContrasenya.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		lblRepContrasenya.setBounds(291, 54, 108, 20);
		contentPane.add(lblRepContrasenya);
		
		pwdField = new JPasswordField();
		pwdField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		pwdField.setBounds(173, 76, 108, 25);
		contentPane.add(pwdField);
		
		pwdRepField = new JPasswordField();
		pwdRepField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		pwdRepField.setBounds(291, 76, 157, 25);
		contentPane.add(pwdRepField);
		
		btnAlta = new JButton("Donar d'alta");
		btnAlta.setBounds(461, 67, 118, 34);
		contentPane.add(btnAlta);		
	}

	public JTextField getTxtUsername() {
		return txtUsername;
	}

	public JPasswordField getPwdField() {
		return pwdField;
	}

	public JPasswordField getPwdRepField() {
		return pwdRepField;
	}

	public JButton getBtnAlta() {
		return btnAlta;
	}
}
