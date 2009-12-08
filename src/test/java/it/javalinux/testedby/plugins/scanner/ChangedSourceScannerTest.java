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

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.matchers.JUnitMatchers.hasItem;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.codehaus.plexus.compiler.util.scan.InclusionScanException;
import org.codehaus.plexus.compiler.util.scan.mapping.SuffixMapping;
import org.junit.Test;

/**
 * 
 * @author alessio.soldano@javalinux.it
 * @since 21-Nov-2009
 *
 */
public class ChangedSourceScannerTest {
    
    @Test
    public void shouldReturnNoClassesOnFirstRun() throws IOException, InclusionScanException
    {
	File srcDir = createTempDir();
	File targetDir = createTempDir();
	File source1 = new File(srcDir, "MyClass1.java");
	assertTrue(source1.createNewFile());
	File target1 = new File(targetDir, "MyClass1.class");
	assertTrue(target1.createNewFile());
	File source2 = new File(srcDir, "MyClass2.java");
	assertTrue(source2.createNewFile());
	File target2 = new File(targetDir, "MyClass2.class");
	assertTrue(target2.createNewFile());
	
	RunsRepository repository = new RunsRepository();
	
	ChangedSourceScanner scanner = new ChangedSourceScanner(0, repository);
	scanner.addSourceMapping(new SuffixMapping(".java", ".class"));
	Set<File> newFiles = scanner.getIncludedSources(srcDir, targetDir);
	assertThat(newFiles.size(), is(0));
	Set<File> newTargetFiles = scanner.getIncludedTargets(srcDir, targetDir);
	assertThat(newTargetFiles.size(), is(0));
	
	delete(new File[]{source1, target1, source2, target2, srcDir, targetDir});
    }
    
    @Test
    public void shouldDetectChangedClasses() throws IOException, InclusionScanException
    {
	File srcDir = createTempDir();
	File targetDir = createTempDir();
	File source1 = new File(srcDir, "MyClass1.java");
	assertTrue(source1.createNewFile());
	File target1 = new File(targetDir, "MyClass1.class");
	assertTrue(target1.createNewFile());
	File source2 = new File(srcDir, "MyClass2.java");
	assertTrue(source2.createNewFile());
	File target2 = new File(targetDir, "MyClass2.class");
	assertTrue(target2.createNewFile());
	
	RunsRepository repository = new RunsRepository();
	repository.setLastRunTimeMillis(source1.getCanonicalPath(), new Long(1)); //target last ran years ago...
	
	ChangedSourceScanner scanner = new ChangedSourceScanner(0, repository);
	scanner.addSourceMapping(new SuffixMapping(".java", ".class"));
	
	Set<File> changedFiles = scanner.getIncludedSources(srcDir, targetDir);
	assertThat(changedFiles.size(), is(1));
	assertThat(changedFiles, hasItem(source1));
	
	Set<File> changedTargetFiles = scanner.getIncludedTargets(srcDir, targetDir);
	assertThat(changedTargetFiles.size(), is(1));
	assertThat(changedTargetFiles, hasItem(target1));
	
	repository.setLastRunTimeMillis(source2.getCanonicalPath(), new Long(1)); //target last ran years ago...
	
	changedFiles = scanner.getIncludedSources(srcDir, targetDir);
	assertThat(changedFiles.size(), is(2));
	assertThat(changedFiles, hasItem(source1));
	assertThat(changedFiles, hasItem(source2));
	
	changedTargetFiles = scanner.getIncludedTargets(srcDir, targetDir);
	assertThat(changedTargetFiles.size(), is(2));
	assertThat(changedTargetFiles, hasItem(target1));
	assertThat(changedTargetFiles, hasItem(target2));
	
	delete(new File[]{source1, target1, source2, target2, srcDir, targetDir});
    }
    
    @Test
    public void shouldDetectChangedClassesWithPartialPreviousRuns() throws IOException, InclusionScanException
    {
	File srcDir = createTempDir();
	File targetDir = createTempDir();
	File source1 = new File(srcDir, "MyClass1.java");
	assertTrue(source1.createNewFile());
	source1.setLastModified(System.currentTimeMillis() - 10*60*1000); //10 minutes ago
	File target1 = new File(targetDir, "MyClass1.class");
	assertTrue(target1.createNewFile());
	File source2 = new File(srcDir, "MyClass2.java");
	assertTrue(source2.createNewFile());
	source2.setLastModified(System.currentTimeMillis() - 10*60*1000); //10 minutes ago
	File target2 = new File(targetDir, "MyClass2.class");
	assertTrue(target2.createNewFile());
	
	RunsRepository repository = new RunsRepository();
	repository.setLastRunTimeMillis(source1.getCanonicalPath(), System.currentTimeMillis() - 5*60*1000); //5 minutes ago
	repository.setLastRunTimeMillis(source2.getCanonicalPath(), System.currentTimeMillis() - 15*60*1000); //15 minutes ago
	
	ChangedSourceScanner scanner = new ChangedSourceScanner(0, repository);
	scanner.addSourceMapping(new SuffixMapping(".java", ".class"));
	
	Set<File> changedFiles = scanner.getIncludedSources(srcDir, targetDir);
	assertThat(changedFiles.size(), is(1));
	assertThat(changedFiles, hasItem(source2));
	
	Set<File> changedTargetFiles = scanner.getIncludedTargets(srcDir, targetDir);
	assertThat(changedTargetFiles.size(), is(1));
	assertThat(changedTargetFiles, hasItem(target2));
	
	delete(new File[]{source1, target1, source2, target2, srcDir, targetDir});
    }
    
