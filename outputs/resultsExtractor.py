import sys
from os.path import isfile, join, isdir
from os import listdir

directory = "."
prefix = ""
results_files = [f for f in listdir(directory + "/") if isfile(join(directory, f)) and f. endswith(".txt")]

output_file = open(prefix + "results.csv", "w")

header = "sparse,numClustersMalware,numClustersBenignware,trainingAcc,trainingFalsePositives,testAcc,testTruePositives,testFalseNegatives,testFalsePositives,testTrueNegatives"

output_file.write(header + "\n")

for results_file in results_files:

	file_name_array = results_file.replace("B", "").replace("M", "").split("-")
	sparse = file_name_array[0]
	numClustersMalware = file_name_array[1]
	numClustersBenignware = file_name_array[2]

	file_lines = open(directory + "/" + results_file, "rb").readlines()

	testAccuracy = None
	testFalsePositives = None
	trainingAccuracy = None
	trainingFalsePositives = None

	for num_line in range(0, len(file_lines)):

		line = file_lines[num_line]

		if line.startswith("BEST SOLUTION IN TRAINING"):

			num_line += 4
			line = file_lines[num_line]
			fitness_part = line[ line.index("[")+1 : line.index("]") ]
			fitness_part_array = fitness_part.split(", ")
			trainingAccuracy = fitness_part_array[0]
			trainingFalsePositives = fitness_part_array[1]

			num_line += 1
			line = file_lines[num_line]

			testAccuracy = line.split(": ")[1].replace("\n", "")


			num_line += 1
			line = file_lines[num_line]

			testTruePositives = line.split(": ")[1].replace("\n", "")


			num_line += 1
			line = file_lines[num_line]

			testFalseNegatives = line.split(": ")[1].replace("\n", "")

			num_line += 1
			line = file_lines[num_line]

			testFalsePositives = line.split(": ")[1].replace("\n", "")


			num_line += 1
			line = file_lines[num_line]

			testTrueNegatives = line.split(": ")[1].replace("\n", "")



			#print "TEST ACCURACY " + testAccuracy + "__\n"

			
			continue


	output_line = sparse + "," + numClustersMalware + "," + numClustersBenignware + "," + trainingAccuracy + "," + trainingFalsePositives + "," + testAccuracy + "," + testTruePositives + "," + testFalseNegatives + "," + testFalsePositives + "," + testTrueNegatives + "\n"

	output_file.write(output_line)

output_file.close()
