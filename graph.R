library(ggplot2)
data <- read.csv(file="/Users/mazharul.islam/compression/adaptive-huffman/results/CompressionFactor.csv", header=TRUE, sep=",")
print(data)


sizes = c(1024, 2048, 4096, 8192, 16384)
xLabels = c("1024", "2048", "4096", "8192", "16384")
print(sizes)
means = c()
sds = c()
for(size in sizes){
  #print(size)
  newData = data[data$FileSize == size,]
  #print(mean(newData$CompressedSize))
  means = c(means, mean(size/newData$CompressedSize))
  #print(sd(newData$CompressedSize))
  sds = c(sds, sd(size/newData$CompressedSize))
}
algos = c(rep("ECC",5))
comFac = data.frame(
  "mean" =  means,
  "std" =  sds,
  "sizes" =  sizes,
  "algo" = algos,
  "labels"= xLabels
)
print(comFac)

#print(means)
#print(sds)
p =  ggplot(comFac, aes(fill=comFac$algo, y=comFac$mean, x=reorder(comFac$labels,comFac$mean)))
p = p + geom_bar(position="dodge", stat="identity",colour="black")
p = p + geom_errorbar( aes(ymin=comFac$mean-comFac$std, ymax=comFac$mean+comFac$std), 
                      width=0.3, position=position_dodge(.9))+
                      ylab("average compressed file size")+
                      xlab("original file size")
p =  p + theme(legend.title=element_blank())
p            

