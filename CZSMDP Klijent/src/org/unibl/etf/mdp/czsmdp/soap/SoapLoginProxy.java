package org.unibl.etf.mdp.czsmdp.soap;

public class SoapLoginProxy implements org.unibl.etf.mdp.czsmdp.soap.SoapLogin {
  private String _endpoint = null;
  private org.unibl.etf.mdp.czsmdp.soap.SoapLogin soapLogin = null;
  
  public SoapLoginProxy() {
    _initSoapLoginProxy();
  }
  
  public SoapLoginProxy(String endpoint) {
    _endpoint = endpoint;
    _initSoapLoginProxy();
  }
  
  private void _initSoapLoginProxy() {
    try {
      soapLogin = (new org.unibl.etf.mdp.czsmdp.soap.SoapLoginServiceLocator()).getSoapLogin();
      if (soapLogin != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)soapLogin)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)soapLogin)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (soapLogin != null)
      ((javax.xml.rpc.Stub)soapLogin)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public org.unibl.etf.mdp.czsmdp.soap.SoapLogin getSoapLogin() {
    if (soapLogin == null)
      _initSoapLoginProxy();
    return soapLogin;
  }
  
  public java.lang.String getUsers(java.lang.String station) throws java.rmi.RemoteException{
    if (soapLogin == null)
      _initSoapLoginProxy();
    return soapLogin.getUsers(station);
  }
  
  public boolean login(java.lang.String username, java.lang.String password, java.lang.String station) throws java.rmi.RemoteException{
    if (soapLogin == null)
      _initSoapLoginProxy();
    return soapLogin.login(username, password, station);
  }
  
  public void logout(java.lang.String station) throws java.rmi.RemoteException{
    if (soapLogin == null)
      _initSoapLoginProxy();
    soapLogin.logout(station);
  }
  
  public java.lang.String getStations() throws java.rmi.RemoteException{
    if (soapLogin == null)
      _initSoapLoginProxy();
    return soapLogin.getStations();
  }
  
  public void addUser(java.lang.String username, java.lang.String password, java.lang.String station) throws java.rmi.RemoteException{
    if (soapLogin == null)
      _initSoapLoginProxy();
    soapLogin.addUser(username, password, station);
  }
  
  public java.lang.String getOnlineUsers(java.lang.String station) throws java.rmi.RemoteException{
    if (soapLogin == null)
      _initSoapLoginProxy();
    return soapLogin.getOnlineUsers(station);
  }
  
  public void deleteUser(java.lang.String username, java.lang.String station) throws java.rmi.RemoteException{
    if (soapLogin == null)
      _initSoapLoginProxy();
    soapLogin.deleteUser(username, station);
  }
  
  
}