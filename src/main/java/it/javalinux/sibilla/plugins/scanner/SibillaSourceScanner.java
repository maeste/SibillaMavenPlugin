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
package it.javalinux.sibilla.plugins.scanner;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.codehaus.plexus.compiler.util.scan.AbstractSourceInclusionScanner;
import org.codehaus.plexus.compiler.util.scan.InclusionScanException;
import org.codehaus.plexus.compiler.util.scan.mapping.SourceMapping;

/**
 * The source inclusion scanner for Sibilla rules. Also provides a method for
 * getting corresponding included targets.
 * 
 * @author alessio.soldano@javalinux.it
 * @since 20-Nov-2009
 * 
 */
public abstract class SibillaSourceScanner extends AbstractSourceInclusionScanner {

    private final Set<String> sourceIncludes;
    private final Set<String> sourceExcludes;
    private final RunsRepository repository;
    private final Map<Key, Set<File>> targets = new HashMap<Key, Set<File>>();
    
    private static final Set<String> emptyStringSet = Collections.emptySet();
    
    public SibillaSourceScanner(RunsRepository repository) {
	this(Collections.singleton("**/*"), emptyStringSet, repository);
    }

    public SibillaSourceScanner(Set<String> sourceIncludes, Set<String> sourceExcludes, RunsRepository repository) {
	this.sourceIncludes = sourceIncludes;
	this.sourceExcludes = sourceExcludes;
	this.repository = repository;
    }

    @SuppressWarnings("unchecked")
    public Set<File> getIncludedSources(File sourceDir, File targetDir) throws InclusionScanException {
	List<SourceMapping> srcMappings = getSourceMappings();
	if (srcMappings.isEmpty()) {
	    return Collections.emptySet();
	}

	String[] potentialIncludes = scanForSources(sourceDir, sourceIncludes, sourceExcludes);
	Set<File> matchingSources = new HashSet<File>();

	Key k = new Key(sourceDir, targetDir);
	if (!targets.containsKey(k)) {
	    targets.put(k, new HashSet<File>());
	}
	Set<File> mappedTargets = targets.get(k);
	
	for (int i = 0; i < potentialIncludes.length; i++) {
	    String path = potentialIncludes[i];

	    File sourceFile = new File(sourceDir, path);

	    for (SourceMapping mapping : srcMappings) {
		Set<File> targetFiles = mapping.getTargetFiles(targetDir, path);
		for (File targetFile : targetFiles) {
		    //check the target binary files exist, otherwise the sibilla plugin can't run tests
		    if (!targetFile.exists()) {
			throw new InclusionScanException("Missing target file, please make sure the maven-compiler-plugin is run first: " + targetFile);
		    }
		}
		//never include sources without corresponding target files
		if (!targetFiles.isEmpty() && include(sourceFile)) {
		    matchingSources.add(sourceFile);
		    mappedTargets.addAll(targetFiles);
		}
	    }
	}
	return matchingSources;
    }
    
    public Set<File> getIncludedTargets(File sourceDir, File targetDir) throws InclusionScanException {
	Key k = new Key(sourceDir, targetDir);
	if (!targets.containsKey(k)) {
	    getIncludedSources(sourceDir, targetDir);
	}
	return targets.get(k);
    }
    
    protected RunsRepository getRepository() {
	return repository;
    }

    protected abstract boolean include(File sourceFile);
    
    
    private class Key {
	
	private final File sourceDir;
	private final File targetDir;
	
	/**
	 * @param sourceDir
	 * @param targetDir
	 */
	public Key(File sourceDir, File targetDir) {
	    super();
	    this.sourceDir = sourceDir;
	    this.targetDir = targetDir;
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object o) {
	    if (o == null || !(o instanceof Key)) {
		return false;
	    }
	    Key k = (Key)o;
	    if (sourceDir == null) {
		if (k.sourceDir != null) {
		    return false;
		}
	    } else if (!sourceDir.equals(k.sourceDir)) {
		return false;
	    }
	    if (targetDir == null) {
		if (k.targetDir != null) {
		    return false;
		}
	    } else if (!targetDir.equals(k.targetDir)) {
		return false;
	    }
	    return true;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
	    int result = 1;
	    result = 31 * result + ((sourceDir == null) ? 0 : sourceDir.hashCode());
	    result = 31 * result + ((targetDir == null) ? 0 : targetDir.hashCode());
	    return result;
	}
    }
    
}
