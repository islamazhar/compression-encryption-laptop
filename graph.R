library(ggplot2)
data1 <- read.csv(file="/Users/mazharul.islam/compression/HComECC/result.csv", header=TRUE, sep=",")
print(data1)
#data1 = data[ data$genetrees == "200g" & data$basepair == "500b", ]
#data1 = data[ data$ilsLabel == "noscale" & data$genetrees == "200g", ]
#data1 = data[ data$ilsLabel == "noscale" & data$basepair == "500b", ]
#print(data1)
#nrow(data1)
p =  ggplot(data1, aes(y=data1$time, x=data1$size, group=data1$metod)) + geom_line(aes(linetype=data1$metod)) + geom_point(aes(shap=data1$metod))
p = p + scale_color_grey()  
#p = p + geom_line(position="dodge", stat="identity",colour="black", width =.8)
#p = p + geom_point(position="dodge", stat="identity",colour="black", width =.8)
#p =  p + theme(axis.text= element_text(size = 12, face = "bold"))
#p =  p + theme(axis.text.y = element_text(size = 12, face = "bold"))
#p = p + scale_color_discrete(name = "Y series", labels = c("FTPS", "ourmethod+FTP"))
#p = p + geom_errorbar(aes(ymin=data1$mean-data1$std, ymax=data1$mean+data1$std),
                     # width=0.3,                    # Width of the error bars
                      #position=position_dodge(.8))+ylab("average FN rate") + xlab("sequence length")
#p  = p + facet_grid(cols = vars(data1$basepair)) + theme(strip.text.x = element_text(size = 12, face="bold"))
p