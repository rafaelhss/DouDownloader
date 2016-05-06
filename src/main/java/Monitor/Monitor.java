package Monitor;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.AbstractQueue;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Monitora o arquivo logProcessado.txt gerado pelo DouPDFDownloaderMain
 *
 * @param1 caminho para o arquivo logProcessado.txt
 */
public class Monitor {

    public static void main(String[] args) throws Exception {

        int c = 0;
        int prog = 0;
        String[] progress = {"-","\\","|","/"};

        while(true) {
            String content = new String(Files.readAllBytes(Paths.get(args[0])));

            String[] lines = content.split("\r\n");




            if (lines.length > c) {

                System.out.println("");

                int maior = 0;
                int menor = 999999999;
                c = 0;
                int s = 0;

                int ultimo = 0;
                LinkedList fila = new LinkedList<Integer>();
                for (String line : lines) {
                    int t = Integer.parseInt(line.substring(0, line.indexOf(";")));
                    s += t;
                    //System.out.println(t);
                    c++;
                    fila.add(t);
                    if (fila.size() > 10) fila.remove();

                    if (t > maior) maior = t;
                    if (t < menor) menor = t;

                    ultimo = t;
                }


                System.out.println("Maior:" + maior + ", menor:" + menor + ", ultimo:" + ultimo);


                System.out.println("(total  ) " + " Media:" + (float) ((float) s / (float) c)+" Count:" + c + " Soma:" + s );

                int count10 = 0;
                int soma10 = 0;

                while (!fila.isEmpty()) {
                    Integer v = (Integer) fila.remove();
                    soma10 += v;
                    //System.out.println(t);
                    count10++;
                }

                float media10 = (float) ((float) soma10 / (float) count10);
                float ms24h = 40 * 60 * 60 * 1000;
                System.out.println("(last 10) "  + " Media:" + media10 +" Count:" + count10 + " Soma:" + soma10 + ". em 24h:" + (int) (ms24h/media10) +" arqs ("+ (int) (ms24h/media10)/3 + " dias)" );


                Thread.sleep(3000);
            }
            else {


                System.out.print("\r");
                System.out.print(progress[prog++%progress.length]);
                Thread.sleep(300);
            }
        }
    }
}