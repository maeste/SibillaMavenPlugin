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

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * The information required by TestedBy runner in order to execute 
 * 
 * @author alessio.soldano@javalinux.it
 * @since 28-Nov-2009
 *
 */
public class Configuration implements Serializable {

    private static final long serialVersionUID = 4239665033888035442L;
    
    private String runner;
    private List<String> changedClassesUnderTest = new LinkedList<String>();
    private List<String> changedTestClasses = new LinkedList<String>();
    private String serializer;
    
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
    public List<String> getChangedClassesUnderTest() {
        return changedClassesUnderTest;
    }
    /**
     * @param changedClassesUnderTest Sets changedClassesUnderTest to the specified value.
     */
    public void setChangedClassesUnderTest(List<String> changedClassesUnderTest) {
        this.changedClassesUnderTest = changedClassesUnderTest;
    }
    /**
     * @return changedTestClasses
     */
    public List<String> getChangedTestClasses() {
        return changedTestClasses;
    }
    /**
     * @param changedTestClasses Sets changedTestClasses to the specified value.
     */
    public void setChangedTestClasses(List<String> changedTestClasses) {
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
}
