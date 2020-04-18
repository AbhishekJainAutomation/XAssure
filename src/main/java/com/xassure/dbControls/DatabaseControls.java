package com.xassure.dbControls;

import java.util.List;
import java.util.Map;

public interface DatabaseControls {

	public List readAllDataFromTable(String tableName);

	public List readAllDataFromTableUsingCriterion(String tableName);

	public List readAllDataFromTableByConditions(String tableName, Map<String, Object> columnNameAndValueMap);

	public List readDataFromTableByPrimaryKey(String tableName, String primaryColumnName, String primaryKeyValue);

	public void updateDB(String tableName, Map<String, Object> updateColValues, Map<String, Object> updateCondition);

	public void insertIntoDB(Object classObject);

}
