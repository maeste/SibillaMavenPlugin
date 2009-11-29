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
package it.javalinux.testedby.plugins.scanner;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Keep track of when target source files have gone through a TestedBy run last time
 * 
 * @author alessio.soldano@javalinux.it
 * @since 20-Nov-2009
 *
 */
public class RunsRepository {
    
    private static Logger log = Logger.getLogger(RunsRepository.class.getName());
    static final String DEFAULT_REPO_FILENAME = "testedby-runs-repository.bin";
    private File file;
    private Map<String, Long> repository = new HashMap<String, Long>();
    
    /**
     * Create a repository using the default filename in the current directory
     * 
     */
    public RunsRepository() {
	file = new File(DEFAULT_REPO_FILENAME);
    }
    
    /**
     * Create a repository using the default filename in the specified directory
     * 
     * @param dir
     */
    public RunsRepository(File dir) {
	file = new File(dir, DEFAULT_REPO_FILENAME);
    }
    
    /**
     * Create a repository using a filename with the provided name in the specified directory
     * 
     * @param dir
     * @param filename
     */
    public RunsRepository(File dir, String filename) {
	file = new File(dir, filename);
    }
    
    public void load() {
	repository = deserialize(file);
    }
    
    public void save() throws IOException {
	serialize(repository, file);
    }
    
    public Long getLastRunTimeMillis(String target) {
	return repository.get(target);
    }
    
    public void clear() {
	repository.clear();
    }
    
    public void setLastRunTimeMillis(String target, Long time) {
	repository.put(target, time);
    }
    
    @SuppressWarnings({ "null", "unchecked" })
    private Map<String, Long> deserialize(File file) {
	FileInputStream fis = null;
	ObjectInputStream ois = null;
	Map<String, Long> data = null;
	if (!file.exists()) {
	    log.info("Could not find " + file + ", returning a new empty map.");
	    return new HashMap<String, Long>();
	}
	try {
	    fis = new FileInputStream(file);
	    ois = new ObjectInputStream(fis);
	    data = (Map<String, Long>)ois.readObject();
	} catch (Exception e) {
	    log.log(Level.INFO, "Unable to deserialize TestedBy runs repository from file: " + file, e);
	    return new HashMap<String, Long>();
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
    private void serialize(Map<String, Long> data, File file) throws IOException {
	FileOutputStream fos = null;
	ObjectOutputStream oos = null;
	try {
	    fos = new FileOutputStream(file);
	    oos = new ObjectOutputStream(fos);
	    oos.writeObject(data);
	} catch (IOException e) {
	    log.log(Level.INFO, "Unable to serialize TestedBy runs repository to file: " + file, e);
	    throw e;
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