    @Test
    public void shouldDetectChangedClassesUsingTimeTolerance() throws IOException, InclusionScanException
    {
	File srcDir = createTempDir();
	File targetDir = createTempDir();
	File source1 = new File(srcDir, "MyClass1.java");
	assertTrue(source1.createNewFile());
	source1.setLastModified(System.currentTimeMillis() - 10*60*1000); //10 minutes ago
	File target1 = new File(targetDir, "MyClass1.class");
	assertTrue(target1.createNewFile());
	File source2 = new File(srcDir, "MyClass2.java");
	assertTrue(source2.createNewFile());
	source2.setLastModified(System.currentTimeMillis() - 10*60*1000); //10 minutes ago
	File target2 = new File(targetDir, "MyClass2.class");
	assertTrue(target2.createNewFile());
	
	RunsRepository repository = new RunsRepository();
	repository.setLastRunTimeMillis(source1.getCanonicalPath(), System.currentTimeMillis() - 5*60*1000); //5 minutes ago
	repository.setLastRunTimeMillis(source2.getCanonicalPath(), System.currentTimeMillis() - 7*60*1000); //7 minutes ago
	
	ChangedSourceScanner scanner = new ChangedSourceScanner(2*60*1000, repository); //2 minutes of tolerance
	scanner.addSourceMapping(new SuffixMapping(".java", ".class"));
	
	Set<File> changedFiles = scanner.getIncludedSources(srcDir, targetDir);
	assertThat(changedFiles.size(), is(0));
	Set<File> changedTargetFiles = scanner.getIncludedTargets(srcDir, targetDir);
	assertThat(changedTargetFiles.size(), is(0));
	
	repository.setLastRunTimeMillis(source2.getCanonicalPath(), System.currentTimeMillis() - 9*60*1000); //9 minutes ago
	
	changedFiles = scanner.getIncludedSources(srcDir, targetDir);
	assertThat(changedFiles.size(), is(1));
	assertThat(changedFiles, hasItem(source2));
	changedTargetFiles = scanner.getIncludedTargets(srcDir, targetDir);
	assertThat(changedTargetFiles.size(), is(1));
	assertThat(changedTargetFiles, hasItem(target2));
	
	repository.setLastRunTimeMillis(source2.getCanonicalPath(), System.currentTimeMillis() - 15*60*1000); //15 minutes ago
	
	changedFiles = scanner.getIncludedSources(srcDir, targetDir);
	assertThat(changedFiles.size(), is(1));
	assertThat(changedFiles, hasItem(source2));
	changedTargetFiles = scanner.getIncludedTargets(srcDir, targetDir);
	assertThat(changedTargetFiles.size(), is(1));
	assertThat(changedTargetFiles, hasItem(target2));
	
	delete(new File[]{source1, target1, source2, target2, srcDir, targetDir});
    }

    private static File createTempDir() throws IOException
    {
	File dir = File.createTempFile("TestedBy-maven-plugin-test-changedsourcescanner-", String.valueOf(System.currentTimeMillis()));
	dir.delete();
	dir.mkdir();
	return dir;
    }
    
