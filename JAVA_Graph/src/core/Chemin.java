package core;

import java.util.ArrayList;


public class Chemin {
	
	private Sommet somD ;
	private Sommet somA ;
	private int nbNoeuds ;
	private ArrayList <Sommet> somm ;
	
	public Chemin(Sommet depart , Sommet arrivee, int nb){
		this.somD = depart ;
		this.somA = arrivee ;
		this.nbNoeuds = nb ;
		this.somm = new ArrayList<Sommet>();
	}
	
	public void add(Sommet sommet){
		this.somm.add(sommet) ;
	}
	
	public float calcul(){
		float tps = 0 ;

		
		for(int i = 0 ; i<nbNoeuds-1; i++){ 
			tps = tps + somm.get(i).tpsSom(somm.get(i+1)) ;			
		}	
return tps;
	}

}
