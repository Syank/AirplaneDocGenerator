package api.crabteam.utils;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


@Entity(name = "situacao_projeto")
public class ProjectSituation {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	@Column(columnDefinition = "text")
	private String situationTitle;
	
	@Column(columnDefinition = "text")
	private String situationMessage;
	
	@Column
	private boolean ok;
	
	
	
	public String getSituationTitle() {
		return situationTitle;
	}
	public void setSituationTitle(String situationTitle) {
		this.situationTitle = situationTitle;
	}
	public String getSituationMessage() {
		return situationMessage;
	}
	public void setSituationMessage(String situationMessage) {
		this.situationMessage = situationMessage;
	}
	public boolean isOk() {
		return ok;
	}
	public void setOk(boolean ok) {
		this.ok = ok;
	}
	
}
