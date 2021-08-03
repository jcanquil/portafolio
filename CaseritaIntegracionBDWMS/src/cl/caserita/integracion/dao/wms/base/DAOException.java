// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   DAOException.java

package cl.caserita.integracion.dao.wms.base;

import java.io.PrintStream;

public class DAOException extends Exception
{

    public DAOException(String message)
    {
        super(message);
        System.err.println((new StringBuilder("Error: ")).append(message).toString());
    }
}
