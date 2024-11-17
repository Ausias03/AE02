package main;

public class Controlador {

	private VistaLogin vista;
	private Model model;

	public Controlador(VistaLogin vista, Model model) {
		this.vista = vista;
		this.model = model;

		initEventHandlers();
	}
	
	public void initEventHandlers() {
		
	}
}
