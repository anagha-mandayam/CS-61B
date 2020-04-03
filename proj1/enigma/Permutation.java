package enigma;
import java.util.HashMap;

/** Represents a permutation of a range of integers starting at 0 corresponding
 *  to the characters of an alphabet.
 *  @author Anagha Mandayam
 */
class Permutation {

    /** Set this Permutation to that specified by CYCLES, a string in the
     *  form "(cccc) (cc) ..." where the c's are characters in ALPHABET, which
     *  is interpreted as a permutation in cycle notation.  Characters in the
     *  alphabet that are not included in any cycle map to themselves.
     *  Whitespace is ignored. */
    Permutation(String cycles, Alphabet alphabet) {
        _alphabet = alphabet;
        _cycles = cycles;
        addCycle(cycles);
    }

    /** Add the cycle c0->c1->...->cm->c0 to the permutation, where CYCLE is
     *  c0c1...cm. */
    private void addCycle(String cycle) {

        HashMap<Character, Character> permuteCycles =
                new HashMap<Character, Character>();
        for (int i = 0; i < cycle.length(); i++) {
            if (cycle.charAt(i) == ')' || cycle.charAt(i) == ' ') {
                continue;
            }
            char currentLetter = cycle.charAt(i);
            if (currentLetter != '(' && currentLetter != ')') {
                char nextLetter = cycle.charAt(i + 1);
                if (nextLetter != ')') {
                    permuteCycles.put(currentLetter, nextLetter);
                } else {
                    for (int j = i; j >= 0; j--) {
                        if (cycle.charAt(j) == '(') {
                            permuteCycles.put(currentLetter,
                                    cycle.charAt(j + 1));
                            break;
                        }
                    }
                }
            }
        }

        HashMap<Character, Character> invertCycles =
                new HashMap<Character, Character>();
        for (int i = 0; i < cycle.length(); i++) {
            if (cycle.charAt(i) == ')' || cycle.charAt(i) == ' ') {
                continue;
            }
            char previousLetter = cycle.charAt(i);
            char currentLetter = cycle.charAt(i + 1);
            if (previousLetter != '(' && previousLetter != ')') {
                if (currentLetter != ')') {
                    invertCycles.put(currentLetter, previousLetter);
                } else {
                    for (int j = i; j >= 0; j--) {
                        if (cycle.charAt(j) == '(') {
                            invertCycles.put(cycle.charAt(j + 1),
                                    previousLetter);
                            break;
                        }
                    }
                }
            }
        }
        _permuteCycles = permuteCycles;
        _invertCycles = invertCycles;
    }

    /** Return the value of P modulo the size of this permutation. */
    final int wrap(int p) {
        int r = p % size();
        if (r < 0) {
            r += size();
        }
        return r;
    }

    /** Returns the size of the alphabet I permute. */
    int size() {
        return _alphabet.size();
    }

    /** Return the result of applying this permutation to P modulo the
     *  alphabet size. */
    int permute(int p) {
        p = wrap(p);
        char letter =  _alphabet.toChar(p);
        if (_permuteCycles.containsKey(letter)) {
            return _alphabet.toInt(_permuteCycles.get(letter));
        } else {
            return p;
        }
    }

    /** Return the result of applying the inverse of this permutation
     *  to  C modulo the alphabet size. */
    int invert(int c) {
        char letter =  _alphabet.toChar(c);
        if (_invertCycles.containsKey(letter)) {
            return _alphabet.toInt(_invertCycles.get(letter));
        } else {
            return c;
        }
    }

    /** Return the result of applying this permutation to the index of P
     *  in ALPHABET, and converting the result to a character of ALPHABET. */
    char permute(char p) {
        int permutation = permute(_alphabet.toInt(p));
        return _alphabet.toChar(permutation);
    }

    /** Return the result of applying the inverse of this permutation to C. */
    char invert(char c) {
        int inverted = invert(_alphabet.toInt(c));
        return _alphabet.toChar(inverted);
    }

    /** Return the alphabet used to initialize this Permutation. */
    Alphabet alphabet() {
        return _alphabet;
    }

    /** Return true iff this permutation is a derangement (i.e., a
     *  permutation for which no value maps to itself). */
    boolean derangement() {
        int count = 0;
        for (int i = 0; i < _cycles.length(); i++) {
            if (count == alphabet().size()) {
                return true;
            }
            count++;
        }
        return false;
    }

    /** Alphabet of this permutation. */
    private Alphabet _alphabet;
    /** String cycles of this permution. */
    private String _cycles;
    /** Permute hashmap. */
    private HashMap<Character, Character> _permuteCycles;
    /** Invert hashmap. */
    private HashMap<Character, Character> _invertCycles;
}
