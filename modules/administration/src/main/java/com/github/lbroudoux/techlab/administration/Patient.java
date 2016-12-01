package com.github.lbroudoux.techlab.administration;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
/**
 * 
 * @author laurent
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "id",
    "firstname",
    "lastname"
})
@XmlRootElement(name = "patient")
public class Patient implements Serializable{

	private static final long serialVersionUID = -1203438531129693285L;

	@XmlElement(name="id")
	private String id;
	
	@XmlElement(name="firstname")
	private String firstname;
	
	@XmlElement(name="lastname")
	private String lastname;
	
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
}
