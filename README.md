# DouDownloader
Faz download do DOU (Diario oficial da uniao)


## Executando rapidamente 
A pasta 'Executavel' na raiz ja contem o arquivo jar gerado a partir do projeto. Nela tambem voce encontra o arquivo 'ExecutarDownload.bat' que invoca o jar adequadamente.

Edite o arquivo 'ExecutarDownload.bat' adequadamente:
> nas linhas 5,6,7 defina a data final (maior)
> nas linhas 23, 30 e 37 defina a data inicial (menor)

O bat invocara o downloader para cada data da maior para a menor.

Alternativamente voce pode chamar o jar diretamente na linha de comando informando a menor data, a maior data e o caminho onde quer que os arquivos sejam guardados. Nesse caso, eh importante ter uma pasta 'temp' criada na raiz do diretorio onde voce pretende salvar os arquivos baixados.
exemplo:
> java -jar DouDownloader.jar 02/05/2016 06/05/2016 c:\dou\arquivos
