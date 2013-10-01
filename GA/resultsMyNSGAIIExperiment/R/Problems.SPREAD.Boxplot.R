postscript("Problems.SPREAD.Boxplot.eps", horizontal=FALSE, onefile=FALSE, height=8, width=12, pointsize=10)
resultDirectory<-"../data/"
qIndicator <- function(indicator, problem)
{
fileSBXEscal<-paste(resultDirectory, "SBXEscal", sep="/")
fileSBXEscal<-paste(fileSBXEscal, problem, sep="/")
fileSBXEscal<-paste(fileSBXEscal, indicator, sep="/")
SBXEscal<-scan(fileSBXEscal)

fileBLXEscal<-paste(resultDirectory, "BLXEscal", sep="/")
fileBLXEscal<-paste(fileBLXEscal, problem, sep="/")
fileBLXEscal<-paste(fileBLXEscal, indicator, sep="/")
BLXEscal<-scan(fileBLXEscal)

algs<-c("SBXEscal","BLXEscal")
boxplot(SBXEscal,BLXEscal,names=algs, notch = TRUE)
titulo <-paste(indicator, problem, sep=":")
title(main=titulo)
}
par(mfrow=c(2,3))
indicator<-"SPREAD"
qIndicator(indicator, "MyProblem")
