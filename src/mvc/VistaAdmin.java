package mvc;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;

public class VistaAdmin extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtUsername;
	private JPasswordField pwdField;
	private JPasswordField pwdRepField;
	private JButton btnAlta;
	private JTextField txtRutaFitxer;
	private JButton btnImportar;
	private JTextArea txaMostra;
	private JButton btnVistaUser;
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					VistaAdmin frame = new VistaAdmin();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public VistaAdmin() {
		setTitle("AE02");
		initComponents();
	}

	public void initComponents() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 637, 484);
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
		
		JSeparator separator = new JSeparator();
		separator.setBounds(35, 116, 544, 2);
		contentPane.add(separator);
		
		JLabel lblFicher = new JLabel("Ruta Fitxer CSV:");
		lblFicher.setFont(new Font("Segoe UI", Font.BOLD, 14));
		lblFicher.setBounds(35, 131, 148, 20);
		contentPane.add(lblFicher);
		
		txtRutaFitxer = new JTextField();
		txtRutaFitxer.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		txtRutaFitxer.setColumns(10);
		txtRutaFitxer.setBounds(35, 160, 413, 25);
		contentPane.add(txtRutaFitxer);
		
		btnImportar = new JButton("Importar");
		btnImportar.setBounds(461, 151, 118, 34);
		contentPane.add(btnImportar);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(35, 218, 544, 162);
		contentPane.add(scrollPane);
		
		txaMostra = new JTextArea();
		scrollPane.setViewportView(txaMostra);
		
		JSeparator separator_1 = new JSeparator();
		separator_1.setBounds(35, 205, 544, 2);
		contentPane.add(separator_1);
		
		btnVistaUser = new JButton("Anar a Finestra Usuari");
		btnVistaUser.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		btnVistaUser.setBounds(422, 391, 157, 36);
		contentPane.add(btnVistaUser);
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

	public JTextField getTxtRutaFitxer() {
		return txtRutaFitxer;
	}

	public JButton getBtnImportar() {
		return btnImportar;
	}

	public JTextArea getTxaMostra() {
		return txaMostra;
	}

	public JButton getBtnVistaUser() {
		return btnVistaUser;
	}
}
