package cl.caserita.dao.base;



public class DAOException extends Exception {

	public DAOException(String message) {
		super(message);
		
		System.err.println("Error: " + message);
	}
}
