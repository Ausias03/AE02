package main;

public class Principal {
	public static void main(String[] args) {
		VistaLogin vistaLogin = new VistaLogin();
		VistaMain vistaMain = new VistaMain();
		Model model = new Model();
		Controlador controlador = new Controlador(vistaLogin, vistaMain, model);
	}
}
