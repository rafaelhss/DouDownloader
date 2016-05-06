package DouPDFPagesToTextDayConverter;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;

import Pdf2TxtUtils.Util;
import org.apache.commons.io.FileUtils;
/*
* Converte os arquivos baixados atraves do DouPDFDownloaderMain em TXT e troca quebras de linha duplas identificadas por @@DOUBLE_NW@@
*
*
* */
public class DouPagesToDayConverter{
/*
	public static void main(String[] args) throws Exception {
		if(args.length != 1)
			showUsage();

		convertBasedateFiles(new File(args[0]));
	}

	private static void showUsage() {
		System.out.println("DouPagesToDayConverter");
		System.out.println("Converte os PDF em TXT e troca quebras de linha duplas identificadas por @@DOUBLE_NW@@");
		System.out.println("param1 = Caminho completo da raiz do diretorio onde estao os PDF.");
	}*/

	public static void convertBasedateFiles(final File firstPage) throws IOException
	{
		File dir = new File(firstPage.getParent());

		File[] currentfiles = dir.listFiles(new FilenameFilter()
		{
			// arquivos da mesma data e jornal
			public boolean accept(File dir, String name)
			{
				try {
					boolean result = name.endsWith(".pdf")
							&& name.substring(0, Util.FILENAME_DATE_JOR_SIZE).equals(
							firstPage.getName().substring(0, Util.FILENAME_DATE_JOR_SIZE));
					return result;
				} catch (Throwable e){
					e.printStackTrace();
					System.out.println("name:" + name + "   firstpage:" + firstPage);
					return false;
				}
			}

		});

		if (currentfiles.length > 0)
		{
			try
			{
				// ordena os arquivos de acordo com a pagina
				Arrays.sort(currentfiles, new Comparator<File>()
				{
					public int compare(File o1, File o2)
					{
						int ext1 = o1.getName().indexOf(".pdf");
						int dash1 = o1.getName().lastIndexOf("-");
						int ext2 = o2.getName().indexOf(".pdf");
						int dash2 = o2.getName().lastIndexOf("-");

						String p1 = o1.getName().substring(dash1 + 1, ext1);
						String p2 = o2.getName().substring(dash2 + 1, ext2);

						return new Integer(Integer.parseInt(p1)).compareTo(new Integer(Integer.parseInt(p2)));
					}
				});
			} catch (Exception e)
			{
				System.out
						.println("*ERRO ****** Exce��o ao ordenar lista de arquivos. Processamento continuar� normalmente");
				e.printStackTrace();
			}
			// joga o conteudo de todos os pdf em um unico txt
			StringBuilder sbFinalContent = new StringBuilder();
			for (File currentFileChild : currentfiles)
			{
				System.out.print("*");
				sbFinalContent.append(DouPDFtoTextConverter.convertFile(currentFileChild));
			}

			String novoNome = dir.getAbsolutePath().replace("pdf", "txt") + "\\"
					+ firstPage.getName().substring(0, Util.FILENAME_DATE_JOR_SIZE) + ".txt";

			FileUtils.writeStringToFile(new File(novoNome), sbFinalContent.toString());
		}
	}
}