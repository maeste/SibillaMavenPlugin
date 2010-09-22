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
import java.io.IOException;
import java.util.Set;

/**
 * @author alessio.soldano@javalinux.it
 * @since 20-Nov-2009
 *
 */
public class ChangedSourceScanner extends SibillaSourceScanner {
    
    private final long lastUpdatedWithinMsecs;
    
    public ChangedSourceScanner(long lastUpdatedWithinMsecs, RunsRepository repository) {
	super(repository);
	this.lastUpdatedWithinMsecs = lastUpdatedWithinMsecs;
    }

    public ChangedSourceScanner(long lastUpdatedWithinMsecs, Set<String> sourceIncludes, Set<String> sourceExcludes, RunsRepository repository) {
	super(sourceIncludes, sourceExcludes, repository);
	this.lastUpdatedWithinMsecs = lastUpdatedWithinMsecs;
    }
    
    @Override
    protected boolean include(File sourceFile) {
	try {
	    Long lastRun = getRepository().getLastRunTimeMillis(sourceFile.getCanonicalPath());
	    return (lastRun != null && sourceFile.lastModified() > lastRun - lastUpdatedWithinMsecs);
	} catch (IOException e) {
	    throw new RuntimeException(e);
	}
    }

}
