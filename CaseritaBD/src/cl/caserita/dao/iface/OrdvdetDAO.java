package cl.caserita.dao.iface;

import java.util.List;

import cl.caserita.dto.OrdvdetDTO;

public interface OrdvdetDAO {
	
	
	public int insertaOrdvdet(OrdvdetDTO dto);
	public int buscaOrdvdet(OrdvdetDTO dto);
	public int actualizaOrdvdet(OrdvdetDTO dto);
	public List buscaOrdvdetDatos(int codigoEmpresa, int numeroOV, int rutCli, String digCli, int codigoBodega, int tipodespacho);
	
}
