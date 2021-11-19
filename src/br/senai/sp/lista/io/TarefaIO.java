package br.senai.sp.lista.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import br.senai.sp.lista.model.Tarefa;

public class TarefaIO {
	private static final String FOLDER = System.getProperty("user.home") + "/todolist";
	private static final String FILE_IDS = FOLDER + "/id.csv";
	private static final String FILE_TAREFA = FOLDER + "/tarefa.csv";

	public static void createFiles() {
		try {
			File pasta = new File(FOLDER);
			File arquivoIds = new File(FILE_IDS);
			File arquivoTarefas = new File(FILE_TAREFA);
			if (!pasta.exists()) {
				pasta.mkdir();
				arquivoIds.createNewFile();
				arquivoTarefas.createNewFile();
				FileWriter writer = new FileWriter(arquivoIds);
				writer.write("1");
				writer.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void insert(Tarefa tarefa) throws FileNotFoundException, IOException {
		File arquivoTarefas = new File(FILE_TAREFA);
		File arquivoIds = new File(FILE_IDS);
		// ler o último id no file_ids
		Scanner leitor = new Scanner(arquivoIds);
		tarefa.setId(leitor.nextLong());
		leitor.close();
		// grava a tarefa no arquivo
		FileWriter writer = new FileWriter(arquivoTarefas, true);
		writer.append(tarefa.formatToSave());
		writer.close();
		// grava o novo "proximo id" no arquivo de ids
		writer = new FileWriter(arquivoIds);
		writer.write((tarefa.getId() + 1) + "");
		writer.close();
	}
	
	public static List<Tarefa> readTarefas() throws FileNotFoundException {
		File arquivoTarefas = new File(FILE_TAREFA);
		List<Tarefa> tarefas = new ArrayList<>();
		FileReader reader = new FileReader(arquivoTarefas);
		BufferedReader buff = new BufferedReader(reader);
		return null;
	}
	
}
