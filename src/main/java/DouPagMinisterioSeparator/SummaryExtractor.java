package DouPagMinisterioSeparator;


import Pdf2TxtUtils.Util;

import java.io.File;
import java.net.MalformedURLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
@Deprecated //Funcionou um dia... nao sei se ainda presta...
public class SummaryExtractor
{

	private static final int MAX_SUMMARY_LEN = 4000;

	private static final int MAX_MINISTERIOS = 30;

	private static StringBuilder sb = new StringBuilder();

	private static String pageText;

	private static class MatcherRunner implements Runnable
	{
		public void run()
		{
			String importantInfo[] =
			{ "Mares eat oats", "Does eat oats", "Little lambs eat ivy", "A kid will eat ivy too" };
			try
			{
				Pattern p2 = Pattern.compile("([\\p{L}|[\\,]]+\\s?)+[\\.]+(^|\\s)([0-9]+)($|\\s)");

				String summaryCandidate = "";
				try
				{
					summaryCandidate = pageText.substring(0, MAX_SUMMARY_LEN);
				} catch (StringIndexOutOfBoundsException e)
				{
					summaryCandidate = pageText;
				}
				Matcher m2 = p2.matcher(new InterruptibleCharSequence(summaryCandidate));

				// System.out.println(summaryCandidate);
				while (m2.find())
				{
					sb.append(m2.group());
					sb.append('\n');
				}

			} catch (RuntimeException e)
			{
				// ok, sb ja esta pronto;
			}
		}
	}

	static String extractRawSummaryFromFile(File file) throws MalformedURLException, Exception
	{
//
//		Properties props = System.getProperties();
//		// props.setProperty("gate.plugins.home", ".\\plugins\\ANNIE");
//		props.setProperty("gate.home",
//				"C:\\Users\\Rafael\\workspace\\analisador-dou\\AnalisadorDou\\lib\\GateAPI\\gate-7.1-build4485-ALL");
//		// props.setProperty("gate.site.config", ".\\gate.xml");
//
//		/*
//		 * Properties props = System.getProperties(); props.setProperty("gate.plugins.home", ".\\plugins\\ANNIE");
//		 * props.setProperty("gate.home", ".\\bin\\gate.jar"); props.setProperty("gate.site.config", ".\\gate.xml");
//		 *
//		 * String gateHomeStr = System.getProperty(Gate.GATE_HOME_PROPERTY_NAME);
//		 *
//		 * // gateHomeStr = // Thread.currentThread().getContextClassLoader().getResource ("gate/Gate.class").toString();
//		 *
//		 * System.out.println("#########8gfah: " + gateHomeStr);
//		 */
//
//		//
//		//
//		String gateHomeStr = System.getProperty(Gate.GATE_HOME_PROPERTY_NAME);
//
//		// //gateHomeStr =
//		// Thread.currentThread().getContextClassLoader().getResource("gate/Gate.class").toString();
//		//
//		// System.out.println("#########8gfah: " + gateHomeStr);
//
//		Gate.init();
//
//		Document doc = Factory.newDocument(file.toURL(), null);
//
//		DocumentContentImpl docImpl = (DocumentContentImpl) doc.getContent();
//
//		pageText = docImpl.getOriginalContent();
//
//		// System.out.println(pageText);

		// pageText = moveCursor(pageText, "Sum�rio", file);

		pageText = Util.getFileContent2(file);
		pageText = moveCursorEnd(pageText, "PÁGINA", file);

		Thread t = new Thread(new MatcherRunner());
		t.start();

//		while (t.isAlive())
//		{
//			t.join(2000);
//			if (t.isAlive())
//			{
//				t.interrupt();
//				// Shouldn't be long now
//				// -- wait indefinitely
//				t.join();
//			}
//		}

		String sumario = sb.toString();

		// System.out.println("Sumario: " + sumario);

		return pageText;
	}

	private static String moveCursor(String input, String toFind, File file) throws Exception
	{
		int idxSumario = input.indexOf(toFind);

		if (idxSumario < 0)
			throw new Exception("N�o achei a palavra " + toFind + " no arquivo '" + file.getName() + "'");

		input = input.substring(idxSumario);

		return input;

	}

	private static String moveCursorEnd(String input, String toFind, File file) throws Exception
	{
		String result = moveCursor(input, toFind, file);
		return result.substring(toFind.length());

	}
	/*
	 * private static String readUntilNext(String pageText, String token) { return pageText.substring(0,
	 * pageText.indexOf(token)).trim(); }
	 * 
	 * private static String readBetween(String pageText, String token1, String token2) { String temp = pageText;
	 * 
	 * int tokenIdx1 = temp.indexOf(token1);
	 * 
	 * if (tokenIdx1 < 0) return pageText;
	 * 
	 * temp = pageText.substring(tokenIdx1 + token1.length());
	 * 
	 * int tokenIdx2 = temp.indexOf(token2) + tokenIdx1 + token1.length();
	 * 
	 * if (tokenIdx2 < 0) return pageText;
	 * 
	 * return pageText.substring(tokenIdx1, tokenIdx2).trim();
	 * 
	 * }
	 */
}
