package it.polito.tdp.PremierLeague.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.PremierLeague.db.PremierLeagueDAO;

public class Model {
	private PremierLeagueDAO dao;
	private SimpleDirectedWeightedGraph<Player, DefaultWeightedEdge> grafo;
	private Map<Integer, Player> idMap;
	private List<Player> vertici;
	private Player bestPlayer;
	private Simulazione sim;
	private Match matchSelezionato;
	
	public Model() {
		dao = new PremierLeagueDAO();	
		idMap = new HashMap<>();
		sim = new Simulazione();
		matchSelezionato = null;
		bestPlayer = null;
		
		for(Player p : dao.listAllPlayers()) {
			idMap.put(p.getPlayerID(), p);
		}
	}
	
	public List<Match> getMatchs() {
		List<Match> matchs = dao.listAllMatches();
		Collections.sort(matchs);
		return matchs;
	}
	
	public void creaGrafo(Match m) {
		grafo = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
		vertici = new ArrayList<>();
		matchSelezionato = m;
		
		for(Integer id : dao.getIdPlayerVertici(m)) {
			vertici.add(idMap.get(id));
		}
		
		Graphs.addAllVertices(grafo, vertici);
		dao.setEfficienzaPlayer(m, idMap);
		
		List<Adiacenza> adiacenze = dao.getAdiacenzas(m, idMap);
		
		for(Adiacenza adiacenza : adiacenze) {
			Player p1 = adiacenza.getP1();
			Player p2 = adiacenza.getP2();
			double peso = 0;
			
			if(p1.getEff() > p2.getEff()) {
				peso = p1.getEff() - p2.getEff();
				adiacenza.setPeso(peso);
				if(!grafo.containsEdge(p1, p2))
					Graphs.addEdgeWithVertices(grafo, p1, p2, peso);
			}
			else {
				peso = p2.getEff() - p1.getEff();
				adiacenza.setPeso(peso);
				if(!grafo.containsEdge(p2, p1))
					Graphs.addEdgeWithVertices(grafo, p2, p1, peso);
			}
		}		
	}
	
	public int nVertici() {
		return grafo.vertexSet().size();
	}
	
	public int nArchi() {
		return grafo.edgeSet().size();
	}
	
	public Player getGiocatoreMigliore() {
		double bestEff = 0;
		
		for(Player p : grafo.vertexSet()) {
			double pesoOut = 0;
			double pesoIn = 0;
			double delta = 0;
			
			for(DefaultWeightedEdge edge : grafo.outgoingEdgesOf(p)) {
				pesoOut += grafo.getEdgeWeight(edge);
			}
			
			for(DefaultWeightedEdge edge : grafo.incomingEdgesOf(p)) {
				pesoIn += grafo.getEdgeWeight(edge);				
			}
			
			delta = pesoOut-pesoIn;
			p.setDeltaP(delta);
			
			if(p.getDeltaP() > bestEff) {
				bestEff = p.getDeltaP();
				bestPlayer = p;
			}
		}
		
		return bestPlayer;
	}

	public Integer teamIDPlayerBest(Player b) {
		return dao.getTeamID(b);
	}
	
	public Statistiche simula(int num, Match m) {
		bestPlayer = getGiocatoreMigliore();
		sim.init(num, bestPlayer, m);
		sim.run();
		return sim.getStatistiche();
	}
}
