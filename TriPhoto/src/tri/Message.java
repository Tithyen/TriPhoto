package tri;

/**
 * Classe permettant de définir des objets avancements comportant des données de type différents qui
 * seront retournés à un SwingWorker ; le SwingWorker accepte pour les échanges en cours 
 * de thread qu'un seul type d'objet : SwingWOrker<T,V> ou T est un objet retourné en fin
 * de thread et V un objet retourné en cours de thread ; dans notre cas, nous avons besoin de retourner
 * des données texte (pour le mémo) et des données de type integer pour la JStatusBar
 * @author thier
 *
 */
public class Message {
	
	public static enum Type {
		Memo,
		Progression,
		Max;
	}
	
	public static enum Level {	
		NORMAL(0),
		DEBUG(1);
		
		//permet de définir le niveau d'enregistrement dans le log 
		//on enregistre tous les logs de degrés égaux et inférieurs
		int degre; 
		
		Level(int pDegre){
			this.degre = pDegre;
		}
		
		public int getDegre() {
			return this.degre;
		}
	}

	private Type type;
	private Object valeur = null;
	private Level level = Level.NORMAL; //valeur par défaut

	/**
	 * constructeur ; initilaisation des deux variables String et Integer
	 */
	public Message() {}
	
	public Message(Object argO, Type argType, Level argLevel) {
		this.valeur = argO;
		this.type = argType;
		this.level = argLevel;
	}
		
	public Object getValeur() {
		return this.valeur;
	}
	
	public Type getType() {
		return this.type;
	}
	
	public Level getLevel() {
		return this.level;
	}
}
