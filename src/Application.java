import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Application {

	public static void main(String[] args) throws Exception{
		String fileName  = "toto.txt";
		System.out.println("Chargement du fichier : "+new java.io.File(".").getCanonicalPath()+"/"+fileName);
		BufferedReader lectureF = new BufferedReader(new FileReader(fileName));
		Reseau monPb = new Reseau(lectureF);
		
		//System.out.println("\nMon réseau de contraintes :\n" + monPb);
		
		CSP test = new CSP(monPb);
		//test.searchSolution();
		test.searchAllSolutions();
	}
}
