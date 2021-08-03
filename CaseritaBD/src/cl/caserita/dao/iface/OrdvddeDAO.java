package cl.caserita.dao.iface;

import java.util.List;

import cl.caserita.dto.OrdvddeDTO;

public interface OrdvddeDAO {
	
	public int buscaOrdvdde(OrdvddeDTO dto);
	public int insertaOrdvdde(OrdvddeDTO dto);
	public int actualizaOrdvdde(OrdvddeDTO dto);
	public int actualizaOrdvddeEstado(OrdvddeDTO dto);
	public List buscaOrdvddeDatos(int codigoEmpresa, int numeroOV, int rutCli, String digCli, int codigoBodega, int tipodespacho);
	
}
