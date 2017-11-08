import java.util.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class KnowledgeBase {
	private FactBase BF;
	private ArrayList<Rule> regles;
	private boolean sature;
	
	//constructeur pas défault
	public KnowledgeBase(){
		regles = new ArrayList<Rule>();
		sature = false;
	}
	
	//constructeur a partir d'un nom de fichier
	public KnowledgeBase(String chemin) throws IOException{
		regles = new ArrayList<Rule>();
		sature = false;

		BufferedReader file = new BufferedReader(new FileReader(chemin));
		String ligne;
		
		while((ligne = file.readLine()) != null)//tant qu'il y a une ligne à lire
		{
			Rule a = new Rule(ligne);
			regles.add(a);//on ajoute la regle
		}
	}
	
	private void saturation(){
		
		boolean fin = false;
		while(!fin)
		{
			ArrayList<Atom> nouveau = new ArrayList<Atom>();
			for(Rule r : regles)
			{
				Homomorphisms homo = new Homomorphisms(r.getHypothese(), BF);
				ArrayList<Assignation> homomorphismes = homo.getHomo();

				Atom conclusion = r.getConclusion();
				
				for(Assignation a : homomorphismes)
				{
					ArrayList<Term> terms = new ArrayList<Term>();
					for(Term t : conclusion.getArgs())
						terms.add((Term)a.get(t));
					
					Atom tmp = new Atom(conclusion.getPredicate(), terms);
					if(!nouveau.contains(tmp) && !BF.getFaits().contains(tmp))
						nouveau.add(tmp);
				}
			}
			if(nouveau.size() == 0)
			{
				fin = true;
				sature = true;
			}
			else
			{
				for(Atom a : nouveau)
					BF.addFact(a);
			}
		}
	}
	
	//recherche des homomorphisme dans la base de fait saturé
	public ArrayList<Assignation> Requete(ArrayList<Atom> requete){
		if(!sature)
			saturation();
		return BF.Requete( requete );
	}
	
	public FactBase getBF(){
		return BF;
	}
	
	public void setBF(FactBase bf){
		BF = bf;
	}
	
	public ArrayList<Rule> getRegles(){
		return regles;
	}
	
	public void addRegle(Rule a){
		regles.add(a);
	}
	
	public void addFact(Atom a){
		BF.addFact(a);
	}
	
	public String toString(){
		String s = BF.toString();
		s += "\nla liste de règle est : ";
		s += regles.toString();
		return s;
	}

}
