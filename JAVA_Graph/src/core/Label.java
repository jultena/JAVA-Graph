package core;

public class Label implements Comparable<Label>{
	
	protected float cost ;
	
	protected int father ;
	
	protected boolean marked ;
	
	protected int numSommet ;
	
	protected Sommet sommet ; 
	
	protected float mancost ; 
	
	public Label(float c , int f, int num, Sommet som, float mancost){
		this.cost = c ;
		this.father = f ;
		this.marked = false ;
		this.numSommet = num ;
		this.sommet = som ;
		this.mancost =mancost ;
		
	}
	
	public float getCost(){
		return this.cost ;
	}
	
	public int getFather(){
		return this.father ;
	}
	
	public boolean getMarked(){
		return this.marked ;
	}
	
	public Sommet getSom() {
		return this.sommet ;
	}
	
	public int getnumSommet(){
		return this.numSommet ;
	}

	
	
	public void modifSom(int i){
		this.numSommet = i;
	}
	public void modifMarked(boolean b){
		this.marked = b;
	}
	public void modifFather(int i){
		this.father = i;
	}
	public void modifCost(float i){
		this.cost = i;
	}
	
	public float getManCost() {
		return this.mancost ;
	}
	
	public void modifManCost(float modif){
		this.mancost = modif ;
	}
	
	
	
	public int compareTo(Label autre){
		if (this.cost+this.mancost > autre.cost+autre.mancost) {
			return 1;
		}
		if (this.cost+this.mancost == autre.cost+autre.mancost){
			if(this.mancost>autre.mancost){return 1;}
			else {return -1;}
			
		}
	return -1;
	}
	
	
}
