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

import org.apache.maven.plugin.CompilerMojo;
import org.junit.Test;

/**
 * A basic test
 * 
 * @author alessio.soldano@javalinux.it
 * 
 */
public class SimpleTest extends AbstractTestedByMojoTestCase {

    @Test
    public void testPluginCanBeRun() throws Exception {
	CompilerMojo compilerMojo = getMojo(CompilerMojo.class, "org.apache.maven.plugins", "maven-compiler-plugin", "2.0.2", "compile", "target/test-classes/test-simple/plugin-config.xml");
	compilerMojo.execute();
	TestedByMojo testedByMojo = getMojo(TestedByMojo.class, "testedby", "target/test-classes/test-simple/plugin-config.xml");
	testedByMojo.execute();
	assertTrue(new File("target/test/test-simple/target/touch.txt").exists());
    }

}
