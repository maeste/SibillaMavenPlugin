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
package it.javalinux.sibilla.plugins;

import static org.junit.Assert.assertThat;
import static org.junit.matchers.JUnitMatchers.hasItems;

import it.javalinux.sibilla.plugins.SibillaMojo;
import it.javalinux.sibilla.plugins.util.AbstractSibillaMojoTestCase;
import it.javalinux.sibilla.runner.impl.JunitTestRunner;

import org.junit.Test;

/**
 * A test for checking Sibilla Mojo arguments are correctly understood
 * 
 * @author alessio.soldano@javalinux.it
 * @since 14-Nov-2009
 * 
 */
public class ArgumentTest extends AbstractSibillaMojoTestCase {

    @Test
    public void testExplicitPluginArguments() throws Exception {
	final String pluginConfig = "target/test-classes/test-argument/plugin-config.xml";
	
	SibillaMojo sibillaMojo = getMojo(SibillaMojo.class, "sibilla", pluginConfig);
//	sibillaMojo.execute();
	assertEquals("target-dir", sibillaMojo.getTargetDirectory().getName());
	assertEquals("output-dir", sibillaMojo.getOutputDirectory().getName());
	assertEquals("test-output-dir", sibillaMojo.getTestOutputDirectory().getName());
	assertEquals(true, sibillaMojo.isVerbose());
	assertEquals(100, sibillaMojo.getStaleMillis());
	assertEquals("it.javalinux.sibilla.runner.impl.MyTestRunner", sibillaMojo.getRunnerClass());
	assertThat(sibillaMojo.getSourceRoots(), hasItems(new String[]{"source1", "source2"}));
	assertThat(sibillaMojo.getClasspathElements(), hasItems(new String[]{"cpEl1", "cpEl2"}));
	assertThat(sibillaMojo.getTestSourceRoots(), hasItems(new String[]{"testSource1", "testSource2"}));
	assertThat(sibillaMojo.getTestClasspathElements(), hasItems(new String[]{"testCpEl1", "testCpEl2"}));
	assertThat(sibillaMojo.getIncludes(), hasItems(new String[]{"include1", "include2"}));
	assertThat(sibillaMojo.getExcludes(), hasItems(new String[]{"exclude1", "exclude2"}));
    }
    
    @Test
    public void testDefaultPluginArguments() throws Exception {
	final String pluginConfig = "target/test-classes/test-argument/default-plugin-config.xml";
	
	SibillaMojo sibillaMojo = getMojo(SibillaMojo.class, "sibilla", pluginConfig);
	assertNull(sibillaMojo.getTargetDirectory());
	assertNull(sibillaMojo.getOutputDirectory());
	assertNull(sibillaMojo.getTestOutputDirectory());
	assertEquals(false, sibillaMojo.isVerbose());
	assertEquals(0, sibillaMojo.getStaleMillis());
	assertEquals(JunitTestRunner.class.getName(), sibillaMojo.getRunnerClass());
	assertNull(sibillaMojo.getSourceRoots());
	assertNull(sibillaMojo.getClasspathElements());
	assertNull(sibillaMojo.getTestSourceRoots());
	assertNull(sibillaMojo.getTestClasspathElements());
	assertTrue(sibillaMojo.getIncludes().isEmpty());
	assertTrue(sibillaMojo.getExcludes().isEmpty());
    }
    
}
