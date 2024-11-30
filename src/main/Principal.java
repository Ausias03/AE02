package main;

public class Principal {
	public static void main(String[] args) {
		VistaLogin vistaLogin = new VistaLogin();
		VistaAdmin vistaMain = new VistaAdmin();
		VistaUser vistaUser = new VistaUser();
		Model model = new Model();
		Controlador controlador = new Controlador(vistaLogin, vistaMain, vistaUser, model);
	}
}
