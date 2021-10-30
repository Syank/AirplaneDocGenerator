package api.crabteam.controllers.requestsBody;

import org.springframework.web.multipart.MultipartFile;

public class RevisedLine {

	private int lineId;
	private MultipartFile newFile;
	
	
	
	public int getLineId() {
		return lineId;
	}
	public void setLineId(int lineId) {
		this.lineId = lineId;
	}
	public MultipartFile getNewFile() {
		return newFile;
	}
	public void setNewFile(MultipartFile newFile) {
		this.newFile = newFile;
	}

	
}
