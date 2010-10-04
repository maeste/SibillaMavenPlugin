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
package it.javalinux.sibilla.plugins.testHelpers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import it.javalinux.sibilla.metadata.ClassLinkMetadata;
import it.javalinux.sibilla.metadata.TestsMetadata;
import it.javalinux.sibilla.metadata.serializer.impl.SimpleMetadataSerializer;
import it.javalinux.sibilla.plugins.scanner.RunsRepository;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * A helper class for test1 setup/verify scripts.
 * 
 * @author alessio.soldano@javalinux.it
 * @since 13-Dec-2009
 *
 */
public class Test1Helper implements VerifyScriptHelper, SetupScriptHelper {
    
    private static final Logger log = Logger.getLogger(Test1Helper.class.getName());
    private static final String START_TIME = "StartTime";

    /**
     * {@inheritDoc}
     *
     * @see it.javalinux.sibilla.plugins.testHelpers.SetupScriptHelper#setup(java.io.File, java.io.File, java.util.Map)
     */
    @SuppressWarnings("unchecked")
    public void setup(File basedir, File localRepositoryPath, Map context) throws Exception {
	Long startTime = System.currentTimeMillis();
	log.info("Setting start time: " + startTime);
	context.put(START_TIME, startTime);
    }
    
    /**
     * {@inheritDoc}
     *
     * @see it.javalinux.sibilla.plugins.testHelpers.VerifyScriptHelper#verify(java.io.File, java.io.File, java.util.Map)
     */
    public boolean verify(File basedir, File localRepositoryPath, Map<?, ?> context) throws Exception {
	// make sure the Sibilla plugin was indeed run and the build didn't fail somewhere else
	File sibillaMetadata = new File(basedir, "target" + File.separator + "sibilla-metadata");
	log.info("Checking for existence of sibilla metadata: " + sibillaMetadata);
	if (!sibillaMetadata.exists()) {
	    log.severe("FAILED!");
	    return false;
	}
	File runsRepository = new File(basedir, "target" + File.separator + "sibilla-runs-repository.bin");
	log.info("Checking for existence of runs repository: " + runsRepository);
	if (!runsRepository.exists()) {
	    log.severe("FAILED!");
	    return false;
	}
	
	//verify metadata contents
	SimpleMetadataSerializer deserializer = new SimpleMetadataSerializer();
	TestsMetadata metadata = deserializer.deserialize(sibillaMetadata.getCanonicalPath());
	List<ClassLinkMetadata> testClasses = metadata.getAllTestClasses();
	List<ClassLinkMetadata> testedClasses = metadata.getAllTestedClasses();
	assertEquals(1, testClasses.size());
	assertEquals(1, testedClasses.size());
	assertEquals("it.javalinux.sibilla.SampleTest", testClasses.iterator().next().getClazz());
	assertEquals("it.javalinux.sibilla.Foo", testedClasses.iterator().next().getClazz());
	
	// verify runs repository contents
	RunsRepository repo = new RunsRepository(new File(basedir, "target"));
	repo.load();
	String sampleTestPath = basedir.getCanonicalPath() + "/src/test/java/it/javalinux/sibilla/SampleTest.java";
	Long sampleTestRunTime = repo.getLastRunTimeMillis(sampleTestPath);
	String fooPath = basedir.getCanonicalPath() + "/src/main/java/it/javalinux/sibilla/Foo.java";
	Long fooRunTime = repo.getLastRunTimeMillis(fooPath);
	log.info(sampleTestPath + ": " + sampleTestRunTime);
	log.info(fooPath + ": " + fooRunTime);
	assertNotNull(sampleTestRunTime);
	assertNotNull(fooRunTime);
	Long startTime = (Long)context.get(START_TIME);
	log.info("startTime: " + startTime);
	assertTrue(sampleTestRunTime > startTime);
	assertTrue(fooRunTime > startTime);
	return true;
    }

}
