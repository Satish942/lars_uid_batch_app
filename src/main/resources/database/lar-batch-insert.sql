
DELETE from LAR_BATCH_CONFIG;
Insert into LAR_BATCH_CONFIG (ID,CTL_TYPE,FILE_COUNTER,LAST_UPDATE_DT) values (1,'DAILY_SRV_LIST',1,to_date(sysdate,'YYYY-MM-DD'));
Insert into LAR_BATCH_CONFIG (ID,CTL_TYPE,FILE_COUNTER,LAST_UPDATE_DT) values (2,'MARK_PR_EXTRACT',1,to_date(sysdate,'YYYY-MM-DD'));
commit;