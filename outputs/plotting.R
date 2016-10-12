library(ggplot2)
library(dplyr)
library(scales)

data <- read.csv("results.csv")

data$sparse <- data$sparse / 1000

testSize <- data$testFalseNegatives[1] + data$testFalsePositives[1] + data$testTruePositives[1] + data$testTrueNegatives[1]

data$testFalsePositives <- data$testFalsePositives / testSize


pdf("figure3a.pdf", width=8, height=3)
plot1bar <- ggplot(data, aes(x = numClustersBenignware, y = testAcc,  fill = factor(sparse))) +
  geom_bar(stat = "summary", fun.y = "mean", position = "dodge") +
  labs(x = "Number of clusters", y = "Accuracy")  +
  theme(axis.text = element_text(colour = "black")) + 
  labs(name = "Sparse\nparameter") + scale_y_continuous(limits=c(0.77,0.95), breaks = seq(0.0, 0.95, by = 0.05),oob = rescale_none) +scale_fill_brewer()+
  scale_x_continuous(limits=c(10,190), breaks = seq(20, 180, by = 20)) +
  scale_linetype_discrete(name = "Sparse\nParameter") + 
  scale_shape_discrete(name = "Sparse\nParameter") + 
  scale_colour_discrete(name = "Sparse\nParameter")
plot1bar
dev.off()




pdf("figure3b.pdf", width=8, height=3)
plot2bar <- ggplot(data, aes(x = numClustersBenignware, y = testFalsePositives,  fill = factor(sparse))) +
  geom_bar(stat = "summary", fun.y = "mean", position = "dodge") +# + geom_point(stat = "summary", fun.y = "mean") +
  
  labs(x = "Number of clusters", y = "Accuracy")  +
  theme(#axis.text.x = element_text(hjust = 1, size=7), 
    axis.text = element_text(colour = "black")) + 
  labs(name = "Sparse\nparameter") + scale_y_continuous(limits=c(0.015,0.175), breaks = seq(0.0, 0.3, by = 0.025),oob = rescale_none) +scale_fill_brewer()+
  
  
  
  scale_x_continuous(limits=c(10,190), breaks = seq(20, 180, by = 20)) +
  scale_linetype_discrete(name = "Sparse\nParameter") + 
  scale_shape_discrete(name = "Sparse\nParameter") + 
  scale_colour_discrete(name = "Sparse\nParameter")
plot2bar
dev.off()


dataPlot3 <- data[data$sparse == 0.970,]



pdf("figure5.pdf", width=5, height=5)
plot3 <- ggplot(dataPlot3,aes(x = factor(numClustersBenignware), y = testAcc, color = factor(numClustersBenignware))) +
  geom_boxplot(stat = "boxplot")+
  labs(x = "Number of clusters", y = "Accuracy")+
  theme(axis.text = element_text(colour = "black")) +guides(color=FALSE)

plot3
dev.off()

dataImports <- read.csv(file="numImportTermsSparse.csv")
pdf("figure4.pdf", width=5, height=3.5)
plot4 <- ggplot(dataImports, aes(x = sparse)) + 
  geom_line(aes(y = termsBenignware, linetype = "Benignware")) +
  geom_line(aes(y = termsMalware, linetype = "Malware")) +
  geom_point(aes(y = termsBenignware)) +
  geom_point(aes(y = termsMalware)) +
  labs(x = "Sparse parameter", y = "Number of import terms") +
  scale_linetype_discrete(name = "Label") +
  scale_y_continuous(limits=c(500,14500), breaks = seq(500, 14500, by = 2000))+
  theme(legend.position="bottom")
plot4
dev.off()















