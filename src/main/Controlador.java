package main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

public class Controlador {

	private VistaLogin vista;
	private Model model;

	public Controlador(VistaLogin vista, Model model) {
		this.vista = vista;
		this.model = model;

		initEventHandlers();
	}
	
	public void initEventHandlers() {
		vista.getBtnLogIn().addActionListener(new ActionListener( ) {
			public void actionPerformed(ActionEvent arg0) {
				if (model.checkUserInfo(vista.getTxtUsername().getText(), vista.getPwdField().getPassword())) {
					JOptionPane.showMessageDialog(null, "Datos Correctos!", "ACTION BUTTON SEARCH",
							JOptionPane.INFORMATION_MESSAGE);
				} else {
					JOptionPane.showMessageDialog(null, "Datos Incorrectos", "ACTION BUTTON SEARCH",
							JOptionPane.INFORMATION_MESSAGE);
				}
			}
		});
	}
}
