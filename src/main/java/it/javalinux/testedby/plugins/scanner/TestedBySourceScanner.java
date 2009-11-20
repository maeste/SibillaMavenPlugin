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
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.codehaus.plexus.compiler.util.scan.AbstractSourceInclusionScanner;
import org.codehaus.plexus.compiler.util.scan.InclusionScanException;
import org.codehaus.plexus.compiler.util.scan.mapping.SourceMapping;

/**
 * 
 * 
 * @author alessio.soldano@javalinux.it
 * @since 20-Nov-2009
 * 
 */
public abstract class TestedBySourceScanner extends AbstractSourceInclusionScanner {

    private final Set<String> sourceIncludes;
    private final Set<String> sourceExcludes;
    private final RunsRepository repository;
    
    private static final Set<String> emptyStringSet = Collections.emptySet();
    
    public TestedBySourceScanner(RunsRepository repository) {
	this(Collections.singleton("**/*"), emptyStringSet, repository);
    }

    public TestedBySourceScanner(Set<String> sourceIncludes, Set<String> sourceExcludes, RunsRepository repository) {
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

	for (int i = 0; i < potentialIncludes.length; i++) {
	    String path = potentialIncludes[i];

	    File sourceFile = new File(sourceDir, path);

	    staleSourceFileTesting: for (SourceMapping mapping : srcMappings) {
		Set<File> targetFiles = mapping.getTargetFiles(targetDir, path);
		// Always use mappings to be sure we're looking for the right
		// binary files
		for (File targetFile : targetFiles) {
		    //check the target binary file exists, otherwise the testedby plugin can't run tests
		    if (!targetFile.exists()) {
			throw new InclusionScanException("Missing target file, please make sure the maven-compiler-plugin is run first: " + targetFile);
		    }
		    if (includeSource(sourceFile)) {
			matchingSources.add(sourceFile);
			break staleSourceFileTesting;
		    }
		}
	    }
	}
	return matchingSources;
    }
    
    protected RunsRepository getRepository() {
	return repository;
    }

    protected abstract boolean includeSource(File file);
}
