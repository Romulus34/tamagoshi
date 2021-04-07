package tamagoshi.jeu;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.stream.IntStream;

import tamagoshi.tamagoshis.GrosJoueur;
import tamagoshi.tamagoshis.GrosMangeur;
import tamagoshi.tamagoshis.Tamagoshi;
import tamagoshi.util.Utilisateur;

/**
 * un petit jeu avec des tamagoshis
 */
public class TamaGameWithLambda {

	/**
	 * contains all the initial tamagoshis
	 */
	private List<Tamagoshi> allTamagoshis;
	/**
	 * contains only alive tamagoshis
	 */
	private List<Tamagoshi> aliveTamagoshis;

	/** build the game */
	private TamaGameWithLambda() {
		allTamagoshis = new ArrayList<>();
		initialisation();
	}
	
	/**
	 * Initialisation des gros joueur et gros mangeure
	 */

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
		IntStream.range(0, n).forEach(i -> {
			System.out.println("Entrez le nom du tamagoshi numéro " + i + " : ");
			if (Math.random() < .5)
				allTamagoshis.add(new GrosJoueur(Utilisateur.saisieClavier()));
			else
				allTamagoshis.add(new GrosMangeur(Utilisateur.saisieClavier()));
		});
		aliveTamagoshis = new ArrayList<>(allTamagoshis);
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
		while (!aliveTamagoshis.isEmpty()) {
			System.out.println("\n------------Cycle n°" + (cycle++) + "-------------");
			aliveTamagoshis.forEach(Tamagoshi::parle);
			tamaSelection("\n\tNourrir quel tamagoshi ?").mange();
			tamaSelection("\n\tJouer avec quel tamagoshi ?").joue();
			aliveTamagoshis.removeIf(t -> !t.consommeFun() || !t.consommeFun() || t.vieillit());
		}
		System.out.println("\n\t--------- fin de partie !! ----------------\n\n");
		resultat();
	}
	
	/**
	 * 
	 * @return the score
	 */

	private double score() {
		int score = allTamagoshis.stream().mapToInt(Tamagoshi::getAge).sum();
		return score * 100 / (Tamagoshi.getLifeTime() * allTamagoshis.size());
	}

	/**
	 * @return le bilan 
	 */
	
	private void resultat() {
		System.out.println("-------------bilan------------");
		allTamagoshis.forEach(t -> {
			String classe = t.getClass().getSimpleName();
			System.out.println(t.getName() + " qui était un " + classe + " "
					+ (t.getAge() == Tamagoshi.getLifeTime() ? "a survécu et vous remercie :)"
							: "n'est pas arrivé au bout et ne vous félicite pas :("));
		});
		System.out.println("\nniveau de difficulté : " + allTamagoshis.size() + ", score obtenu : " + score() + "%");
	}

	/** Launch a new instance of the game */
	public static void main(String[] args) {
		TamaGameWithLambda jeu = new TamaGameWithLambda();
		jeu.play();
	}

}
