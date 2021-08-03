// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   CoordenadasDTO.java

package cl.caserita.dto;

import java.io.Serializable;

public class CoordenadasDTO
    implements Serializable
{

    public CoordenadasDTO()
    {
    }

    public String getLatitud()
    {
        return latitud;
    }

    public void setLatitud(String latitud)
    {
        this.latitud = latitud;
    }

    public String getLongitud()
    {
        return longitud;
    }

    public void setLongitud(String longitud)
    {
        this.longitud = longitud;
    }

    private String latitud;
    private String longitud;
}
