package it.polito.tdp.PremierLeague.model;

public class Statistiche { // Fanno riferimento ad un match
	// Classe che mi serve per stampare l'output
	
	// La squadra 1 è quella che contiene il giocatore migliore
	private int goal1;
	private int goal2;
	private int espulsi1;
	private int espulsi2;
	
	public Statistiche() { // Alla creazione dell'oggetto questi parametri saranno a 0 e poi verranno incrementati con opportuni metodi
		super();
		this.goal1 = 0;
		this.goal2 = 0;
		this.espulsi1 = 0;
		this.espulsi2 = 0;
	}	
	
	// Creo i metodi per incrementare
	public void incrementaGoal1() {
		goal1++;
	}
	
	public void incrementaGoal2() {
		goal2++;
	}
	
	public void incrementaEsp1() {
		espulsi1++;
	}
	
	public void incrementaEsp2() {
		espulsi2++;
	}

	public int getGoal1() {
		return goal1;
	}

	public int getGoal2() {
		return goal2;
	}

	public int getEspulsi1() {
		return espulsi1;
	}

	public int getEspulsi2() {
		return espulsi2;
	}

	@Override
	public String toString() {
		return "Statistiche [goal1=" + goal1 + ", goal2=" + goal2 + ", espulsi1=" + espulsi1 + ", espulsi2=" + espulsi2
				+ "]\n";
	}
	
	
}
