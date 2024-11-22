package main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

public class Controlador {

	private VistaLogin vistaLogin;
	private VistaMain vistaMain;
	private Model model;

	public Controlador(VistaLogin vistaLogin, VistaMain vistaMain, Model model) {
		this.vistaLogin = vistaLogin;
		this.vistaMain = vistaMain;
		this.model = model;

		initEventHandlers();
	}

	public void initEventHandlers() {
		vistaLogin.getBtnLogIn().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (model.checkUserInfo(vistaLogin.getTxtUsername().getText(),
						vistaLogin.getPwdField().getPassword())) {
					vistaLogin.dispose();
					vistaMain.setVisible(true);
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
	}
}
