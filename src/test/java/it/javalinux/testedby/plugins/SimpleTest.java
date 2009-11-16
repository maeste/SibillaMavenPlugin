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

import it.javalinux.testedby.plugins.util.AbstractTestedByMojoTestCase;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.apache.maven.plugin.CompilerMojo;
import org.apache.maven.plugin.TestCompilerMojo;
import org.apache.maven.plugin.testing.stubs.ArtifactStub;
import org.junit.Test;

/**
 * A basic test
 * 
 * @author alessio.soldano@javalinux.it
 * @since 14-Nov-2009
 * 
 */
public class SimpleTest extends AbstractTestedByMojoTestCase {

    @Test
    public void testPluginCanBeRun() throws Exception {
	final String pluginConfig = "target/test-classes/test-simple/plugin-config.xml";
	
	CompilerMojo compilerMojo = getMojo(CompilerMojo.class, "org.apache.maven.plugins", "maven-compiler-plugin", COMPILER_PLUGIN_VERSION, "compile", pluginConfig);
	configureCompilerMojo(compilerMojo);
	compilerMojo.execute();
	
	TestCompilerMojo testMojo = getMojo(TestCompilerMojo.class, "org.apache.maven.plugins", "maven-compiler-plugin", COMPILER_PLUGIN_VERSION, "testCompile", pluginConfig);
	configureTestCompilerMojo(testMojo, compilerMojo);
	testMojo.execute();
	
	TestedByMojo testedByMojo = getMojo(TestedByMojo.class, "testedby", pluginConfig);
	testedByMojo.execute();
	assertTrue(new File("target/test/test-simple/target/touch.txt").exists());
    }
    
    private void configureCompilerMojo(CompilerMojo mojo) throws Exception {
	setVariableValueToObject( mojo, "log", new DebugEnabledLog() );
        setVariableValueToObject( mojo, "projectArtifact", new ArtifactStub() );
        setVariableValueToObject( mojo, "classpathElements", Collections.EMPTY_LIST );
    }
    
    @SuppressWarnings("unchecked")
    private void configureTestCompilerMojo(TestCompilerMojo testMojo, CompilerMojo compilerMojo) throws Exception {
	setVariableValueToObject(testMojo, "log", new DebugEnabledLog());

	File buildDir = (File) getVariableValueFromObject(compilerMojo, "buildDirectory");
	File testClassesDir = new File(buildDir, "test-classes");
	setVariableValueToObject(testMojo, "outputDirectory", testClassesDir);

	List<String> testClasspathList = new ArrayList<String>();
	testClasspathList.add(System.getProperty("localRepository") + "/junit/junit/" + JUNIT_VERSION + "/junit-" + JUNIT_VERSION + ".jar");
	testClasspathList.add(((File) getVariableValueFromObject(compilerMojo, "outputDirectory")).getPath());
	setVariableValueToObject(testMojo, "classpathElements", testClasspathList);

	List<String> compileSourceRoots = (List<String>) getVariableValueFromObject(compilerMojo, "compileSourceRoots");
	List<String> testSourceRoots = new LinkedList<String>();
	for (String compileSourceRoot : compileSourceRoots) {
	    int pos = compileSourceRoot.lastIndexOf("/main/java");
	    String testSourceRoot = compileSourceRoot.substring(0, pos) + "/test/java";
	    if (compileSourceRoot.length() > pos + 10) {
		testSourceRoot = testSourceRoot + compileSourceRoot.substring(pos + 10);
	    }
	    testSourceRoots.add(testSourceRoot);
	}
	setVariableValueToObject(testMojo, "compileSourceRoots", testSourceRoots);
    }

}
