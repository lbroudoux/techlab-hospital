package com.github.lbroudoux.techlab.administration;


public interface PatientEndpoint {

    public java.lang.String getAllPatients();

    public java.lang.String admitPatient(
        com.github.lbroudoux.techlab.administration.AdmitPatient.Arg0 arg0);
}
