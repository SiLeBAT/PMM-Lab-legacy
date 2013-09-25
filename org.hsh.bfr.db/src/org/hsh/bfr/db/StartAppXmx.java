/*******************************************************************************
 * PMM-Lab © 2012, Federal Institute for Risk Assessment (BfR), Germany
 * 
 * PMM-Lab is a set of KNIME-Nodes and KNIME workflows running within the KNIME software plattform (http://www.knime.org.).
 * 
 * PMM-Lab © 2012, Federal Institute for Risk Assessment (BfR), Germany
 * Contact: armin.weiser@bfr.bund.de or matthias.filter@bfr.bund.de 
 * 
 * Developers and contributors to the PMM-Lab project are 
 * Joergen Brandt (BfR)
 * Armin A. Weiser (BfR)
 * Matthias Filter (BfR)
 * Alexander Falenski (BfR)
 * Christian Thoens (BfR)
 * Annemarie Kaesbohrer (BfR)
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

import java.io.InputStream;
import java.io.InputStreamReader;

/* Image to ZX Spec
* Copyright (C) 2010 Silent Software (Benjamin Brown)
* All rights reserved.
*
* Redistribution and use in source and binary forms, with or 
* without modification, are permitted provided that the following 
* conditions are met:
* - Redistributions of source code must retain the above 
* copyright notice, this list of conditions and the following disclaimer.
* 
* - Redistributions in binary form must reproduce the above copyright 
* notice, this list of conditions and the following disclaimer in the 
* documentation and/or other materials provided with the distribution.
* 
* - Neither the name of Silent Software, Silent Development, Benjamin
* Brown nor the names of its contributors may be used to endorse or promote 
* products derived from this software without specific prior written 
* permission.
*
* THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS 
* "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED 
* TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A 
* PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT 
* HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, 
* SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED 
* TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR 
* PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF 
* LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING 
* NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS 
* SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/

/**
* Solution/workaround to allow executable jars
* to change their memory settings as Java cannot 
* have its command line properties defined in a 
* Jar file. Why Sun never implemented anything like 
* this is beyond me.
*/
public class StartAppXmx {

  /**
    * Bare minimum heap memory for starting app and allowing
    * for reasonable sized images (note deliberate 1 MB
    * smaller than 512 due to rounding/non accurate free 
    * heap calculation). The solution allows JVMs with
    * enough heap already to just start without spawning 
    * a new process. 
    */
    private final static int MIN_HEAP = 255; 
    private final static int RECOMMENDED_HEAP = 256;
    
    public static void main(String[] args) throws Exception {

    // Do we have enough memory already (some VMs and later Java 6 
    // revisions have bigger default heaps based on total machine memory)?
    float heapSizeMegs = (Runtime.getRuntime().maxMemory()/1024)/1024;

    // Yes so start
    if (heapSizeMegs > MIN_HEAP) {
      StartApp.main(args);

    // No so set a large heap. Tut - I did use -server mode here originally
    // which does something similar for heap (i.e. can choose a machine specific
    // maximum) but this has some problems with Java 6 R19 for some reason 
    // on my single core machine but worked on Java 6 R13 :( so for now I'll 
    // use a naughty non standard -XX:+AggressiveHeap option.
    // NOTE I DO NOT RECOMMEND YOU DO THIS FOR PRODUCTION CODE use -Xmx1024m 
    // instead (or whatever memory you need) as another constant and use this
    // in place of "-XX:AggressiveHeap" below. E.g. 
    // "private final static int RECOMMENDED_HEAP = 1024;"
    // and
    // ...new ProcessBuilder("java","-Xmx"+RECOMMENDED_HEAP+"m"... 
    }
    else {
      String pathToJar = StartApp.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
      //ProcessBuilder pb = new ProcessBuilder("java","-XX:+AggressiveHeap", "-classpath", pathToJar, "org.hsh.bfr.db.StartApp");
      ProcessBuilder pb = new ProcessBuilder("java","-Xmx" + RECOMMENDED_HEAP+"m", "-classpath", pathToJar, "org.hsh.bfr.db.StartApp");
      System.out.println(pathToJar.substring(1));
      Process p = pb.start();
      InputStream es = p.getErrorStream();
      InputStreamReader ir = new InputStreamReader(es);
       
      char[] buf = new char[1024];
      int i=0;
      StringBuilder builder = new StringBuilder();
      while((i=ir.read(buf))!=-1){
         builder.append(buf,0,i);
      }
      System.out.println(builder);
    }
  }
}