    @Test
    public void shouldCorrectlyHandleExclusions() throws IOException, InclusionScanException
    {
	File srcDir = createTempDir();
	File targetDir = createTempDir();
	File source1 = new File(srcDir, "MyClass1.java");
	assertTrue(source1.createNewFile());
	File target1 = new File(targetDir, "MyClass1.class");
	assertTrue(target1.createNewFile());
	File source2 = new File(srcDir, "MyClass2.java");
	assertTrue(source2.createNewFile());
	File target2 = new File(targetDir, "MyClass2.class");
	assertTrue(target2.createNewFile());
	
	RunsRepository repository = new RunsRepository();
	repository.setLastRunTimeMillis(source1.getCanonicalPath(), new Long(1)); //target last ran years ago...
	repository.setLastRunTimeMillis(source2.getCanonicalPath(), new Long(1)); //target last ran years ago...
	
	Set<String> excludes = new HashSet<String>();
	Set<String> includes = new HashSet<String>();
	includes.add("**/*");
	ChangedSourceScanner scanner = new ChangedSourceScanner(0, includes, excludes, repository);
	scanner.addSourceMapping(new SuffixMapping(".java", ".class"));
	Set<File> changedFiles = scanner.getIncludedSources(srcDir, targetDir);
	assertThat(changedFiles.size(), is(2));
	assertThat(changedFiles, hasItem(source1));
	assertThat(changedFiles, hasItem(source2));
	Set<File> changedTargetFiles = scanner.getIncludedTargets(srcDir, targetDir);
	assertThat(changedTargetFiles.size(), is(2));
	assertThat(changedTargetFiles, hasItem(target1));
	assertThat(changedTargetFiles, hasItem(target2));
	
	excludes.add(source1.getName());
	scanner = new ChangedSourceScanner(0, includes, excludes, repository);
	scanner.addSourceMapping(new SuffixMapping(".java", ".class"));
	changedFiles = scanner.getIncludedSources(srcDir, targetDir);
	assertThat(changedFiles.size(), is(1));
	assertThat(changedFiles, hasItem(source2));
	changedTargetFiles = scanner.getIncludedTargets(srcDir, targetDir);
	assertThat(changedTargetFiles.size(), is(1));
	assertThat(changedTargetFiles, hasItem(target2));
	
	excludes.add(source2.getName());
	scanner = new ChangedSourceScanner(0, includes, excludes, repository);
	scanner.addSourceMapping(new SuffixMapping(".java", ".class"));
	changedFiles = scanner.getIncludedSources(srcDir, targetDir);
	assertThat(changedFiles.size(), is(0));
	changedTargetFiles = scanner.getIncludedTargets(srcDir, targetDir);
	assertThat(changedTargetFiles.size(), is(0));

	delete(new File[]{source1, target1, source2, target2, srcDir, targetDir});
    }
    
    @Test
    public void shouldCorrectlyHandleInclusions() throws IOException, InclusionScanException
    {
	File srcDir = createTempDir();
	File targetDir = createTempDir();
	File source1 = new File(srcDir, "MyClass1.java");
	assertTrue(source1.createNewFile());
	File target1 = new File(targetDir, "MyClass1.class");
	assertTrue(target1.createNewFile());
	File source2 = new File(srcDir, "MyClass2.java");
	assertTrue(source2.createNewFile());
	File target2 = new File(targetDir, "MyClass2.class");
	assertTrue(target2.createNewFile());
	
	RunsRepository repository = new RunsRepository();
	repository.setLastRunTimeMillis(source1.getCanonicalPath(), new Long(1)); //target last ran years ago...
	repository.setLastRunTimeMillis(source2.getCanonicalPath(), new Long(1)); //target last ran years ago...
	
	Set<String> excludes = Collections.emptySet();
	Set<String> includes = new HashSet<String>();
	ChangedSourceScanner scanner = new ChangedSourceScanner(0, includes, excludes, repository);
	scanner.addSourceMapping(new SuffixMapping(".java", ".class"));
	Set<File> changedFiles = scanner.getIncludedSources(srcDir, targetDir);
	assertThat(changedFiles.size(), is(0));
	Set<File> changedTargetFiles = scanner.getIncludedTargets(srcDir, targetDir);
	assertThat(changedTargetFiles.size(), is(0));
	
	includes.add(source1.getName());
	scanner = new ChangedSourceScanner(0, includes, excludes, repository);
	scanner.addSourceMapping(new SuffixMapping(".java", ".class"));
	changedFiles = scanner.getIncludedSources(srcDir, targetDir);
	assertThat(changedFiles.size(), is(1));
	assertThat(changedFiles, hasItem(source1));
	changedTargetFiles = scanner.getIncludedTargets(srcDir, targetDir);
	assertThat(changedTargetFiles.size(), is(1));
	assertThat(changedTargetFiles, hasItem(target1));
	
	includes.add(source2.getName());
	scanner = new ChangedSourceScanner(0, includes, excludes, repository);
	scanner.addSourceMapping(new SuffixMapping(".java", ".class"));
	changedFiles = scanner.getIncludedSources(srcDir, targetDir);
	assertThat(changedFiles.size(), is(2));
	assertThat(changedFiles, hasItem(source1));
	assertThat(changedFiles, hasItem(source2));
	changedTargetFiles = scanner.getIncludedTargets(srcDir, targetDir);
	assertThat(changedTargetFiles.size(), is(2));
	assertThat(changedTargetFiles, hasItem(target1));
	assertThat(changedTargetFiles, hasItem(target2));

	delete(new File[]{source1, target1, source2, target2, srcDir, targetDir});
    }
    
    private static void delete(File... files) {
	for (File file : files) {
	    try {
		file.delete();
	    } catch (Exception e) {
		//ignore
	    }
	}
    }
    
}
