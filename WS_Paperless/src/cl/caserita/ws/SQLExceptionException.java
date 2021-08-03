
/**
 * SQLExceptionException.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.2  Built on : Apr 17, 2012 (05:33:49 IST)
 */

package cl.caserita.ws;

public class SQLExceptionException extends java.lang.Exception{

    private static final long serialVersionUID = 1356018497034L;
    
    private cl.caserita.ws.OnlineStub.SQLExceptionE faultMessage;

    
        public SQLExceptionException() {
            super("SQLExceptionException");
        }

        public SQLExceptionException(java.lang.String s) {
           super(s);
        }

        public SQLExceptionException(java.lang.String s, java.lang.Throwable ex) {
          super(s, ex);
        }

        public SQLExceptionException(java.lang.Throwable cause) {
            super(cause);
        }
    

    public void setFaultMessage(cl.caserita.ws.OnlineStub.SQLExceptionE msg){
       faultMessage = msg;
    }
    
    public cl.caserita.ws.OnlineStub.SQLExceptionE getFaultMessage(){
       return faultMessage;
    }
}
    