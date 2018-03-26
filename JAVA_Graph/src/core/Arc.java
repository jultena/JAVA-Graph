package core;


public class Arc {
	
	private Sommet sommet ;
	private int longueur ;
	private Descripteur desc ;
	
	public Arc (Sommet sommet, int longue, Descripteur descripteur){
		this.sommet = sommet ;
		this.longueur = longue ;
		this.desc = descripteur ;
	}
	
	public Descripteur getDesc (){
		return this.desc ;
	}
	
	public Sommet getSommet (){
		return this.sommet ;
	}

	public int getLongueur (){
		return this.longueur ;
	}
	
	
	/**
	 * @return Temps minimum pour parcourir l'arc en *******.
	 */
	public float getTempsMin(int t_ou_d) {
		float vit ;
		int longue ;
		float temppiet;
		float vit2;
		if (t_ou_d==1){
			return this.longueur ;
		}
		else if (t_ou_d ==0){
		longue = this.longueur ;
		vit = (float) 1000*desc.getVitesse()/60 ;
		return (float) longue/vit;
		}
		else {

			longue=this.longueur;
			vit2 = (float)(4000/60);
			temppiet = (float)(longue/vit2);
			return temppiet;
		}
	}
}
