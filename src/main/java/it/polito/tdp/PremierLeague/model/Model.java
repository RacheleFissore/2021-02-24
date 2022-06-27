package it.polito.tdp.PremierLeague.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.PremierLeague.db.PremierLeagueDAO;

public class Model {

	private PremierLeagueDAO dao;
	private Graph<Player, DefaultWeightedEdge> grafo;
	private Map<Integer, Player> idMap;
	private Player best;
	
	public Model() {
		dao = new PremierLeagueDAO();
		idMap = new HashMap<>();
		
		for(Player player : dao.listAllPlayers()) {
			idMap.put(player.getPlayerID(), player);			
		}
	}
	
	public List<Match> getMatchs() {
		return dao.listAllMatches();
	}
	
	public void creaGrafo(Match match) {
		grafo = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
		Graphs.addAllVertices(grafo, dao.getVertici(idMap, match));
		
		for(Adiacenza adiacenza : dao.getArchi(idMap, match)) {
			if(grafo.containsVertex(adiacenza.getP1()) && grafo.containsVertex(adiacenza.getP2()) && grafo.getEdge(adiacenza.getP1(), adiacenza.getP2()) == null) {
				if(adiacenza.getPeso() < 0) {
					Graphs.addEdgeWithVertices(grafo, adiacenza.getP2(), adiacenza.getP1(), -1*adiacenza.getPeso());
					System.out.println(-1*adiacenza.getPeso() + "\n");
				}
				else {
					Graphs.addEdgeWithVertices(grafo, adiacenza.getP1(), adiacenza.getP2(), adiacenza.getPeso());
					System.out.println(adiacenza.getPeso() + "\n");
				}
				
				

			}
		}
	}

	public int getNVertici() {
		return grafo.vertexSet().size();
	}

	public int getNArchi() {
		return grafo.edgeSet().size();
	}

	public String getGiocatoreMigliore() {
		best = null;
		double max = 0.0;
		
		
		for(Player player : grafo.vertexSet()) {
			double somma = 0.0;
			for(DefaultWeightedEdge edge : grafo.outgoingEdgesOf(player)) {
				somma += grafo.getEdgeWeight(edge);
			}
			
			for(DefaultWeightedEdge edge : grafo.incomingEdgesOf(player)) {
				somma -= grafo.getEdgeWeight(edge);
			}
			
			if(max < somma) {
				max = somma;
				best = player;
			}
		}
		return best.toString() + ", delta efficienza = " + max;
	}
	
}
