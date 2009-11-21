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
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

/**
 * 
 * @author alessio.soldano@javalinux.it
 * @since 21-Nov-2009
 *
 */
public class RunsRepositoryTest {

    @Test
    public void shouldCorrectlySerializeAndDeserialize() throws IOException
    {
	File file = File.createTempFile("TestedBy-maven-plugin-test-run-repository-", String.valueOf(System.currentTimeMillis()) + ".bin");

	RunsRepository repo = new RunsRepository(file.getParentFile(), file.getName());
	repo.setLastRunTimeMillis("a", new Long(1));
	repo.setLastRunTimeMillis("b", new Long(2));
	repo.setLastRunTimeMillis("c", new Long(3));
	repo.setLastRunTimeMillis("d", new Long(4));
	repo.setLastRunTimeMillis("b", new Long(7));
	repo.setLastRunTimeMillis("c", new Long(8));
	repo.save();
	
	RunsRepository loadedRepo = new RunsRepository(file.getParentFile(), file.getName());
	loadedRepo.load();
	assertThat(loadedRepo.getLastRunTimeMillis("a"), is(new Long(1)));
	assertThat(loadedRepo.getLastRunTimeMillis("b"), is(new Long(7)));
	assertThat(loadedRepo.getLastRunTimeMillis("c"), is(new Long(8)));
	assertThat(loadedRepo.getLastRunTimeMillis("d"), is(new Long(4)));
	
	file.delete();
    }
    
    @Test
    public void shouldClearContents() throws IOException
    {
	File file = File.createTempFile("TestedBy-maven-plugin-test-run-repository-", String.valueOf(System.currentTimeMillis()) + ".bin");

	RunsRepository repo = new RunsRepository(file.getParentFile(), file.getName());

	assertNull(repo.getLastRunTimeMillis("a"));
	assertNull(repo.getLastRunTimeMillis("b"));
	assertNull(repo.getLastRunTimeMillis("c"));
	assertNull(repo.getLastRunTimeMillis("d"));
	
	repo.setLastRunTimeMillis("a", new Long(10));
	repo.setLastRunTimeMillis("b", new Long(20));
	repo.setLastRunTimeMillis("c", new Long(30));
	repo.setLastRunTimeMillis("d", new Long(40));
	repo.setLastRunTimeMillis("b", new Long(70));
	repo.setLastRunTimeMillis("c", new Long(80));
	
	assertThat(repo.getLastRunTimeMillis("a"), is(new Long(10)));
	assertThat(repo.getLastRunTimeMillis("b"), is(new Long(70)));
	assertThat(repo.getLastRunTimeMillis("c"), is(new Long(80)));
	assertThat(repo.getLastRunTimeMillis("d"), is(new Long(40)));
	
	repo.clear();
	
	assertNull(repo.getLastRunTimeMillis("a"));
	assertNull(repo.getLastRunTimeMillis("b"));
	assertNull(repo.getLastRunTimeMillis("c"));
	assertNull(repo.getLastRunTimeMillis("d"));
	
	file.delete();
    }
    
    @Test
    public void shouldWorkWithDefaultName() throws IOException
    {
	File dir = File.createTempFile("TestedBy-maven-plugin-test-run-repository-", String.valueOf(System.currentTimeMillis()));
	dir.delete();
	dir.mkdir();

	RunsRepository repo = new RunsRepository(dir);
	repo.setLastRunTimeMillis("a", new Long(1));
	repo.setLastRunTimeMillis("b", new Long(2));
	repo.setLastRunTimeMillis("c", new Long(3));
	repo.setLastRunTimeMillis("d", new Long(4));
	repo.setLastRunTimeMillis("b", new Long(7));
	repo.setLastRunTimeMillis("c", new Long(8));
	repo.save();
	
	RunsRepository loadedRepo = new RunsRepository(dir);
	loadedRepo.load();
	assertThat(loadedRepo.getLastRunTimeMillis("a"), is(new Long(1)));
	assertThat(loadedRepo.getLastRunTimeMillis("b"), is(new Long(7)));
	assertThat(loadedRepo.getLastRunTimeMillis("c"), is(new Long(8)));
	assertThat(loadedRepo.getLastRunTimeMillis("d"), is(new Long(4)));
	
	File repoFile = new File(dir, RunsRepository.DEFAULT_REPO_FILENAME);
	assertTrue(repoFile.exists());
	assertTrue(repoFile.length() > 0);
	
	repoFile.delete();
	dir.delete();
    }
    
    
}
