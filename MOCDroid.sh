Rscript clustering.R Data/imports_malware.csv
Rscript clustering.R Data/imports_benignware.csv
cd Genetic
javac -cp ".:ecj.22.jar" mainPackage/*.java
java -cp ".:ecj.22.jar" mainPackage.MOCDroid file_parameters.txt /full_path_to/MOCDroid/ 1
cd ..
cd outputs/
python resultsExtractor.py
Rscript plotting.R
