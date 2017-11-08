import java.util.ArrayList;
import java.util.HashMap;

/**
 * Permet de résoudre un problème de contrainte par Backtrack : 
 * 	Calcul d'une solution, 
 * 	Calcul de toutes les solutions
 *
 */
public class CSP {

		private Reseau reseau;						// le réseau à résoudre
		private ArrayList<Assignation> solutions; 	// les solutions du réseau
		private Assignation assignation;			// l'assignation courante
		int cptr;									// le compteur de noeuds explorés
		
		/**
		 * Crée un problème de résolution de contraintes pour un réseau donné
		 * 
		 * @param r le réseau de contraintes à résoudre
		 */
		public CSP(Reseau r) {
			reseau = r;
			solutions = new ArrayList<Assignation>();
			assignation = new Assignation();
		}
		
		/**
		 * Cherche une solution au réseau de contraintes
		 * 
		 * @return une assignation solution du réseau ou null si pas de solution
		 */
		public Assignation searchSolution() {
			cptr=0;
			assignation.clear();
			Assignation sol = backtrack();
			//Assignation sol = forward();
			System.out.println(sol.toString());
			System.out.println(cptr + " noeuds ont été explorés");
			return sol;
		}

		/**
		 * Exécute l'algorithme de backtrack à la recherche d'une solution à partir de l'assignation courante
		 * 
		 * @return la prochaine solution ou null si pas de nouvelle solution
		 */
		private Assignation backtrack() {
			cptr++;
			int i;
			if(assignation.size() == reseau.getVarNumber())//si toutes les variables ont été assigner
			{
				return assignation;//retourne le resultat
			}
			else
			{
				String variable = chooseVar(); //choisie une variable non assigner
				for(i = 0; i < reseau.getDomSize(variable); i++)//pour toutes les valeurs possible de la variable
				{
					assignation.put(variable, reseau.getDom(variable).get(i));//on assigne la variable avec cette valeurs
					if(consistant(variable))//si elle ne viole aucune contrainte
					{
						backtrack();
						if(assignation.size() == reseau.getVarNumber())//si toutes les variables ont été assigner
							return assignation;//retourne le resultat
					}
					assignation.remove(variable);//on supprime l'assignation
				}
			}
			return assignation;//remonte dans l'arbre si c'est bloqué
		}

		/**
		 * Calcule toute les solutions au réseau de contraintes
		 * 
		 * @return la liste des assignations solution
		 * 
		 */
		public ArrayList<Assignation> searchAllSolutions(){
			cptr=0;
			solutions.clear();
			assignation = new Assignation();
			backtrackAll();
			//System.out.println(solutions.toString());
			//System.out.println(cptr + " noeuds ont été explorés");
			return solutions;
		}
		
		/**
		 * Exécute l'algorithme de backtrack à la recherche de toutes les solutions
		 * à partir de l'assignation courante en stockant solutions rencontrées.
		 * 
		 */
		private void backtrackAll() {
			cptr++;
			int i;
			if(assignation.size() == reseau.getVarNumber())//si toutes les variables ont été assigner
			{
				//System.out.println(assignation.toString());
				Assignation b = new Assignation();
				b = assignation.clone();
				solutions.add(b);
			}
			else
			{
				String variable = chooseVar(); //choisie une variable non assigner
				for(i = 0; i < reseau.getDomSize(variable); i++)//pour toutes les valeurs possible de la variable
				{
					assignation.put(variable, reseau.getDom(variable).get(i));//on assigne la variable avec cette valeurs
					if(consistant(variable))//si elle ne viole aucune contrainte
					{
						backtrackAll();
					}
					assignation.remove(variable);//on supprime l'assignation
				}
			}
		}
		
		private Assignation forward() {
			cptr++;
			int i;
			if(assignation.size() == reseau.getVarNumber())//si toutes les variables ont été assigner
			{
				return assignation;//retourne le resultat
			}
			else
			{
				String variable = chooseVarForward(); //choisie une variable non assigner
				HashMap<String,ArrayList<Object>> dold = new HashMap<String,ArrayList<Object>>();
				//copie les variables avec leur domaines
				for(String variables : reseau.getVars()) //pour toutes les variables du réseau
				{

					dold.put(variables, (ArrayList<Object>) reseau.getDom(variable).clone());
				}
				for(i = 0; i < reseau.getDomSize(variable); i++)//pour toutes les valeurs possible de la variable
				{
					assignation.put(variable, reseau.getDom(variable).get(i));//on assigne la variable avec cette valeurs
					if(propagation(variable))//si elle ne viole aucune contrainte
					{
						forward();
						if(assignation.size() == reseau.getVarNumber())//si toutes les variables ont été assigner
							return assignation;//retourne le resultat
					}
					assignation.remove(variable);//on supprime l'assignation
					reseau.getDomEntier().clear();//on efface tous
					//copie les variables avec leur domaines
					for(String variables : dold.keySet()) //pour toutes les variables du réseau
					{
						reseau.getDomEntier().put(variables, (ArrayList<Object>) dold.get(variables).clone());
					}
				}
			}
			return assignation;//remonte dans l'arbre si c'est bloqué
		}

