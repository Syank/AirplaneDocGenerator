package api.crabteam.controllers.requestsBody;

import java.util.ArrayList;

public class NewRevision {

	private ArrayList<RevisedLine> revisedLines;

	public ArrayList<RevisedLine> getRevisedLines() {
		return revisedLines;
	}

	public void setRevisedLines(ArrayList<RevisedLine> revisedLines) {
		this.revisedLines = revisedLines;
	}
	
}
