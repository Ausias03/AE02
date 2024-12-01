package main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.SQLException;

import javax.swing.JOptionPane;

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
				if (model.checkUserInfo(vistaLogin.getTxtUsername().getText(),
						vistaLogin.getPwdField().getPassword())) {
					vistaLogin.setVisible(false);
					vistaUser.setVisible(true);
					model.setSessionUsername(vistaLogin.getTxtUsername().getText());
					model.setSessionPwd(new String(vistaLogin.getPwdField().getPassword()));
				} else {
					JOptionPane.showMessageDialog(null, "Dades Incorrectes", "ACTION BUTTON SEARCH",
							JOptionPane.INFORMATION_MESSAGE);
				}
			}

		});

		vistaAdmin.getBtnAlta().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (new String(vistaAdmin.getPwdField().getPassword())
						.equals(new String(vistaAdmin.getPwdRepField().getPassword()))) {
					if (model.signUpUser(vistaAdmin.getTxtUsername().getText(),
							vistaAdmin.getPwdField().getPassword())) {
						JOptionPane.showMessageDialog(null, "Usuari afegit!", "ACTION BUTTON SEARCH",
								JOptionPane.INFORMATION_MESSAGE);
						vistaAdmin.getTxtUsername().setText("");
						vistaAdmin.getPwdField().setText("");
						vistaAdmin.getPwdRepField().setText("");
					} else {
						JOptionPane.showMessageDialog(null, "Error, no s'ha pogut afegir l'usuari",
								"ACTION BUTTON SEARCH", JOptionPane.INFORMATION_MESSAGE);
					}
				} else {
					JOptionPane.showMessageDialog(null, "Les contrasenyes no coincideixen", "ACTION BUTTON SEARCH",
							JOptionPane.INFORMATION_MESSAGE);
				}
			}

		});

		vistaAdmin.getBtnImportar().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (model.importCSV(vistaAdmin.getTxtRutaFitxer().getText())) {
					JOptionPane.showMessageDialog(null, "Dades importades correctament!", "ACTION BUTTON SEARCH",
							JOptionPane.INFORMATION_MESSAGE);
					vistaAdmin.getTxaMostra().setText(model.retrieveXMLS());
				} else {
					JOptionPane.showMessageDialog(null, "Error, no s'han pogut importar les dades.",
							"ACTION BUTTON SEARCH", JOptionPane.INFORMATION_MESSAGE);
				}
			}

		});

		vistaAdmin.getBtnVistaUser().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				vistaAdmin.setVisible(false);
				vistaUser.setVisible(true);
			}

		});

		vistaUser.getBtnVistaAdmin().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (model.checkCredentials()) {
					vistaUser.setVisible(false);
					vistaAdmin.setVisible(true);
				} else {
					JOptionPane.showMessageDialog(null, "No tens permisos per a entrar a este apartat.",
							"ACTION BUTTON SEARCH", JOptionPane.INFORMATION_MESSAGE);
				}
			}

		});

		vistaUser.getBtnLogOut().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				vistaUser.setVisible(false);
				vistaLogin.setVisible(true);
				vistaUser.getTxtQuery().setText("");
			}

		});

		vistaUser.getBtnBuscar().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String query = vistaUser.getTxtQuery().getText();

				try {
					vistaUser.getTblResults().setModel(model.executeQuery(query));
					model.setQueryLog(query);
				} catch (SQLException ex) {
					System.out.println(ex.getMessage());
					JOptionPane.showMessageDialog(null, "Error, falta de permisos o error de sintaxis.",
							"ACTION BUTTON SEARCH", JOptionPane.INFORMATION_MESSAGE);
				}
			}

		});

		vistaUser.getBtnExportar().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					model.exportCSV();
					JOptionPane.showMessageDialog(null, "Dades exportades correctament!", "ACTION BUTTON SEARCH",
							JOptionPane.INFORMATION_MESSAGE);
				} catch (SQLException ex) {
					System.out.println(ex.getMessage());
					JOptionPane.showMessageDialog(null, "Error en la consulta.", "ACTION BUTTON SEARCH",
							JOptionPane.INFORMATION_MESSAGE);
				} catch (IOException ex) {
					System.out.println(ex.getMessage());
					JOptionPane.showMessageDialog(null, "Error escrivint l'arxiu.", "ACTION BUTTON SEARCH",
							JOptionPane.INFORMATION_MESSAGE);
				}
			}

		});
	}
}
