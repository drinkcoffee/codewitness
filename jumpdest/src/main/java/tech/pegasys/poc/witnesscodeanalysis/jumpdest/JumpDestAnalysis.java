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
package tech.pegasys.poc.witnesscodeanalysis.jumpdest;

import org.apache.logging.log4j.Logger;
import org.apache.tuweni.bytes.Bytes;
import tech.pegasys.poc.witnesscodeanalysis.CodeAnalysisBase;
import tech.pegasys.poc.witnesscodeanalysis.common.PcUtils;
import tech.pegasys.poc.witnesscodeanalysis.vm.MainnetEvmRegistries;
import tech.pegasys.poc.witnesscodeanalysis.vm.Operation;
import tech.pegasys.poc.witnesscodeanalysis.vm.OperationRegistry;
import tech.pegasys.poc.witnesscodeanalysis.vm.operations.InvalidOperation;
import tech.pegasys.poc.witnesscodeanalysis.vm.operations.JumpDestOperation;
import tech.pegasys.poc.witnesscodeanalysis.vm.operations.JumpOperation;

import java.math.BigInteger;
import java.util.ArrayList;


import static org.apache.logging.log4j.LogManager.getLogger;

public class JumpDestAnalysis extends CodeAnalysisBase {
  private static final Logger LOG = getLogger();
  int threshold;
  private boolean isInvalidSeen;

  public static OperationRegistry registry = MainnetEvmRegistries.berlin(BigInteger.ONE);

  public JumpDestAnalysis(Bytes code, int threshold) {
    super(code);
    this.threshold = threshold;
    isInvalidSeen = false;
  }

  public ArrayList<Integer> analyse() {
    int pc = 0;
    int currentChunkSize = 0;
    ArrayList<Integer> chunkStartAddresses = new ArrayList<>();
    chunkStartAddresses.add(0);
    while (pc != this.possibleEndOfCode) {
      final Operation curOp = registry.get(code.get(pc), 0);
      if (curOp == null) {
        LOG.error("Unknown opcode 0x{} at PC {}", Integer.toHexString(code.get(pc)), PcUtils.pcStr(pc));
        throw new Error("Unknown opcode");
      }
      int opSize = curOp.getOpSize();
      int opCode = curOp.getOpcode();

      if(isInvalidSeen && curOp.getOpcode() == JumpOperation.OPCODE) {
        LOG.info("JUMP after Invalid is seen. Ending.");
        break;
      }

      if (opCode == InvalidOperation.OPCODE) {
        LOG.info("Invalid OPCODE is hit.");
        isInvalidSeen = true;
      }

      if (opCode == JumpDestOperation.OPCODE) {
        //LOG.info("****Found JumpDest at {}", pc);

        if(currentChunkSize + opSize >= threshold) {
          currentChunkSize = 0;
          pc += opSize;
          chunkStartAddresses.add(pc);
          continue;
        }
      }

      currentChunkSize += opSize;
      pc += opSize;
    }

    return chunkStartAddresses;
    /*LOG.info("There are {} chunks with starting addresses : ", chunkStartAddresses.size());
    for(Integer e : chunkStartAddresses) {
      LOG.info(e);
    }*/
  }
}
