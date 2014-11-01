package info.fluxprojects.kaggle.digitrecognozer.files;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.function.Predicate;

/**
 * Date: 1/11/14 - 13:07
 *
 * @author Jeroen Meulemeester
 */
public class DataEntry {

	/**
	 * The image size
	 */
	public static final int IMAGE_SIZE = 28;

	/**
	 * The id of this entry
	 */
	private final int id;

	/**
	 * The image representing this entry
	 */
	private final short[][] image = new short[IMAGE_SIZE][IMAGE_SIZE];

	/**
	 * Create a new data entry with the following parameters
	 *
	 * @param id    The optional id used for the new entry
	 * @param image The image encoded as 28x28 shorts with each pixel value between 0 and 255
	 */
	public DataEntry(final int id, final short[][] image) {
		this.id = id;
		for (int i = 0; i < image.length; i++) {
			System.arraycopy(image[i], 0, this.image[i], 0, IMAGE_SIZE);
		}
	}

	/**
	 * Get the id of this data entry (sequence number)
	 *
	 * @return The id of this data entry
	 */
	public final int getId() {
		return id;
	}

	/**
	 * Get the image encoded as 28x28 shorts with each pixel value between 0 and 255
	 *
	 * @return The image encoded as a 28x28 array of shorts
	 */
	public final short[][] getImage() {
		return image;
	}

	/**
	 * Count the number of pixels matching the given predicate
	 *
	 * @param predicate The predicate used for matching.
	 *                  When the predicate is 'null' none of the pixels is matched
	 * @return The number of pixels matching the predicate
	 */
	public final int countPixels(final Predicate<Short> predicate) {
		int count = 0;
		if (predicate != null) {
			for (final short[] line : image) {
				for (final short pixel : line) {
					if (predicate.test(pixel)) {
						count++;
					}
				}
			}
		}
		return count;
	}

	/**
	 * Convert this data entry to an actual image
	 *
	 * @return The image, as a new {@link java.awt.image.BufferedImage}
	 */
	public final BufferedImage toImage() {
		final int imageSize = 10;
		final BufferedImage bufferedImage = new BufferedImage(IMAGE_SIZE * 10, IMAGE_SIZE * 10, BufferedImage.TYPE_BYTE_GRAY);
		final Graphics graphics = bufferedImage.getGraphics();
		for (int y = 0; y < image.length; y++) {
			for (int x = 0; x < image[y].length; x++) {
				final short pixel = image[x][y];
				graphics.setColor(pixel > 128 ? Color.BLACK : Color.WHITE);
				graphics.setPaintMode();
				graphics.fillRect(x * imageSize, y * imageSize, imageSize, imageSize);
			}
		}
		return bufferedImage;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("TrainingDataEntry{\n");
		sb.append("id = ").append(id).append("\n");
		for (int y = 0; y < image.length; y++) {
			for (int x = 0; x < image[y].length; x++) {
				final short pixel = image[x][y];
				sb.append(String.format("%02X", pixel));
			}
			sb.append('\n');
		}
		sb.append("\n}\n");
		return sb.toString();
	}

}
