/*******************************************************************************
 * PMM-Lab © 2012, Federal Institute for Risk Assessment (BfR), Germany
 * 
 * PMM-Lab is a set of KNIME-Nodes and KNIME workflows running within the KNIME software plattform (http://www.knime.org.).
 * 
 * PMM-Lab © 2012, Federal Institute for Risk Assessment (BfR), Germany
 * Contact: armin.weiser@bfr.bund.de or matthias.filter@bfr.bund.de 
 * 
 * Developers and contributors to the PMM-Lab project are 
 * Jörgen Brandt (BfR)
 * Armin A. Weiser (BfR)
 * Matthias Filter (BfR)
 * Alexander Falenski (BfR)
 * Christian Thöns (BfR)
 * Annemarie Käsbohrer (BfR)
 * Bernd Appel (BfR)
 * 
 * PMM-Lab is a project under development. Contributions are welcome.
 * 
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
/**
 * 
 */
package org.hsh.bfr.db;

/**
 * @author Weiser
 *
 */



/**
 * <p>Title: Turniere</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: HSH</p>
 * @author Armin Weiser
 * @version 1.0
 */

public class InexactStringMatcher {

  final static int DEFAULT_RESULT = 10000000;
  static boolean caseSensitive = false;

  public InexactStringMatcher() {
  }

  /**
   * See how closely two strings match each other
   * by checking for proximity of similar characters.
   * the lower the return value, the closer the match
   */
  public static int getMatchScore(String str1, String str2) {
    int tmp, result;
    String strA, strB;
    //System.out.println(str1 + "\t"+str2);

// Vergleiche den kleineren mit dem größerem String
    if (str1.length() < str2.length()) {strA = str1; strB = str2;}
    else {strA = str2; strB = str1;}
    if (strA.length() == 0) return DEFAULT_RESULT;

    if (!caseSensitive) {
      strA = strA.toLowerCase();
      strB = strB.toLowerCase();
    }

    result = getOneWayMatchScore(strA, strB);

    return result;
  }
  private static int getOneWayMatchScore(String str1, String str2) {
    int totalScore = 0;
    int str1Len = str1.length();
    for (int i = 0; i < str1Len; i++) {
      char c = str1.charAt(i);
      int proximity = findCharProximity(str2, i, c);
      totalScore += proximity;
    }
    return totalScore;
  }

  private static int findCharProximity(String str,
                                int position,
                                char c) {
    int strlen = str.length();
    int closestCharDist = -1;
    boolean doRightSearch = true;
    int startPosition = position;

    // may need to fix start position if it's past end
    // of string!
    if (position >= str.length()) {
      doRightSearch = false;
      startPosition = str.length() - 1;
    }

    // work left along the string from the given position,
    // looking for matching char
    for (int i = startPosition; i >= 0; i--) {
      if (str.charAt(i) == c) {
        int thisCharDist = position - i;
        if (thisCharDist < closestCharDist
            || closestCharDist == -1) {
          closestCharDist = thisCharDist;
          break;
        }
      }
    }

    if (doRightSearch == true) {
      // work right along the string from the give position,
      // looking for matching char
      for (int i = position; i < strlen; i++) {
        if (str.charAt(i) == c) {
          int thisCharDist = i - position;
          if (thisCharDist < closestCharDist
              || closestCharDist == -1) {
            closestCharDist = thisCharDist;
            break;
          }
        }
      }
    }
    if (closestCharDist < 0 || closestCharDist > 5) closestCharDist = 5;
    return closestCharDist;
  }

}

/** 
 * InexactStringMatcher class sketch. 
 * Use freely, enjoy kittens 
 * 
 * @author Alex Hunsley 

public class InexactStringMatcher 
{ 
    public int getMatchScore(String str1, String str2, String spaceChars) { 
       str1 = removeChars(str1, spaceChars).toLowerCase(); 
       str2 = removeChars(str2, spaceChars).toLowerCase(); 
       return getMatchScore(str1, str2); 
    } 

    public String removeChars(String str, String charsToRemove) { 
       for (int charIndex = 0; charIndex < charsToRemove.length(); charIndex ++) { 
           char charToRemove = charsToRemove.charAt(charIndex); 
           StringBuffer buf = new StringBuffer(); 
        
           int strIndex = 0; 
           int charOccurencePos = -1; 
           do { 
               charOccurencePos = str.indexOf(charToRemove); 
               if (charOccurencePos >= 0) { 
                   buf.append(str.substring(0, charOccurencePos)); 
                   str = str.substring(charOccurencePos + 1, 
                                       str.length()); 
               } 
           } while (charOccurencePos >= 0); 
        
           // now put remainder of string in buf 
           buf.append(str); 
           // set str to be our newly made string with possibly 
           // chars removed 
           str = buf.toString(); 
       } 
       return str; 
    } 

    public int getMatchScore(String str1, String str2) { 
       int scoreA = getOneWayMatchScore(str1, str2); 
       int scoreB = getOneWayMatchScore(str2, str1); 
//System.out.println("___scoreA="+scoreA 
//+ " scoreB="+scoreB); 
       // average the two scores 
       // (could always take the minimum or maximum instead, 
       // the average seem to work) 
       return (int) ((scoreA + scoreB) / 2); 
    } 
    public int getOneWayMatchScore(String str1, String str2) { 
       int totalScore = 0; 
       int str1Len = str1.length(); 
       int str2Len = str2.length(); 
       for (int i = 0; i < str1Len; i++) { 
           char c = str1.charAt(i); 
           int proximity = findCharProximity(str2, i, c); 
           totalScore += proximity; 
       } 
       return totalScore; 
    } 
    private int findCharProximity(String str, int position, char c) { 
       int strlen = str.length(); 
       int closestCharDist = -1; 
       boolean doRightSearch = true; 
       int startPosition = position; 
       // may need to fix start position if it's past end 
       // of string! 
       if (position >= str.length()) { 
           doRightSearch = false; 
           startPosition = str.length() - 1; 
       } 
        
       // work left along the string from the given position, 
       // looking for matching char 
       for (int i = startPosition; i >= 0; i--) { 
           if (str.charAt(i) == c) { 
               int thisCharDist = position - i; 
               if (thisCharDist < closestCharDist 
                   || closestCharDist == -1) { 
                   closestCharDist = thisCharDist; 
                   break; 
               } 
           } 
       } 
       if (doRightSearch == true) { 
           // work right along the string from the give position, 
           // looking for matching char 
           for (int i = position; i < strlen; i++) { 
               if (str.charAt(i) == c) { 
                   int thisCharDist = i - position; 
                   if (thisCharDist < closestCharDist 
                       || closestCharDist == -1) { 
                       closestCharDist = thisCharDist; 
                       break; 
                   } 
               } 
           } 
       } 
       return closestCharDist; 
    } 
    public static void main(String[] args) { 
       InexactStringMatcher matcher = new InexactStringMatcher(); 
       System.out.println("score from closely matched items is " +matcher.getMatchScore("msedcationoflarynhill", "the_miseducation_of_lauryn_hill", "_ ")); 
       System.out.println("score from not so matched item is " +matcher.getMatchScore("msedcationoflarynhill", "another_track_name_entirely", "_ ")); 
    } 
} 
*/ 
