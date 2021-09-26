package api.crabteam.model.entities.builders;

import java.util.HashMap;

import api.crabteam.model.entities.Remark;

public class RemarkBuilder {
	
	private Remark remark;
	
	public RemarkBuilder(HashMap<String, String> remarkValues) {
		String remarkTraco = remarkValues.keySet().iterator().next();
		String remarkApelido = remarkValues.get(remarkTraco);
		
		this.remark = new Remark();
		this.remark.setApelido(remarkApelido);
		this.remark.setTraco(remarkTraco);
		
	}
	
	public Remark getBuildedRemark() {
		return this.remark;
	}
	
}
