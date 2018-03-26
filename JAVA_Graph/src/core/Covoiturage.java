package core;

import java.awt.Color;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import base.Readarg;

public class Covoiturage extends Algo{
	
	 // Numero des sommets origine et destination
    protected int zoneOrigine ;
    protected int somDepartVoiture ;
    protected int somDepartPieton ;

    protected int zoneDestination ;
    protected int somArrivee ;
    
    private int t_ou_d; 
    
    private Label tabLabelv[] ;       	//voiture
    private BinaryHeap<Label> tasv;	
    
    private Label tabLabelp[] ;			//pieton
    private BinaryHeap<Label> tasp;	
    
    private BinaryHeap<Label> tas;		//A*
    
	
	public Covoiturage(Graphe gr, PrintStream sortie, Readarg readarg) {
		super(gr, sortie, readarg) ;
		
		this.zoneOrigine = gr.getZone () ;
		this.somDepartVoiture = readarg.lireInt ("Numero du sommet d'origine de la voiture ? ") ;
		
		this.zoneOrigine = gr.getZone () ;
		this.somDepartPieton = readarg.lireInt ("Numero du sommet d'origine du pieton ? ") ;

		this.zoneOrigine = gr.getZone () ;
		this.somArrivee = readarg.lireInt ("Numero du sommet destination ? ");
		
		this.t_ou_d = 0;
		
		//***********************************************************************************************
		//Declarations pour le premier dijkstra
		this.tabLabelv = new Label[graphe.getnb_Noeuds()];
		for (int i=0; i<graphe.getnb_Noeuds(); i++){
			tabLabelv[i]= new Label(Float.MAX_VALUE, 0 , i , gr.getSommet(i), 0); //faire un getter d'un sommet on a un tableau de sommet
		
		}
		this.tasv = new BinaryHeap<Label>(); 
		
		//Declarations pour le second Dijkstra
		this.tabLabelp = new Label[graphe.getnb_Noeuds()];
		for (int i=0; i<graphe.getnb_Noeuds(); i++){
			tabLabelp[i]= new Label(Float.MAX_VALUE, 0 , i , gr.getSommet(i), 0); //faire un getter d'un sommet on a un tableau de sommet
		
		}
		this.tasp = new BinaryHeap<Label>(); 
		
		//********************************************************************************************
		//Declarations pour le A*
		
		this.tas = new BinaryHeap<Label>(); 
    }
	
	
	
	//********************************************************************************************
	//############################################################################################
	//############################################################################################
	//********************************************************************************************
	
