package tech.pegasys.poc.witnesscodeanalysis.functionid;

import org.apache.logging.log4j.Logger;
import org.apache.tuweni.bytes.Bytes;

import static org.apache.logging.log4j.LogManager.getLogger;

public class FunctionIdProcess {
  private static final Logger LOG = getLogger();

  Bytes code;
  int endOfFunctionIdBlock;
  int endOfCode;

  public FunctionIdProcess(Bytes code, int endOfFunctionIdBlock, int endOfCode) {
    this.code = code;
    this.endOfFunctionIdBlock = endOfFunctionIdBlock;
    this.endOfCode = endOfCode;
  }


  public void executeAnalysis() {
    CodePaths codePaths = new CodePaths(this.code);
    codePaths.findFunctionBlockCodePaths(this.endOfFunctionIdBlock);
    codePaths.findCodeSegmentsForFunctions();
    codePaths.showAllCodePaths();
    boolean codePathsValid = codePaths.validateCodeSegments(this.endOfCode);
    LOG.info("Code Paths Valid: {}", codePathsValid);
    if (!codePathsValid) {
      return;
    }

    int COMBINATION_GAP = 4;
    LOG.info("Combining Code Segments using bytes between segments: {}", COMBINATION_GAP);
    codePaths.combineCodeSegments(COMBINATION_GAP);

    codePaths.showCombinedCodeSegments();

    codePaths.estimateWitnessSize();
  }
}