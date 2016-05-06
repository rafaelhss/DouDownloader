package DouPDFPagesToTextDayConverter;

import Pdf2TxtUtils.Util;
import java.io.File;
import java.net.MalformedURLException;

public class DouPDFtoTextConverter
{
	public static String convertFile(File file) throws MalformedURLException
	{
		try
		{
			String fileContent = Util.getFileContent2(file);
			String adjustedFileContent = adjustLineBreaks(fileContent);
			return adjustedFileContent;
		} catch (Throwable e)
		{
			e.printStackTrace();
			System.out.println("Erro processando: " + file.getAbsolutePath());
			return "";
		}
	}

	private static String adjustLineBreaks(String fileContent)
	{
		return fileContent.replace("-\n", " ");
	}
}
