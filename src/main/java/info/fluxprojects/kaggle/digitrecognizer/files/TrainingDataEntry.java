package info.fluxprojects.kaggle.digitrecognizer.files;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Date: 28/10/14 - 21:16
 *
 * @author Jeroen Meulemeester
 */
public class TrainingDataEntry extends DataEntry {

	/**
	 * The numerical value of this entry
	 */
	private final int solution;

	/**
	 * Create a new training data entry with the given values
	 *
	 * @param id       The optional id used for the new entry
	 * @param image    The image encoded as 28x28 shorts with each pixel value between 0 and 255
	 * @param solution The solution of this data
	 */
	private TrainingDataEntry(final int id, final short[][] image, final int solution) {
		super(id, image);
		this.solution = solution;
	}

	/**
	 * Gets the numerical solution of this image in the training data
	 *
	 * @return The solution of this image entry
	 */
	public final int getSolution() {
		return solution;
	}


	/**
	 * Create a new {@link TrainingDataEntry} from the given CSV line
	 *
	 * @param id      The optional id used for the new entry
	 * @param csvLine The CSV line to parse as a new {@link TrainingDataEntry}
	 * @return The new entry, with the correct data from the CSV line
	 * @throws IllegalArgumentException When the given CSV entry could not be parsed
	 */
	public static final TrainingDataEntry fromCSVLine(final int id, final String csvLine) throws IllegalArgumentException {
		if (csvLine == null || csvLine.isEmpty()) {
			throw new IllegalArgumentException("Unable to parse CSV line [" + csvLine + "] as training data entry.");
		}

		final String[] parts = csvLine.split(",");
		if (parts.length != (1 + IMAGE_SIZE * IMAGE_SIZE)) {
			throw new IllegalArgumentException("Unable to parse CSV line [" + csvLine + "] as training data entry.");
		}

		final int solution = Integer.valueOf(parts[0]);
		final short[][] image = new short[IMAGE_SIZE][IMAGE_SIZE];
		for (int y = 0; y < image.length; y++) {
			for (int x = 0; x < image[y].length; x++) {
				final int index = 1 + x + (y * IMAGE_SIZE);
				final String rawValue = parts[index];
				image[x][y] = Short.valueOf(rawValue);
			}
		}

		return new TrainingDataEntry(id, image, solution);
	}

	/**
	 * Extract a list of training entries from the given CSV file
	 *
	 * @param fileName The name of the file containing the training data (with header)
	 * @return The list of training data
	 * @throws IOException When the file could not be read
	 */
	public static final List<TrainingDataEntry> fromFile(final String fileName) throws IOException {
		final List<TrainingDataEntry> entries = new ArrayList<>();
		try (final BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
			int id = 0;

			// Skip the header
			for (String line = reader.readLine(); (line = reader.readLine()) != null; id++) {
				final TrainingDataEntry entry = TrainingDataEntry.fromCSVLine(id, line);
				if (entry != null) {
					entries.add(entry);
				} else {
					throw new IllegalStateException("");
				}
			}

		}
		return entries;
	}

}
