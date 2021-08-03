
/**
 * UserExceptionException.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.2  Built on : Apr 17, 2012 (05:33:49 IST)
 */

package cl.caserita.ws;

public class UserExceptionException extends java.lang.Exception{

    private static final long serialVersionUID = 1356018497288L;
    
    private cl.caserita.ws.OnlineStub.UserExceptionE faultMessage;

    
        public UserExceptionException() {
            super("UserExceptionException");
        }

        public UserExceptionException(java.lang.String s) {
           super(s);
        }

        public UserExceptionException(java.lang.String s, java.lang.Throwable ex) {
          super(s, ex);
        }

        public UserExceptionException(java.lang.Throwable cause) {
            super(cause);
        }
    

    public void setFaultMessage(cl.caserita.ws.OnlineStub.UserExceptionE msg){
       faultMessage = msg;
    }
    
    public cl.caserita.ws.OnlineStub.UserExceptionE getFaultMessage(){
       return faultMessage;
    }
}
    