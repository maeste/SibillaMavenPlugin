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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * The information required by Sibilla runner in order to execute 
 * 
 * @author alessio.soldano@javalinux.it
 * @since 28-Nov-2009
 *
 */
public class Configuration implements Serializable {

    private static final long serialVersionUID = 4239665033888035442L;
    
    private String runner;
    private List<File> changedClassesUnderTest = new LinkedList<File>();
    private List<File> changedTestClasses = new LinkedList<File>();
    private String serializer;
    private String targetDir;
    
    /**
     * @return targetDir
     */
    public String getTargetDir() {
        return targetDir;
    }
    /**
     * @param targetDir Sets targetDir to the specified value.
     */
    public void setTargetDir(String targetDir) {
        this.targetDir = targetDir;
    }
    /**
     * @return runner
     */
    public String getRunner() {
        return runner;
    }
    /**
     * @param runner Sets runner to the specified value.
     */
    public void setRunner(String runner) {
        this.runner = runner;
    }
    /**
     * @return changedClassesUnderTest
     */
    public List<File> getChangedClassesUnderTest() {
        return changedClassesUnderTest;
    }
    /**
     * @param changedClassesUnderTest Sets changedClassesUnderTest to the specified value.
     */
    public void setChangedClassesUnderTest(List<File> changedClassesUnderTest) {
        this.changedClassesUnderTest = changedClassesUnderTest;
    }
    /**
     * @return changedTestClasses
     */
    public List<File> getChangedTestClasses() {
        return changedTestClasses;
    }
    /**
     * @param changedTestClasses Sets changedTestClasses to the specified value.
     */
    public void setChangedTestClasses(List<File> changedTestClasses) {
        this.changedTestClasses = changedTestClasses;
    }
    /**
     * @return serializer
     */
    public String getSerializer() {
        return serializer;
    }
    /**
     * @param serializer Sets serializer to the specified value.
     */
    public void setSerializer(String serializer) {
        this.serializer = serializer;
    }
    
    @SuppressWarnings({ "null" })
    public static Configuration load(File file) {
	FileInputStream fis = null;
	ObjectInputStream ois = null;
	Configuration data = null;
	try {
	    fis = new FileInputStream(file);
	    ois = new ObjectInputStream(fis);
	    data = (Configuration)ois.readObject();
	} catch (Exception e) {
	    return null;
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
	return data;
    }
    
    @SuppressWarnings("null")
    public void save(File file) throws IOException {
	FileOutputStream fos = null;
	ObjectOutputStream oos = null;
	try {
	    fos = new FileOutputStream(file);
	    oos = new ObjectOutputStream(fos);
	    oos.writeObject(this);
	} finally {
	    try {
		oos.close();
	    } catch (Exception e) {
		//ignore
	    }
	    try {
		fos.close();
	    } catch (Exception e) {
		//ignore
	    }
	}
    }
}
