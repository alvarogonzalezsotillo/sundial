/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package computation.sideralis;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Hashtable;

/**
 * This class contains a localization support for MIDP applications
 * Feel free to modify.
 *
 * @author  breh
 * @version 1.0
 */
public class LocalizationSupport {

    public static final short NB_LANG = 7;                                      // 8 with ru, 9 with cz
    /**************************************************************************
     ****
     ****   Localization Support Begin
     ****
     **************************************************************************/


    /**
     * Full path to the messages resource bundle. Feel free to change it if you don't
     * use the message bundle file generated by IDE.
     */
    private static final String _MESSAGES_BUNDLE = "/fr/dox/sideralis/languages/messages.properties";

    /**
     * Error message used in the case there is any problem with initialization of
     * localization support. Please note, the error message should contain one
     * parameter sign - '{0}', which is used to fill in a reason of the failure.
     */
    private static final String _INIT_LOCALIZATION_ERROR_MSG = "Error when initializing localization support, reason: {0}";

    /**
     * Default String is returned from getMessage() methods when there is any problem
     * with finding the appropriate localized message or any part of it.
     */
    private static final String _DEFAULT_STRING = "???";

    /**
     * a char table used to accelerate reading of line
     */
    private static char c[] = new char[10];

    /**
     * An offset used to accelerate reading of line
     */
     private static int off = 0;
    /**
     * Initializes localization support based on currently set locale (obtained
     * from "microedition.locale" system property). The initialization method is called
     * automatically when a call to {@link #getMessage(java.lang.String)} method is attempted for the first time.
     *
     *
     * You can call this method explicitly to see whether there was any problem
     * with initialization of the localization support. Method returns a status
     * of the successfulness. If there was any problem with initialization, you can
     * get reason by using {@link #getErrorMessage()} method.
     * @return true if the intialization was succesfull, false if there was any problem.
     */
    public static boolean initLocalizationSupport() {
//        System.out.println("Microedition "+System.getProperty("microedition.locale"));          // *** For DBG only ***
        return initLocalizationSupport(System.getProperty("microedition.locale"));     // NOI18N
    }


    /**
     * Explicit initialization of the localization support. This method is usually
     * called when a particular locale used in the application. E.g. the application
     * contains only french messages (no default messages, only <CODE>messages_fr.properties</CODE>
     * files is available), you should initialize the localization support (by calling
     * <CODE>initLocalizationSupport("fr");</CODE>) before using {@link #getMessage(java.lang.String)} method for the first
     * time.
     *
     * Method returns a status of the successfulness. If there was any problem with
     * the initialization, you can get explanation by using {@link #getErrorMessage()}
     * method.
     * @param locale locale which will be used to determine which message file from bundle will be used
     * @return true if the intialization was succesfull, false if there was any problem.
     */
    public static boolean initLocalizationSupport(String locale) {
        InputStream in = null;
        Class clazz = Object.class;
        try {
            // try to find localized resource first (in format ${name}_locale.${suffix})
            if ((locale != null) && (locale.length() > 1)) {
                int lastIndex = _MESSAGES_BUNDLE.lastIndexOf('.');
                String prefix = _MESSAGES_BUNDLE.substring(0,lastIndex);
                String suffix = _MESSAGES_BUNDLE.substring(lastIndex);
                // replace '-' with '_', some phones returns locales with
                // '-' instead of '_'. For example Nokia or Motorola
                locale = locale.replace('-','_');
                in = clazz.getResourceAsStream(prefix+"_"+locale+suffix);
                if (in == null) {
                    // if no localized resource is found or localization is available
                    // try broader???? locale (i.e. instead og en_US, try just en)
                    in = clazz.getResourceAsStream(prefix+"_"+locale.substring(0,2)+suffix);
                }
            }
            if (in == null) {
                // if not found or locale is not set, try default locale
                in = clazz.getResourceAsStream(_MESSAGES_BUNDLE);
            }
            if (in == null) {
                // no messages bundle was found - initialization failed
                _localizationErrorMessage = _processPattern(_INIT_LOCALIZATION_ERROR_MSG,new Object[] {"No messages found"}); // NOI18N
            } else {
                // load messages to _messageTable hashtable
                _messageTable = new Hashtable();
                _loadMessages(in);
                // we are ok - return true as success ...
                return true;
            }
        } catch (Exception e) {
            // houston we have a problem
            _localizationErrorMessage = _processPattern(_INIT_LOCALIZATION_ERROR_MSG,new Object[] {e.getMessage()});
        }
        return false;
    }

