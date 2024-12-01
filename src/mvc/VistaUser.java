package mvc;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JTable;
import javax.swing.JSeparator;
import javax.swing.JButton;
import javax.swing.JScrollPane;

public class VistaUser extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtQuery;
	private JTable tblResults;
	private JButton btnVistaAdmin;
	private JButton btnLogOut;
	private JButton btnBuscar;
	private JButton btnExportar;
	private JScrollPane scrollPane;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					VistaUser frame = new VistaUser();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public VistaUser() {
		setTitle("AE02");
		initComponents();
	}

	public void initComponents() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 645, 435);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblQuery = new JLabel("Consulta SQL a realizar:");
		lblQuery.setFont(new Font("Segoe UI", Font.BOLD, 14));
		lblQuery.setBounds(24, 11, 190, 20);
		contentPane.add(lblQuery);
		
		txtQuery = new JTextField();
		txtQuery.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		txtQuery.setBounds(24, 34, 462, 22);
		contentPane.add(txtQuery);
		txtQuery.setColumns(10);
		
		scrollPane = new JScrollPane();
		scrollPane.setBounds(24, 80, 587, 256);
		contentPane.add(scrollPane);
		
		tblResults = new JTable();
		scrollPane.setViewportView(tblResults);
		tblResults.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		
		JSeparator separator = new JSeparator();
		separator.setBounds(24, 65, 587, 2);
		contentPane.add(separator);
		
		btnVistaAdmin = new JButton("Anar a Finestra Admin");
		btnVistaAdmin.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		btnVistaAdmin.setBounds(129, 347, 157, 36);
		contentPane.add(btnVistaAdmin);
		
		btnLogOut = new JButton("Log Out");
		btnLogOut.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		btnLogOut.setBounds(24, 347, 95, 36);
		contentPane.add(btnLogOut);
		
		btnBuscar = new JButton("Buscar");
		btnBuscar.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		btnBuscar.setBounds(516, 20, 95, 36);
		contentPane.add(btnBuscar);
		
		btnExportar = new JButton("Exportar a CSV");
		btnExportar.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		btnExportar.setBounds(482, 347, 129, 36);
		contentPane.add(btnExportar);
	}

	public JButton getBtnVistaAdmin() {
		return btnVistaAdmin;
	}

	public JButton getBtnLogOut() {
		return btnLogOut;
	}

	public JButton getBtnBuscar() {
		return btnBuscar;
	}

	public JButton getBtnExportar() {
		return btnExportar;
	}

	public JTextField getTxtQuery() {
		return txtQuery;
	}

	public JTable getTblResults() {
		return tblResults;
	}
}
