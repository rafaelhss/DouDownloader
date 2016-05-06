package DouPDFDownloader;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;

import DouPDFPagesToTextDayConverter.DouPagesToDayConverter;
import Pdf2TxtUtils.Util;
import com.sun.org.apache.xpath.internal.SourceTree;
import org.apache.commons.io.FileUtils;
/*
* Classe principal "executavel" que baixa as paginas em PDF em um intervalo de datas.
*
* Na linha de comando use:
* @param arg1 data inicial
* @param arg2 data final
* @param arg3 raiz do caminho onde serao salvos os arquivos
*
* */
public class DouPDFDownloaderMain
{

	private static final int MAX_PAG = 500;

	private static String URL = "http://pesquisa.in.gov.br/imprensa/servlet/INPDFViewer?jornal=@JOR@&pagina=@PAG@&data=@DATA@&captchafield=firistAccess";

	private static String FILENAME = "@PATH@\\temp\\@DATA@\\Dou-@DATA@-@JOR@-@PAG@.pdf";
	private static String DIRNAME = "@PATH@\\temp\\@DATA@";



	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception
	{
		if (args.length != 3) {
			showUsage();
			return;
		}

		String iniDate = args[0];
		String endDate = args[1];
		String path = args[2];

		System.out.println("Dou PDF Downloader: " + iniDate + " " + endDate + " " + path);

		String data = endDate.equals(iniDate) ? Util.getDate(endDate) : Util.getnextDate(endDate, iniDate);
		do
		{
			long startTime = System.currentTimeMillis();

			String url = URL.replace("@DATA@", data);
			String file = FILENAME.replace("@DATA@", data.replaceAll("/", "")).replace("@PATH@", path); // TODO colocar cada dia
			String dir = DIRNAME.replace("@DATA@", data.replaceAll("/", "")).replace("@PATH@", path);

			File theDir = new File(dir);
			if (!theDir.exists())
				if (!theDir.mkdir()) {
					System.out.println("Nao foi possivel criar: " + theDir.toString());
					System.out.println("certifique-se ha um diretorio temp na pasta.");
					return;
				}

			for (int i = 1; i <= 3; i++)
			{
				String urlj = url.replace("@JOR@", String.valueOf(i));
				String filej = file.replace("@JOR@", String.valueOf(i));

				deleteFileIfExists(filej);

				//System.out.println("Download paginas:" + filej);
				for (int j = 1; j < MAX_PAG; j++) {
					System.out.print("+");
					String urljp = urlj.replace("@PAG@", String.valueOf(j));
					String filejp = filej.replace("@PAG@", String.valueOf(j));

					if (!PDFDownloader.DownloadPDF(urljp, filejp))
						break;
				}
				// cheguei ao final das paginas. bora pro proximo jornal
				//	System.out.println("cheguei ao final das paginas.");
				//	System.out.println("bora pro proximo jornal");
			}

			File pdf = new File(dir.replace("temp", "pdf"));
			try {
				FileUtils.copyDirectory(new File(dir), pdf);
			}
			catch (Throwable e) {
				e.printStackTrace();
			}

			for(int i=1; i<=3; i++) {
				String f = file.replace("@JOR@", String.valueOf(i)).replace("@PAG@","1").replace("temp", "pdf");
			//	System.out.println("convertendo para texto...:" + f);
				DouPagesToDayConverter.convertBasedateFiles(new File(f));
			}

			//System.out.println("bora pro proximo dia");



			data = Util.getnextDate(data, iniDate);


			long stopTime = System.currentTimeMillis();
			long elapsedTime = stopTime - startTime;
			System.out.println("");

			log2text(path, file, elapsedTime);

			System.out.println("Processado["+elapsedTime+"] : (" + file +")");



		} while (data != "fim");
		//System.out.println("Fim do processamento!");
	}

	private static void log2text(String path, String file, long elapsedTime) throws IOException {
		Path logpath = Paths.get(path + "\\logProcessado.txt");
		try {
			Files.write(logpath, (elapsedTime + ";" + file + ";\r\n").getBytes(), StandardOpenOption.APPEND);
		}
		catch (NoSuchFileException e) {
			try{
				Files.write(logpath, (elapsedTime + ";" + file + ";\r\n").getBytes(), StandardOpenOption.CREATE_NEW);
			}
			catch (Throwable t){
				System.out.println("Erro ao salvar log. Processamento continuara. " + t.getMessage());

			}
		}
	}

	private static void showUsage() {
		System.out.println("DouPDFDownloaderMain");
		System.out.println("Faz o download de todas as paginas dos jornais nos dias do intervalo de datas informado. Salva um arquivo PDF por jornal (Jornal 1, 2 ou 3). ");
		System.out.println("Param1 = data inicial no formato dd/mm/aaaa");
		System.out.println("Param2 = data final no formato dd/mm/aaaa");
		System.out.println("Param3 = caminho completo da pasta destino dos PDF.");
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
}
