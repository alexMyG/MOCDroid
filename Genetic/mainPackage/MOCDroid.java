package mainPackage;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import ec.EvolutionState;
import ec.Evolve;
import ec.Individual;
import ec.Problem;
import ec.multiobjective.MultiObjectiveFitness;
import ec.simple.SimpleProblemForm;
import ec.util.Output;
import ec.util.Parameter;
import ec.util.ParameterDatabase;
import ec.vector.IntegerVectorIndividual;

public class MOCDroid extends Problem implements SimpleProblemForm {

	static PrintWriter writer;


	static String path; 


	//Files names containing clustering models and imports datasets
	static String fileClusteringMalware;
	static String fileClusteringBenignware;
	static String fileDatasetMalware;
	static String fileDatasetBenignware;

	//This class name, necessary to load ECJ library
	static String className = "mainPackage.MOCDroid";

	//Labels
	final static int MALWARE = 1;
	final static int BENIGNWARE = 0;


	//GA parameters
	final static int NUM_OBJECTIVES = 2;
	static int generations;
	static int population;
	static int breedElite;
	static double crossoverProb = 0.2;
	static double mutationProb = 0.1;

	static String sparse;

	//Segment 0 - Malware
	static int maxValueSegmentMalware = 1;
	static int minValueSegmentMalware = 0;


	//Segment 1 - Benignware
	static int maxValueSegmentBenignware = 1;
	static int minValueSegmentBenignware = 0;

	//General parameters
	static int numClustersMalware;
	static int numClustersBenignware;
	static int genomeSize;
	static double trainingPercentage = 0.5;


	//Class variables
	static HashMap<Integer, ArrayList<String>> clustersMalware;
	static HashMap<Integer, ArrayList<String>> clustersBenignware;
	static ArrayList<Example> training = new ArrayList<Example>();
	static ArrayList<Example> testing = new ArrayList<Example>();

	//Examples are load into memory in order to avoid multiple file reads
	static int [][] exampleClusterNumImportsTRAININGmalware;
	static int [][] exampleClusterNumImportsTRAININGbenignware;
	static int [][] exampleClusterNumImportsTESTmalware;
	static int [][] exampleClusterNumImportsTESTbenignware;

