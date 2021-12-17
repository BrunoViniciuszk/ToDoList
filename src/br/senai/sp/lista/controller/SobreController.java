package br.senai.sp.lista.controller;

import javax.swing.JOptionPane;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class SobreController {
	
	@FXML
	private Button btnFechar;
	
	
	@FXML 
	public void btFechar() {
		Stage stage = (Stage) btnFechar.getScene().getWindow();
		stage.close();
	}
}
