package com.github.lbroudoux.techlab.fuse.administration.svc;

import java.util.HashMap;
/**
 * 
 * @author laurent
 */
public class PatientService {

	private HashMap<String, PatientInfo> allPatients = new HashMap<>();
	
	public PatientInfo getPatientInfo(String id) {
		return allPatients.get(id);
	}
	
	public PatientInfo getPatientInfo(String firstname, String lastname, String id) {
		PatientInfo patient = new PatientInfo();
		
		if (allPatients.containsKey(id)) {
			return allPatients.get(id);
		}
		
		patient.setId(id);
		patient.setFirstname(firstname);
		patient.setLastname(lastname);
		patient.setGender(RegistryUtil.genRandomGender());
		patient.setAddress(RegistryUtil.genAddress());
		patient.setBirthday(RegistryUtil.genRandomBirthday());
		patient.setEmergencycontact(RegistryUtil.genAddress());
		patient.setEmergencycontactAddress(RegistryUtil.genAddress());
		
		allPatients.put(id, patient);
		
		return patient;
	}
	
	public String getAllPatientsInTxt() {
		StringBuilder sb = new StringBuilder();
		for (PatientInfo patient: allPatients.values()){
			sb.append(patient.getLastname());
			sb.append("\t");
			sb.append(patient.getFirstname());
			sb.append("\t");
			sb.append(patient.getId());
			sb.append("\t");
			sb.append(patient.getGender());
			sb.append("\n");
		}
		return sb.toString();
	}
}
