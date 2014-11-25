import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author miguelangelnavasgarcia
 * <p> Test for Algorithms: Design and Analysis, Part 1
 * Given a file with number, order the list and count the number of inversions contained in it.</p>
 * <p>This is a merge sort algorithm that counts the inversions. Just change the generic Pair</p>
 * <p>A generic class is used to contain the ordered list and the total number of inversion, 
 * this is breaking the <a href="http://en.wikipedia.org/wiki/Single_responsibility_principle">Single responsability principle</a>,
 * but this is fast solution.
 * @see <a href="http://en.wikipedia.org/wiki/Merge_sort">Merge sort</a>
 */
public class CountInversions {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		List<Integer> numbers = getNumbers(); 
		Pair<Long, List<Integer>> inversorsAndSort = new Pair<Long, List<Integer>>(0L, numbers);
		inversorsAndSort = countInversions(inversorsAndSort);

		System.out.println("Nubers list elements: " + numbers.toString());
		System.out.println("Ordered list elements: " + inversorsAndSort.getRight().toString());
		System.out.println("Number of elements: " + numbers.size());
		System.out.println("Number of inversion: " + inversorsAndSort.getLeft());
		System.out.println("Ordered list elements: " + inversorsAndSort.getRight().size());
	}

	private static Pair<Long, List<Integer>> countInversions(Pair<Long, List<Integer>> inversorsAndSort) {
		if (inversorsAndSort.getRight().size() == 1) return inversorsAndSort;
		
		Pair<Long, List<Integer>> leftInversion = countInversions(getHalfLeftArray(inversorsAndSort));
		Pair<Long, List<Integer>> rightInversions = countInversions(getHalfRightArray(inversorsAndSort));
		return countSplitInversions(leftInversion, rightInversions);
	}

	private static Pair<Long, List<Integer>> countSplitInversions(
			final Pair<Long, List<Integer>> leftInversion, final Pair<Long, List<Integer>> rightInversions) {
		List<Integer> sortedList = new ArrayList<Integer>();
		int leftPointer = 0;
		int rightPointer = 0;
		int inversion = 0;
		while (leftInversion.getRight().size() > leftPointer || rightInversions.getRight().size() > rightPointer) {
			if (leftInversion.getRight().size() > leftPointer && rightInversions.getRight().size() > rightPointer) {
				if (leftInversion.getRight().get(leftPointer) <= rightInversions.getRight().get(rightPointer)) {
					sortedList.add(leftInversion.getRight().get(leftPointer));
					leftPointer++;
				} else {
					//aqui hay una inversion [leftInversion.get(leftPointer), rightInversions.get(rightPointer)]
					// y se debe contar todos los elementos que quedan en el arrai izquierdo, ya que esos elementos son
					//menores que el elemento de la derecha que se esta ordenando.
					inversion += remainingElements(leftInversion, leftPointer);
					sortedList.add(rightInversions.getRight().get(rightPointer));
					rightPointer++;
				}
			} else if (leftInversion.getRight().size() > leftPointer) {
				sortedList.add(leftInversion.getRight().get(leftPointer));
				leftPointer++;
			} else if (rightInversions.getRight().size() > rightPointer) {
				sortedList.add(rightInversions.getRight().get(rightPointer));
				rightPointer++;
			}
		}
		return new Pair<Long, List<Integer>>(inversion + leftInversion.getLeft() + rightInversions.getLeft(), sortedList);
	}

	private static int remainingElements(
			Pair<Long, List<Integer>> leftInversion, int leftPointer) {
		return leftInversion.getRight().size() - leftPointer;
	}

	private static Pair<Long, List<Integer>> getHalfRightArray(Pair<Long, List<Integer>> numbers) {
		return new Pair<Long, List<Integer>> (numbers.getLeft(),
				new LinkedList<Integer>(numbers.right.subList(numbers.right.size() / 2, numbers.right.size())));
	}

	private static Pair<Long, List<Integer>> getHalfLeftArray(Pair<Long, List<Integer>> numbers) {
		return new Pair<Long, List<Integer>> (numbers.getLeft(),
				new LinkedList<Integer>(numbers.right.subList(0, numbers.right.size() / 2)));
	}
	

	/**
	 * 
	 * @return the list of numbers contained in the file
	 */
	private static List<Integer> getNumbers() {
		BufferedReader br = null;
		List<Integer> numbers = new LinkedList<Integer>();
		try {
			br = new BufferedReader(new FileReader("../file.txt"));

			String line = br.readLine();

			while (line != null) {
				if (line != null) {
					numbers.add(Integer.parseInt(line));
				}
				line = br.readLine();
			}
			br.close();
		} catch (IOException e) {
				e.printStackTrace();
		}
		return numbers;
	}

	/**
	 * 
	 * @author miguelangelnavasgarcia
	 * <p>Thanks Stackoverflow</p>
	 * @see <a href="http://stackoverflow.com/questions/521171/a-java-collection-of-value-pairs-tuples?#521235">Pair tuples</a>
	 *
	 * @param <L>
	 * @param <R>
	 */
	public static class Pair<L,R> {

		  private L left;
		  private R right;

		  public Pair(L left, R right) {
		    this.left = left;
		    this.right = right;
		  }

		  public L getLeft() { return left; }
		  public R getRight() { return right; }

		  @Override
		  public int hashCode() { return left.hashCode() ^ right.hashCode(); }

		  @Override
		  public boolean equals(Object o) {
		    if (o == null) return false;
		    if (!(o instanceof Pair)) return false;
		    Pair pairo = (Pair) o;
		    return this.left.equals(pairo.getLeft()) &&
		           this.right.equals(pairo.getRight());
		  }

		}
	
}
