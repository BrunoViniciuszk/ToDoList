package br.senai.sp.lista.model;

public enum StatusTarefa {
	ABERTA("ABERTA"), 
	CONCLUIDA("CONCLU�DA"), 
	ADIADA("ADIADA");
	
	String descricao;
	
	private StatusTarefa(String desc) {
		this.descricao = desc;
	}
	
	@Override
	public String toString() { 
		return descricao;
	}
	
}
