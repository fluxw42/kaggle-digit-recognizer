package info.fluxprojects.kaggle.utils;

import java.io.Closeable;
import java.io.IOException;

/**
 * Date: 1/11/14 - 12:55
 *
 * @author Jeroen Meulemeester
 */
public final class Utils {

	/**
	 * Hidden default constructor to prevent instantiation: It's a util class with only static methods
	 */
	private Utils() {
		throw new IllegalAccessError("You're not supposed to create a new Utils instance ...");
	}

	/**
	 * Close the given {@link Closeable} object. Absorb exceptions and ignore when the {@link Closeable} is 'null'.
	 *
	 * @param closeable The {@link Closeable} object that has to be closed
	 */
	public static final void close(final Closeable closeable) {
		if (closeable != null) {
			try {
				closeable.close();
			} catch (IOException e) {
				// Absorb exception
			}
		}
	}

}
