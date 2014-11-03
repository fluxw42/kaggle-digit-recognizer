package info.fluxprojects.kaggle.digitrecognizer;

import info.fluxprojects.kaggle.digitrecognizer.files.TrainingDataEntry;
import org.neuroph.core.data.DataSet;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.util.TransferFunctionType;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Date: 2/11/14 - 11:28
 *
 * @author Jeroen Meulemeester
 */
public class NeuralNetworkSolution {

	/**
	 * The CSV file containing the training data
	 */
	public static final String TRAINING_DATA_FILE_NAME = DataRecognizer.class.getResource("/train.csv").getFile();

	/**
	 * The CSV file containing the actual test data
	 */
	public static final String TEST_DATA_FILE_NAME = DataRecognizer.class.getResource("/test.csv").getFile();

	public static void main(String[] args) throws Exception {
		learn("network.nnet");
	}

	/**
	 * Create a neural network and train it with the training data.
	 * When finished, save it to the given neural net file
	 *
	 * @param neuralNetFile The file name used to save the neural network to
	 * @throws IOException When the neural network could not be saved
	 */
	private static void learn(final String neuralNetFile) throws IOException {
		System.out.println("Reading data entries from [" + TRAINING_DATA_FILE_NAME + "]");
		final List<TrainingDataEntry> trainingEntries = TrainingDataEntry.fromTrainingCSV(TRAINING_DATA_FILE_NAME);
		System.out.println("Found [" + trainingEntries.size() + "] training entries. Converting to datasets ...");

		final int inputLength = trainingEntries.get(0).getPlainImage().length;
		final DataSet trainingSet = new DataSet(inputLength, 10);
		for (int i = 0; i < trainingEntries.size(); i++) {
			final TrainingDataEntry trainingEntry = trainingEntries.get(i);
			final double[] input = Arrays.stream(trainingEntry.getPlainImage())
					.mapToDouble(pixel -> pixel == 0 ? 0 : 1)
					.toArray();

			final double[] output = new double[10];
			for (int value = 0; value < output.length; value++) {
				output[value] = trainingEntry.getSolution() != value ? 0 : 1;
			}

			trainingSet.addRow(input, output);
		}

		System.out.println("Training neural network ...");
		final MultiLayerPerceptron network = new MultiLayerPerceptron(TransferFunctionType.SIGMOID, inputLength, 1000, 10);
		network.learn(trainingSet);

		System.out.println("Saving neural network to [" + neuralNetFile + "]");
		network.save(neuralNetFile);

	}

}
