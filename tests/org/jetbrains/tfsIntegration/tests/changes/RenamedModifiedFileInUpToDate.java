/*
 * Copyright 2000-2008 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jetbrains.tfsIntegration.tests.changes;

import com.intellij.openapi.vcs.FilePath;
import com.intellij.openapi.vcs.VcsException;
import com.intellij.openapi.vcs.changes.Change;
import org.jetbrains.annotations.Nullable;
import org.junit.Test;

import java.io.IOException;


public class RenamedModifiedFileInUpToDate extends ChildChangeTestCase {

  private FilePath myOriginalFile;
  private FilePath myRenamedModifiedFile;

  protected void preparePaths() {
    myOriginalFile = getChildPath(mySandboxRoot, "original.txt");
    myRenamedModifiedFile = getChildPath(mySandboxRoot, "file_renamed_modified.txt");
  }

  protected void checkChildChangePending() throws VcsException {
    getChanges().assertTotalItems(1);
    getChanges().assertRenamedOrMoved(myOriginalFile, myRenamedModifiedFile, ORIGINAL_CONTENT, MODIFIED_CONTENT);
    
    assertFolder(mySandboxRoot, 1);
    assertFile(myRenamedModifiedFile, MODIFIED_CONTENT, true);
  }

  protected void checkOriginalStateAfterUpdate() throws VcsException {
    getChanges().assertTotalItems(0);
    assertFolder(mySandboxRoot, 1);
    assertFile(myOriginalFile, ORIGINAL_CONTENT, false);
  }

  protected void checkOriginalStateAfterRollback() throws VcsException {
    checkOriginalStateAfterUpdate();
  }

  protected void checkChildChangeCommitted() throws VcsException {
    getChanges().assertTotalItems(0);
    
    assertFolder(mySandboxRoot, 1);
    assertFile(myRenamedModifiedFile, MODIFIED_CONTENT, false);
  }

  protected void makeOriginalState() throws VcsException {
    createFileInCommand(myOriginalFile, ORIGINAL_CONTENT);
  }

  protected void makeChildChange() throws IOException, VcsException {
    rename(myOriginalFile, myRenamedModifiedFile.getName());
    editFiles(myRenamedModifiedFile);
    setFileContent(myRenamedModifiedFile, MODIFIED_CONTENT);
  }

  @Nullable
  protected Change getPendingChildChange() throws VcsException {
    return getChanges().getMoveChange(myOriginalFile, myRenamedModifiedFile);
  }

  @Test
  public void doTest() throws VcsException, IOException {
    super.doTest();
  }

}
