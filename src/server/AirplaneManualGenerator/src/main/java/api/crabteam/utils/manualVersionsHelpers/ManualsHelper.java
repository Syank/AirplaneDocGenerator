package api.crabteam.utils.manualVersionsHelpers;

import java.util.List;

/**
 * Classe responsável por armazenar a estrutura que os manuais terão
 * @author Bárbara Port
 *
 */
public class ManualsHelper {
	
	private String deltaLetter;
	private String deltaCover;
	private String deltaLEP;
	private List<String> remainingPages;
	
	ManualsHelper (String deltaLetter, String deltaCover, String deltaLEP, List<String> remainingPages) {
		this.deltaLetter = deltaLetter;
		this.deltaCover = deltaCover;
		this.deltaLEP = deltaLEP;
		this.remainingPages = remainingPages;
	}
	
	String getDeltaLetter() {
		return deltaLetter;
	}
	
	void setDeltaLetter(String deltaLetter) {
		this.deltaLetter = deltaLetter;
	}
	
	String getDeltaCover() {
		return deltaCover;
	}
	
	void setDeltaCover(String deltaCover) {
		this.deltaCover = deltaCover;
	}
	
	String getDeltaLEP() {
		return deltaLEP;
	}
	
	void setDeltaLEP(String deltaLEP) {
		this.deltaLEP = deltaLEP;
	}

	public List<String> getRemainingPages() {
		return remainingPages;
	}

	public void setRemainingPages(List<String> remainingPages) {
		this.remainingPages = remainingPages;
	}
	
}
