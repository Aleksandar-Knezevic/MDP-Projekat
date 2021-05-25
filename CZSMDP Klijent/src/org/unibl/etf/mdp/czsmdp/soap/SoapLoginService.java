/**
 * SoapLoginService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.unibl.etf.mdp.czsmdp.soap;

public interface SoapLoginService extends javax.xml.rpc.Service {
    public java.lang.String getSoapLoginAddress();

    public org.unibl.etf.mdp.czsmdp.soap.SoapLogin getSoapLogin() throws javax.xml.rpc.ServiceException;

    public org.unibl.etf.mdp.czsmdp.soap.SoapLogin getSoapLogin(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
