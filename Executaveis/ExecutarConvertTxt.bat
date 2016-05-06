cls
@echo off 
setlocal enableDelayedExpansion 

set MYDIR=C:\Users\rafa\Documents\Artigos\DatasetDOU\pdf

echo Processando diretorio: [%MYDIR%]
echo pressione uma tecla para listar os arquivos...
pause

REM dir /A:-D-S/S %MYDIR%

REM echo Pressione uma tecla para comecar o processamento

REM pause
echo comecou!


for /F %%x in ('dir /B/A:-D-S/S %MYDIR%') do (

 SET TEXT=%%x
 SET SUBSTRING=!TEXT:~-6!

 IF "!SUBSTRING!" == "-1.pdf" (
	echo processar : %%x
	java -jar C:\Users\rafa\Documents\Projects\DouDownloader\classes\artifacts\DouDownloader_jar2\DouDownloader.jar %%x
 )

)
echo terminou!