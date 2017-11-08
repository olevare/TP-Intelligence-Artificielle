import java.io.BufferedReader;
import java.util.ArrayList;

/**
 * Pour manipuler des contraintes en extension
 *
 */
public class ConstraintDif extends Constraint{
	
	
	/**
	 * Construit une contrainte en extension à partir d'une représentation
	 * textuelle de la contrainte. La liste de variables est donnée sous la forme : Var1;Var2;...
	 * Puis le nombre de tuples est indiqué et enfin chaque tupe est donné sous la forme d'une
	 * suite de valeurs "String" : Val1;Val2;...
	 * Aucune vérification n'est prévue si la syntaxe n'est pas respectée !!
	 * 
	 * @param in le buffer de lecture de la représentation textuelle de la contrainte
	 * @throws Exception en cas d'erreur de format
	 */
	public ConstraintDif(BufferedReader in) throws Exception{
		super(in);
	}
	

	/* (non-Javadoc)
	 * A Implanter !
	 * @see Constraint#violation(Assignation)
	 */
	public boolean violation(Assignation a) {
		int i , j;
		int cpt = 0;
		for(i = 0; i < this.getVars().size(); i++)//pour toutes les variables
		{
			if(a.containsKey(this.getVars().get(i)))//si elle a une assignations
				cpt++;
		}
		
		if(cpt != this.getVars().size())//si toutes les variables ne sont pas assigner
			return false;//elle n'est pas violer

		for(i = 0; i < this.getVars().size(); i++)//pour toutes les variables
		{
			for(j = i+1; j < this.getVars().size(); j++)//pour toutes les autres variables
			{
				if(a.get(this.getVars().get(i)).equals(a.get(this.getVars().get(j))))//si la variable i est egal à une autre variable
					return true;//elle est violer
			}
		}
		return false;//elle n'est pas violer
}
	
	public boolean violationOpt(Assignation a) {
		int i , j;
		int cpt = 0;
		for(i = 0; i < this.getVars().size(); i++)//pour toutes les variables
		{
			if(a.containsKey(this.getVars().get(i)))//si elle a une assignations
			{
				for(j = i+1; j < this.getVars().size(); j++)//pour toutes les autres variables
				{
					if(a.containsKey(this.getVars().get(j)))//si elle a une assignations
					{
						if(a.get(this.getVars().get(i)).equals(a.get(this.getVars().get(j))))//si la variable i est egal à une autre variable
							return true;//elle est violer	
					}
				}
			}
		}
		return false;//elle n'est pas violer
}
	
	/* (non-Javadoc)
	 * @see Constraint#toString()
	 */
	public String toString() {
		return "\n\t Dif "+ super.toString(); 
	}


}
