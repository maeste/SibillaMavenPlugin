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

import org.apache.maven.plugin.testing.AbstractMojoTestCase;
import org.junit.Test;

/**
 * A basic test
 * 
 * @author alessio.soldano@javalinux.it
 * 
 */
public class SimpleTest extends AbstractMojoTestCase {
    
    @Test
    public void testPluginCanBeRun() throws Exception {
	TestedByMojo mojo = getTestedByMojo("target/test-classes/test-simple/plugin-config.xml");
	mojo.execute();
	assertTrue(new File("target/test/test-simple/target/touch.txt").exists());
    }
    
    private TestedByMojo getTestedByMojo(String pomXml) throws Exception {
	File testPom = new File(getBasedir(), pomXml);

	TestedByMojo mojo = (TestedByMojo) lookupMojo("testedby", testPom);

	assertNotNull(mojo);

	return mojo;
    }

}
