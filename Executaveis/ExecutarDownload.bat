cls
@echo off 
setlocal enableDelayedExpansion 

set DIA=31
set MES=12
set ANO=2016




mkdir arquivos\temp

:loop

	set DATA=%DIA%/%MES%/%ANO%
	echo data: !DATA!

	

   echo DouDownloader_jar\DouDownloader.jar !DATA! !DATA! arquivos
	
	if %DIA% LSS 31 (
		set /A DIA=%DIA%+1
		goto loop
	) else (
		set DIA=1
	)
	
	if %MES% LSS 12 (
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

	
