package br.senai.sp.lista.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UTFDataFormatException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import br.senai.sp.lista.io.TarefaIO;
import br.senai.sp.lista.model.StatusTarefa;
import br.senai.sp.lista.model.Tarefa;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Callback;

public class IndexController implements Initializable{
	@FXML
	private DatePicker dateLimit;
	
	@FXML
	private TextField tfTarefa;
	
	@FXML
	private TextField tfStatus;
	
	@FXML
	private TextArea tfComments;
	
	@FXML
	private TableView<Tarefa> tvTarefa;
	
	@FXML
	private TableColumn<Tarefa, LocalDate> tcData;
	
	@FXML
	private TableColumn<Tarefa, String> tcTarefa;
	
	@FXML
	private Button btDate;
	
	@FXML
	private Button btConcluir;
	
	@FXML
	private Button btDelete;
	
	//variavel para guardar tarefa
	private Tarefa tarefa;
	
	//variavel para guardar lista de tarefas
	private List<Tarefa> tarefas;
	
	
	
	public void btSaveClick() {
		if (dateLimit.getValue()== null) {
			JOptionPane.showMessageDialog(null, "Informe a data limite", "Informe", JOptionPane.ERROR_MESSAGE);
			dateLimit.requestFocus();
		}else if (tfTarefa.getText().isEmpty()) {
			JOptionPane.showMessageDialog(null, "Informe o nome da tarefa", "Informe", JOptionPane.ERROR_MESSAGE);
			tfTarefa.requestFocus();
		}else if (tfComments.getText().isEmpty()) {
			JOptionPane.showMessageDialog(null, "Informe o comentário da tarefa", "Informe", JOptionPane.ERROR_MESSAGE);
			tfComments.requestFocus();
		}else {
			// verifica se a data informada não é anterior a data atual
			if(dateLimit.getValue().isBefore(LocalDate.now())) {
			JOptionPane.showMessageDialog(null, "Informe uma data válida", "Informe", JOptionPane.ERROR_MESSAGE);
			dateLimit.requestFocus();
		}else if (tfTarefa.getText().length() > 20) {
				JOptionPane.showMessageDialog(null, "Informe um nome com no máximo 10 caracteres", "Informe", JOptionPane.ERROR_MESSAGE);
				tfTarefa.requestFocus();
		}else if(tfComments.getText().length() > 50) {
				JOptionPane.showMessageDialog(null, "Informe o comentário com no máximo 50 caracteres", "Informe", JOptionPane.ERROR_MESSAGE);
				tfComments.requestFocus();
			}else {
				//instancia a tarefa
				tarefa = new Tarefa();
				
				//popular a tarefa
				tarefa.setDataCriacao(LocalDate.now());
				
				tarefa.setDataLimite(dateLimit.getValue());
				
				tarefa.setStatus(StatusTarefa.ABERTA);
				
				tarefa.setDescricao(tfTarefa.getText());
				
				tarefa.setComentario(tfComments.getText());
				
				//TODO salvar no banco de dados
				try {
					TarefaIO.insert(tarefa);
					limparCampos();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(null, "Arquivo não encontrado: "+e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
				} catch (IOException e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(null, "Erro de I/O: "+e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
				}
				
				carregarTarefas();
			}
		}
		
	}
	
	
	public void btDeleteClick() {
		
		
	}
	
	public void btDateClick() {
		
	}
	
	public void btEditClick() {
		limparCampos();
		
		
	}
	
	public void btConcluirClick() {
		tarefa.setStatus(StatusTarefa.CONCLUIDA);
		tarefa.setDataFinalizada(LocalDate.now());
	}
	
	public void limparCampos() {
		tarefa = null;
		
		dateLimit.setValue(null);
		tfTarefa.setText(null);
		tfComments.setText(null);
		tfStatus.setText(null);
		dateLimit.setDisable(false);
		btConcluir.setDisable(true);
		btDate.setDisable(true);
		btDelete.setDisable(true);
		tvTarefa.getSelectionModel().clearSelection();
		
		dateLimit.requestFocus();
	}


	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// define os parâmetros para as colunas do TableView
		tcData.setCellValueFactory(new PropertyValueFactory<>("dataLimite"));
		tcTarefa.setCellValueFactory(new PropertyValueFactory<>("descricao"));
		// formatando a data ba coluna
		tcData.setCellFactory(call -> {
				TableCell<Tarefa, LocalDate> celula = new TableCell<Tarefa, LocalDate>() {
					@Override
					protected void updateItem(LocalDate item, boolean empty) {
						super.updateItem(item, empty);
						DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MMM/yyyy");
						if(!empty) {
							setText(item.format(fmt));
						} else {
							setText("");
						}
					}
					
				};
				celula.setStyle("-fx-alignment: CENTER;");
				return celula;
			
		});
		
		// evento de seleção de um item na TableView
		// evento que dispara pixel quando seleciona (lâmbida java) os parametros é tarefa, ou o que ele recebe no TableCell
		 tvTarefa.getSelectionModel().selectedItemProperty().addListener((obs, oV, nV)-> {
			//passar a referencia da var local para a tarefa global
			 tarefa = nV;
			 // se houver uma tarefa selecionada
			 if(tarefa != null) {
				 tfTarefa.setText(tarefa.getDescricao());
				 tfComments.setText(tarefa.getComentario());
				 tfStatus.setText(tarefa.getStatus().toString());
				 dateLimit.setValue(tarefa.getDataLimite());
				 dateLimit.setDisable(true);
				 btConcluir.setDisable(false);
				 btDelete.setDisable(false);
				 btDate.setDisable(false);
			 }
		 });
		
		
		carregarTarefas();
	}
	
	private void carregarTarefas() {
		try {
			tarefas = TarefaIO.readTarefas();
			tvTarefa.setItems(FXCollections.observableArrayList(tarefas));
			// atualiza tabela   
			tvTarefa.refresh();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Erro ao buscar tarefas", "ERRO", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		} 
		
	}
	
	
	
	
	@FXML
	public void taKey(KeyEvent evento) {
		if(evento.getCode().equals(KeyCode.ENTER)) {
			evento.consume();
			btSaveClick();
		}
	}
		 
}
