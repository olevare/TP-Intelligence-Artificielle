import java.util.ArrayList;


public class Rule {
	private ArrayList<Atom> hypothese;
	private Atom conclusion; 
	
	//constructeur a partir d'une chaine de caractère
	public Rule(String read){
		hypothese = new ArrayList<Atom>();
		
		String[] tab = read.split(";");
		for(int i = 0; i < tab.length-1; i++)
		{
			Atom a = new Atom(tab[i]);
			hypothese.add(a);
		}
		conclusion =  new Atom(tab[tab.length-1]);
	}
	
	public ArrayList<Atom> getHypothese(){
		return hypothese;
	}
	
	public Atom getConclusion(){
		return conclusion;
	}
	
	public String toString(){
		String s = "\nl'hypothèse est : ";
		s += hypothese.toString();
		s += "\nla conclusion est : ";
		s += conclusion.toString();
		return s;
	}
}