	public static void main (String[]args) {

		if (args.length !=  3 || args[0] == null){
			System.out.println("Introduce parameters file, path to folder with all necessary files and number of executions!");
			System.exit(0);
		}

		int maxExecutions = Integer.parseInt(args[2]); 

		path = args[1];


		String fileParameters = path + args[0];


		configure(fileParameters);



		for (int numExecution = 0; numExecution < maxExecutions; numExecution++){
			System.out.println();
			System.out.println("No. Execution: " + numExecution);
			System.out.println();


			// TODO Auto-generated method stub
			try {
				writer = new PrintWriter(path + "outputs/" + sparse + "-" + numClustersMalware + "M-" + numClustersBenignware + "B" 
						+ "-" + System.currentTimeMillis() + ".txt", "UTF-8");
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}



			writer.println("\n\n--------------------------------------------------");
			writer.println("--------------------------------------------------");

			writer.println("File clusters malware: " + fileClusteringMalware);
			writer.println("File clusters benignware: " + fileClusteringBenignware);
			writer.println("Dataset malware: " + fileDatasetMalware);
			writer.println("Dataset benignware: " + fileDatasetBenignware);
			writer.println("generations: " + generations);
			writer.println("population: " + population);
			writer.println("breedElite: " +  breedElite);
			writer.println("crossoverProb: " + crossoverProb);
			writer.println("mutationProb: " + mutationProb);
			writer.println("sparse: " + sparse);
			writer.println("Training percentage: " + trainingPercentage);
			writer.println("Clusters Malware: " + numClustersMalware);
			writer.println("Clusters Benignware: " + numClustersBenignware);
			writer.println("Training size: " + training.size());
			writer.println("Test size: " + testing.size());

			writer.println("--------------------------------------------------");

			//Genetic algorithm

			//Loading ECJ parameters file
			ParameterDatabase child = null;
			ParameterDatabase database = null;
			args = new String[] {"-file", "parameters.params"};
			database = Evolve.loadParameterDatabase(args);

			child = new ParameterDatabase();
			child.addParent(database);

			Output out = Evolve.buildOutput();

			EvolutionState evaluatedState = Evolve.initialize(child, 0, out);



			database.set(new Parameter("pop.subpop.0.species.genome-size"), Integer.toString(genomeSize));
			database.set(new Parameter("pop.subpop.0.species.min-gene"), "0");
			database.set(new Parameter("pop.subpop.0.species.max-gene"), Double.toString(1.0));
			database.set(new Parameter("eval.problem"), className);
			database.set(new Parameter("generations"), Integer.toString(generations));
			database.set(new Parameter("pop.subpop.0.size"), Integer.toString(population));
			database.set(new Parameter("breed.elite.0"), Integer.toString(breedElite));
			database.set(new Parameter("pop.subpop.0.species.crossover-prob"), Double.toString(crossoverProb));
			database.set(new Parameter("pop.subpop.0.species.mutation-prob"), Double.toString(mutationProb));
			database.set(new Parameter("pop.subpop.0.species.mutation-type"), "reset");


			// Segments
			database.set(new Parameter("pop.subpop.0.species.num-segments"), Integer.toString(2));
			database.set(new Parameter("pop.subpop.0.species.segment.0.min-gene"), Integer.toString(minValueSegmentMalware));
			database.set(new Parameter("pop.subpop.0.species.segment.0.max-gene"), Integer.toString(maxValueSegmentMalware));
			database.set(new Parameter("pop.subpop.0.species.segment.1.min-gene"), Integer.toString(minValueSegmentBenignware));
			database.set(new Parameter("pop.subpop.0.species.segment.1.max-gene"), Integer.toString(maxValueSegmentBenignware));


			database.set(new Parameter("pop.subpop.0.species.segment-type"), "start");
			database.set(new Parameter("pop.subpop.0.species.segment.0.start"), "0");
			database.set(new Parameter("pop.subpop.0.species.segment.1.start"), Integer.toString(numClustersMalware));



			evaluatedState.startFresh();

			int result = EvolutionState.R_NOTDONE;

			

			String evolutionOutput = "";
			//int countGenerations = 0;
			while( result == EvolutionState.R_NOTDONE ){
				
				result = evaluatedState.evolve();
			}

			Individual [] inds = evaluatedState.population.subpops[0].individuals;

			Individual bestIndividualFirstObjective = getBestFirstObjective(inds);
			ArrayList<Individual> bestSolutionArray = new ArrayList<Individual>();
			bestSolutionArray.add(bestIndividualFirstObjective);


			System.out.println("\n\n++++++++++++++++++++++++++++++++++++++++++++++++++++");
			System.out.println("BEST SOLUTION IN TRAINING: " );

			writer.println("\n\n++++++++++++++++++++++++++++++++++++++++++++++++++++");
			writer.println("BEST SOLUTION IN TRAINING: " );

			processSolutions(bestSolutionArray);

			System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++\n\n");
			writer.println("++++++++++++++++++++++++++++++++++++++++++++++++++++\n\n");

			ArrayList<Individual>  paretoFrontier = new ArrayList<Individual>();

			paretoFrontier = getParetoFrontier(inds);

			System.out.println("NUM SOLUTIONS PARETO FRONTIER: " + paretoFrontier.size());


			System.out.println("\n\n++++++++++++++++++++++++++++++++++++++++++++++++++++");
			System.out.println("PARETO FRONTIER: " );

			writer.println("\n\n++++++++++++++++++++++++++++++++++++++++++++++++++++");
			writer.println("PARETO FRONTIER: " );

			processSolutions(paretoFrontier);

			writer.println("++++++++++++++++++++++++++++++++++++++++++++++++++++\n\n");
			System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++\n\n");

			


			Evolve.cleanup(evaluatedState);
			writer.close();

		}

	}




	public static ArrayList<Individual> getParetoFrontier(Individual [] individuals){

		ArrayList<Individual> paretoFrontier = new ArrayList<Individual>();

		for (int i = 0; i < individuals.length; i++){
			Individual indRef = individuals[i];

			boolean goodCandidate = true;

			for (int j = 0; j < individuals.length; j++){
				Individual indCurr = individuals[j];
				if (i != j){
					IntegerVectorIndividual integerVectorIndividual1 = (IntegerVectorIndividual) indRef;
					MultiObjectiveFitness fitness1 = (MultiObjectiveFitness) integerVectorIndividual1.fitness;

					IntegerVectorIndividual integerVectorIndividual2 = (IntegerVectorIndividual) indCurr;
					MultiObjectiveFitness fitness2 = (MultiObjectiveFitness) integerVectorIndividual2.fitness;

					double obj1max = fitness1.getObjective(0);
					double obj1min = fitness1.getObjective(1);
					double obj2max = fitness2.getObjective(0);
					double obj2min = fitness2.getObjective(1);

					if (obj1max < obj2max && obj1min > obj2min){
						goodCandidate = false;
					}

				}
			}
			if (goodCandidate){
				paretoFrontier.add(indRef);
			}
		}
		return paretoFrontier;
	}

