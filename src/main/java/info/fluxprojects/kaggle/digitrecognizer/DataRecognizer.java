package info.fluxprojects.kaggle.digitrecognizer;

import info.fluxprojects.kaggle.digitrecognizer.files.TrainingDataEntry;

import java.util.IntSummaryStatistics;
import java.util.List;

/**
 * Date: 28/10/14 - 21:05
 *
 * @author Jeroen Meulemeester
 */
public class DataRecognizer {

	/**
	 * The training data resource
	 */
	public static final String TRAINING_DATA_FILE_NAME = DataRecognizer.class.getResource("/train.csv").getFile();

	public static void main(String[] args) throws Exception {
		final List<TrainingDataEntry> entries = TrainingDataEntry.fromFile(TRAINING_DATA_FILE_NAME);
		final IntSummaryStatistics[] statistics = new IntSummaryStatistics[10];
		for (int i = 0; i < statistics.length; i++) {
			final int index = i;
			statistics[i] = entries.parallelStream()
					.filter(e -> e.getSolution() == index)
					.mapToInt(e -> e.countPixels(p -> p > 0))
					.summaryStatistics();
		}

		for (int i = 0; i < statistics.length; i++) {
			System.out.println(i + "  ->  " + statistics[i]);
		}

	}

}
