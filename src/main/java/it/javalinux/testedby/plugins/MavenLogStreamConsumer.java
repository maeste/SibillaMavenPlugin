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

import org.apache.maven.plugin.logging.Log;
import org.codehaus.plexus.util.cli.StreamConsumer;

/**
 * A StreamConsumer that redirects logs to the maven log system
 * 
 * @author alessio.soldano@javalinux.it
 * @since 29-Nov-2009
 */
public class MavenLogStreamConsumer implements StreamConsumer {

    public static enum Type {OUTPUT, ERROR}
    private final Type type;
    private final Log log;
    
    public MavenLogStreamConsumer(Log log, Type type) {
	this.log = log;
	this.type = type;
    }
    
    /**
     * {@inheritDoc}
     *
     * @see org.codehaus.plexus.util.cli.StreamConsumer#consumeLine(java.lang.String)
     */
    public void consumeLine(String line) {
	if (Type.ERROR.equals(type)) {
	    log.error(line);
	} else {
	    log.info(line);
	}
    }
    
}
