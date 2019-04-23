library(ggplot2)
data <- read.csv(file="/Users/mazharul.islam/Desktop/compression-files/large/result.csv", header=TRUE, sep=",")
print(data)
p =  ggplot(data, aes(x=data$X, y = data$Y))
p =  ggplot(data, aes(fill= reorder(data$method, -data$filesize), y= data$time,  x=data$method)) 
p = p + geom_bar(stat="identity",colour="black")
p = p + facet_grid(.~data$Filename)
#p = p + theme_bw()
p =  p + theme(legend.title=element_blank())
p = p + theme(axis.title.x=element_blank(),
              axis.text.x=element_blank(),
              axis.ticks.x=element_blank())
p = p +  ylab( "average compress and decompress time" )
p
