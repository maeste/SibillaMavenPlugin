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

import static org.junit.Assert.assertThat;
import static org.junit.matchers.JUnitMatchers.hasItems;

import it.javalinux.testedby.plugins.util.AbstractTestedByMojoTestCase;
import it.javalinux.testedby.runner.impl.JunitTestRunner;

import org.junit.Test;

/**
 * A test for checking TestedBy Mojo arguments are correctly understood
 * 
 * @author alessio.soldano@javalinux.it
 * @since 14-Nov-2009
 * 
 */
public class ArgumentTest extends AbstractTestedByMojoTestCase {

    @Test
    public void testExplicitPluginArguments() throws Exception {
	final String pluginConfig = "target/test-classes/test-argument/plugin-config.xml";
	
	TestedByMojo testedByMojo = getMojo(TestedByMojo.class, "testedby", pluginConfig);
//	testedByMojo.execute();
	assertEquals("target-dir", testedByMojo.getTargetDirectory().getName());
	assertEquals("output-dir", testedByMojo.getOutputDirectory().getName());
	assertEquals("test-output-dir", testedByMojo.getTestOutputDirectory().getName());
	assertEquals(true, testedByMojo.isVerbose());
	assertEquals(100, testedByMojo.getStaleMillis());
	assertEquals("it.javalinux.testedby.runner.impl.MyTestRunner", testedByMojo.getRunnerClass());
	assertThat(testedByMojo.getSourceRoots(), hasItems(new String[]{"source1", "source2"}));
	assertThat(testedByMojo.getClasspathElements(), hasItems(new String[]{"cpEl1", "cpEl2"}));
	assertThat(testedByMojo.getTestSourceRoots(), hasItems(new String[]{"testSource1", "testSource2"}));
	assertThat(testedByMojo.getTestClasspathElements(), hasItems(new String[]{"testCpEl1", "testCpEl2"}));
	assertThat(testedByMojo.getIncludes(), hasItems(new String[]{"include1", "include2"}));
	assertThat(testedByMojo.getExcludes(), hasItems(new String[]{"exclude1", "exclude2"}));
    }
    
    @Test
    public void testDefaultPluginArguments() throws Exception {
	final String pluginConfig = "target/test-classes/test-argument/default-plugin-config.xml";
	
	TestedByMojo testedByMojo = getMojo(TestedByMojo.class, "testedby", pluginConfig);
	assertNull(testedByMojo.getTargetDirectory());
	assertNull(testedByMojo.getOutputDirectory());
	assertNull(testedByMojo.getTestOutputDirectory());
	assertEquals(false, testedByMojo.isVerbose());
	assertEquals(0, testedByMojo.getStaleMillis());
	assertEquals(JunitTestRunner.class.getName(), testedByMojo.getRunnerClass());
	assertNull(testedByMojo.getSourceRoots());
	assertNull(testedByMojo.getClasspathElements());
	assertNull(testedByMojo.getTestSourceRoots());
	assertNull(testedByMojo.getTestClasspathElements());
	assertTrue(testedByMojo.getIncludes().isEmpty());
	assertTrue(testedByMojo.getExcludes().isEmpty());
    }
    
}
