import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;


public class main {
	public static void main(String[] args) throws Exception{
		/*//4reines
		String fileName  = "4reines.txt";
		System.out.println("Problème des 4 reines");
		System.out.println("Solution trouvée:");
		BufferedReader lectureF = new BufferedReader(new FileReader(fileName));
		Reseau monPb = new Reseau(lectureF);
		
		
		CSP test = new CSP(monPb);
		test.searchSolution();
		test.searchAllSolutions();
		
		System.out.println();
		System.out.println();
		
		//colorations
		fileName  = "Coloration.txt";
		System.out.println("Problème de colorations");
		System.out.println("Solution trouvée:");
		lectureF = new BufferedReader(new FileReader(fileName));
		monPb = new Reseau(lectureF);
		
		
		test = new CSP(monPb);
		test.searchSolution();
		test.searchAllSolutions();
		
		System.out.println();
		System.out.println();
		
		//homomorphisme
		fileName  = "homomorphisme.txt";
		System.out.println("Problème d'homomorphisme");
		System.out.println("Solution trouvée:");
		lectureF = new BufferedReader(new FileReader(fileName));
		monPb = new Reseau(lectureF);
				
				
		test = new CSP(monPb);
		test.searchSolution();
		test.searchAllSolutions();
				
		System.out.println();
		System.out.println();
		
		//8reines
		fileName  = "8reinesExt.txt";
		System.out.println("Problème des 8 reines");
		System.out.println("Solution trouvée:");
		lectureF = new BufferedReader(new FileReader(fileName));
		monPb = new Reseau(lectureF);
		
		
		test = new CSP(monPb);
		test.searchSolution();
		test.searchAllSolutions();
		System.out.println("Nombre de solutions trouvées: " + test.getsolutions().size());
		
		System.out.println();
		System.out.println();
		
		//zebre
		fileName  = "zebre.txt";
		System.out.println("Problème du zebre");
		System.out.println("Solution trouvée:");
		lectureF = new BufferedReader(new FileReader(fileName));
		monPb = new Reseau(lectureF);
		
		
		test = new CSP(monPb);
		test.searchSolution();*/
		
		
		
		/*FactBase BF = new FactBase("p('a','b');p('a','c');p('b','c');p('b','d');p('b','e');p('d','e');p('e','e');r('a');r('b');r('c')");
		Homomorphisms h = new Homomorphisms("p(x,y);p(y,z);p(x,u);p(z,z);r(x);r(u)", BF);
		System.out.println(h.toString());*/
		
		
		//on crée la base de fait a partir d'un fichier texte
		BufferedReader a = new BufferedReader(new FileReader("faits.txt"));
		String read = a.readLine();
		FactBase BF = new FactBase(read);
		
		//on crée knowledge a partir d'un fichier texte puis on y met la base de fait
		KnowledgeBase know = new KnowledgeBase("regles.txt");
		know.setBF(BF);
		
		System.out.println(know.toString());
		
		while(true)
		{
			System.out.println("Entre votre demande");
			Scanner sc = new Scanner(System.in);
			String s = sc.nextLine();
			
			String[] tab = s.split(";");
			ArrayList<Atom> liste = new ArrayList<Atom>();
			
			for(int i = 0; i < tab.length; ++i )
			{
				Atom at = new Atom(tab[i]);
				liste.add(at);
				
			}
				
			ArrayList<Assignation> reponse = know.Requete(liste);
			if(reponse == null)
				System.out.println( "Il n'y a pas d'homomorphisme" );
			else
			{
				System.out.println("Les homomorphismes sont : ");
				System.out.println(reponse.toString());
			}
		}
	}
}
