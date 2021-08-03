<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"><%@page
	language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<html>
<head>
<title>Consulta Documentos</title>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
</head>
<body>
	<form method="Post" action="ingreso.asp"> 
<body bgcolor="#FFCC00"> 

'esto es modificable, lo que debemos dejar siempre es el nombre de el INPUT, 
'que como vemos es Dato1, Dato2 etc. 
<p align="left"><font face="Tahoma" size="2"><b><i>Nombre <input type="text" name="Dato1" size="20"> 
 </i></b></font></p> 

<p align="left"><font face="Tahoma" size="2"><b><i>Descripcion <textarea rows="2" name="Dato2" cols="20"></textarea></i></b></font></p> 

<p align="left"><font face="Tahoma" size="2"><b><i>Url <input type="text" name="Dato3" size="17"></i></b></font></p> 

<p align="left"><font face="Tahoma" size="2"><b><i>Categoria 
'una lista desplegable con categorías ya predeterminadas, 
'esto tambien lo podemos modificar 
</i></b></font><select size="1" name="Dato4"> 
    <option value="Municipios">Municipios</option> 
    <option value="Comidas Regionales">Comidas Regionales</option> 
    <option value="Atractivos Turisticos">Atractivos Turisticos</option> 
    <option value="Servicios al Turista">Servicios al Turista</option> 
    <option value="Datos Historicos">Datos Historicos</option> 
    <option value="Productos Regionales">Productos Regionales</option> 
    <option value="Eventos Culturales">Eventos Culturales</option> 
    <option value="Galeria Fotografica">Galeria Fotografica</option> 
    <option value="Videos">Videos</option> 
    <option value="Historias y Leyendas">Historias y Leyendas</option> 
    <option value="Rutas y Caminos">Rutas y Caminos</option> 
    <option value="Turismo Aventura">Turismo Aventura</option> 
    <option selected value="-Seleccionar-">-Seleccionar-</option> 
</select></p> 

<p align="left"><font face="Tahoma" size="2"><b><i>Palabras Clave <textarea rows="2" name="Dato5" cols="20"></textarea></i></b></font></p> 
<p align="left">                                         
</p> 
<p align="center">  
<input type="submit" value="Ingresar" name="B1"> 
<input type="reset" value="Restablecer" name="B2"> 
</form> 
</body>
</html>