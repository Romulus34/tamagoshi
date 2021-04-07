package tamagoshi.jeu;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Properties;


import tamagoshi.tamagoshis.GrosJoueur;
import tamagoshi.tamagoshis.GrosMangeur;
import tamagoshi.tamagoshis.Tamagoshi;
import tamagoshi.util.Utilisateur;
//import java.util.logging.*;


/**
 * un petit jeu avec des tamagoshis
 */
public class TamaGame {

	/**
	 * contains all the initial tamagoshis
	 */
	private List<Tamagoshi> allTamagoshis;
	/**
	 * contains only alive tamagoshis
	 */
	private List<Tamagoshi> aliveTamagoshis;
	/**
	 * contains only properties list
	 */	
	private List<Properties> propertiesList;
	
	//private Logger log = Logger.getLogger("tamagoshi.logger"))

	/** build the game */
	private TamaGame() {
		allTamagoshis = new ArrayList<Tamagoshi>();
		aliveTamagoshis = new ArrayList<Tamagoshi>();
		propertiesList = new ArrayList<Properties>();
		initialisation();
	}
	
	/**
	 * initialisation des différentes phases du jeu
	 */

	@SuppressWarnings("unchecked")
	private void initialisation() {
		
		System.out.println("Entrez le nombre de tamagoshis désiré !");
		int n = 0;
		while (n < 1) {
			System.out.println("Saisisez un nombre > 0 :");
			try {
				n = Integer.parseInt(Utilisateur.saisieClavier());
			} catch (NumberFormatException e) {
				System.out.println("Il faut saisir un nombre !");
			}
		}
		for (int i = 0; i < n; i++) {
			Properties props = new Properties();
			System.out.println("Entrez le nom du tamagoshi numéro " + i + " : ");
			if (Math.random() < .5) { 
				GrosJoueur GJ = new GrosJoueur(Utilisateur.saisieClavier());				
				allTamagoshis.add(GJ);
				props.setProperty("tamagoshi.name", GJ.getName());
				props.setProperty("tamagoshi.type", GJ.getClass().getSimpleName());
				propertiesList.add(props);
			}
			else {
				GrosMangeur GM = new GrosMangeur(Utilisateur.saisieClavier());
			allTamagoshis.add(GM);
			props.setProperty("tamagoshi.name", GM.getName());
			props.setProperty("tamagoshi.type", GM.getClass().getSimpleName());
			propertiesList.add(props);
			}
		}
//		aliveTamagoshis = (List<Tamagoshi>) allTamagoshis.clone();
		// ou encore pour le mÃªme rÃ©sultat
		 aliveTamagoshis = new ArrayList<Tamagoshi>(allTamagoshis);
	}

	/**
	 * returns the selected tamagoshi
	 * 
	 * @param question the question to ask to the user
	 * @return the selected instance
	 */
	private Tamagoshi tamaSelection(String question) {
		System.out.println(question);
		int index = 0;
		for (ListIterator<Tamagoshi> iterator = aliveTamagoshis.listIterator(); iterator.hasNext();) {
			System.out.print("\t(" + (iterator.nextIndex()) + ") " + iterator.next().getName() + "  ");
		}
		System.out.print("\n\tEntrez un choix : ");
		try {
			index = Integer.parseInt(Utilisateur.saisieClavier());
		} catch (NumberFormatException e) {
			System.out.println("Il faut saisir un nombre !");
			return tamaSelection(question);
		}
		if (index < 0 || index >= aliveTamagoshis.size()) {
			System.out.println("il n'y a pas de tamagoshi portant le numéro " + index);
			return tamaSelection(question);
		}
		return aliveTamagoshis.get(index);
	}

	/**
	 * Starts the game
	 */
	public void play() {
		int cycle = 1;
		while (! aliveTamagoshis.isEmpty()) {
			System.out.println("\n------------Cycle n°" + (cycle++) + "-------------");
			for (Tamagoshi t : aliveTamagoshis)
				t.parle();
			tamaSelection("\n\tNourrir quel tamagoshi ?").mange();
			tamaSelection("\n\tJouer avec quel tamagoshi ?").joue();
			
			for (Iterator<Tamagoshi> iterator = aliveTamagoshis.iterator(); iterator.hasNext();) {
				Tamagoshi t = iterator.next();
				if (!t.consommeEnergy() || !t.consommeFun() || t.vieillit())
					iterator.remove();
			}
		}
		System.out.println("\n\t--------- fin de partie !! ----------------\n\n");
		resultat();
	}
	
	/**
	 * 
	 * @return the score
	 */

	private double score() {
		int score = 0;
		for (Tamagoshi t : allTamagoshis)
			score += t.getAge();
		return score * 100 / (Tamagoshi.getLifeTime() * allTamagoshis.size());
	}
	
	/**
	 * @return the resultat
	 */

	private void resultat() {
		System.out.println("-------------bilan------------");
		for (Tamagoshi t : allTamagoshis) {
			String classe = t.getClass().getSimpleName();
			System.out.println(t.getName() + " qui était un " + classe + " "
					+ (t.getAge() == Tamagoshi.getLifeTime() ? " a survaicu et vous remercie :)"
							: " n'est pas arrivé au bout et ne vous félicite pas :("));
		}
		System.out.println("\nniveau de difficulté : " + allTamagoshis.size() + ", score obtenu : " + score() + "%");
		printProperties();		
	}

	/** Launch a new instance of the game */
	public static void main(String[] args) {
		TamaGame jeu = new TamaGame();
		jeu.play();
	}
	
	@Override
	public String toString() {
		return "tamagame";
	}
	
	/**
	 * @return properties's file
	 */
	
	private void printProperties() {
		try (OutputStream out = new FileOutputStream("Properties.File")) {
			for (Properties p : propertiesList) 
				p.store(out, "Properties");
		} catch (IOException e) {
			e.printStackTrace();			
		}
	}

}
