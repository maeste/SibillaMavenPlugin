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

import it.javalinux.testedby.metadata.serializer.MetadataSerializer;
import it.javalinux.testedby.runner.TestRunner;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.PrintStream;
import java.util.LinkedList;
import java.util.List;

import javassist.ClassPool;
import javassist.CtClass;

/**
 * This class is responsible for calling a TestedBy runner in a new process.
 * The plugin delegates to this class that runs in a different process to
 * have the proper boot classpath set up (for the instrumentation).
 * 
 * @author alessio.soldano@javalinux.it
 * @since 28-Nov-2009
 *
 */
public class Executor {
    
    private static final PrintStream output = System.out;
    
    /**
     * Entry point for the executor; the caller (the mojo) provides
     * the Configuration containing the information required to
     * get an instance of the runner and use it.
     * 
     * @param args	Arguments: the filesystem path to the run configuration
     */
    public static void main(String[] args) {
	try {
	    Configuration config = readConfiguration(args[0]);
	    TestRunner runner = getRunnerInstance(config.getRunner());
	    MetadataSerializer serializer = getSerializerInstance(config.getSerializer());
	    if (serializer != null) {
		runner.run(getClassDefitions(config.getChangedClassesUnderTest()), getClassDefitions(config.getChangedTestClasses()), serializer);
	    } else {
		runner.run(getClassDefitions(config.getChangedClassesUnderTest()), getClassDefitions(config.getChangedTestClasses()));
	    }
	} catch (Throwable t) {
	    t.printStackTrace(output);
	    throw new RuntimeException(t);
	}
    }
    
    @SuppressWarnings("unchecked")
    private static TestRunner getRunnerInstance(String clazz) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
	Class<TestRunner> runnerClass = (Class<TestRunner>) Class.forName(clazz);
	return runnerClass.newInstance();
    }
    
    @SuppressWarnings("unchecked")
    private static MetadataSerializer getSerializerInstance(String clazz) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
	if (clazz == null) {
	    return null;
	}
	Class<MetadataSerializer> serializerClass = (Class<MetadataSerializer>) Class.forName(clazz);
	return serializerClass.newInstance();
    }
    
    private static List<Class<?>> getClassDefitions(List<File> classFiles) throws IOException, RuntimeException, ClassNotFoundException {
	List<Class<?>> result = new LinkedList<Class<?>>();
	ClassPool cp = ClassPool.getDefault();
	for (File f : classFiles) {
	    InputStream fis = new FileInputStream(f);
	    try {
		CtClass cc = cp.makeClassIfNew(fis);
		Class<?> clazz = Class.forName(cc.getName());
		result.add(clazz);
	    } finally {
		try {
		    fis.close();
		} catch (Exception e) {
		    // ignore
		}
	    }
	}
	return result;
    }
    
    @SuppressWarnings("null")
    private static Configuration readConfiguration(String file) throws IOException, ClassNotFoundException
    {
	FileInputStream fis = null;
	ObjectInputStream ois = null;
	Configuration config = null;
	try {
	    fis = new FileInputStream(file);
	    ois = new ObjectInputStream(fis);
	    config = (Configuration) ois.readObject();
	} finally {
	    try {
		ois.close();
	    } catch (Exception e) {
		//ignore
	    }
	    try {
		fis.close();
	    } catch (Exception e) {
		//ignore
	    }
	}
	return config;
    }
    
}
