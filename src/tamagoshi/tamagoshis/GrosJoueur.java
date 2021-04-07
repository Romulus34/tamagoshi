package tamagoshi.tamagoshis;

public class GrosJoueur extends Tamagoshi {
	
	public GrosJoueur(String name) {
		super(name);
	}

	@Override
	public boolean consommeFun() {
		setFun(getFun()-1);
		return super.consommeFun();
	}

	public String toString() {
		return super.toString() + "Je suis un gros joueur";
	}
	
}
