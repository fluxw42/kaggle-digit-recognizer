package info.fluxprojects.kaggle.digitrecognizer;

import info.fluxprojects.kaggle.digitrecognizer.files.DataEntry;
import info.fluxprojects.kaggle.digitrecognizer.files.TrainingDataEntry;

import java.util.IntSummaryStatistics;
import java.util.List;

/**
 * Date: 28/10/14 - 21:05
 *
 * @author Jeroen Meulemeester
 */
public class PixelCountSolution {

	/**
	 * The training data resource
	 */
	public static final String TRAINING_DATA_FILE_NAME = PixelCountSolution.class.getResource("/train.csv").getFile();

	/**
	 * The real data resource
	 */
	public static final String REAL_DATA_FILE_NAME = PixelCountSolution.class.getResource("/test.csv").getFile();

	public static void main(String[] args) throws Exception {
		final List<TrainingDataEntry> trainingEntries = TrainingDataEntry.fromTrainingCSV(TRAINING_DATA_FILE_NAME);
		final IntSummaryStatistics[] statistics = new IntSummaryStatistics[10];
		for (int i = 0; i < statistics.length; i++) {
			final int index = i;
			statistics[i] = trainingEntries.parallelStream()
					.filter(e -> e.getSolution() == index)
					.mapToInt(e -> e.countPixels(p -> p > 0))
					.summaryStatistics();
		}

		for (int i = 0; i < statistics.length; i++) {
			System.out.println(i + "  ->  " + statistics[i]);
		}

		final StringBuilder sb = new StringBuilder();
		sb.append("ImageId,Label\n");

		final List<DataEntry> entries = DataEntry.fromCSV(REAL_DATA_FILE_NAME);
		for (final DataEntry entry : entries) {
			sb.append((entry.getId() + 1) + "," + getValue(entry, statistics) + "\n");
		}
		System.out.println(sb.toString());
	}

	private static int getValue(final DataEntry entry, final IntSummaryStatistics[] statistics) {
		double minDiff = Double.MAX_VALUE;
		final double[] diff = new double[statistics.length];
		for (int i = 0; i < statistics.length; i++) {
			final IntSummaryStatistics statistic = statistics[i];
			final int pixels = entry.countPixels(p -> p > 0);
			diff[i] = Math.abs(statistic.getAverage() - pixels);
			minDiff = Math.min(minDiff, diff[i]);
		}

		for (int i = 0; i < diff.length; i++) {
			if (diff[i] == minDiff) {
				return i;
			}
		}
		throw new IllegalStateException();
	}

}
