package testy.helper;

import java.util.Collection;

public class UnmodifiableChecker {

	public static boolean isUnmodifiable(Collection<?> collection) {
		try {
			collection.clear();
		} catch (UnsupportedOperationException e) {
			return true;
		}
		return false;
	}
	
}
