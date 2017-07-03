package com.github.lbroudoux.techlab.fuse.administration.svc;

/**
 * 
 * @author laurent
 */
public class AdmissionService {

	private PatientService service;
	
	public void setPatientService(PatientService service) {
		this.service = service;
	}
	
	public String processAdmission(String patientId) {
		PatientInfo patient = service.getPatientInfo(patientId);
		AdmissionInfo admission = new AdmissionInfo(RegistryUtil.genUDSCode(), patient);
		return admission.toXmlString();
	}
}
