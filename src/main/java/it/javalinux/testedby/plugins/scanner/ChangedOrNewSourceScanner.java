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
import java.io.IOException;
import java.util.Set;

/**
 * @author alessio.soldano@javalinux.it
 * @since 28-Nov-2009
 *
 */
public class ChangedOrNewSourceScanner extends TestedBySourceScanner {
    
    private final long lastUpdatedWithinMsecs;
    
    public ChangedOrNewSourceScanner(long lastUpdatedWithinMsecs, RunsRepository repository) {
	super(repository);
	this.lastUpdatedWithinMsecs = lastUpdatedWithinMsecs;
    }

    public ChangedOrNewSourceScanner(long lastUpdatedWithinMsecs, Set<String> sourceIncludes, Set<String> sourceExcludes, RunsRepository repository) {
	super(sourceIncludes, sourceExcludes, repository);
	this.lastUpdatedWithinMsecs = lastUpdatedWithinMsecs;
    }
    
    @Override
    protected boolean includeSource(File file) {
	try {
	    Long lastRun = getRepository().getLastRunTimeMillis(file.getCanonicalPath());
	    return (lastRun == null || file.lastModified() > lastRun - lastUpdatedWithinMsecs);
	} catch (IOException e) {
	    throw new RuntimeException(e);
	}
    }

}