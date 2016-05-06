package Pdf2TxtUtils;

import Pdf2TxtUtils.ExtractPageContentArea;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.exceptions.InvalidPdfException;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Util
{

	public static String FILEPATERN = "@FILEDATEJOR@-@PAG@.pdf";

	public static final int ESTIMATED_LAST_SECTION_PAGES = 200;

	public static final int FILENAME_DATE_JOR_SIZE = 14; // Tamanho do nome do arquivo at� uma posi�ao antes da pagina

	public static String getDate(String baseDate)
	{

		String dt = baseDate;// "2008-01-01"; // Start date
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Calendar c = Calendar.getInstance();
		try
		{
			c.setTime(sdf.parse(dt));
		} catch (ParseException e)
		{
			// TODO Auto-generated catch block
			return "fim";
		}
		return sdf.format(c.getTime());
	}

	public static String getFileContent2(File file) {
		try{
			String pdfFile = file.getAbsolutePath();

			int col_height = 830;

			StringBuilder sb = new StringBuilder();

			int col_width1 = 264;
			int col_ini1 = 0;
			Rectangle rect_col1 = new Rectangle(col_ini1, 30, col_width1, col_height);
			sb.append(ExtractPageContentArea.parsePdf(pdfFile, rect_col1));

			int col_width2 = 250;
			int col_ini2 = 280;
			Rectangle rect_col2 = new Rectangle(col_ini2, 30, col_ini2 + col_width2, col_height);
			sb.append(ExtractPageContentArea.parsePdf(pdfFile, rect_col2));

			int col_width3 = 280;
			int col_ini3 = 280 + 250;
			Rectangle rect_col3 = new Rectangle(col_ini3, 30, col_ini3 + col_width3, col_height);
			sb.append(ExtractPageContentArea.parsePdf(pdfFile, rect_col3));

			return sb.toString();

		}
		catch(InvalidPdfException ex){
			System.out.println("Pdf invalido:" + file.getAbsolutePath());
			System.out.println(ex.getLocalizedMessage());
		}
		catch (Exception e){
			e.printStackTrace();
		}
		return  "ERROOO em getFileContent2";
	}



	public static String getnextDate(String baseDate, String dtlimite)
	{

		String dt = baseDate;// "2008-01-01"; // Start date
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Calendar c = Calendar.getInstance();
		try
		{
			c.setTime(sdf.parse(dt));
		} catch (ParseException e)
		{
			e.printStackTrace();
			return "fim";
		}

		c.add(Calendar.DATE, -1); // number of days to add

		Calendar climite = Calendar.getInstance();
		try
		{
			climite.setTime(sdf.parse(dtlimite));
		} catch (ParseException e)
		{
			// TODO Auto-generated catch block
			return "fim";
		}

		if (!c.before(climite))
			dt = sdf.format(c.getTime()); // dt is now the new date
		else
			dt = "fim";
		return dt;
	}
}
