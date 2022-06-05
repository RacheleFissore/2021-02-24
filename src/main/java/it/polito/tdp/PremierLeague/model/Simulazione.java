package it.polito.tdp.PremierLeague.model;

import java.time.Duration;
import java.util.PriorityQueue;
import it.polito.tdp.PremierLeague.model.Event.EventType;

public class Simulazione {
	// Coda degli eventi
	private PriorityQueue<Event> queue;
	
	// Statistiche --> permettono di ritornare i risultati
	private Statistiche statistiche;
	
	// Parametri della simulazione 
	private Match match;
	private int N; // Numero di azioni salienti da simulare per quel match
	private Model model;
	int teamBest; // Sarebbe il team1
	int team2;
	int numGBest;
	int numG2;
	
	public void init(int num, Player best, Match m) {
		model = new Model();
		
		team2 = -1;
		numGBest = 0;
		numG2 = 0;
		teamBest = model.teamIDPlayerBest(best);
		match = m;
		N = num;
		
		this.queue = new PriorityQueue<Event>();
		this.statistiche = new Statistiche();
		assegnaTeam();
		creaEventi();		
	}
	
	private void assegnaTeam() {
		if(teamBest == match.getTeamAwayID()) { // In base a quale è il teamBest capisco qual è il team2
			team2 = match.getTeamHomeID();
			numGBest = match.getTeamAwayFormation(); // Numero di giocatori squadra 1
			numG2 = match.getTeamHomeFormation(); // Numero giocatori squadra 2
		}
		else {
			team2 = match.getTeamAwayID();
			numG2 = match.getTeamAwayFormation();
			numGBest = match.getTeamHomeFormation();
		}
	}

	public void run() {
		while(!queue.isEmpty()) {
			Event e = queue.poll();
			processaEvento(e); // Eseguo l'evento
		}
	}
	
	private void creaEventi() {
		int numInf = 0;
		Duration durata= Duration.ofMinutes(0);
		for (int i = 0; i < this.N; i++) {
			// Math.random(): numero tra 0 e 0.9999
			double prob = Math.random(); 
			
			if(prob <= 0.5) {
				// Il 50% delle volte faccio GOAL
				durata = Duration.ofMinutes((int)(Math.random()*90+1));
				int numPersoneInCampo1 = numGBest - statistiche.getEspulsi1();
				int numPersoneInCampo2 = numG2 - statistiche.getEspulsi2();
				
				if((numPersoneInCampo1 == numPersoneInCampo2) || (numPersoneInCampo1 > numPersoneInCampo2)) {
					Event e = new Event(EventType.GOAL, durata, teamBest);
					this.queue.add(e);
				}
				else {
					Event e = new Event(EventType.GOAL, durata, team2);
					this.queue.add(e);
				}				
			}
			else if(prob > 0.5 && prob <= 0.8) {
				// Il 30% delle volte si verifica una ESPULSIONE
				
				double espRandom = Math.random();
				int teamEspluso = -1;
				durata = Duration.ofMinutes((int)(Math.random()*90+1)); // Il momento in cui si verifica l'espulsione è un numero random in 90 minuti
				if(espRandom <= 0.6) {
					teamEspluso = teamBest;
					Event e = new Event(EventType.ESPULSIONE, durata, teamEspluso);
					this.queue.add(e);
				}
				else {
					if(match.getTeamHomeID() == teamBest) {
						teamEspluso = match.getTeamAwayID();
					}
					else {
						teamEspluso = match.getTeamHomeID();
					}
					
					Event e = new Event(EventType.ESPULSIONE, durata, teamEspluso);
					this.queue.add(e);
				}
			}
			else {
				// Il 20% delle volte si verifica un INFORTUNIO
				numInf++;
				double azioniRandom = Math.random();
				if(azioniRandom <= 0.5) {
					N += 2;
				}
				else {
					N += 3;
				}
				
				durata = Duration.ofMinutes(90 + numInf); // Il tempo di recupero aumenta proporzionalmente al numero di infortuni
				Event e = new Event(EventType.INFORTUNIO, durata, match.getTeamAwayID());
				queue.add(e);
			}
			
		}
	}

	private void processaEvento(Event e) {
		int squadra = e.getTeam();
		switch (e.getType()) {
			case GOAL:
				if(squadra == teamBest) {
					statistiche.incrementaGoal1();
				}
				else {
					statistiche.incrementaGoal2();
				}
				break;
	
			case ESPULSIONE:
				if(squadra == teamBest) {
					statistiche.incrementaEsp1();
				}
				else {
					statistiche.incrementaEsp2();
				}
				break;
			default:
				break;
		}	
	}
	
	public Statistiche getStatistiche() {
		return this.statistiche;
	}
}
