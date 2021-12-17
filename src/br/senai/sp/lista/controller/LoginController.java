package br.senai.sp.lista.controller;

import br.senai.sp.lista.io.TarefaIO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class LoginController {
	@FXML
	private TextField tfUsuario;
	
	@FXML
	private PasswordField pfSenha;
	
	@FXML
	private Button btEntrar;
	
	@FXML
	private Label lbUsuario, lbSenha;
	
	@FXML
	private void btEntrarClick() {
		// validando se o usuario e a senha estão corretas
		if (tfUsuario.getText().equals("admin") && pfSenha.getText().equals("admin")) {
		System.out.println("Entrou");
		removerEstilos();
		
		// fechando a tela de login
		Stage stage = (Stage) btEntrar.getScene().getWindow();
		stage.close();
		
		try {
			TarefaIO.createFiles();
			AnchorPane root = (AnchorPane)FXMLLoader.load(getClass().getResource("/br/senai/sp/lista/view/Index.fxml"));
			Scene scene = new Scene(root,1064,600);
			scene.getStylesheets().add(getClass().getResource("/br/senai/sp/lista/view/application.css").toExternalForm());
			stage.setScene(scene);
			stage.setTitle("Login");
			stage.getIcons().add(new Image(getClass().getResourceAsStream("/br/senai/sp/lista/imagens/icone-agenda.png")));
			stage.show();
			stage.setResizable(false);
		} catch(Exception e) {
			e.printStackTrace();
			
		}
		
	}else {
		validacao();
	}
}
	
	public void validacao() {
		// validando o usuario estiver vazio ou diferente de admin
		if (tfUsuario.getText().isEmpty() || !tfUsuario.getText().equals("admin")) {
		if (!tfUsuario.getStyleClass().contains("erro") || !lbUsuario.getStyleClass().contains("erroLable")) {
		adicionarEstiloUsuario();
		}
		
		} else {
		removerEstiloUsuario();
		}
		
		// validando a senha estiver vazia ou diferente de admin
		if (pfSenha.getText().isEmpty() || !pfSenha.getText().equals("admin")) {
		if (!pfSenha.getStyleClass().contains("erro") || !lbSenha.getStyleClass().contains("erroLable")) {
		adicionarEstiloSenha();
		}
		} else {
		removerEstiloSenha();
		}
	
}
	

		public void adicionarEstilos() {
			adicionarEstiloUsuario();
			adicionarEstiloSenha();
		}

		public void adicionarEstiloUsuario() {
		// adicionando os estilos caso estejam vazios (usuario)
		tfUsuario.requestFocus();
		tfUsuario.getStyleClass().add("erro");
		lbUsuario.setText("* Usuário incorreto..");
		lbUsuario.getStyleClass().add("erroLable");
		}

		public void adicionarEstiloSenha() {
		// adicionando os estilos caso estejam vazios (senha)
		pfSenha.getStyleClass().add("erro");
		lbSenha.setText("* Senha incorreta..");
		lbSenha.getStyleClass().add("erroLable");
		}

		public void removerEstilos() {
		removerEstiloUsuario();
		removerEstiloSenha();
		} 

		public void removerEstiloUsuario() {
		// removendo os erros caso esteja tudo certo (usuario)
		tfUsuario.getStyleClass().remove("erro");
		lbUsuario.setText("Usuário:");
		lbUsuario.getStyleClass().remove("erroLable");
		}

		public void removerEstiloSenha() {
		// removendo os erros caso esteja tudo certo (senha)
		pfSenha.getStyleClass().remove("erro");
		lbSenha.setText("Senha:");
		lbSenha.getStyleClass().remove("erroLable");
		}
	

}
