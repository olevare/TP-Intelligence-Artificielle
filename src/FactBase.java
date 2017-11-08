import java.util.ArrayList;
import java.util.HashMap;


public class FactBase {

		private HashMap<String,ArrayList<ArrayList<Term>>> atoms;//base de connaissance
		private ArrayList<Term> termes; //liste les constantes
		private ArrayList<Atom> faits; //base de faits
		
		//constructeur a partir d'une chaine de caractère
		public FactBase(String read) {
			atoms = new HashMap<String, ArrayList<ArrayList<Term>>>();
			termes = new ArrayList<Term>();
			faits = new ArrayList<Atom>();
			
			String[] tab = read.split(";");//découpe la chaine de caractère
			for(int i = 0; i < tab.length; i++)//pour tout les atomes de la chaine
			{
				Atom a = new Atom (tab[i]);//on crée l'atome
				faits.add(a);//on l'ajoute à la base de faits
			}
			
			for(Atom atome : faits)//pour tout les atomes de la base de faits
			{
				if(atoms.containsKey(atome.getPredicate()))//si le prédicat et déja dans atoms
				{
					boolean bool = false;
					for(ArrayList<Term> listeterme : atoms.get( atome.getPredicate()))//pour toutes la listes de termes associé a ce prédicat
					{
						if(atome.getArgs().equals(listeterme))//si c'est égal
							bool = true;//il existe déja
					}
					if(!bool)
						atoms.get(atome.getPredicate()).add(atome.getArgs());//sinon on l'ajoute
				}
				else//si le prédicat n'est pas dans atoms on l'ajoute avec l'atome
				{
					ArrayList<ArrayList<Term>> tmp = new ArrayList<ArrayList<Term>>();
					ArrayList<Term> tmpTerm = new ArrayList<Term>();
					
					for(Term t: atome.getArgs())
						tmpTerm.add(t);
					
					tmp.add( tmpTerm );
					atoms.put(atome.getPredicate(), tmp);
				}
				
				for(Term constante : atome.getArgs())//pour tous les termes de atome
				{
					boolean bool = false;
					
					if(constante.isConstant())//si le terme est une constante
					{
						for(Term t : termes)//pour toutes les constante déja enregistrer
						{
							if(constante.equalsT(t))
								bool = true;
						}
						
						if(!bool)//si la constante n'est pas encore enregistrer
							termes.add(constante);
					}
				}
			}
		}
		
		//recherche les homomorphisme de requete dans la base de fait
		public ArrayList<Assignation> Requete(ArrayList<Atom> requete){ 
			Homomorphisms homo = new Homomorphisms(requete, this);
			return homo.getHomo();
		}
		
		public ArrayList<Term> getTerms(){
			return termes;
		}
		
		public HashMap<String, ArrayList<ArrayList<Term>>> getAtoms() {
			return atoms;
		}
		
		public ArrayList<Atom> getFaits(){
			return faits;
		}
		
		public void addFact(Atom a){
			faits.add(a);
		}
		
		public String toString(){
			String s = "\nla base de faits : ";
			s += atoms.toString();
			s += "\nles constantes sont : ";
			s += termes.toString();
			s += "\nil y a ";
			s += faits.size();
			s += " faits";
			return s;
		}
}