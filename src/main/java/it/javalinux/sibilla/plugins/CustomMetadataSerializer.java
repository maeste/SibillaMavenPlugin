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

import it.javalinux.sibilla.metadata.TestsMetadata;
import it.javalinux.sibilla.metadata.serializer.impl.SimpleMetadataSerializer;

/**
 * A simple metadata serializer that specifies the metadata location
 * 
 * @author alessio.soldano@javalinux.it
 * @since 04-Oct-2010
 *
 */
public class CustomMetadataSerializer extends SimpleMetadataSerializer {

    private String filename;

    public CustomMetadataSerializer(String filename) {
	this.filename = filename;
    }

    /**
     * {@inheritDoc}
     * 
     * @see it.javalinux.sibilla.metadata.serializer.MetadataSerializer#deserialize()
     */
    @Override
    public TestsMetadata deserialize() {
	return this.deserialize(filename);

    }

    /**
     * {@inheritDoc}
     * 
     * @see it.javalinux.sibilla.metadata.serializer.MetadataSerializer#serialize(it.javalinux.sibilla.metadata.TestsMetadata)
     */
    @Override
    public boolean serialize(TestsMetadata metadata) {
	return this.serialize(metadata, filename);
    }

}
