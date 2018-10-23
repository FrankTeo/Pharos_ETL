package com.sysnet.poc.mapping;

import java.util.List;
import java.util.Map;

import pharos.ctm.vo.BContractNode;

import com.sysnet.poc.mapping.vo.MappingDescription;

public interface MappingBuilder {

	public void setMapDocument(MappingDescription mapDesc);

	public void setBContractNode(BContractNode bContractNode);

	// public void setBContract(BContract bContract);

	public void setGlobalDataMap(Map<String, String> globalDataMap);

	public boolean execute() throws Exception;

	@SuppressWarnings("rawtypes")
	public List getResult();

}
