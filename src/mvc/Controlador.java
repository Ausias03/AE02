package mvc;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class Controlador {

	private VistaLogin vistaLogin;
	private VistaAdmin vistaAdmin;
	private VistaUser vistaUser;
	private Model model;

	public Controlador(VistaLogin vistaLogin, VistaAdmin vistaAdmin, VistaUser vistaUser, Model model) {
		this.vistaLogin = vistaLogin;
		this.vistaAdmin = vistaAdmin;
		this.vistaUser = vistaUser;
		this.model = model;

		initEventHandlers();
	}

	public void initEventHandlers() {
		vistaLogin.getBtnLogIn().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String username = vistaLogin.getTxtUsername().getText();
				String pwd = new String(vistaLogin.getPwdField().getPassword());
				try {
					model.userExists(username, pwd);
					vistaLogin.setVisible(false);
					vistaUser.setVisible(true);
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(null, ex.getMessage(), "ACTION BUTTON SEARCH",
							JOptionPane.INFORMATION_MESSAGE);
				}
			}
		});

		vistaAdmin.getBtnAlta().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String newUsername = vistaAdmin.getTxtUsername().getText();
				String pwdField1 = new String(vistaAdmin.getPwdField().getPassword());
				String pwdField2 = new String(vistaAdmin.getPwdRepField().getPassword());

				if (pwdField1.equals(pwdField2)) {
					try {
						model.signUpUser(newUsername, pwdField1);
						JOptionPane.showMessageDialog(null, "Usuari afegit!", "ACTION BUTTON SEARCH",
								JOptionPane.INFORMATION_MESSAGE);

						vistaAdmin.getTxtUsername().setText("");
						vistaAdmin.getPwdField().setText("");
						vistaAdmin.getPwdRepField().setText("");
					} catch (Exception ex) {
						JOptionPane.showMessageDialog(null, ex.getMessage(), "ACTION BUTTON SEARCH",
								JOptionPane.INFORMATION_MESSAGE);
					}
				} else {
					JOptionPane.showMessageDialog(null, "Les contrasenyes no coincideixen", "ACTION BUTTON SEARCH",
							JOptionPane.INFORMATION_MESSAGE);
				}
			}
		});

		vistaAdmin.getBtnImportar().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String rutaCsv = vistaAdmin.getTxtRutaFitxer().getText();

				try {
					model.importCSV(rutaCsv);
					JOptionPane.showMessageDialog(null, "Dades importades correctament!", "ACTION BUTTON SEARCH",
							JOptionPane.INFORMATION_MESSAGE);
					vistaAdmin.getTxaMostra().setText(model.retrieveXMLS());
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(null, ex.getMessage(), "ACTION BUTTON SEARCH",
							JOptionPane.INFORMATION_MESSAGE);
				}
			}

		});

		vistaAdmin.getBtnVistaUser().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				vistaAdmin.setVisible(false);
				vistaUser.setVisible(true);
				vistaAdmin.getTxtUsername().setText("");
				vistaAdmin.getPwdField().setText("");
				vistaAdmin.getPwdRepField().setText("");
				vistaAdmin.getTxtRutaFitxer().setText("");
				vistaAdmin.getTxaMostra().setText("");
			}

		});

		vistaUser.getBtnVistaAdmin().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (model.getTypus().equals("admin")) {
					vistaUser.setVisible(false);
					vistaAdmin.setVisible(true);
				} else {
					JOptionPane.showMessageDialog(null, "No tens permisos per a entrar en este apartat.",
							"ACTION BUTTON SEARCH", JOptionPane.INFORMATION_MESSAGE);
				}
			}

		});

		vistaUser.getBtnLogOut().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				vistaUser.setVisible(false);
				vistaLogin.setVisible(true);
				vistaUser.getTxtQuery().setText("");
				vistaUser.getTblResults().setModel(new DefaultTableModel());
			}

		});

		vistaUser.getBtnBuscar().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String query = vistaUser.getTxtQuery().getText();

				try {
					vistaUser.getTblResults().setModel(model.executeQuery(query));
					model.setQueryLog(query);
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(null, ex.getMessage(), "ACTION BUTTON SEARCH",
							JOptionPane.INFORMATION_MESSAGE);
				}
			}

		});

		vistaUser.getBtnExportar().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					model.exportCSV();
					JOptionPane.showMessageDialog(null, "Dades exportades correctament!", "ACTION BUTTON SEARCH",
							JOptionPane.INFORMATION_MESSAGE);
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(null, ex.getMessage(), "ACTION BUTTON SEARCH",
							JOptionPane.INFORMATION_MESSAGE);
				}
			}

		});
	}
}
