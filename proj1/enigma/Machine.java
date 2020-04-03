package enigma;

import java.util.Collection;


/** Class that represents a complete enigma machine.
 *  @author Anagha Mandayam
 */
class Machine {

    /** A new Enigma machine with alphabet ALPHA, 1 < NUMROTORS rotor slots,
     *  and 0 <= PAWLS < NUMROTORS pawls.  ALLROTORS contains all the
     *  available rotors. */
    Machine(Alphabet alpha, int numRotors, int pawls,
            Collection<Rotor> allRotors) {
        _alphabet = alpha;
        _numRotors = numRotors;
        _numPawls = numPawls();
        _allRotors = allRotors;
        _rotorArr = new Rotor [numRotors];
    }

    /** Return the number of rotor slots I have. */
    int numRotors() {
        return _numRotors;
    }

    /** Return the number pawls (and thus rotating rotors) I have. */
    int numPawls() {
        return _numPawls;
    }

    /** Set my rotor slots to the rotors named ROTORS from my set of
     *  available rotors (ROTORS[0] names the reflector).
     *  Initially, all rotors are set at their 0 setting. */
    void insertRotors(String[] rotors) {
        for (int i = 0; i < rotors.length; i++) {
            for (Rotor r : _allRotors) {
                if (rotors[i].toUpperCase().equals(r.name())) {
                    if (_rotorArr.toString().contains(r.toString())) {
                        throw new EnigmaException("same rotor again");
                    } else {
                        _rotorArr[i] = r;
                    }
                }
            }
        }
        if (_rotorArr.length != rotors.length) {
            throw new EnigmaException("wrong number of rotors");
        }
    }

    /** Set my rotors according to SETTING, which must be a string of
     *  numRotors()-1 characters in my alphabet. The first letter refers
     *  to the leftmost rotor setting (not counting the reflector).  */
    void setRotors(String setting) {
        if (setting.length() != numRotors() - 1) {
            throw new EnigmaException("settings not right length");
        }
        if (!_rotorArr[0].reflecting()) {
            throw new EnigmaException("first rotor isn't a reflector");
        }
        for (int i = 1; i < numRotors(); i++) {
            if (_alphabet.contains((setting.charAt(i - 1)))) {
                _rotorArr[i].set(_alphabet.toInt(setting.charAt(i - 1)));
            } else {
                throw new EnigmaException("first part of settings wrong");
            }
        }
    }

    /** Set the plugboard to PLUGBOARD. */
    void setPlugboard(Permutation plugboard) {
        _plugboard = plugboard;
    }

    /** Returns the result of converting the input character C (as an
     *  index in the range 0..alphabet size - 1), after first advancing
     *  the machine. */
    int convert(int c) {
        for (int i = 0; i < _rotorArr.length; i++) {
            if (_rotorArr[i] == _rotorArr[0]) {
                continue;
            }
            if (i != _rotorArr.length - 1) {
                if (_rotorArr[i].atNotch() || _rotorArr[i + 1].atNotch()) {
                    _rotorArr[i].advance();
                }
            } else {
                _rotorArr[_rotorArr.length - 1].advance();
            }
        }

        int converted = _plugboard.permute(c);
        for (int i = _numRotors - 1; i >= 0; i--) {
            converted = _rotorArr[i].convertForward(converted);
        }
        for (int i = 1; i < _numRotors; i++) {
            converted = _rotorArr[i].convertBackward(converted);
        }
        return _plugboard.permute(converted);
    }

    /** Returns the encoding/decoding of MSG, updating the state of
     *  the rotors accordingly. */
    String convert(String msg) {
        String finalMsg  = "";
        for (int i = 0; i < msg.length(); i++) {
            char curr = msg.charAt(i);
            int currMsg = _alphabet.toInt(curr);
            char convertedMsg = _alphabet.toChar(convert(currMsg));
            finalMsg += convertedMsg;
        }
        return finalMsg;
    }

    /** Common alphabet of my rotors. */
    private final Alphabet _alphabet;
    /** Number of rotors. */
    private int _numRotors;
    /** Number of pawls. */
    private int _numPawls;
    /** The actual plugboard. */
    private Permutation _plugboard;
    /** All the possible rotors. */
    private Collection<Rotor> _allRotors;
    /** An array of rotors. */
    private Rotor[] _rotorArr;
    /** Checking if at third notch. */
    private boolean at3Notch = false;
    /** Checking if at fourth notch. */
    private boolean at4Notch = false;

}
