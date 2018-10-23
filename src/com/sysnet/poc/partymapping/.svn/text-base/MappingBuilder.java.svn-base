package com.sysnet.poc.partymapping;

import java.util.List;
import java.util.Map;
import pharos.party.vo.PartyVO;
import com.sysnet.poc.partymapping.vo.MappingDescription;
import com.sysnet.poc.service.storage.SColumn;

public interface MappingBuilder {

	public void setMapDocument(MappingDescription mapDesc);

	public void setPartyVo(PartyVO partyVo);

	public void setGlobalDataMap(Map<String, String> globalDataMap);

	public boolean execute(String key) throws Exception;

	public List<SColumn> getResult();

}