    /**
     * Returns an error message if there was any problem with accessing the localized
     * text. The message also possibly explainins a reason of the failure. The message
     * is taken from <CODE>_INIT_LOCALIZATION_ERROR_MSG</CODE>.
     * @return error message if there was any failure or null when everything is OK.
     */
    public static String getErrorMessage() {
        return _localizationErrorMessage;
    }

    /** Return all supported languages 
     * 
     */
    public static String[] getLanguages() {
        String[] ret;
        boolean langSupported[];
        InputStream in = null;                                                  // need access to a class object - cannot use Object.class, because of MIDP1 bug
        Class clazz = Runtime.getRuntime().getClass();
        int lastIndex = _MESSAGES_BUNDLE.lastIndexOf('.');
        String prefix = _MESSAGES_BUNDLE.substring(0,lastIndex);
        String suffix = _MESSAGES_BUNDLE.substring(lastIndex);
        
        langSupported = new boolean[NB_LANG];
        langSupported[0] = true;                                                // English ok
        
        in = clazz.getResourceAsStream(prefix+"_fr"+suffix);
        if (in != null) {
            langSupported[1] = true;
        }
        in = clazz.getResourceAsStream(prefix+"_it"+suffix);
        if (in != null) {
            langSupported[2] = true;
        }
        in = clazz.getResourceAsStream(prefix+"_es"+suffix);
        if (in != null) {
            langSupported[3] = true;
        }
        in = clazz.getResourceAsStream(prefix+"_pt"+suffix);
        if (in != null) {
            langSupported[4] = true;
        }
        in = clazz.getResourceAsStream(prefix+"_de"+suffix);
        if (in != null) {
            langSupported[5] = true;
        }
        in = clazz.getResourceAsStream(prefix+"_pl"+suffix);
        if (in != null) {
            langSupported[6] = true;
        }

//        in = clazz.getResourceAsStream(prefix+"_ru"+suffix);
//        if (in != null) {
//            langSupported[7] = true;
//        }

//        in = clazz.getResourceAsStream(prefix+"_cs"+suffix);
//        if (in != null) {
//            langSupported[8] = true;
//        }

        ret = new String[NB_LANG];

        ret[0] = "English";
        ret[1] = (langSupported[1] == true?"Français":"English (Français)");
        ret[2] = (langSupported[2] == true?"Italiano":"English (Italiano)");
        ret[3] = (langSupported[3] == true?"Español":"English (Español)");
        ret[4] = (langSupported[4] == true?"Portugués":"English (Portugués)");
        ret[5] = (langSupported[5] == true?"Deutsch":"English (Deutsch)");
        ret[6] = (langSupported[6] == true?"Polski":"English (Polski)");
//        ret[7] = (langSupported[7] == true?"Russian":"English (Russian)");
//        ret[8] = (langSupported[7] == true?"\u010cesky":"English (\u010cesky)");
        
        return ret;        
    }
    /**
     * Finds a localized string in a message bundle.
     * @param key key of the localized string to look for
     * @return the localized string. If key is not found, then  <CODE>_DEFAULT_STRING</CODE> string
     * is returned
     */
    public static final String getMessage(String key) {
        //System.out.println(" -- " + key);
        return getMessage(key,null);
    }

    /**
     * Finds a localized string in a message bundle and formats the message by passing
     * requested parameters.
     * @param key key of the localized string to look for
     * @param args array of arguments to use for formatting the message
     * @return the localized string. If key is not found, then <CODE>_DEFAULT_STRING</CODE> string
     * is returned
     */
    public static final String getMessage(String key, Object[] args) {
        if (_messageTable == null) {
            if (!initLocalizationSupport()) {
                return _DEFAULT_STRING;
            }
        }
        StringBuffer toAppendTo = new StringBuffer();
        String s = (String) _messageTable.get(key);
        if (s == null)
            return _DEFAULT_STRING;
        int l = s.length();
        int n = 0, lidx = -1, lastidx = 0;
        for (int i = 0; i < l; i++) {
            if (s.charAt(i) == '{') {
                n++;
                if (n == 1) {
                    lidx = i;
                    toAppendTo.append(s.substring(lastidx, i));
                    lastidx = i;
                }
            }
            if (s.charAt(i) == '}') {
                if (n == 1) {
                    toAppendTo.append(_processPattern(s.substring(lidx + 1, i), args));
                    lidx = -1;
                    lastidx = i + 1;
                }
                n--;
            }
        }
        if (n > 0) {
            toAppendTo.append(_processPattern(s.substring(lidx + 1),args));
        }
        else {
            toAppendTo.append(s.substring(lastidx));
        }

        return toAppendTo.toString();
    }

