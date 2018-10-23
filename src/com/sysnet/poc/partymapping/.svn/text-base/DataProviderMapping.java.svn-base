package com.sysnet.poc.partymapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import pharos.ctm.vo.BDeclarationFormList;
import pharos.framework.pkgen.PKGenerator;
import pharos.party.bo.PartyQueryBO;
import pharos.party.vo.CorpRelationshipVO;
import pharos.party.vo.PartNameVO;
import pharos.party.vo.PartiesRelationshipVO;
import pharos.party.vo.PartyCertIDVO;
import pharos.party.vo.PartyDeclarationFormList;
import pharos.party.vo.PartyNameVO;
import pharos.party.vo.PartyVO;

import com.sysnet.poc.partymapping.vo.DataProvide;
import com.sysnet.poc.partymapping.vo.MappingDescription;
import com.sysnet.poc.util.PharosServiceContainer;

/**
 * Mar 4, 2011
 * 
 * @author DBC
 */
public class DataProviderMapping {

	public void init(String object_No, MappingDescription md) {
		PharosServiceContainer service = PharosServiceContainer.Instance();
		PartyQueryBO partyQueryBO = service.getPartyQueryBO();
		// Long l = 0L;
		// if (history_no != null && !history_no.equals("")) {
		// l = Long.parseLong(history_no);
		// }
		// PartyVO partyVo = partyQueryBO.getPartyByCodeAndHistoryId(object_No, l);
		PartyVO partyVo = partyQueryBO.getPartybyCode(object_No);
		List<PartyVO> pl = new ArrayList<PartyVO>();
		pl.add(partyVo);
		dataProvider.put("PartyVO", pl);
		dataProvider.put("Declaration", partyVo.getDecls());
		List<PartiesRelationshipVO> partiesRelationshipVOList = partyVo.getPartiesRelationships();
		for (PartiesRelationshipVO vo : partiesRelationshipVOList) {
			if (vo.getId() == 0) {
				vo.setId(PKGenerator.nextPK());
			}
		}
		dataProvider.put("PartiesRelationshipVO", partiesRelationshipVOList);
		List<CorpRelationshipVO> corpRelationshipVOList = partyVo.getCorpRelationships();
		if (corpRelationshipVOList != null && corpRelationshipVOList.size() > 0) {
			Iterator<CorpRelationshipVO> iter = corpRelationshipVOList.iterator();
			while (iter.hasNext()) {
				CorpRelationshipVO cvo = iter.next();
				if (!cvo.getStatus().equals("1")) {
					iter.remove();
				}
			}
		}
		dataProvider.put("CorpRelationshipVO", corpRelationshipVOList);
		List<PartyCertIDVO> partyCertIDVOList = partyVo.getCerts();
		for (PartyCertIDVO partyCertIDVO : partyCertIDVOList) {
			if (partyCertIDVO.getId() == 0) {
				partyCertIDVO.setId(PKGenerator.nextPK());
			}
		}
		dataProvider.put("PartyCertIDVO", partyCertIDVOList);
		List<PartyNameVO> partyNameVOList = partyVo.getNames();
		dataProvider.put("PartyNameVO", partyNameVOList);
		List<PartNameVO> partNameVOList = new ArrayList<PartNameVO>();
		for (PartyNameVO partyNameVO : partyNameVOList) {
			partNameVOList.addAll(partyNameVO.getPartnamelist());
		}
		dataProvider.put("PartNameVO", partNameVOList);
		List<DataProvide> dl = md.getDataProvide();
		for (DataProvide dataProvide : dl) {
			if (dataProvide.getNodeName() != null && !dataProvide.getNodeName().equals("")) {
				if (dataProvide.getProvideType().equals("PARTYVO-CORPRELATIONSHIPVO")) {
					List<PartyVO> cl = new ArrayList<PartyVO>();
					String[] temp = dataProvide.getNodeName().split("/");
					for (CorpRelationshipVO crv : corpRelationshipVOList) {
						if (crv.getRsCode().equals(temp[1])) {
							if (partyVo.getType().equals(temp[0]) || temp[0].equals("")) {
								cl.add(partyVo);
								dataProvider.put(dataProvide.getName(), cl);
								break;
							}
						}
					}
				} else if (dataProvide.getProvideType().equals("PARTYVO-DFL")) {
					PartyDeclarationFormList pdfl = partyVo.getPartyDeclFromListByCode(dataProvide.getNodeName());
					if (pdfl != null) {
						List<BDeclarationFormList> bdfl = pdfl.getDecFormValueList();
						for (BDeclarationFormList bDeclarationFormList : bdfl) {
							bDeclarationFormList.setParent(pdfl.getParent());
							bDeclarationFormList.setDDeclarationFormList(pdfl.getDDeclarationFormList());
						}
						dataProvider.put(dataProvide.getName(), bdfl);
					}
				}
			}
		}
		List<Map<String, String>> globalData = new ArrayList<Map<String, String>>();
		Map<String, String> globalDataMap = new HashMap<String, String>();
		globalDataMap.put("PartyCode", partyVo.getCode());
		globalDataMap.put("versionno", partyVo.getVersionNo());
		globalDataMap.put("ObjectNo", object_No);
		globalDataMap.put("PartyId", partyVo.getId() + "");
		globalData.add(globalDataMap);
		dataProvider.put("GLOBAL", globalData);
	}

	private Map<String, List<?>> dataProvider = new HashMap<String, List<?>>();

	/**
	 * 获取数据提供者List
	 * 
	 * @param name
	 * @return
	 */
	public List<?> getDataProvider(String name) {
		return dataProvider.get(name);
	}

	/**
	 * 获取只有一个的数据提供者
	 * 
	 * @param name
	 * @return
	 */
	public Object getOnlyDataProvider(String name) {
		if (dataProvider.get(name) != null) {
			return dataProvider.get(name).get(0);
		} else {
			return null;
		}
	}
}
