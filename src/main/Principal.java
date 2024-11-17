package main;

public class Principal {
	public static void main(String[] args) {
		VistaLogin vista = new VistaLogin();
		Model model = new Model();
		Controlador controlador = new Controlador(vista, model);
	}
}
