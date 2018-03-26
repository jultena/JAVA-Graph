package core ;

import java.awt.Color;
import java.io.* ;
import base.Readarg ;
import java.util.Vector;
public class Pcc extends Algo {

    // Numero des sommets origine et destination
    protected int zoneOrigine ;
    protected int somDepart ;

    protected int zoneDestination ;
    protected int somArrivee ;
    
    // Variable de test
    private int nbSommetMaxTas ;
    private int nbSommetParcouru = 0 ;
    
    private int t_ou_d;



    private Label tabLabel[] ;
    
    private BinaryHeap<Label> tas;
    
    public Pcc(Graphe gr, PrintStream sortie, Readarg readarg) {
	super(gr, sortie, readarg) ;

	this.zoneOrigine = gr.getZone () ;
	this.somDepart = readarg.lireInt ("Numero du sommet d'origine ? ") ;

	// Demander la zone et le sommet destination.
	this.zoneOrigine = gr.getZone () ;
	this.somArrivee = readarg.lireInt ("Numero du sommet destination ? ");
	this.t_ou_d = readarg.lireInt("Choisir en distance(1) ou en temps(0)");
	
	this.tabLabel = new Label[graphe.getnb_Noeuds()] ;

	for (int i=0; i<graphe.getnb_Noeuds(); i++){
		tabLabel[i]= new Label(Float.MAX_VALUE, 0 , i , gr.getSommet(i), 0); //faire un getter d'un sommet on a un tableau de sommet
	
	}
	this.tas = new BinaryHeap<Label>(); 
    }
    
    //****************************************************************************************

    
    
    public Pcc(Graphe gr, PrintStream sortie, Readarg readarg, int dep, int arr) {
    	super(gr, sortie, readarg) ;

    	this.zoneOrigine = gr.getZone () ;
    	this.somDepart = dep ;

    	// Demander la zone et le sommet destination.
    	this.zoneOrigine = gr.getZone () ;
    	this.somArrivee = arr;
    	
    	this.t_ou_d = 0;
    	
    	this.tabLabel = new Label[graphe.getnb_Noeuds()] ;

    	for (int i=0; i<graphe.getnb_Noeuds(); i++){
    		tabLabel[i]= new Label(Float.MAX_VALUE, 0 , i , gr.getSommet(i), 0); //faire un getter d'un sommet on a un tableau de sommet
    	
    	}
    	this.tas = new BinaryHeap<Label>(); 
        }
    
    
    
    

    public void run() {


	System.out.println("Run PCC de " + zoneOrigine + ":" + somDepart + " vers " + zoneDestination + ":" + somArrivee) ;
	

			int father = 0 ;

		     if (somDepart < 0 || somDepart >= graphe.getnb_Noeuds()){
		    	 throw new IllegalStateException("Le sommet de départ n'existe pas !");
		     }
		     if (somArrivee < 0 || somArrivee >= graphe.getnb_Noeuds()){
		    	 throw new IllegalStateException("Le sommet d'arrivée n'existe pas !");
		     }

			
		    long debut = System.currentTimeMillis() ;
			tabLabel[somDepart].modifCost(0);
			tas.insert(tabLabel[somDepart]);
			nbSommetParcouru++ ;
			
			while (!tas.isEmpty() && tabLabel[somArrivee].getMarked()==false){
				float cost ;
				Label labelActuel ;
				if(nbSommetMaxTas< tas.size()){nbSommetMaxTas = tas.size();}
				//on pourra eventuellement faire un tas.findMin() et mettre le label qq part
				tabLabel[tas.findMin().getnumSommet()].modifMarked(true);
				//on pourra creer une liste qui retient tous les label par lesquels on passe
				labelActuel =tas.findMin() ;
				father = labelActuel.getnumSommet();
				graphe.dessin.setColor(Color.GREEN);
				graphe.dessin.drawPoint(labelActuel.getSom().getLongi(),labelActuel.getSom().getLati(), 5 );
				tas.deleteMin();
				
				
				for(Arc a : tabLabel[father].getSom().getArc()){
					
					if (tabLabel[a.getSommet().getnumSommet()].getMarked()==false) {
						
						if (tas.getArray().indexOf(tabLabel[a.getSommet().getnumSommet()])==-1){          //si le suivant n'est pas deja dans le tas on l'insere
							tabLabel[a.getSommet().getnumSommet()].modifCost(tabLabel[father].getCost()+a.getTempsMin(t_ou_d));
							tabLabel[a.getSommet().getnumSommet()].modifFather(father);
							tas.insert(tabLabel[a.getSommet().getnumSommet()]);	
							nbSommetParcouru++ ;
							
							graphe.dessin.setColor(Color.ORANGE);
							graphe.dessin.drawPoint(a.getSommet().getLongi(), a.getSommet().getLati(), 5 );
						}
							
						cost = tabLabel[father].getCost()+a.getTempsMin(t_ou_d) ;
						
						if (cost < tabLabel[a.getSommet().getnumSommet()].getCost() ){          //sinon si le nouveau coup est plus bas on met a jour 
							//int index = tas.getArray().indexOf(tabLabel[a.getSommet().getnumSommet()]);
							tabLabel[a.getSommet().getnumSommet()].modifFather(father);
							tabLabel[a.getSommet().getnumSommet()].modifCost(tabLabel[father].getCost()+a.getTempsMin(t_ou_d));
							tas.update(tabLabel[a.getSommet().getnumSommet()]);
						}
						
						
					
					
				}
				
			}
					
		}
			long fin = System.currentTimeMillis() ; //on ne compte que le temps de calcul
			if (tabLabel[somArrivee].getMarked() == false){
			   	 throw new IllegalStateException("Le sommet d'arrivée n'a pas pu être atteint");
			    } ;   
			 
			 
			 //Label tabChem[] = new Label[nbSomChem];
			 Vector<Label> tabchem = new Vector<Label>() ;
			 Label actuel = tabLabel[somArrivee];
			 tabchem.add(actuel);
			 int t=0;
		
			 while (actuel.getFather()!=0){
				 actuel = tabLabel[actuel.getFather()];
				 tabchem.add(actuel);
				 t++;
			 }
			 
			 int a ;
			 int x=1;
			 for (a=t; a>=0; a--){
				 System.out.println("On passe en " + x + " par le Sommet : "+ tabchem.elementAt(a).getnumSommet());
				 x++;
			 }
			 graphe.dessin.setColor(Color.RED);
			 graphe.dessin.drawPoint(tabLabel[somDepart].getSom().getLongi(),tabLabel[somArrivee].getSom().getLati(), 5 );
			 System.out.println("Nombre de sommets max dans le tas : " + nbSommetMaxTas) ;
			 System.out.println("Nombre de sommets parcourus : " + nbSommetParcouru) ;
			 System.out.println("Cout total par Dijkstra : " + tabLabel[somArrivee].getCost());
			 System.out.println("Méthode exécutée en " + Long.toString(fin - debut) + " millisecondes");

    }

}
