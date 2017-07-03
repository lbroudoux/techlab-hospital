package com.github.lbroudoux.techlab.fuse.administration;


public interface PatientEndpoint {

    public java.lang.String getAllPatients();

    public java.lang.String admitPatient(
        com.github.lbroudoux.techlab.fuse.administration.AdmitPatient.Arg0 arg0);
}
