package it.polito.tdp.imdb.model;

import java.util.Comparator;

public class RegistaPerCodice  implements Comparator<Director>{
	@Override
	public int compare(Director d1, Director d2){
		return   d1.getId()-d2.getId();
	}
}
