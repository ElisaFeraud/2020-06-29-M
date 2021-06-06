package it.polito.tdp.imdb.model;
import java.util.Comparator;
public class CorrispondenzaPerPeso implements Comparator<Corrispondenza>{
	@Override
	public int compare(Corrispondenza c1, Corrispondenza c2){
		return   Double.compare(c1.getPeso(), c2.getPeso());
	}
}


	