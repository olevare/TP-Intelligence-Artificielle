import java.util.ArrayList;


public class Homomorphisms {

	private ArrayList<Atom> Q;
	private FactBase BF;
	private ArrayList<Assignation> homo;
	
	//constructeur a partir d'une chaine de caractère et d'une base de faits
	public Homomorphisms(String read, FactBase bf){
		Q = new ArrayList<Atom>();
		
		String[] tab = read.split(";");//découpe la chaine de caractère
		for (int i = 0; i < tab.length; ++i)//pour tout les atomes de la chaine
		{
			Atom a = new Atom(tab[i]);//on crée l'atome
			Q.add(a);//on l'ajoute à la liste d'atome Q
		}
		
		homo = new ArrayList<Assignation>();
		BF = bf;
		backtrackAll();
	}
	
	//constructeur a partir d'une liste d'atome et dune base de fait
	public Homomorphisms(ArrayList<Atom> q, FactBase bf){
		Q = new ArrayList<Atom>();
		Q = q;
		homo = new ArrayList<Assignation>();
		BF = bf;
		backtrackAll();
	}
	
	//transformation d'un problème homomorphisme en un problème CSP
	private Reseau transformationCSP(){
		Reseau P = new Reseau();//création du reseau
		
		for(Atom q :Q)//pour tous les atomes
		{
			for(Term term : q.getArgs())//pour tous les termes de l'atome
			{
				if(!term.isConstant())//si le terme n'est pas une constante
				{
					P.addVariable(term.getLabel());//on ajoute la variable au reseau
					for(Term t : BF.getTerms())//pour toutes les constante de la base de fait
						P.addValue(term.getLabel(), t.getLabel());//on l'ajoute dans le domaine de la variable
				}
				else//si c'est une constante
				{
					P.addVariable("c"+term.getLabel());//on crée une nouvelle variable que l'on ajoute au réseau
					P.addValue("c"+term.getLabel(),term.getLabel());//on met sa propre valeur comme domaine de la variable
				}
			}
		}

		for(Atom q :Q)//pour tous les atomes
		{
			if(BF.getAtoms().containsKey(q.getPredicate()))//si la base de fait contient le meme predicat que le predicat de l'atome
			{
				ArrayList<String> vars = new ArrayList<String>();
				for(Term t : q.getArgs())//pour tous les termes de l'atome
				{
					if(t.isConstant())//si il est constant
						vars.add("c"+t.getLabel());
					else
						vars.add(t.getLabel());
				}
				ArrayList<ArrayList<Term>> tuples = BF.getAtoms().get(q.getPredicate());
				ConstraintExt cExt = new ConstraintExt(vars,tuples);
				P.addConstraint(cExt);//on ajoute la contrainte
			}
		}
		return P;
	}
	
	//recherche tous les homomorphismes
	private void backtrackAll(){
		Reseau reseau = transformationCSP();
		CSP csp = new CSP(reseau);
		homo = csp.searchAllSolutions();		
	}
	
	public ArrayList<Assignation> getHomo(){
		return homo;
	}
	
	public String toString(){
		String s = "\nles atomes sont : ";
		s += Q.toString();
		s += "\nla base de faits est : ";
		s += BF.toString();
		s += "\nles homomorphismes sont : ";
		s += homo.toString();
		return s;
	}
}
