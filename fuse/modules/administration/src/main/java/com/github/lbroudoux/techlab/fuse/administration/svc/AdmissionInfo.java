package com.github.lbroudoux.techlab.fuse.administration.svc;

/**
 * 
 * @author laurent
 */
public class AdmissionInfo {
	
	private String udsCode;
	private PatientInfo patient;
	
	public AdmissionInfo(String udsCode, PatientInfo patient) {
		this.udsCode = udsCode;
		this.patient = patient;
	}

	public String getUdsCode() {
		return udsCode;
	}
	public void setUdsCode(String udsCode) {
		this.udsCode = udsCode;
	}
	public PatientInfo getPatient() {
		return patient;
	}
	public void setPatient(PatientInfo patient) {
		this.patient = patient;
	}
	
	public String toXmlString() {
		StringBuilder builder = new StringBuilder();
		builder.append("<admission>");
		builder.append("<udsCode>").append(udsCode).append("</udsCode>");
		builder.append("<patient>")
			.append("<id>").append(patient.getId()).append("</id>")
			.append("<firstname>").append(patient.getFirstname()).append("</firstname>")
			.append("<lastname>").append(patient.getLastname()).append("</lastname>")
		.append("</patient>");
		builder.append("</admission>");
		return builder.toString();
	}
}
