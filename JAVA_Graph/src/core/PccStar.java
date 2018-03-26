package core ;

import java.awt.Color;
import java.io.* ;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import base.Readarg ;

import java.lang.System ;

public class PccStar extends Algo {

    // Numero des sommets origine et destination
    protected int zoneOrigine ;
    protected int somDepart ;

    protected int zoneDestination ;
    protected int somArrivee ;
    
    //Variables de test
    private int nbSommetParcouru = 0 ;
    private int nbSommetMaxTas = 0 ;
    private int t_ou_d;
    
   // private Label tabLabel[] ;
    
    private BinaryHeap<Label> tas;
    
	
    public PccStar(Graphe gr, PrintStream sortie, Readarg readarg) {
	super(gr, sortie, readarg) ;
	
	this.zoneOrigine = gr.getZone () ;
	this.somDepart = readarg.lireInt ("Numero du sommet d'origine ? ") ;

	// Demander la zone et le sommet destination.
	this.zoneOrigine = gr.getZone () ;
	this.somArrivee = readarg.lireInt ("Numero du sommet destination ? ");
	this.t_ou_d = readarg.lireInt("Choisir en distance(1) ou en temps(0)");

	this.tas = new BinaryHeap<Label>(); 
    }
	
  //******************************************************************************************************//  

    public void run() {
    	


	System.out.println("Run PCC-Star de " + zoneOrigine + ":" + somDepart + " vers " + zoneDestination + ":" + somArrivee) ;
	
    Map<Integer, Label> maplabels = new HashMap<>();
    
    if (somDepart < 0 || somDepart >= graphe.getnb_Noeuds()){
   	 throw new IllegalStateException("Le sommet de départ n'existe pas !");
    }
    if (somArrivee < 0 || somArrivee >= graphe.getnb_Noeuds()){
   	 throw new IllegalStateException("Le sommet d'arrivée n'existe pas !");
    }
    

    Label arrivee = new Label(Float.MAX_VALUE, 0 ,somArrivee, graphe.getSommet(somArrivee), 0);
    maplabels.put(somArrivee, arrivee);
    Label depart = new Label(0, 0, somDepart, graphe.getSommet(somDepart),Float.MAX_VALUE);
    maplabels.put(somDepart, depart);
    long debut = System.currentTimeMillis() ;
    tas.insert(maplabels.get(somDepart));

    Label actuel = null;
	
	while (maplabels.get(somArrivee).getMarked()==false && !tas.isEmpty()){
		
		if(nbSommetMaxTas < tas.size()){nbSommetMaxTas = tas.size();}
		
        actuel = tas.deleteMin();
        //System.out.println("on vient de passer par : " + actuel.getnumSommet() + "\n en mettant un temps de "+ actuel.getCost());
        //penser a le mettre dans une liste pour faire le chamein
        actuel.modifMarked(true);
        int father = actuel.getnumSommet();
        graphe.dessin.setColor(Color.BLUE);
		graphe.dessin.drawPoint(actuel.getSom().getLongi(), actuel.getSom().getLati(), 5 );
        

		
		

		for(Arc a : maplabels.get(father).getSom().getArc()){
			
			
			Sommet next = a.getSommet();
			float mancost = (float)Graphe.distance(next.getLongi(), next.getLati(), maplabels.get(somArrivee).getSom().getLongi(), maplabels.get(somArrivee).getSom().getLati());
			if (t_ou_d == 0){
				//en fonction de la vitesse le mancost est plus ou moins pris en compte 
			float vitmoy = (70*1000)/60;
			float mancostv = mancost/vitmoy;
			mancost = mancostv;
			//System.out.println("mancost : " + mancost);
			}
			
			if (maplabels.get(next.getnumSommet())==null){
				
				Label nouveau = new Label(maplabels.get(father).getCost()+a.getTempsMin(t_ou_d), father, next.getnumSommet(), next, mancost); 

				maplabels.put(next.getnumSommet(),nouveau);
				tas.insert(maplabels.get(next.getnumSommet()));	
		        graphe.dessin.setColor(Color.CYAN);
				graphe.dessin.drawPoint(next.getLongi(), next.getLati(), 3 );
				nbSommetParcouru++ ;
			}
			
			else {
				if ( maplabels.get(father).getCost()+a.getTempsMin(t_ou_d) < maplabels.get(next.getnumSommet()).getCost()){
					if (maplabels.get(next.getnumSommet()).getCost() == Float.MAX_VALUE){
						nbSommetParcouru++ ;
					}
					maplabels.get(next.getnumSommet()).modifFather(father);
					maplabels.get(next.getnumSommet()).modifCost(maplabels.get(father).getCost()+a.getTempsMin(t_ou_d));
					tas.insertOrUpdate(maplabels.get(next.getnumSommet()));
					
				}
			}	
		}
		
	}
	long fin = System.currentTimeMillis() ; //on ne compte que le temps de calcul
	
	if (tas.isEmpty()){
	   	 throw new IllegalStateException("Le sommet d'arrivée n'a pas pu être atteint");
	} ;
	
	 Vector<Label> tabchem = new Vector<Label>() ;
	 Label actual = maplabels.get(somArrivee);
	 tabchem.add(actual);
	 int t=0;

	 while (actual.getFather()!=0){
		 actual = maplabels.get(actual.getFather());
		 tabchem.add(actual);
		 t++;
	 }
	 int a ;
	 int x=1;
	 for (a=t; a>=0; a--){
		 System.out.println("On passe en " + x + " par le Sommet : "+ tabchem.elementAt(a).getnumSommet());
		 x++;
	 }
	
	 graphe.dessin.setColor(Color.RED);
	 graphe.dessin.drawPoint(maplabels.get(somArrivee).getSom().getLongi(),maplabels.get(somArrivee).getSom().getLati(), 5 );
	System.out.println("Nombre de sommets max dans le tas : " + nbSommetMaxTas) ;
	System.out.println("Nombre de sommets parcourus : " + nbSommetParcouru) ;
	System.out.println("Cout total par Astar : " + maplabels.get(somArrivee).getCost());
	
	
	System.out.println("Méthode exécutée en " + Long.toString(fin - debut) + " millisecondes");
    }

}

