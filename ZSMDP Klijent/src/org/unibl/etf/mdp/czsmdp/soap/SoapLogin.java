/**
 * SoapLogin.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.unibl.etf.mdp.czsmdp.soap;

public interface SoapLogin extends java.rmi.Remote {
    public boolean login(java.lang.String username, java.lang.String password, java.lang.String station) throws java.rmi.RemoteException;
    public java.lang.String getUsers(java.lang.String station) throws java.rmi.RemoteException;
    public java.lang.String getStations() throws java.rmi.RemoteException;
    public void addUser(java.lang.String username, java.lang.String password, java.lang.String station) throws java.rmi.RemoteException;
    public void deleteUser(java.lang.String username, java.lang.String station) throws java.rmi.RemoteException;
}
