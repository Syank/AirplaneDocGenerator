package api.crabteam.model.enumarations;

public enum ManualValues {

	LETTER("00"),
	COVER("01"),
	LEP("02");
	
	public String value;
	
	ManualValues (String manualValue) {
		this.value = manualValue;
	}
}
