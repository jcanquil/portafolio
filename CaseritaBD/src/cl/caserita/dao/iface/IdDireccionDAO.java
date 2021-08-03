// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   IdDireccionDAO.java

package cl.caserita.dao.iface;

import cl.caserita.dto.IdDireccionDTO;
import java.util.List;

public interface IdDireccionDAO
{

    public abstract List direcciones();

    public abstract int actualiza(IdDireccionDTO iddirecciondto);
    public IdDireccionDTO buscaDireccion(int iddir, int correlativo);
    public int obtieneTVendedor(int codVendedor);
	public List buscaVendedores (int tipoVendedor);
	public List vendedoresNuevos();
	public List direccionesPorRut(int rut);
}
