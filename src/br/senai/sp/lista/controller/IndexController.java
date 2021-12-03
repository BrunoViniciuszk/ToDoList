package br.senai.sp.lista.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UTFDataFormatException;
import java.io.Writer;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import br.senai.sp.lista.io.TarefaIO;
import br.senai.sp.lista.model.StatusTarefa;
import br.senai.sp.lista.model.Tarefa;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Callback;

public class IndexController implements Initializable, ChangeListener<Tarefa> {
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

	@FXML
	private TextField tfCode;

	@FXML
	private Button btSalvar;

	@FXML
	private Button btEdit1;

	@FXML
	private Label lbTarefa, lbComments, lbData;

	// variavel para guardar tarefa
	private Tarefa tarefa;

	// variavel para guardar lista de tarefas
	private List<Tarefa> tarefas;

	public void btSaveClick() {
		boolean valido = true;

		if (dateLimit.getValue() == null) {
			lbData.getStyleClass().add("erro");
			lbData.setText("* OBRIGATÓRIO");
			dateLimit.requestFocus();
			valido = false;
		} else {
			lbData.getStyleClass().remove("erro");
			lbData.setText("Data para realização");
		}

		if (tfComments.getText().isEmpty()) {
			lbComments.getStyleClass().add("erro");
			lbComments.setText("* OBRIGATÓRIO");
			tfComments.requestFocus();
			valido = false;
		} else {
			lbComments.getStyleClass().remove("erro");
			lbComments.setText("Comentários");
		}

		if (tfTarefa.getText().isEmpty()) {
			lbTarefa.getStyleClass().add("erro");
			lbTarefa.setText("* OBRIGATÓRIO");
			tfTarefa.requestFocus();
			valido = false;
		} else {
			lbTarefa.getStyleClass().remove("erro");
			lbTarefa.setText("Tarefa");
		}

		if (valido) {

			if (tarefa == null) {
				// instancia a tarefa
				tarefa = new Tarefa();
				tarefa.setDataCriacao(LocalDate.now());
				tarefa.setStatus(StatusTarefa.ABERTA);
			}

			// popular a tarefa
			tarefa.setDataLimite(dateLimit.getValue());
			tarefa.setDescricao(tfTarefa.getText());

			tarefa.setComentario(tfComments.getText());

			// TODO salvar no banco de dados
			try {
				if (tarefa.getId() == 0) {
					TarefaIO.insert(tarefa);
				} else {
					TarefaIO.atualizaTarefas(tarefas);
				}
				limparCampos();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(null, "Arquivo não encontrado: " + e.getMessage(), "Erro",
						JOptionPane.ERROR_MESSAGE);
			} catch (IOException e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(null, "Erro de I/O: " + e.getMessage(), "Erro",
						JOptionPane.ERROR_MESSAGE);
			}

			carregarTarefas();
		}

	}

