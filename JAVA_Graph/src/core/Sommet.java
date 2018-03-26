package core;
import java.util.ArrayList ;



public class Sommet {
	
	private int numSommet ;
	private float longi ;
	private float lati ;
	private ArrayList <Arc> arc ;
	
	public Sommet(float longitude , float latitude, int num){		
		this.numSommet = num ;
		this.longi = longitude ;
		this.lati = latitude ;
		this.arc = new ArrayList<Arc>();
	}
	
	public void add(Sommet sommet, int longue, Descripteur descripteur){
		Arc nouv = new Arc(sommet, longue, descripteur) ;
		this.arc.add(nouv) ;
	}
	
	public int distSom(Sommet dest){
		int dist = Integer.MAX_VALUE;
		for(Arc a : this.arc){
			if(a.getSommet().equals(dest) && a.getLongueur() < dist) {
				dist = a.getLongueur();
			}
		}
		return dist ;
	}
	
	public float tpsSom(Sommet dest){
		float tps = Float.POSITIVE_INFINITY;
		for(Arc a : this.arc){
			if(a.getSommet().equals(dest) && a.getTempsMin(0) < tps) {
				tps = a.getTempsMin(0);
			}
		}
		return tps ;
	}
	
	public int getnumSommet(){
		return this.numSommet ;
	}
	
	public float getLongi(){
		return this.longi ;
	}
	
	public float getLati(){
		return this.lati ;
	}
	
	public ArrayList<Arc> getArc (){
		return this.arc ;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof Sommet) {
			Sommet s = (Sommet)o;
			return this.numSommet == s.numSommet;
		}
		return false;
	}
		
}
//Soi on rajoute un numéro de sommet dans la classe sommet (redondance avec le tableauy dans graphe)
// Ou un getter

