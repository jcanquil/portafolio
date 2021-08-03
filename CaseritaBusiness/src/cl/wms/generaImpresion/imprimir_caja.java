package cl.caserita.wms.generaImpresion;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.print.*;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.Date;
import java.util.logging.*;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.AttributeSet;
import javax.print.attribute.HashAttributeSet;
import javax.print.attribute.standard.PrinterName;
import net.sourceforge.barbecue.*; 
import net.sourceforge.barbecue.output.OutputException; 
 
public class imprimir_caja {
   int count=0;
    String iden, country, country2, qty, cust_num;
    String item="16U-26084-03";
   boolean band=false;
     String item_no,ident,country_o,country_o2,qty2,cus;
     
     public static void main(String args[]) throws Exception{
	      imprimir_caja obj=new imprimir_caja();
	      obj.impresion();
     }  
     
  	private static Connection getConnection() throws Exception {
        Class.forName("com.mysql.jdbc.Driver");
        String url = "jdbc:mysql://localhost/etiquetas";
        return (Connection) DriverManager.getConnection(url, "root", "");
    }
  	
  	public void impresion() throws Exception{
      Connection con;
        ResultSet rs;
        Statement smt;
       /* con = getConnection();
        smt = (Statement) con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        rs = smt.executeQuery("consulta sql");
        rs.first();
        item = rs.getString("item");
        System.out.println(item);
        iden = rs.getString("identifier");
        country = rs.getString("country_origin");
        country2 = rs.getString("country2");
        qty = rs.getString("qty");
        cust_num = rs.getString("customer_num");*/
        item="Pruebas";
        iden="11222";
        country="CHIL";
        country2="CHIL";
        qty="1111";
        cust_num="333333";
        String printName="";
        PrinterJob printJob = PrinterJob.getPrinterJob();
        Book book = new Book();
        book.append(new IntroPage(), printJob.defaultPage());
        printJob.setPageable(book);
        int count = 0;
        PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null);//con este vemos las impresoras instaladas como mis pruebas estan en red pues por eso uso este
        for (PrintService printService : services) {//este for se quita
            if (count == 1) {//junto con esto
                printName = printService.getName();//si se le da el nombre a la impresora cambia por printName="nombre impresora"
            }//esto
            count = count + 1;//esto
        }//y esto si se quiere dar el nombre de la impresora
         AttributeSet aset = new HashAttributeSet();
        aset.add(new PrinterName(printName, null));
        services = PrintServiceLookup.lookupPrintServices(null, aset);//busca la impresora
        for (PrintService printService : services) {
            PrintService printers[] = PrintServiceLookup.lookupPrintServices(null, aset);
                if (printers.length == 1) {
               printJob.setPrintService(printers[0]);//le asignamos la impresora de trabajo al job
             try {
        printJob.print();//imprimimos lo  q esta en el job
      } catch (Exception PrintException) {
        PrintException.printStackTrace();
      }
      }
        }
      }
  	
  	
  	private class IntroPage implements Printable {
         public int print(Graphics g, PageFormat pageFormat, int page) {
            try {
                Graphics2D g2d = (Graphics2D) g;
                java.util.Date fecha = new Date();
                int iNumero=fecha.getYear();
                int anio = 2016;
                int m= fecha.getMonth()+1;
               String mes=""+m+"";
                if(mes.length()==1){
                mes="0"+mes;
                }
                String julian = anio+mes;
                String serial = "0001";
                g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
                g2d.setPaint(Color.black);
                Rectangle2D.Double rec_item = new Rectangle2D.Double(1, 1, 227, 84);
                Rectangle2D.Double rec_q = new Rectangle2D.Double(1, 85, 227, 56);
                Rectangle2D.Double rec_l = new Rectangle2D.Double(1, 141, 227, 56);
                Rectangle2D.Double rec_c = new Rectangle2D.Double(1, 197, 227, 30);
                g2d.draw(rec_item);
                g2d.draw(rec_q);
                g2d.draw(rec_l);
                g2d.draw(rec_c);
                String country = "HARADA INDUSTRY OF AMERICA";
                Font countryFont = new Font("Arial", Font.BOLD, 8);
                String item2 = "ITEM No.";
                Font itemFont = new Font("Arial", Font.BOLD, 6);
                String n = "(N)";
                Font nFont = new Font("Arial", Font.BOLD, 6);
                String itemn = item;
                String item3="N"+itemn;
                Font itemnFont = new Font("Arial", Font.BOLD, 14);
                System.out.println("el valor de itemn: "+itemn);
                System.out.println("el valor de item3: "+item3);
                Barcode barcode = BarcodeFactory.createCode39(item3, false);
                barcode.setDrawingText(false);
                barcode.setBarHeight(45);
                barcode.setBarWidth(1);
                BufferedImage image = BarcodeImageHandler.getImage(barcode);
                String quality = "QUALITY";
                Font qualityFont = new Font("Arial", Font.BOLD, 6);
                String q = "(Q)";
                Font qFont = new Font("Arial", Font.BOLD, 6);
                String qtyn = "Q"+qty;
                Font qtyFont = new Font("Arial", Font.BOLD, 14);
                Barcode barcodeqty = BarcodeFactory.createCode39(qtyn, false);
                barcodeqty.setDrawingText(false);
                barcodeqty.setBarHeight(42);
                barcodeqty.setBarWidth(1);
                BufferedImage imageqty = BarcodeImageHandler.getImage(barcodeqty);
                String lote = "Lot No.";
                Font loteFont = new Font("Arial", Font.BOLD, 6);
                String l = "(L)";
                Font lFont = new Font("Arial", Font.BOLD, 6);
                String lotn = julian;
                Font lotFont = new Font("Arial", Font.BOLD, 14);
                julian="L"+julian;
                Barcode barcodelot = BarcodeFactory.createCode39(julian, false);
                barcodelot.setDrawingText(false);
                barcodelot.setBarHeight(42);
                barcodelot.setBarWidth(1);
                BufferedImage imagelot = BarcodeImageHandler.getImage(barcodelot);
                String cus = "Customer No.";
                Font cusFont = new Font("Arial", Font.BOLD, 6);
                String cust_n = cust_num;
                Font cus_nFont = new Font("Arial", Font.BOLD, 14);
                String country2 = "ASSEMBLED IN MEXICO";
                Font country2Font = new Font("Arial", Font.BOLD, 10);
                g2d.setFont(countryFont);
                FontMetrics countryfontMetrics = g2d.getFontMetrics();
                g2d.drawString(country, 51, 12);
                g2d.setFont(itemFont);
                FontMetrics itemfontMetrics = g2d.getFontMetrics();
                g2d.drawString(item2, 6, 24);
                g2d.setFont(nFont);
                FontMetrics nfontMetrics = g2d.getFontMetrics();
                g2d.drawString(n, 15, 33);
                g2d.setFont(itemnFont);
                FontMetrics itemnfontMetrics = g2d.getFontMetrics();
                g2d.drawString(itemn, 82, 32);
                g2d.drawImage(image, 18, 37, null);
                g2d.setFont(qualityFont);
                FontMetrics qualityfontMetrics = g2d.getFontMetrics();
                g2d.drawString(quality, 6, 93);
                g2d.setFont(qFont);
                FontMetrics qfontMetrics = g2d.getFontMetrics();
                g2d.drawString(q, 15, 102);
                g2d.setFont(qtyFont);
                FontMetrics qtyfontMetrics = g2d.getFontMetrics();
                g2d.drawString(qty, 115, 96);
                g2d.drawImage(imageqty, 86, 97, null);
                g2d.setFont(loteFont);
                FontMetrics lotefontMetrics = g2d.getFontMetrics();
                g2d.drawString(lote, 6, 150);
                g2d.setFont(lFont);
                FontMetrics lfontMetrics = g2d.getFontMetrics();
                g2d.drawString(l, 12, 159);
                g2d.setFont(lotFont);
                FontMetrics lotfontMetrics = g2d.getFontMetrics();
                g2d.drawString(lotn, 109, 152);
                g2d.drawImage(imagelot, 83, 153, null);
                g2d.setFont(cusFont);
                FontMetrics cusfontMetrics = g2d.getFontMetrics();
                g2d.drawString(cus, 6, 204);
                g2d.setFont(cus_nFont);
                FontMetrics cust_nfontMetrics = g2d.getFontMetrics();
                g2d.drawString(cust_n, 80, 209);
                g2d.setFont(country2Font);
                FontMetrics country2fontMetrics = g2d.getFontMetrics();
                g2d.drawString(country2, 76, 225);
            } catch (OutputException ex) {
                Logger.getLogger(imprimir_caja.class.getName()).log(Level.SEVERE, null, ex);
            } catch (BarcodeException ex) {
                Logger.getLogger(imprimir_caja.class.getName()).log(Level.SEVERE, null, ex);
            } return PAGE_EXISTS;
   }
  }
}