package cl.caserita.comunes.fecha;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Fecha
		 extends GregorianCalendar 
{
	
	public Fecha()
	{
		super();
	}
	public Fecha(Timestamp t)
	{
		this();
		setTime(t);
	}
	public Fecha(String fecha, String formato) throws FechaException
	{
		this();
		Date fechaDate = new Date();
		if("ddmmaaaa".equalsIgnoreCase(formato))
			formato = "ddMMyyyy";
		else
		if("ddmmaaaahhmmss".equalsIgnoreCase(formato))
			formato = "ddMMyyyyHHmmss";
		else
		if("mmddaaaa".equalsIgnoreCase(formato))
			formato = "MMddyyyy";
		else
		if("aaaammdd".equalsIgnoreCase(formato))
			formato = "yyyyMMdd";
		else
		if("aaaammddhhmmss".equalsIgnoreCase(formato))
			formato="yyyyMMddHHmmss";
		try
		{
			SimpleDateFormat dateFormat = new SimpleDateFormat(formato);
			dateFormat.setLenient(false);
			fechaDate = dateFormat.parse(fecha);
		}
		catch(IllegalArgumentException e)
		{
			throw new FechaException("Fecha no valida por argumento");
		}
		catch(ParseException e)
		{
			throw new FechaException("Fecha no valida");
		}
		setTime(fechaDate);
	}
	
	public String getFechaconFormato()
	{
		SimpleDateFormat formato=new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		return String.valueOf(formato.format(this.getDate()));
	}
	public String getFechaconFormato(int valor)
	{
		SimpleDateFormat formato;
		switch(valor)
		{
			case 1:
				formato=new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
				break;
			case 2:
				formato=new SimpleDateFormat("dd/MMM/yyyy HH:mm:ss");
				break;
			case 3:
				formato=new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
				break;
			case 4:
				formato=new SimpleDateFormat("yyyy-MM-dd");
				break;
			case 5:
				formato=new SimpleDateFormat("yyyyMMdd");
				break;
			default:
				formato=new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
				break;
		}
		return String.valueOf(formato.format(this.getDate()));
	}

	public Timestamp getTimestamp()
	{
		return new Timestamp(getDate().getTime());
	}

	public Date getDate()
	{
		return getTime();
	}

	public void addDays(int days)
	{
		add(5, days);
	}
	
	public void addSeconds(int seconds)
	{
		add(13,seconds);
	}

	public void addMonths(int months)
	{
		add(2,months);
	}
	
	public boolean greaterThan(Fecha fec)
	{
		if(this.compareTo(fec)>0)
			return true;
		else
			return false;
	}
	
	public int compareTo(Fecha f)
	{
		return getTime().compareTo(f.getTime());
	}

	public void substractDays(int days)
	{
		add(5, -days);
	}

	

	public int compareDate(Fecha f)
	{
		Fecha othis = new Fecha();
		Fecha of = new Fecha();
		othis.set(get(1), get(2), get(5));
		of.set(f.get(1), f.get(2), f.get(5));
		return othis.compareTo(of);
	}

	public int differenceInDays(Fecha dd)
	{
		long t1 = getTimeInMillis() / (long)0x5265c00;
		long t2 = dd.getTimeInMillis() / (long)0x5265c00;
		Long t3 = new Long(t2 - t1);
		return t3.intValue();
	}
	
	public Long  differenceInLong(Fecha dd)
	{
		long t1 = getTimeInMillis();
		long t2 = dd.getTimeInMillis();
		return new Long(t2-t1);
	}
	
	public Long differenceEnHoras(Fecha dd)
	{
		long t1 = getTimeInMillis();
		long t2 = dd.getTimeInMillis();
		long t3=(t2-t1)/3600000;
		return new Long(t3);
	}
	//Metodos agregados el 20060510
	public Long differenceInSeconds(Fecha mm){
		long t1 = getTimeInMillis()/ 1000;
		long t2 = mm.getTimeInMillis()/1000;
		long t3=(t2-t1);
		return new Long(t3);
	}
	
	public Long differenceInMinutes(Fecha mm){
		long t1 = getTimeInMillis()/ 60000;
		long t2 = mm.getTimeInMillis()/60000;
		long t3=(t2-t1);
		return new Long(t3);
	}

	public int getLastMonthDay()
	{
		return getActualMaximum(5);
	}

	public int getNextMonth()
	{
		Fecha f = (Fecha)clone();
		f.add(2, 1);
		return f.get(2);
	}

	public String getDDMMYYYY()
	{
		return getDDMMYYYY(null);
	}

	public String getDDMMYYYY(String sep)
	{
		int d = get(5);
		int m = get(2) + 1;
		StringBuffer s = new StringBuffer();
		if(d < 10)
			s.append("0");
		s.append(d);
		if(sep != null)
			s.append(sep);
		if(m < 10)
			s.append("0");
		s.append(m);
		if(sep != null)
			s.append(sep);
		s.append(get(1));
		return s.toString();
	}

	public String getHHMMSS()
	{
		return getHHMMSS(null);
	}

	public String getHHMMSS(String sep)
	{
		int h = get(11);
		int m = get(12);
		int ss = get(13);
		StringBuffer s = new StringBuffer();
		if(h < 10)
			s.append("0");
		s.append(h);
		if(sep != null)
			s.append(sep);
		if(m < 10)
			s.append("0");
		s.append(m);
		if(sep != null)
			s.append(sep);
		if(ss < 10)
			s.append("0");
		s.append(ss);
		return s.toString();
	}
	
	public String getDDMMYYYYHHMMSS()
	{
		return getDDMMYYYY(null)+getHHMMSS(null);
	}
	
	public String getYYYYMMDDHHMMSS()
	{
		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmss");
		return String.valueOf(sdf.format(getDate()));	
	}
	
	public String getYYYYMMDD()
	{
		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd");
		return String.valueOf(sdf.format(getDate()));
	}

	public void dump()
	{
		System.out.println("ERA: " + get(0));
		System.out.println("YEAR: " + get(1));
		System.out.println("MONTH: " + get(2));
		System.out.println("WEEK_OF_YEAR: " + get(3));
		System.out.println("WEEK_OF_MONTH: " + get(4));
		System.out.println("DATE: " + get(5));
		System.out.println("DAY_OF_MONTH: " + get(5));
		System.out.println("DAY_OF_YEAR: " + get(6));
		System.out.println("DAY_OF_WEEK: " + get(7));
		System.out.println("DAY_OF_WEEK_IN_MONTH: " + get(8));
		System.out.println("AM_PM: " + get(9));
		System.out.println("HOUR: " + get(10));
		System.out.println("HOUR_OF_DAY: " + get(11));
		System.out.println("MINUTE: " + get(12));
		System.out.println("SECOND: " + get(13));
		System.out.println("MILLISECOND: " + get(14));
		System.out.println("ZONE_OFFSET: " + get(15) / 0x36ee80);
		System.out.println("DST_OFFSET: " + get(16) / 0x36ee80);
	}
	public String recuperaMes(int mes){
		String mesPal="";
		//System.out.println("Recupera mes:"+mes);
		if (mes== 1)
			mesPal="Enero";
		if (mes== 2)
			mesPal="Febrero";
		if (mes==3)
			mesPal="Marzo";
		if (mes== 4)
			mesPal="Abril";
		if (mes== 5)
			mesPal="Mayo";
		if (mes== 6)
			mesPal="Junio";
		if (mes== 7)
			mesPal="Julio";
		if (mes==8)
			mesPal="Agosto";
		if (mes==9)
			mesPal="Septiembre";
		if (mes== 10)
			mesPal="Octubre";
		if (mes== 11)
			mesPal="Noviembre";
		if (mes== 12)
			mesPal="Diciembre";
			
			
		//System.out.println("Mes en palabra:"+mesPal);
		return mesPal;
	}
	public static void main (String []args){
		Fecha fch = new Fecha();
		Date fecha = new Date();
		
		fch.sumarRestarDiasFecha(fecha, 2);
	}
	public Date sumarRestarDiasFecha(Date fecha, int dias){
			
		 
			
		      Calendar calendar = Calendar.getInstance();
			
		      calendar.setTime(fecha); // Configuramos la fecha que se recibe
			
		      calendar.add(Calendar.DAY_OF_YEAR, dias);  // numero de días a añadir, o restar en caso de días<0
			
		      System.out.println("Fecha :"+calendar.getTime());
			
		      return calendar.getTime(); // Devuelve el objeto Date con los nuevos días añadidos
			
		 
			
		 }
	
	
	
}