		public void run() {
			

			// on lance un Dijkstra classique voiture vers piéton
			
			int fatherv = 0 ;

		     if (somDepartVoiture < 0 || somDepartVoiture >= graphe.getnb_Noeuds()){
		    	 throw new IllegalStateException("Le sommet de départ n'existe pas !");
		     }
		     if (somDepartPieton < 0 || somDepartPieton >= graphe.getnb_Noeuds()){
		    	 throw new IllegalStateException("Le sommet d'arrivée n'existe pas !");
		     }

			
		   
			tabLabelv[somDepartVoiture].modifCost(0);
			tasv.insert(tabLabelv[somDepartVoiture]);

			
			while (!tasv.isEmpty() && tabLabelv[somDepartPieton].getMarked()==false){
				
				float cost ;
				Label labelActuel ;
				
				tabLabelv[tasv.findMin().getnumSommet()].modifMarked(true);
				
				labelActuel =tasv.findMin() ;
				fatherv = labelActuel.getnumSommet();
				tasv.deleteMin();
				
				
				for(Arc a : tabLabelv[fatherv].getSom().getArc()){
					
					if (tabLabelv[a.getSommet().getnumSommet()].getMarked()==false) {
						
						if (tasv.getArray().indexOf(tabLabelv[a.getSommet().getnumSommet()])==-1){          //si le suivant n'est pas deja dans le tasv on l'insere
							tabLabelv[a.getSommet().getnumSommet()].modifCost(tabLabelv[fatherv].getCost()+a.getTempsMin(t_ou_d));
							tabLabelv[a.getSommet().getnumSommet()].modifFather(fatherv);
							tasv.insert(tabLabelv[a.getSommet().getnumSommet()]);
							graphe.dessin.setColor(Color.BLUE);
							graphe.dessin.drawPoint(labelActuel.getSom().getLongi(),labelActuel.getSom().getLati(), 5 );
							
						}
							
						cost = tabLabelv[fatherv].getCost()+a.getTempsMin(t_ou_d) ;
						
						if (cost < tabLabelv[a.getSommet().getnumSommet()].getCost() ){          //sinon si le nouveau coup est plus bas on met a jour 
							//int index = tasv.getArray().indexOf(tabLabelv[a.getSommet().getnumSommet()]);
							tabLabelv[a.getSommet().getnumSommet()].modifFather(fatherv);
							tabLabelv[a.getSommet().getnumSommet()].modifCost(tabLabelv[fatherv].getCost()+a.getTempsMin(t_ou_d));
							tasv.update(tabLabelv[a.getSommet().getnumSommet()]);
						}

					
				}
				
			}
					
		}

			if (tabLabelv[somDepartPieton].getMarked() == false){
			   	 throw new IllegalStateException("Le sommet d'arrivée n'a pas pu être atteint");
			    } ;   
			    System.out.println("Cout total par Dijkstra voiture : " + tabLabelv[somDepartPieton].getCost());
			 
		/*	 
			 Vector<Label> tabchemv = new Vector<Label>() ;
			 Label actuel = tabLabelv[somDepartPieton];
			 tabchemv.add(actuel);
			 int t=0;
		
			 while (actuel.getFather()!=0){
				 actuel = tabLabelv[actuel.getFather()];
				 tabchemv.add(actuel);
				 t++;
			 }*/
			
		
		//********************************************************************************************
		//############################################################################################
		//############################################################################################
		//********************************************************************************************
		
		//On lance le Dijkstra pieton
		
		int fatherp = 0 ;

	     if (somDepartPieton < 0 || somDepartPieton >= graphe.getnb_Noeuds()){
	    	 throw new IllegalStateException("Le sommet de départ n'existe pas !");
	     }
	     if (somDepartVoiture < 0 || somDepartVoiture >= graphe.getnb_Noeuds()){
	    	 throw new IllegalStateException("Le sommet d'arrivée n'existe pas !");
	     }

		
	   
		tabLabelp[somDepartPieton].modifCost(0);
		tasp.insert(tabLabelp[somDepartPieton]);

		
		while (!tasp.isEmpty() && tabLabelp[somDepartVoiture].getMarked()==false){
			float costp ;
			Label labelActuelp ;
			
			tabLabelp[tasp.findMin().getnumSommet()].modifMarked(true);
		
			labelActuelp =tasp.findMin() ;
			fatherp = labelActuelp.getnumSommet();
			tasp.deleteMin();
			
			
			for(Arc a : tabLabelp[fatherp].getSom().getArc()){
				
				if (tabLabelp[a.getSommet().getnumSommet()].getMarked()==false) {
					
					if (tasp.getArray().indexOf(tabLabelp[a.getSommet().getnumSommet()])==-1){          //si le suivant n'est pas deja dans le tasp on l'insere
						tabLabelp[a.getSommet().getnumSommet()].modifCost(tabLabelp[fatherp].getCost()+a.getTempsMin(2)); //on met t_ou_d a 2 pour pieton
						tabLabelp[a.getSommet().getnumSommet()].modifFather(fatherp);
						tasp.insert(tabLabelp[a.getSommet().getnumSommet()]);
						graphe.dessin.setColor(Color.CYAN);
						graphe.dessin.drawPoint(labelActuelp.getSom().getLongi(),labelActuelp.getSom().getLati(), 5 );
						
					}
						
					costp = tabLabelp[fatherp].getCost()+a.getTempsMin(2) ;
					
					if (costp < tabLabelp[a.getSommet().getnumSommet()].getCost() ){          //sinon si le nouveau coup est plus bas on met a jour 
						tabLabelp[a.getSommet().getnumSommet()].modifFather(fatherp);
						tabLabelp[a.getSommet().getnumSommet()].modifCost(tabLabelp[fatherp].getCost()+a.getTempsMin(2));
						tasp.update(tabLabelp[a.getSommet().getnumSommet()]);
					}

				
			}
			
		}
				
	}

		if (tabLabelp[somDepartVoiture].getMarked() == false){
		   	 throw new IllegalStateException("Le sommet d'arrivée n'a pas pu être atteint");
		    } ;   
		 
		    System.out.println("Cout total par Dijkstra pieton: " + tabLabelp[somDepartVoiture].getCost());
		 
		/* Vector<Label> tabchemp = new Vector<Label>() ;
		 Label actuelp = tabLabelp[somDepartVoiture];
		 tabchemp.add(actuel);
		 int tp=0;
	
		 while (actuel.getFather()!=0){
			 actuelp = tabLabelp[actuel.getFather()];
			 tabchemp.add(actuel);
			 tp++;
		 }*/
		
		 
		 	//********************************************************************************************
			//############################################################################################
			//############################################################################################
			//********************************************************************************************
			
		 
		 // on s'organise pour l'intersection des tableaux de chemins
		 // on va parcourir les tablabel et chercher le meilleur sommet pour ce rejoindre
		    
		    
		    
		    
		    float somme;
		    int bestSom=0;
		    float mancostf=0;
		    float vit=(float)(50*1000)/60;
		    float coutmax=Float.MAX_VALUE ;
		    float coutreel=0;
		    float sommin = Float.MAX_VALUE;
		    int i;
		    
		    for (i=0;i<graphe.getnb_Noeuds();i++){
		    	if (tabLabelv[i].getCost()==Float.MAX_VALUE ||tabLabelp[i].getCost()==Float.MAX_VALUE){}
		    	
		    	else {
		    		graphe.dessin.setColor(Color.GRAY);
		    		graphe.dessin.drawPoint(tabLabelv[i].getSom().getLongi(), tabLabelv[i].getSom().getLati(), 3 );
		    		//on rajoute ce mancostf si on veut en plus qu'ils se rejoignent le plus près possible du sommet de destination
		    		mancostf=(float)graphe.distance(tabLabelv[i].getSom().getLongi(), tabLabelv[i].getSom().getLati(), tabLabelv[somArrivee].getSom().getLongi(), tabLabelv[somArrivee].getSom().getLati());
		    		mancostf = mancostf/vit;
		    		if (tabLabelv[i].getCost()>=tabLabelp[i].getCost()){
		    			if (tabLabelv[i].getCost()+mancostf<=coutmax){
		    					coutmax = tabLabelv[i].getCost()+mancostf;
		    					coutreel = tabLabelv[i].getCost();
		    					bestSom = tabLabelv[i].getnumSommet();
		    			}
		    		}
		    		else {
		    			if(tabLabelp[i].getCost()+mancostf<=coutmax){
		    				coutmax= tabLabelp[i].getCost()+mancostf;
		    				coutreel = tabLabelv[i].getCost();
	    					bestSom = tabLabelp[i].getnumSommet();
		    			}
		    		}
		    	}
		    		
		    		//implementation en minimisant la somme des couts, ici le pieton ne ce deplace jamais au final, 
		    	//cela peut etre logique dans le sens ou en covoiturage c'est plutot la voiture qui vient mais on ne minimise pas vraiment le cout aui final
		    /*		somme = tabLabelv[i].getCost()+tabLabelp[i].getCost();
		    		if (somme < sommin){
		    			System.out.println("cool"); 
		    			System.out.println("cout depuis dep pieton"+tabLabelp[i].getCost());
		    			System.out.println("cout depuis dep voiture"+tabLabelv[i].getCost());
		    			sommin = somme;
		    			bestSom = tabLabelv[i].getnumSommet();
		    			if(tabLabelv[i].getCost()<=tabLabelp[i].getCost()){
		    				coutmax = tabLabelp[i].getCost();
		    			}
		    			else {
		    				coutmax = tabLabelv[i].getCost();
		    			}
		    		}*/
		    	
		    }
		    graphe.dessin.setColor(Color.RED);
    		graphe.dessin.drawPoint(tabLabelv[bestSom].getSom().getLongi(), tabLabelv[bestSom].getSom().getLati(), 15 );
		    System.out.println("Les protagonistes se rejoignent au niveau du sommet : " + bestSom) ;
		    System.out.println("Ils y arrivent avec un coup \"max\" de : "+ coutreel + "min") ;
		    System.out.println("Et n'ont plus qu'a aller ensemble vers le sommet : " + somArrivee) ;
		    
		 	//********************************************************************************************
			//############################################################################################
			//############################################################################################
			//********************************************************************************************

		    
		 
		 //On fait un Astar depuis ce nouveau bestSom jusqua l'arrivée et c'est gagné !
		 
		    Map<Integer, Label> maplabels = new HashMap<>();
		    
		    if (bestSom < 0 || bestSom >= graphe.getnb_Noeuds()){
		   	 throw new IllegalStateException("Le sommet de départ n'existe pas !");
		    }
		    if (somArrivee < 0 || somArrivee >= graphe.getnb_Noeuds()){
		   	 throw new IllegalStateException("Le sommet d'arrivée n'existe pas !");
		    }
		    

		    Label arrivee = new Label(Float.MAX_VALUE, 0 ,somArrivee, graphe.getSommet(somArrivee), 0);
		    maplabels.put(somArrivee, arrivee);
		    Label depart = new Label(0, 0, bestSom, graphe.getSommet(bestSom),Float.MAX_VALUE);
		    maplabels.put(bestSom, depart);
		    tas.insert(maplabels.get(bestSom));

		    Label actuel = null;
			
			while (maplabels.get(somArrivee).getMarked()==false && !tas.isEmpty()){
				
				
		        actuel = tas.deleteMin();
		        actuel.modifMarked(true);
		        int father = actuel.getnumSommet();
		        graphe.dessin.setColor(Color.BLACK);
				graphe.dessin.drawPoint(actuel.getSom().getLongi(), actuel.getSom().getLati(), 5 );
				
				

				for(Arc a : maplabels.get(father).getSom().getArc()){
					
					
					Sommet next = a.getSommet();
					float mancost = (float)Graphe.distance(next.getLongi(), next.getLati(), maplabels.get(somArrivee).getSom().getLongi(), maplabels.get(somArrivee).getSom().getLati());
					if (t_ou_d == 0){
					float vitmoy = (50*1000)/60;
					float mancostv = mancost/vitmoy;
					mancost = mancostv;
					}
					
					if (maplabels.get(next.getnumSommet())==null){
						
						Label nouveau = new Label(maplabels.get(father).getCost()+a.getTempsMin(t_ou_d), father, next.getnumSommet(), next, mancost); 

						maplabels.put(next.getnumSommet(),nouveau);
						tas.insert(maplabels.get(next.getnumSommet()));	
						graphe.dessin.setColor(Color.ORANGE);
						graphe.dessin.drawPoint(next.getLongi(), next.getLati(), 4 );
					}
					
					else {
						if ( maplabels.get(father).getCost()+a.getTempsMin(t_ou_d) < maplabels.get(next.getnumSommet()).getCost()){
							if (maplabels.get(next.getnumSommet()).getCost() == Float.MAX_VALUE){
							}
							maplabels.get(next.getnumSommet()).modifFather(father);
							maplabels.get(next.getnumSommet()).modifCost(maplabels.get(father).getCost()+a.getTempsMin(t_ou_d));
							tas.insertOrUpdate(maplabels.get(next.getnumSommet()));
							
						}
					}	
				}
				
			}
		
			
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
			/* int a ;
			 int x=1;
			 for (a=t; a>=0; a--){
				 System.out.println("On passe en " + x + " par le Sommet : "+ tabchem.elementAt(a).getnumSommet());
				 x++;
			 }*/
			 System.out.println("Cout total par Astar : " + maplabels.get(somArrivee).getCost());
			 System.out.println("Ils arrivent finalement après " + (coutreel+maplabels.get(somArrivee).getCost()) + " min.") ;
		 
	}
			
}
