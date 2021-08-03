
/**
 * NoSuchAlgorithmExceptionException.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.2  Built on : Apr 17, 2012 (05:33:49 IST)
 */

package cl.caserita.ws;

public class NoSuchAlgorithmExceptionException extends java.lang.Exception{

    private static final long serialVersionUID = 1356018497275L;
    
    private cl.caserita.ws.OnlineStub.NoSuchAlgorithmExceptionE faultMessage;

    
        public NoSuchAlgorithmExceptionException() {
            super("NoSuchAlgorithmExceptionException");
        }

        public NoSuchAlgorithmExceptionException(java.lang.String s) {
           super(s);
        }

        public NoSuchAlgorithmExceptionException(java.lang.String s, java.lang.Throwable ex) {
          super(s, ex);
        }

        public NoSuchAlgorithmExceptionException(java.lang.Throwable cause) {
            super(cause);
        }
    

    public void setFaultMessage(cl.caserita.ws.OnlineStub.NoSuchAlgorithmExceptionE msg){
       faultMessage = msg;
    }
    
    public cl.caserita.ws.OnlineStub.NoSuchAlgorithmExceptionE getFaultMessage(){
       return faultMessage;
    }
}
    