    /* The rest is private to localization support. You shouldn't change anything
     * below this comment unless you really know what you are doing
     * Ideally, everyhthing below this should be collapsed.
     */

    /**
     * Characters separating keys and values
     */
    private static final String _KEY_VALUE_SEPARATORS = "=: \t\r\n\f";
    /**
     * Characters strictly separating keys and values
     */
    private static final String _STRICT_KEY_VALUE_SEPARTORS = "=:";
    /**
     * white space characters understood by the support (these can be in the message file)
     */
    private static final String _WHITESPACE_CHARS = " \t\r\n\f";


    /**
     * Contains the parsed message bundle.
     */
    private static Hashtable _messageTable;
    /**
     * Contains an error message if there was any problem with localization support.
     * If everything is OK, this field is null.
     */
    private static String _localizationErrorMessage = null;



    /**
     * Loads messages from input stream to hash table.
     * @param inStream stream from which the messages are read
     * @throws IOException if there is any problem with reading the messages
     */
    private static synchronized void _loadMessages(InputStream inStream) throws IOException {

        InputStreamReader in = new InputStreamReader(inStream);

        while (true) {
            // Get next line
            String line = _readLine(in);
            if (line == null)
                return;

            if (line.length() > 0) {

                // Find start of key
                int len = line.length();
                int keyStart;
                for (keyStart = 0; keyStart < len; keyStart++)
                    if (_WHITESPACE_CHARS.indexOf(line.charAt(keyStart)) == -1)
                        break;

                // Blank lines are ignored
                if (keyStart == len)
                    continue;

                // Continue lines that end in slashes if they are not comments
                char firstChar = line.charAt(keyStart);
                if ((firstChar != '#') && (firstChar != '!')) {
                    while (_continueLine(line)) {
                        String nextLine = _readLine(in);
                        if (nextLine == null)
                            nextLine = "";
                        String loppedLine = line.substring(0, len - 1);
                        // Advance beyond whitespace on new line
                        int startIndex;
                        for (startIndex = 0; startIndex < nextLine.length(); startIndex++)
                            if (_WHITESPACE_CHARS.indexOf(nextLine.charAt(startIndex)) == -1)
                                break;
                        nextLine = nextLine.substring(startIndex, nextLine.length());
                        line = new String(loppedLine + nextLine);
                        len = line.length();
                    }

                    // Find separation between key and value
                    int separatorIndex;
                    for (separatorIndex = keyStart; separatorIndex < len; separatorIndex++) {
                        char currentChar = line.charAt(separatorIndex);
                        if (currentChar == '\\')
                            separatorIndex++;
                        else if (_KEY_VALUE_SEPARATORS.indexOf(currentChar) != -1)
                            break;
                    }

                    // Skip over whitespace after key if any
                    int valueIndex;
                    for (valueIndex = separatorIndex; valueIndex < len; valueIndex++)
                        if (_WHITESPACE_CHARS.indexOf(line.charAt(valueIndex)) == -1)
                            break;

                    // Skip over one non whitespace key value separators if any
                    if (valueIndex < len)
                        if (_STRICT_KEY_VALUE_SEPARTORS.indexOf(line.charAt(valueIndex)) != -1)
                            valueIndex++;

                    // Skip over white space after other separators if any
                    while (valueIndex < len) {
                        if (_WHITESPACE_CHARS.indexOf(line.charAt(valueIndex)) == -1)
                            break;
                        valueIndex++;
                    }
                    String key = line.substring(keyStart, separatorIndex);
                    String value = (separatorIndex < len) ? line.substring(valueIndex, len) : "";

                    // Convert then store key and value
                    key = _convertString(key);
                    value = _convertString(value);
                    _messageTable.put(key, value);
                }
            }
        }

    }