	public void btDeleteClick() throws IOException {
		if (tarefa != null) {
			int resposta = JOptionPane.showConfirmDialog(null, "Deseja mesmo excluir a tarefa " + tarefa.getId() + "?",
					"Apagar tarefa", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

			if (resposta == 0) {
				tarefas.remove(tarefa);
				try {
					TarefaIO.atualizaTarefas(tarefas);
					limparCampos();
					carregarTarefas();
				} catch (IOException e) {
					JOptionPane.showConfirmDialog(null, "Ocorreu um erro ao excluir a tarefa " + tarefa.getId(), "Erro",
							JOptionPane.ERROR_MESSAGE);
				}

				atualizaId();
			}
		}

	}

	public void btDateClick() {
		if (tarefa != null) {
			int dias = Integer.parseInt(JOptionPane.showInputDialog(null, "Quantos dias você deseja adiar?",
					"Informe quantos dias", JOptionPane.QUESTION_MESSAGE));
			LocalDate novaData = tarefa.getDataLimite().plusDays(dias);
			
			try {
				if (novaData.isBefore(LocalDate.now())) {
					JOptionPane.showMessageDialog(null, "Não é possível adiar para uma data anterior a de hoje", "Erro",
							JOptionPane.ERROR_MESSAGE);
				}else {
					DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
					JOptionPane.showMessageDialog(null,
							"A tarefa foi adiada com sucesso, sua nova data é: " + novaData.format(fmt));
					tarefa.setDataLimite(novaData);
					tarefa.setStatus(StatusTarefa.ADIADA);
				}
				TarefaIO.atualizaTarefas(tarefas);
				// Avisar ao usuário que a tarefa foi adiada com sucesso e sua nova data limite
				carregarTarefas();
				limparCampos();
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, "Ocorreu um erro ao atualizar as tarefas", "Erro",
						JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
			}

			 

		}

	}

	public void btEditClick() {
		limparCampos();

	}

	public void btConcluirClick() {
		tarefa.setStatus(StatusTarefa.CONCLUIDA);
		tarefa.setDataFinalizada(LocalDate.now());

		if (tarefa != null) {
			tarefa.setDataFinalizada(LocalDate.now());
			tarefa.setStatus(StatusTarefa.CONCLUIDA);
			LocalDate dataConclusao = tarefa.getDataFinalizada();

			DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");

			try {
				TarefaIO.atualizaTarefas(tarefas);
				carregarTarefas();
				limparCampos();
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null,
						"A tarefa foi concluida, dia de conclusão: " + dataConclusao.format(fmt));
			}

		}
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
		tfCode.setText(null);
		dateLimit.requestFocus();

		lbData.getStyleClass().remove("erro");
		lbComments.getStyleClass().remove("erro");
		lbTarefa.getStyleClass().remove("erro");
		lbData.setText("Data para realização");
		lbComments.setText("Comentários");
		lbTarefa.setText("Tarefa");
		atualizaId();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// define os parâmetros para as colunas do TableView
		tcData.setCellValueFactory(new PropertyValueFactory<>("dataLimite"));
		tcTarefa.setCellValueFactory(new PropertyValueFactory<>("descricao"));
		// formatando a data da coluna
		tcData.setCellFactory(call -> {
			TableCell<Tarefa, LocalDate> celula = new TableCell<Tarefa, LocalDate>() {
				@Override
				protected void updateItem(LocalDate item, boolean empty) {
					super.updateItem(item, empty);
					DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MMM/yyyy");
					if (!empty) {
						setText(item.format(fmt));
					} else {
						setText("");
					}
				}

			};
			celula.setStyle("-fx-alignment: CENTER;");
			return celula;

		});
		tvTarefa.setRowFactory(call -> new TableRow<Tarefa>() {
			protected void updateItem(Tarefa item, boolean empty) {
				super.updateItem(item, empty);
				if (item == null) {
					setStyle("");
				} else if (item.getStatus() == StatusTarefa.CONCLUIDA) {
					setStyle("-fx-background-color: #33FF33");
				} else if (item.getDataLimite().isBefore(LocalDate.now())) {
					setStyle("-fx-background-color: red");
				} else if (item.getStatus() == StatusTarefa.ADIADA) {
					setStyle("-fx-background-color: #ff0");
				} else {
					setStyle("-fx-background-color: #fff");
				}
			};
		});

		// evento de seleção de um item na TableView
		// evento que dispara pixel quando seleciona (lâmbida java) os parametros é
		// tarefa, ou o que ele recebe no TableCell
		tvTarefa.getSelectionModel().selectedItemProperty().addListener((obs, oV, nV) -> {
			// passar a referencia da var local para a tarefa global
			tarefa = nV;
			// se houver uma tarefa selecionada
			if (tarefa != null) {
				tfTarefa.setText(tarefa.getDescricao());
				tfComments.setText(tarefa.getComentario());
				tfStatus.setText(tarefa.getStatus().toString());
				dateLimit.setValue(tarefa.getDataLimite());
				dateLimit.setDisable(true);
				btConcluir.setDisable(false);
				btDelete.setDisable(false);
				btDate.setDisable(false);
				dateLimit.setOpacity(1);

				tfCode.setText(tarefa.getId() + "");

				if (tarefa.getStatus() == StatusTarefa.CONCLUIDA) {
					lbData.setText("Data para conclusão");
					dateLimit.setValue(tarefa.getDataFinalizada());

					btConcluir.setDisable(true);
					btDate.setDisable(true);
					btSalvar.setDisable(true);
					tfTarefa.setEditable(false);
					tfComments.setEditable(false);
				} else {
					btConcluir.setDisable(false);
					btEdit1.setDisable(false);
					btSalvar.setDisable(false);
					tfTarefa.setEditable(true);
					tfComments.setEditable(true);
					lbData.setText("Data para realização");
				}
			}
		});

		carregarTarefas();
		atualizaId();
	}

	private void atualizaId() {
		try {
			tfCode.setText(TarefaIO.proximoId() + "");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
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
		if (evento.getCode().equals(KeyCode.ENTER)) {
			evento.consume();
			btSaveClick();
		}
	}

	@Override
	public void changed(ObservableValue<? extends Tarefa> observable, Tarefa oldValue, Tarefa newValue) {
		// TODO Auto-generated method stub

	}

}
