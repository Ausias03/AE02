package main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

public class Controlador {

	private VistaLogin vistaLogin;
	private VistaMain vistaMain;
	private VistaUser vistaUser;
	private Model model;

	public Controlador(VistaLogin vistaLogin, VistaMain vistaMain, VistaUser vistaUser, Model model) {
		this.vistaLogin = vistaLogin;
		this.vistaMain = vistaMain;
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
				} else {
					JOptionPane.showMessageDialog(null, "Dades Incorrectes", "ACTION BUTTON SEARCH",
							JOptionPane.INFORMATION_MESSAGE);
				}
			}

		});

		vistaMain.getBtnAlta().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (new String(vistaMain.getPwdField().getPassword()).equals(new String(vistaMain.getPwdRepField().getPassword()))) {
					if (model.signUpUser(vistaMain.getTxtUsername().getText(), vistaMain.getPwdField().getPassword())) {
						JOptionPane.showMessageDialog(null, "Usuari afegit!", "ACTION BUTTON SEARCH",
								JOptionPane.INFORMATION_MESSAGE);
						vistaMain.getTxtUsername().setText("");
						vistaMain.getPwdField().setText("");
						vistaMain.getPwdRepField().setText("");
					} else {
						JOptionPane.showMessageDialog(null, "Error, no s'ha pogut afegir l'usuari", "ACTION BUTTON SEARCH",
								JOptionPane.INFORMATION_MESSAGE);
					}
				} else {
					JOptionPane.showMessageDialog(null, "Les contrasenyes no coincideixen", "ACTION BUTTON SEARCH",
							JOptionPane.INFORMATION_MESSAGE);
				}
			}

		});
		
		vistaMain.getBtnImportar().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (model.importCSV(vistaMain.getTxtRutaFitxer().getText())) {
					JOptionPane.showMessageDialog(null, "Dades importades correctament!", "ACTION BUTTON SEARCH",
							JOptionPane.INFORMATION_MESSAGE);
					vistaMain.getTxaMostra().setText(model.retrieveXMLS());
				} else {
					JOptionPane.showMessageDialog(null, "Error, no s'han pogut importar les dades.", "ACTION BUTTON SEARCH",
							JOptionPane.INFORMATION_MESSAGE);
				}
			}

		});
		
		vistaMain.getBtnVistaUser().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				vistaMain.setVisible(false);
				vistaUser.setVisible(true);
			}

		});
		
		vistaUser.getBtnVistaAdmin().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				vistaUser.setVisible(false);
				vistaMain.setVisible(true);
			}

		});
		
		vistaUser.getBtnLogOut().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				vistaUser.setVisible(false);
				vistaLogin.setVisible(true);
			}

		});
	}
}
