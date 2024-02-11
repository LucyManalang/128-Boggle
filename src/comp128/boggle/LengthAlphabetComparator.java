package comp128.boggle;

import java.util.Comparator;

/*
 * Class that implements comparator to compare to objects by length first and then alphabetical
 * Authors: Miri Leonard, May Kinnamon, Lucy Manalang 12/13/23
 */
public class LengthAlphabetComparator implements Comparator<String>{

    @Override
    public int compare(String one, String two) {
        if (one.length() != two.length()) {
            return one.length() - two.length();
        } else {
            return one.compareTo(two);
        }
    }
    
}
