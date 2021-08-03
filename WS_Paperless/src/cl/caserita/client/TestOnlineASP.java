package cl.caserita.client;

import java.rmi.RemoteException;

import org.apache.axis2.transport.http.HTTPConstants;

import cl.caserita.ws.OnlineStub;
import cl.caserita.ws.OnlineStub.OnlineRecovery;



public class TestOnlineASP {

	public static void main(String[] args) throws RemoteException {
		OnlineStub stub = new OnlineStub();
		OnlineRecovery onlineRecovery = new OnlineRecovery();
		onlineRecovery.setParam0(1);
		onlineRecovery.setParam1("");
		onlineRecovery.setParam2("");
		onlineRecovery.setParam3(1);
		onlineRecovery.setParam4(1);
		onlineRecovery.setParam5(1);
		
		stub._getServiceClient().getOptions().setProperty(HTTPConstants.CHUNKED,false);
		
		OnlineStub.OnlineRecoveryResponse response = stub.onlineRecovery(onlineRecovery);
		
		System.out.println("Respuesta: " + response.get_return());
		
	}
	
}
