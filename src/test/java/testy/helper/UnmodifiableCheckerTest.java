package testy.helper;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static testy.helper.UnmodifiableChecker.isUnmodifiable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.TreeSet;

import org.junit.Test;

public class UnmodifiableCheckerTest {

	@Test
	public void isUnmodifiable_shouldReturnTrueIfCollectionIsUnmodifiable() {
		assertTrue("Should return true", isUnmodifiable(Collections.unmodifiableCollection(new ArrayList<Object>())));
		assertTrue("Should return true", isUnmodifiable(Collections.unmodifiableList(new ArrayList<Object>())));
		assertTrue("Should return true", isUnmodifiable(Collections.unmodifiableSet(new HashSet<Object>())));
		assertTrue("Should return true", isUnmodifiable(Collections.unmodifiableSortedSet(new TreeSet<Object>())));
	}
	
	@Test
	public void isUnmodifiable_shouldReturnFalseIfCollectionIsUnmodifiable() {
		assertFalse("Should return false", isUnmodifiable(new ArrayList<Object>()));
		assertFalse("Should return false", isUnmodifiable(new ArrayList<Object>()));
		assertFalse("Should return false", isUnmodifiable(new HashSet<Object>()));
		assertFalse("Should return false", isUnmodifiable(new TreeSet<Object>()));
	}

}
