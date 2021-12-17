package br.senai.sp.lista.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javafx.scene.image.Image;

public class Tarefa implements Comparable<Tarefa> {
	private long id;
	private LocalDate dataCriacao;
	private LocalDate dataLimite;
	private LocalDate dataFinalizada;
	private String descricao;
	private String comentario;
	private StatusTarefa status;
	
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public LocalDate getDataCriacao() {
		return dataCriacao;
	}
	public void setDataCriacao(LocalDate dataCriacao) {
		this.dataCriacao = dataCriacao;
	}
	public LocalDate getDataLimite() {
		return dataLimite;
	}
	public void setDataLimite(LocalDate dataLimite) {
		this.dataLimite = dataLimite;
	}
	public LocalDate getDataFinalizada() {
		return dataFinalizada;
	}
	public void setDataFinalizada(LocalDate dataFinalizada) {
		this.dataFinalizada = dataFinalizada;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public String getComentario() {
		return comentario;
	}
	public void setComentario(String comentario) {
		this.comentario = comentario;
	}
	public StatusTarefa getStatus() {
		return status;
	}
	public void setStatus(StatusTarefa status) {
		this.status = status;
	}
	
	public String formatToSave() {
		StringBuilder builder = new StringBuilder();
		DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		
		builder.append(this.getId()+";");
		builder.append(this.getDataCriacao().format(fmt)+";");
		builder.append(this.getDataLimite().format(fmt)+";");
		if(this.getDataFinalizada() != null) {
			builder.append(this.getDataFinalizada().format(fmt));
		}
		builder.append(";");
		builder.append(this.getDescricao()+";");
		builder.append(this.getComentario()+";");
		builder.append(this.getStatus().ordinal()+"\n");
		return builder.toString();
	}
	@Override
	public int compareTo(Tarefa o) {
		if(this.getDataLimite().isBefore(o.getDataLimite())) {
			return -1;
		}else if(this.getDataLimite().isAfter(o.getDataLimite())) {
			return 1;
		}else {
			return this.getDescricao().compareTo(o.getDescricao());
		}
		
	}
}