    /**
     * reads a single line from InputStreamReader
     * @param in InputStreamReader used to read the line
     * @throws IOException if there is any problem with reading
     * @return the read line
     */
    private static String _readLine(InputStreamReader in) throws IOException {
        StringBuffer strBuf = new StringBuffer(20);
        int l,i;
        boolean ret = false;

        l = off;
        while (ret == false && l!=-1) {
            if (off == 0)
                l = in.read(c);
            if (l != -1) {
                for (i=0;i<l;i++) {
                    if (c[i] == '\r' || c[i] == '\n') {
                        ret = true;
                        off = i;
                        break;
                    }
                }
                if (ret == false) {
                    strBuf.append(c,0,l);
                    off = 0;
                } else {
                    strBuf.append(c,0,off);
                    for (i=off+1;i<l;i++)
                        c[i-off-1] = c[i];
                    off = l-off-1;
                }
            } 
        }
        if (l != -1)
            return strBuf.toString();
        else
            return strBuf.length() > 0 ? strBuf.toString() : null;
    }
    /**
     * determines whether the line of the supplied string continues on the next line
     * @param line a line of String
     * @return true if the string contines on the next line, false otherwise
     */
    private static boolean _continueLine(String line) {
        int slashCount = 0;
        int index = line.length() - 1;
        while ((index >= 0) && (line.charAt(index--) == '\\'))
            slashCount++;
        return (slashCount % 2 == 1);
    }


    /**
     * Decodes a String which uses unicode characters in \\uXXXX format.
     * @param theString String with \\uXXXX characters
     * @return resolved string
     */
    private static String _convertString(String theString) {
        char aChar;
        int len = theString.length();
        StringBuffer outBuffer = new StringBuffer(len);

        for (int x = 0; x < len;) {
            aChar = theString.charAt(x++);
            if (aChar == '\\') {
                aChar = theString.charAt(x++);
                if (aChar == 'u') {
                    // Read the xxxx
                    int value = 0;
                    for (int i = 0; i < 4; i++) {
                        aChar = theString.charAt(x++);
                        switch (aChar) {
                            case '0' :
                            case '1' :
                            case '2' :
                            case '3' :
                            case '4' :
                            case '5' :
                            case '6' :
                            case '7' :
                            case '8' :
                            case '9' :
                                value = (value << 4) + aChar - '0';
                                break;
                            case 'a' :
                            case 'b' :
                            case 'c' :
                            case 'd' :
                            case 'e' :
                            case 'f' :
                                value = (value << 4) + 10 + aChar - 'a';
                                break;
                            case 'A' :
                            case 'B' :
                            case 'C' :
                            case 'D' :
                            case 'E' :
                            case 'F' :
                                value = (value << 4) + 10 + aChar - 'A';
                                break;
                            default :
                                // return DEFAULT STRING if there is any problem
                                return _DEFAULT_STRING;
                        }
                    }
                    outBuffer.append((char) value);
                }
                else {
                    if (aChar == 't')
                        aChar = '\t';
                    else if (aChar == 'r')
                        aChar = '\r';
                    else if (aChar == 'n')
                        aChar = '\n';
                    else if (aChar == 'f')
                        aChar = '\f';
                    outBuffer.append(aChar);
                }
            }
            else {
                outBuffer.append(aChar);
            }
        }
        return outBuffer.toString();
    }

    /**
     * Extracts N-th from an array of argumens.
     * @param indexString a String number
     * @param args array of arguments
     * @return the indexString-th parameter from the array
     */
    private static String _processPattern(String indexString , Object[] args) {
        try {
            int index = Integer.parseInt(indexString);
            if ((args != null) && (index >= 0) && (index < args.length)) {
                if (args[index] != null) {
                    return args[index].toString();
                }
            }
        } catch (NumberFormatException nfe) {
            // NFE - nothing bad basically - the argument is not a number
            // swallow it for the time being and show default string
        }
        return _DEFAULT_STRING;
    }

    /**************************************************************************
     ****
     ****   Localization Support End
     ****
     **************************************************************************/
}