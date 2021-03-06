/*
 * Copyright ConsenSys AG.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package tech.pegasys.poc.witnesscodeanalysis;

import org.apache.tuweni.bytes.Bytes;

public class BasicBlockWithCode {
  private int start;
  private int length;
  private Bytes codeFragment;

  public BasicBlockWithCode(int start, int length, Bytes codeFragment) {
    this.start = start;
    this.length = length;
    this.codeFragment = codeFragment;
  }

  public int getStart() {
    return start;
  }

  public int getLength() {
    return length;
  }

  public Bytes getCodeFragment() {
    return codeFragment;
  }

  @Override
  public String toString() {
    return "Data: Start 0x" + Integer.toHexString(this.start) + ", Length 0x" + Integer.toHexString(this.length);
  }
}
