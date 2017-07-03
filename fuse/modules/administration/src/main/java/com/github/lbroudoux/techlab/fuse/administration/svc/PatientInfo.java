package com.github.lbroudoux.techlab.fuse.administration.svc;

/**
 * 
 * @author laurent
 */
public class PatientInfo {

	private String id;
	private String firstname;
	private String lastname;
	private String birthday;
	private String address;
	private String emergencycontact;
	private String emergencycontactAddress;
	private String gender;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getFirstname() {
		return firstname;
	}
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	public String getLastname() {
		return lastname;
	}
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getEmergencycontact() {
		return emergencycontact;
	}
	public void setEmergencycontact(String emergencycontact) {
		this.emergencycontact = emergencycontact;
	}
	public String getEmergencycontactAddress() {
		return emergencycontactAddress;
	}
	public void setEmergencycontactAddress(String emergencycontactAddress) {
		this.emergencycontactAddress = emergencycontactAddress;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
}
