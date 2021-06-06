package it.polito.tdp.imdb.model;

public class Corrispondenza {
   Director d2;
   int peso;
public Corrispondenza(Director d2, int peso) {
	this.d2 = d2;
	this.peso = peso;
}
public Director getD2() {
	return d2;
}
public void setD2(Director d2) {
	this.d2 = d2;
}
public int getPeso() {
	return peso;
}
public void setPeso(int peso) {
	this.peso = peso;
}
@Override
public String toString() {
	return d2 + " " + peso ;
}
   
}
