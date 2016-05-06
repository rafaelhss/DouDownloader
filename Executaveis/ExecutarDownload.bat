cls
@echo off 
setlocal enableDelayedExpansion 

set DIA=31
set MES=12
set ANO=2016

' F O R  /D %%p IN ("C:\Users\rafa\Documents\Artigos\Rede_da_presidente\DouDownloaded\pdf\*.*") D O  r m d i r "%%p" /s /q

:loop

	set DATA=%DIA%/%MES%/%ANO%
	echo data: !DATA!


    FOR /D %%p IN ("C:\Users\rafa\Documents\Artigos\Rede_da_presidente\DouDownloaded\temp\*.*") DO rmdir "%%p" /s /q


	java -jar C:\Users\rafa\Documents\Projects\DouDownloader\classes\artifacts\DouDownloader_jar\DouDownloader.jar !DATA! !DATA! C:\Users\rafa\Documents\Artigos\DatasetDOU
	
	if %DIA% LSS 4 (
		set /A DIA=%DIA%+1
		goto loop
	) else (
		set DIA=1
	)
	
	if %MES% LSS 1 (
		set /A MES=%MES%+1
		goto loop
	) else (
		set MES=1
	)
		
	if %ANO% GTR 2015 (
		set /A ANO=%ANO%-1
		goto loop
	) else (
		echo ACABEI"""
	)

	
