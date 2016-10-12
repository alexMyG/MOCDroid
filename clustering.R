#!/usr/bin/env Rscript


library(tm)
library(clValid)


args = commandArgs(trailingOnly=TRUE)

if (length(args) == 0 | length(args) > 3) {
  stop("Incorrect number of parameters. Introduce csv file containing import terms, the number of clusters and the sparse parameter desired")
}

file_import_terms = args[1]
num_clusters = args[2]
sparse_parameter = args[3]
sparse_parameter <- as.numeric(sparse_parameter)
num_clusters <- as.numeric(num_clusters)

print(paste("File containing import terms:", file_import_terms))
print(paste("Number of clusters:", num_clusters))
print(paste("Sparse parameter:", sparse_parameter))
#file_import_terms <- "imports_malware.csv"

imports <- read.csv(file_import_terms, header = FALSE)

corp <- Corpus(DataframeSource(imports))
tdm <- TermDocumentMatrix(corp)

#num_clusters <- 20
#sparse_parameter <- 0.98

tdm_no_sparse <- removeSparseTerms(tdm, sparse_parameter)
tdm_matrix <- as.matrix(tdm_no_sparse)
intern <- clValid(tdm_matrix, num_clusters, clMethods = c("kmeans"), maxitems = 100000, validation = "internal")


clusters_kmeans <- clusters(intern,"kmeans")


clusters <- clusters_kmeans[[1]]$cluster
clusters_kmeans_matrix <- as.matrix(clusters)

write.csv(clusters_kmeans_matrix, paste('Data/matrix_', num_clusters, '_clusters_kmeans_sparse_', sparse_parameter ,"_", basename(file_import_terms), sep =""))