	//Method which reads the global parameters files 
	public static void configure(String files_parameter){
		try {
			String [] parametersFilesLines = new String(Files.readAllBytes(Paths.get(files_parameter))).split("\n");

			for (String parameter : parametersFilesLines){

				String parameterId = parameter.split(" ")[0];
				String parameterValue = parameter.split(" ")[1];

				switch(parameterId){
				case "clustersmalware":
					fileClusteringMalware = parameterValue;
					break;

				case "clustersbenignware":
					fileClusteringBenignware = parameterValue;
					break;

				case "datasetmalware":
					fileDatasetMalware = parameterValue;
					break;

				case "datasetbenignware":
					fileDatasetBenignware = parameterValue;
					break;

				case "population":
					population = Integer.parseInt(parameterValue);
					break;

				case "generations":
					generations = Integer.parseInt(parameterValue);
					break;

				case "breedelite":
					breedElite = Integer.parseInt(parameterValue);
					break;

				case "sparse":
					sparse = parameterValue;
					break;

				case "crossover":
					crossoverProb = Double.parseDouble(parameterValue);
					break;

				case "mutation":
					mutationProb = Double.parseDouble(parameterValue);
					break;

				default:
					System.out.println("Parameter: " + parameterId + " not recognised in files_parameters.txt. Process finished!");
					System.exit(0);

				}

			}

		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		fileClusteringMalware = path + "Data/" + fileClusteringMalware;
		fileClusteringBenignware = path + "Data/" + fileClusteringBenignware;
		fileDatasetMalware = path + "Data/" + fileDatasetMalware;
		fileDatasetBenignware = path + "Data/" + fileDatasetBenignware;


		clustersMalware = ReadClusters.readFile(fileClusteringMalware);
		clustersBenignware = ReadClusters.readFile(fileClusteringBenignware);

		try {

			List<String> listDatasetMalware = Files.readAllLines(Paths.get(fileDatasetMalware), StandardCharsets.UTF_8);
			List<String> listDatasetBenignware = Files.readAllLines(Paths.get(fileDatasetBenignware), StandardCharsets.UTF_8);
			String[] fileDatasetMalwareLines = listDatasetMalware.toArray(new String[listDatasetMalware.size()]);
			String[] fileDatasetBenignwareLines = listDatasetBenignware.toArray(new String[listDatasetMalware.size()]);

			ArrayList<Example> examplesMalware = new ArrayList<Example>();
			for (String line : fileDatasetMalwareLines){
				examplesMalware.add(new Example(MALWARE, line.split(" ")));
			}
			Collections.shuffle(examplesMalware);

			for (int i = 0; i < examplesMalware.size(); i++){
				if (i < examplesMalware.size() * trainingPercentage){
					training.add(examplesMalware.get(i));
				}else{
					testing.add(examplesMalware.get(i));
				}
			}

			ArrayList<Example> examplesBenignware = new ArrayList<Example>();
			for (String line : fileDatasetBenignwareLines){
				examplesBenignware.add(new Example(BENIGNWARE, line.split(" ")));
			}
			Collections.shuffle(examplesBenignware);
			for (int i = 0; i < examplesBenignware.size(); i++){
				if (i < examplesBenignware.size() * trainingPercentage){
					training.add(examplesBenignware.get(i));
				}else{
					testing.add(examplesBenignware.get(i));
				}
			}
			Collections.shuffle(training);
			Collections.shuffle(testing);


		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Error reading files!");
			System.exit(0);
		}


		exampleClusterNumImportsTRAININGmalware = new int [training.size()][numClustersMalware];
		exampleClusterNumImportsTRAININGbenignware = new int [training.size()][numClustersBenignware];
		exampleClusterNumImportsTESTmalware = new int [testing.size()][numClustersMalware];
		exampleClusterNumImportsTESTbenignware = new int [testing.size()][numClustersBenignware];

		//TRAINING
		int percentageCompleted = 0;
		//String stringCompleted = "";
		for (int numExample = 0; numExample < training.size(); numExample++){

			System.out.print("[ Running imports extraction in TRAINING ] " + percentageCompleted+ "% \r");
			Example example = training.get(numExample);
			String[] exampleArray = example.getImportsArray();

			int [] numImportsByClusterBenignware = getNumImportsByCluster(exampleArray, clustersBenignware);
			int [] numImportsByClusterMalware = getNumImportsByCluster(exampleArray, clustersMalware);
			exampleClusterNumImportsTRAININGbenignware[numExample] = numImportsByClusterBenignware;
			exampleClusterNumImportsTRAININGmalware[numExample] = numImportsByClusterMalware;
			percentageCompleted = (int)(((double)numExample / (double)training.size())*100.0);

		}

		//TESTING
		percentageCompleted = 0;
		for (int numExample = 0; numExample < testing.size(); numExample++){

			System.out.print("[ Running imports extraction in TESTING ] " + percentageCompleted+ "% \r");
			Example example = testing.get(numExample);
			String[] exampleArray = example.getImportsArray();

			int [] numImportsByClusterBenignware = getNumImportsByCluster(exampleArray, clustersBenignware);
			int [] numImportsByClusterMalware = getNumImportsByCluster(exampleArray, clustersMalware);
			exampleClusterNumImportsTESTbenignware[numExample] = numImportsByClusterBenignware;
			exampleClusterNumImportsTESTmalware[numExample] = numImportsByClusterMalware;
			percentageCompleted = (int)(((double)numExample / (double)testing.size())*100.0);

		}


		numClustersMalware = clustersMalware.size();
		numClustersBenignware = clustersBenignware.size();
		genomeSize = numClustersMalware + numClustersBenignware;


	}

	public static int [] getNumImportsByCluster(String [] example, HashMap<Integer, ArrayList<String>> clusteringModel){


		int [] numImportsByCluster = new int[clusteringModel.size()];

		Collection<String> listExample = new ArrayList<String>(Arrays.asList(example));



		for (int numCluster = 0; numCluster < clusteringModel.size(); numCluster++){

			Collection<String> listCluster = new ArrayList<String>(clusteringModel.get(numCluster));

			int countImportsRepresented = 0;

			Collection<String> listExampleAux = new ArrayList<String>(listExample);
			listExampleAux.retainAll(listCluster);
			countImportsRepresented = listExampleAux.size();
		
			numImportsByCluster[numCluster] = countImportsRepresented;




		}
		return numImportsByCluster;

	}


	public static void processSolutions(ArrayList<Individual> individuals){

		writer.println("\n----------------\n");
		for (Individual individual : individuals){
			IntegerVectorIndividual integerVectorIndividual = (IntegerVectorIndividual) individual;
			MultiObjectiveFitness fitness = (MultiObjectiveFitness) integerVectorIndividual.fitness;

			int [] genome = (int []) integerVectorIndividual.getGenome();

			int [] genomeMalware = new int[numClustersMalware];
			int [] genomeBenignware = new int[numClustersBenignware];
			System.arraycopy(genome, 0, genomeMalware, 0, numClustersMalware);
			System.arraycopy(genome, numClustersMalware, genomeBenignware, 0, numClustersBenignware);

			int individualScore = 0;
			int individualFalsePositives = 0;
			int individualFalseNegatives = 0;
			int individualTruePositives = 0;
			int individualTrueNegatives = 0;

			for (int numExample = 0; numExample < testing.size(); numExample++){
				Example example = testing.get(numExample);
				

				double scoreClusteringMalware = getScoreClusteringModelFAST(genomeMalware, exampleClusterNumImportsTESTmalware, clustersMalware, numExample);
				double scoreClusteringBenignware = getScoreClusteringModelFAST(genomeBenignware, exampleClusterNumImportsTESTbenignware, clustersBenignware, numExample);
				
				
				int labelAssigned = scoreClusteringMalware > scoreClusteringBenignware ? MALWARE : BENIGNWARE;
				
				
				if (labelAssigned == example.getLabel()){
					individualScore +=1;
				}

				if (labelAssigned == MALWARE && example.getLabel() == BENIGNWARE){
					individualFalsePositives += 1;
				}
				if (labelAssigned == MALWARE && example.getLabel() == MALWARE){
					individualTruePositives += 1;
				}
				if (labelAssigned == BENIGNWARE && example.getLabel() == BENIGNWARE){
					individualTrueNegatives += 1;
				}
				if (labelAssigned == BENIGNWARE && example.getLabel() == MALWARE){
					individualFalseNegatives += 1;
				}



			}

			double accuracy = (double) individualScore / (double) testing.size();

			System.out.println("New individual. Fitness: [" + fitness.getObjective(0) + ", " + fitness.getObjective(1) + "]");

			System.out.println("Testing accuracy: " + accuracy + " - False positives: " + individualFalsePositives);

			writer.println("New individual. Fitness: [" + fitness.getObjective(0) + ", " + fitness.getObjective(1) + "]");
			writer.println("Testing accuracy: " + accuracy);
			writer.println("True positives: " + individualTruePositives);
			writer.println("False negatives: " + individualFalseNegatives);
			writer.println("False positives: " + individualFalsePositives);
			writer.println("True negatives: " + individualTrueNegatives);

			writer.println(Arrays.toString(genome));
			writer.println("----------------\n");

		}
	}


	@Override
	public void evaluate(EvolutionState state, Individual individual, int subpopulation, int threadnum) {
		// TODO Auto-generated method stub
		if (individual.evaluated)
			return;

		IntegerVectorIndividual integerVectorIndividual = (IntegerVectorIndividual) individual;
		int [] genome = (int []) integerVectorIndividual.getGenome();

		int [] genomeMalware = new int[numClustersMalware];
		int [] genomeBenignware = new int[numClustersBenignware];
		System.arraycopy(genome, 0, genomeMalware, 0, numClustersMalware);
		System.arraycopy(genome, numClustersMalware, genomeBenignware, 0, numClustersBenignware);

		int individualScore = 0;
		int individualFalsePositives = 0;

		
		for (int numExample = 0; numExample < training.size(); numExample++){
			Example example = training.get(numExample);
	

			double scoreClusteringMalware = getScoreClusteringModelFAST(genomeMalware, exampleClusterNumImportsTRAININGmalware, clustersMalware, numExample);
			double scoreClusteringBenignware = getScoreClusteringModelFAST(genomeBenignware, exampleClusterNumImportsTRAININGbenignware, clustersBenignware, numExample);
			
			
			int labelAssigned = scoreClusteringMalware > scoreClusteringBenignware ? MALWARE : BENIGNWARE;

			if (labelAssigned == example.getLabel()){
				individualScore +=1;
			}

			if (labelAssigned == MALWARE && example.getLabel() == BENIGNWARE){
				individualFalsePositives += 1;
			}


		}

		double individualAccuracyTraining = (double)individualScore/ (double) training.size();

		MultiObjectiveFitness fitness = (MultiObjectiveFitness) integerVectorIndividual.fitness;

		fitness.setObjectives(state, new double[]{individualAccuracyTraining, individualFalsePositives});

		integerVectorIndividual.evaluated = true;


	}

	/*
	 * arrayExamplesID:
	 * 	 	0 - training benignware
	 * 		1 - training malware
	 * 		2 - testing benignware
	 * 		3 - testing malware
	 * 
	 */
	public static double getScoreClusteringModelFAST(int [] genome, int [][] clusteringExamplesRepresentations,  HashMap<Integer, ArrayList<String>> clusteringModel, int numExample){

		int score = 0;
		for (int numCluster = 0; numCluster < genome.length; numCluster++){
			if (genome[numCluster] != 1){
				continue;
			}

			int numImportsClusterRepresented = clusteringExamplesRepresentations[numExample][numCluster];

			if (numImportsClusterRepresented == clusteringModel.get(numCluster).size()){

				score += 1;
			}
		}				

		return (double)score/ (double) genome.length;

	}





	public static Individual getBestFirstObjective(Individual [] individuals){

		if (individuals == null || individuals.length == 0){
			System.out.println("Get first objective can not be executed. Empty individuals array");
			return null;
		}
		Individual selected = individuals[0];
		IntegerVectorIndividual integerVectorIndividual = (IntegerVectorIndividual) selected;
		MultiObjectiveFitness fitnessSelected = (MultiObjectiveFitness) integerVectorIndividual.fitness;

		double maxObjective = fitnessSelected.getObjective(0);

		for (int i = 1 ; i < individuals.length; i++){
			Individual selectedNew = individuals[i];
			IntegerVectorIndividual integerVectorIndividualNew = (IntegerVectorIndividual) selectedNew;
			MultiObjectiveFitness fitnessSelectedNew = (MultiObjectiveFitness) integerVectorIndividualNew.fitness;

			double maxObjectiveNew = fitnessSelectedNew.getObjective(0);

			if (maxObjectiveNew > maxObjective){
				selected = selectedNew;
				maxObjective = maxObjectiveNew;
			}

		}


		return selected;



	}






}