		public boolean propagation(String lastAssignedVar)
		{
			ArrayList<Object> Dom = new ArrayList<Object>();//pour enregistrer un nouveau domaine d'une variable
			reseau.getDom(lastAssignedVar).clear();//on vide le domaine de la variable que l'on vient d'assigner
			reseau.addValue(lastAssignedVar, assignation.get(lastAssignedVar));//on met a la variable assigner sa valeur comme domaine
			for(String variable : reseau.getVars()) //pour toutes les variables du problèmes
			{
				for(Object domaine : reseau.getDom(variable))//pour toutes les valeurs possible de la variable
				{
					if(variable == lastAssignedVar)//si la variable en cours est la dernière assigner
					{
						if(!consistant(lastAssignedVar))//si elle n'est pas consistante
							return false;//elle viole donc se n'est pas bon
					}
					else
					{
						if(!assignation.containsKey(variable)) //s'il ny a pas d'assignations pour la variable
						{
							assignation.put(variable, domaine);//on assigne la variable avec cette valeur
							if(consistant(variable))//si elle ne viole aucune contrainte
								Dom.add(domaine);//on ajoute cette valeur à son nouveau domaine
							assignation.remove(variable);//on supprime l'assignation
						}
					}
				}
				if((variable != lastAssignedVar) && (!assignation.containsKey(variable)))//si la variable courante est différente de la dernière variable assigner ou d'une variable déja assigner
				{
					if(Dom.size() == 0)//si la variable n'a aucun domaine
					{
						return false;// l'assignation empèche une résolution totale du problème
					}
					reseau.getDom(variable).clear();//vide le domaine de la variable
					reseau.getDom(variable).addAll(Dom);//insère le nouveau domaine dans la variable
					Dom.clear();//on vide dom pour recommencer
				}
			}
			return true;//propagation fini
		}
		
		/**
		 * Retourne la prochaine variable à assigner
		 *  
		 * @return une variable non encore assignée
		 */
		private String chooseVar() {
			for(String var : reseau.getVars()) //pour toutes les variables
			{
				if(!assignation.containsKey(var)) //s'il ny a pas d'assignations pour la variable
				{
					return var;
				}
			}
			return null;
		}
	
		/**
		 * Retourne la prochaine variable à assigner
		 *  
		 * @return une variable non encore assignée
		 */
		private String chooseVarForward() {
			String variable = null;//contient la variables à choisir
			int domaines = 0;//contient la taille de son domaine
			for(String var : reseau.getVars()) //pour toutes les variables
			{
				if(!assignation.containsKey(var)) //s'il ny a pas d'assignations pour la variable
				{
					if(variable == null)//si c'est la première variables que l'on regarde
					{
						variable = var;
						domaines = reseau.getDom(var).size();
					}
					else
					{
						if(domaines > reseau.getDom(var).size())//si la variable en cours a un plus petit domaine que la variable choisi
						{
							variable = var;//on l'enregistre
							domaines = reseau.getDom(var).size();
						}
					}
				}
			}
			return variable;//retourne la variable avec le plus petit domaine et si égalité la première trouver
		}
		
		/**
		 * Fixe un ordre de prise en compte des valeurs d'un domaine
		 * 
		 * @param values une liste de valeurs
		 * @return une liste de valeurs
		 */
		private ArrayList<Object> tri(ArrayList<Object> values) {
			return values;
		}
		
		/**
		 * Teste si l'assignation courante est consistante, c'est à dire qu'elle
		 * ne viole aucune contrainte.
		 * 
		 * @param lastAssignedVar la variable que l'on vient d'assigner à cette étape.
		 * @return vrai ssi l'assignation courante ne viole aucune contrainte
		 */
			private boolean consistant(String lastAssignedVar) {
				boolean viol = true;
				if(reseau.getConstraints(lastAssignedVar).size() == 0)//si la variable ne concerne aucune contrainte
				{
					//System.out.println("Pas de contraintes pour cette variables");
					return true; //c'est consistant
				}
				else
				{
					for(Constraint contrainte : reseau.getConstraints(lastAssignedVar)) //pour toutes les contraintes du reseau concernant la variable
					{
							//System.out.println("test de : "+contrainte.toString());
							viol = contrainte.violationOpt(assignation);//verifie si elle viol une contrainte
							if(viol)//si elle viole
								return !viol;//renvoie faux
					}
				}
				return !viol;
			}
			
		//ajoutez tristan
		public ArrayList<Assignation> getsolutions()
		{
			return solutions;
		}
}
