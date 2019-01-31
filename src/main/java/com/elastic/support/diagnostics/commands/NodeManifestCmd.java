package com.elastic.support.diagnostics.commands;

import com.elastic.support.diagnostics.InputParams;
import com.elastic.support.diagnostics.chain.DiagnosticContext;
import com.elastic.support.util.JsonYamlUtils;
import com.elastic.support.util.RestExec;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.Map;


public class NodeManifestCmd extends AbstractDiagnosticCmd {

   public boolean execute(DiagnosticContext context) {

      // Get the version number from the JSON returned
      // by just submitting the host/port combo
      Map resultMap = null;
      InputParams inputs = context.getInputParams();
      boolean rc = true;
      logger.info("Trying REST Endpoint.");

      try {
         RestExec restExec = context.getRestExec();
         String result = restExec.execBasic(inputs.getProtocol() + "://" + inputs.getHost() + ":" + inputs.getPort());
         JsonNode root = JsonYamlUtils.createJsonNodeFromString(result);
         String versionNumber = root.path("version").path("number").asText();
         context.setVersion(versionNumber);
      } catch (Exception e) {
         logger.info("Error retrieving Elasticsearch version. Cannot continue.");
         logger.info(e.getMessage());
         rc = false;
      }

      return rc;
   }


}
