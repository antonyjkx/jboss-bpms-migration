# Upgrading process instances in Kie-server

This is a simple POC to demonstrte how to use PorcessInstanceMigrationService provided in JBoss BPMS 6.4 to upgrade a process instance from one kjar/kie-container to an another kjar/kie-server. 

Note: Please refer [Redhat BPMS documentation](https://access.redhat.com/documentation/en-us/red_hat_jboss_bpm_suite/6.4/html/development_guide/chap_human_tasks_management#process_instance_migration)  before using or modifying to your need. 

To build 
```
mvn clean install
```

Usage 
```
$ java -jar target/bpms-process-upgrade-poc-1.0.0.jar <source-kie-container-id> <process-instance-id> <target-kie-container-id> <process-definition-id> 
```


