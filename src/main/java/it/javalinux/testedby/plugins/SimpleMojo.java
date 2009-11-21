/*
 * Stefano Maestri, javalinuxlabs.org Copyright 2008, and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors. 
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package it.javalinux.testedby.plugins;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Sample goal that touches a timestamp file.
 * 
 * @goal simple
 * 
 * @phase process-sources
 */
public class SimpleMojo extends AbstractMojo {
    /**
     * Location of the file.
     * 
     * @parameter expression="${project.build.directory}"
     * @required
     */
    private File outputDirectory;

    public void execute() throws MojoExecutionException {
	getLog().info("Trying logs... " + outputDirectory);

	File f = outputDirectory;

	if (!f.exists()) {
	    f.mkdirs();
	}

	File touch = new File(f, "touch.txt");

	FileWriter w = null;
	try {
	    w = new FileWriter(touch);

	    w.write("touch.txt");
	} catch (IOException e) {
	    throw new MojoExecutionException("Error creating file " + touch, e);
	} finally {
	    if (w != null) {
		try {
		    w.close();
		} catch (IOException e) {
		    // ignore
		}
	    }
	}
    }
}
