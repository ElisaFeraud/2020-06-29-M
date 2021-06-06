package it.polito.tdp.imdb.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;


import it.polito.tdp.imdb.db.ImdbDAO;

public class Model {
   ImdbDAO dao;
   Map<Integer,Director> idMap;
   Graph<Director,DefaultWeightedEdge> grafo;
   List<Director> best;
   int bestNumero;
   public Model() {
	   dao = new ImdbDAO();
	  
   }
   public void creaGrafo(int year) {
	   idMap = new HashMap<>();
	   this.grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
	   dao.getVertici(idMap, year);
	   Graphs.addAllVertices(this.grafo, idMap.values());
	   for(Collegamento c: dao.getArchi(idMap,year)) {
   		if(this.grafo.containsVertex(c.getD1()) && this.grafo.containsVertex(c.getD2())) {
   			DefaultWeightedEdge e = this.grafo.getEdge(c.getD1(),c.getD2());
   			if(e==null) {
   				Graphs.addEdgeWithVertices(grafo,c.getD1(),c.getD2(),c.getPeso());
   			}
   		}
   		}
	   
   }
   public String infoGrafo() {
		 return "Grafo creato con "+ this.grafo.vertexSet().size()+ " vertici e " + this.grafo.edgeSet().size()+" archi\n";
	 }
   public List<Director> getRegisti(){
	   List<Director> result = new LinkedList<>();
	   for(Director d: this.grafo.vertexSet()) {
		   result.add(d);
	   }
	   result.sort(new RegistaPerCodice());
	   return result;
   }
   public List<Corrispondenza> getRegistiAdiacenti(Director director){
	   List<Corrispondenza> vicini = new LinkedList<>();
		for(DefaultWeightedEdge d: this.grafo.edgesOf(director)) {
			Director d2=null;
			if(!this.grafo.getEdgeSource(d).equals(director) && this.grafo.getEdgeTarget(d).equals(director)) {
			   d2 = this.grafo.getEdgeSource(d);}
			else if(this.grafo.getEdgeSource(d).equals(director) && !this.grafo.getEdgeTarget(d).equals(director))
				 d2 =this.grafo.getEdgeTarget(d);
			  int peso =  (int) this.grafo.getEdgeWeight(d);
			  Corrispondenza c= new Corrispondenza(d2,peso);
			  vicini.add(c);
			 
		}
		 vicini.sort(new CorrispondenzaPerPeso().reversed());
		 return  vicini;
   }
   public List<Director> cerca(Director partenza,int n){
	   this.best = new ArrayList<>();
	   List<Director> parziale = new ArrayList<>();
	   parziale.add(partenza);
	   this.bestNumero=0;
	   this.recursive(parziale, new ArrayList<Director>(this.grafo.vertexSet()), n);
	   /*
	     this.bestDegree = 0;
		this.dreamTeam = new ArrayList<Player>();
		List<Player> partial = new ArrayList<Player>();
		
		
	    
	     */
	   best.remove(partenza);
	   return best;
   }
private void recursive(List<Director> parziale, ArrayList<Director> registi, int n) {
	// TODO Auto-generated method stub
	int somma = calcolaSomma(best);
	if(parziale.size()>best.size() ) {
		if(calcolaSomma(parziale)<=n && calcolaSomma(parziale)>somma) {
		best = new ArrayList<>(parziale);
		this.bestNumero = calcolaSomma(parziale);
		return;}
		
		
	}
	for(Director d : registi) {
		if(!parziale.contains(d)) {
			List<Director> lista = Graphs.neighborListOf(this.grafo, parziale.get(parziale.size()-1));
			if(lista.contains(d)) {
				parziale.add(d);
				List<Director> remainingRegisti = new ArrayList<>(registi);
				remainingRegisti.remove(d);
				recursive(parziale,(ArrayList<Director>) remainingRegisti,n);
			}
		}
	}
	/*
	 
		
		for(Player p : players) {
			if(!partial.contains(p)) {
				partial.add(p);
				//i "battuti" di p non possono più essere considerati
				List<Player> remainingPlayers = new ArrayList<>(players);
				remainingPlayers.removeAll(Graphs.successorListOf(graph, p));
				recursive(partial, remainingPlayers, k);
				partial.remove(p);
				
			}
		}
	 */
}
private int calcolaSomma(List<Director> parziale) {
	// TODO Auto-generated method stub
	int peso = 0;
	if(parziale.size()>1) {
	for(int i=0; i<parziale.size()-1;i++) {
	  	DefaultWeightedEdge e = this.grafo.getEdge(parziale.get(i), parziale.get(i+1));
	  	peso = (int) (peso + this.grafo.getEdgeWeight(e));				
		}
	}
	return peso;
}
 public int getBestNumero() {
	 return bestNumero;
 }  
}
