import lejos.nxt.Button;
/**
 * Diese Klasse gibt den String "Hallo Welt" Auf dem Bildschirm aus, bis ein beliebiger Knopf gedrückt wurde.
 * @author Fabian Friedrichs
 * @version 1.0
 *
 */
public class HelloWorld {
	/**
	 * Main Methode, gibt den String "Hallo Welt" auf dem Bildschirm aus.
	 * @param args
	 */
public static void main (String[] args){
	System.out.println("Hallo Welt");
	Button.waitForAnyPress();
 }
}
