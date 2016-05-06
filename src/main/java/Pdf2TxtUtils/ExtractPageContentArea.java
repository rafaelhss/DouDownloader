package Pdf2TxtUtils;

/*
 * This class is part of the book "iText in Action - 2nd Edition"
 * written by Bruno Lowagie (ISBN: 9781935182610)
 * For more info, go to: http://itextpdf.com/examples/
 * This example only works with the AGPL version of iText.
 */

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.SocketException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.itextpdf.text.Rectangle;
import com.itextpdf.text.exceptions.InvalidPdfException;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.FilteredTextRenderListener;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import com.itextpdf.text.pdf.parser.RegionTextRenderFilter;
import com.itextpdf.text.pdf.parser.RenderFilter;
import com.itextpdf.text.pdf.parser.TextExtractionStrategy;


public class ExtractPageContentArea
{

	private static final int MAX_PAG = 500;

	private static String URL = "http://www.in.gov.br/servlet/INPDFViewer?jornal=@JOR@&pagina=@PAG@&data=@DATA@&captchafield=firistAccess";

	private static String FILENAME = "@PATH@\\Dou-@DATA@-@JOR@.txt";

	/**
	 * Parses a specific area of a PDF to a plain text file.
	 * 
	 * @param pdf
	 *            the original PDF

	 *            the resulting text
	 * @throws IOException
	 */
	public static String parsePdf(String pdf, Rectangle rect) throws IOException
	{

		try {
			PdfReader reader = new PdfReader(pdf);
			RenderFilter filter = new RegionTextRenderFilter(rect);
			TextExtractionStrategy strategy;
			StringBuilder sb = new StringBuilder();
			for (int i = 1; i <= reader.getNumberOfPages(); i++) {
				strategy = new FilteredTextRenderListener(new CustomLocationTextExtractionStrategy(), filter);
				String texto = PdfTextExtractor.getTextFromPage(reader, i, strategy).replace("o\n-", "");
				sb.append(texto);
			}

			reader.close();
			return sb.toString();
		}
		catch(InvalidPdfException ipe) {
			System.out.println("Erro parse pdf:" + pdf + " (Se for a primeira ou ultima pagina o erro eh esperado)");
		}
		catch (Throwable e ){
			e.printStackTrace();
			System.out.println("Erro parse pdf:" + pdf);
		}
		return "";
	}

	/**
	 * Main method.
	 * 
	 * @param args
	 *            no arguments needed
	 * @throws DocumentException
	 * @throws IOException
	 */
	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception
	{

		if (args.length != 3)
			throw new Exception("Argumentos invalidos.");

		String iniDate = args[0];
		String endDate = args[1];
		String path = args[2];

		String data = getnextDate(endDate, iniDate);

		while (data != "fim")
		{

			String url = URL.replace("@DATA@", data);

			String file = FILENAME.replace("@DATA@", data.replaceAll("/", "")).replace("@PATH@", path);

			for (int i = 1; i <= 3; i++)
			{

				String urlj = url.replace("@JOR@", String.valueOf(i));
				String filej = file.replace("@JOR@", String.valueOf(i));

				deleteFileIfExists(filej);

				try
				{
					for (int j = 1; j < MAX_PAG; j++)
					{

						String urljp = urlj.replace("@PAG@", String.valueOf(j));
						String filejp = filej.replace("@PAG@", String.valueOf(j));

						System.out.println(new SimpleDateFormat("dd/MM/yyyy HH:mm:SS").format(new Date()) + " - Processing p: "
								+ urljp);
						System.out.println("Processing r: " + filejp);

						try
						{
							process3Columns(urljp, filejp);
						} catch (ConnectException ce)
						{
							// retentar !!
							ce.printStackTrace();
							j--;
						} catch (SocketException se)
						{
							// retentar !!
							se.printStackTrace();
							j--;
						}

					}
				} catch (InvalidPdfException e)
				{
					// cheguei ao final das paginas. bora pro proximo jornal
					System.out.println("cheguei ao final das paginas. bora pro proximo jornal");
				}
			}
			data = getnextDate(data, iniDate);
		}
		System.out.println("Fim do processamento!");

	}

	private static void deleteFileIfExists(String tempFile)
	{
		// Delete if tempFile exists
		File fileTemp = new File(tempFile);
		if (fileTemp.exists())
		{
			fileTemp.delete();
		}
		fileTemp = null;
	}

	private static String getnextDate(String baseDate, String dtlimite)
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
			return "";
		}

		c.add(Calendar.DATE, -1); // number of days to add

		Calendar climite = Calendar.getInstance();
		try
		{
			climite.setTime(sdf.parse(dtlimite));
		} catch (ParseException e)
		{
			// TODO Auto-generated catch block
			return "";
		}

		if (!c.before(climite))
			dt = sdf.format(c.getTime()); // dt is now the new date
		else
			dt = "fim";
		return dt;
	}

	public static void process3Columns(String pref, String resu) throws IOException
	{
		int col_height = 830;

		PrintWriter out = new PrintWriter(new FileOutputStream(resu, true));

		int col_width1 = 264;
		int col_ini1 = 0;
		Rectangle rect_col1 = new Rectangle(col_ini1, 30, col_width1, col_height);
		out.println(parsePdf(pref, rect_col1));

		int col_width2 = 250;
		int col_ini2 = 280;
		Rectangle rect_col2 = new Rectangle(col_ini2, 30, col_ini2 + col_width2, col_height);
		out.println(parsePdf(pref, rect_col2));

		int col_width3 = 280;
		int col_ini3 = 280 + 250;
		Rectangle rect_col3 = new Rectangle(col_ini3, 30, col_ini3 + col_width3, col_height);
		out.println(parsePdf(pref, rect_col3));

		out.flush();
		out.close();

	}
}
