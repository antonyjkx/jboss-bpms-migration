/*
 * Copyright 2016 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.entoss.bpmspoc;


import org.kie.server.client.KieServicesClient;
import org.kie.server.client.KieServicesConfiguration;
import org.kie.server.client.KieServicesFactory;
import org.kie.server.client.admin.ProcessAdminServicesClient;
import org.kie.server.api.model.admin.MigrationReportInstance;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import org.kie.server.api.marshalling.MarshallingFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProcessInstanceUpgradeService  {

	
    private static final Logger logger = LoggerFactory.getLogger(ProcessInstanceUpgradeService.class);
   
    protected ProcessAdminServicesClient migrationService;
    
    public static void main(String[] args) {
    	
    	if( args.length < 4) {
    		logger.error("Usage ProcessInstanceUpgradeService <source-container> <process-instance-id> <target-container> <target-process-id>");
    		return;
    	}
    	
    	String sourceContainer = args[0];
    	long processInstanceId = Long.parseLong(args[1]);
    	String targetContainer = args[2];
    	String targetProcessId = args[3];
    	
    	ProcessInstanceUpgradeService upgradeService = new ProcessInstanceUpgradeService();
    	upgradeService.prepare();
    	
    	
    	upgradeService.migrateProcessInstance(sourceContainer, processInstanceId, targetContainer, targetProcessId );
        
    }
    
    public void prepare() {
    	
    	try {
			FileInputStream fileInput = new FileInputStream( new File("./kie-server-connection.properties") ) ;
			Properties properties = new Properties();
			properties.load(fileInput);
			fileInput.close();
			
			String kieServerUrl = (String)properties.getProperty("kieServerUrl", "http://localhost:9080/kie-server/services/rest/server");
			String userName = (String)properties.getProperty("userName", "bpmsAdmin");
			String userPwd = (String)properties.getProperty("userPwd", "bpmsadmin1#");
			
			KieServicesConfiguration kieServerConfig = KieServicesFactory.newRestConfiguration( kieServerUrl, userName, userPwd);
	    	kieServerConfig.setMarshallingFormat(MarshallingFormat.XSTREAM);
	    	kieServerConfig.setTimeout(300000);
	    	KieServicesClient kieServiceClient = KieServicesFactory.newKieServicesClient(kieServerConfig);
	    	migrationService = kieServiceClient.getServicesClient(ProcessAdminServicesClient.class);
		
		} catch (Exception e) {
			e.printStackTrace();
		}
        
    }


    public void migrateProcessInstance(String sourceContainer, long processInstanceId, String targetContainer, String targetProcessId) {
    	
    	MigrationReportInstance report = migrationService.migrateProcessInstance(sourceContainer, processInstanceId, targetContainer, targetProcessId);
        if( report.isSuccessful() ) {
        	logger.info("Process instance " + processInstanceId + " is migrated successfully to target container " + targetContainer);
        } else {
        	logger.info("Migration failed for process instance " + processInstanceId); 
        }
    }
    
}
