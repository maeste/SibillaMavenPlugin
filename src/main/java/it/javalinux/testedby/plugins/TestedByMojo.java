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

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;

/**
 * The TestedBy Maven plugin Mojo
 * 
 * @goal testedby
 * 
 * @phase test
 */
public class TestedByMojo extends AbstractMojo {    

    /**
     * The source directories containing the sources
     *
     * @parameter expression="${project.sourceRoots}"
     * @required
     * @readonly
     */
    private List<String> sourceRoots;

    /**
     * Project classpath.
     *
     * @parameter expression="${project.classpathElements}"
     * @required
     * @readonly
     */
    private List<String> classpathElements;
    
    /**
     * Location of the file.
     * 
     * @parameter expression="${project.build.directory}"
     * @required
     */
    private File outputDirectory;
    
    /**
     * The source directories containing the test-source
     *
     * @parameter expression="${project.testCompileSourceRoots}"
     * @required
     * @readonly
     */
    private List<String> testSourceRoots;

    /**
     * Project test classpath.
     *
     * @parameter expression="${project.testClasspathElements}"
     * @required
     * @readonly
     */
    private List<String> testClasspathElements;

    /**
     * The compiled test classes directory
     *
     * @parameter expression="${project.build.testOutputDirectory}"
     * @required
     * @readonly
     */
    private File testOutputDirectory;

    /**
     * A list of inclusion filters for the compiler.
     *
     * @parameter
     */
    private Set<String> includes = new HashSet<String>();

    /**
     * A list of exclusion filters for the compiler.
     *
     * @parameter
     */
    private Set<String> excludes = new HashSet<String>();


    /**
     * Set to true to for verbose logging
     *
     * @parameter expression="${maven.compiler.verbose}" default-value="false"
     */
    private boolean verbose;

    /**
     * Sets the granularity in milliseconds of the last modification
     * date for testing whether a source needs has changed since last run.
     *
     * @parameter expression="${testedby.lastModGranularityMs}" default-value="0"
     */
    private int staleMillis;
    
    
    
    /**
     * 
     * {@inheritDoc}
     *
     * @see org.apache.maven.plugin.AbstractMojo#execute()
     */
    public void execute() throws MojoExecutionException {
	getLog().info("Trying logs... " + outputDirectory);
	
    }
    
    /**
     * @return sourceRoots
     */
    List<String> getSourceRoots() {
        return sourceRoots;
    }

    /**
     * @return classpathElements
     */
    List<String> getClasspathElements() {
        return classpathElements;
    }

    /**
     * @return outputDirectory
     */
    File getOutputDirectory() {
        return outputDirectory;
    }

    /**
     * @return testSourceRoots
     */
    List<String> getTestSourceRoots() {
        return testSourceRoots;
    }

    /**
     * @return testClasspathElements
     */
    List<String> getTestClasspathElements() {
        return testClasspathElements;
    }

    /**
     * @return testOutputDirectory
     */
    File getTestOutputDirectory() {
        return testOutputDirectory;
    }
    
    /**
     * @return includes
     */
    Set<String> getIncludes() {
        return includes;
    }

    /**
     * @return excludes
     */
    Set<String> getExcludes() {
        return excludes;
    }

    /**
     * @return verbose
     */
    boolean isVerbose() {
        return verbose;
    }

    /**
     * @return staleMillis
     */
    int getStaleMillis() {
        return staleMillis;
    }

}
