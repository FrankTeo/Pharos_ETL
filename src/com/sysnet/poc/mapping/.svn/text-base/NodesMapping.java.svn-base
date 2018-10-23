package com.sysnet.poc.mapping;

import java.util.ArrayList;
import java.util.List;

import pharos.ctm.bo.BContractBO;
import pharos.ctm.bo.WContractBO;

import com.sysnet.poc.util.ResourceBundleUtil;

public class NodesMapping {
	private List<List<Long>> lll = new ArrayList<List<Long>>();
	private BContractBO bContractBo;
	private WContractBO wContractBo;

	public List<List<Long>> getLll() {
		return lll;
	}

	public void setLll(List<List<Long>> lll) {
		this.lll = lll;
	}

	public BContractBO getbContractBo() {
		return bContractBo;
	}

	public void setbContractBo(BContractBO bContractBo) {
		this.bContractBo = bContractBo;
	}

	public WContractBO getwContractBo() {
		return wContractBo;
	}

	public void setwContractBo(WContractBO wContractBo) {
		this.wContractBo = wContractBo;
	}

	final static int perThreadNode = 20;

	public List<List<Long>> getMap() throws Exception {
		// public Map<Long, BContractNode> getMap() throws Exception {
		int maxThread = Integer.parseInt(ResourceBundleUtil.readValue("nonRiskNodeThreads"));
		// 线程总数
		int threadCount = (idList.size() / perThreadNode) + 1;
		// if (threadCount == 0) {
		// threadCount = 1;
		// } else
		if (threadCount > maxThread) {
			threadCount = maxThread;
		}
		for (int i = 0; i < threadCount; i++) {
			lll.add(new ArrayList<Long>());
		}

		for (int i = 0; i < idList.size(); i++) {
			lll.get(i % threadCount).add(idList.get(i));
		}
		// //单线程
		// if (threadCount == 1) {
		// NodesMappingThread nmt = new NodesMappingThread(sb, type, bContractBo, wContractBo);
		// nmt.setIdList(lll.get(0));
		// nmt.run();
		// tl.add(nmt);
		// } else {
		// //多线程
		// for (List<Long> ll : lll) {
		// NodesMappingThread nmt = new NodesMappingThread(sb, type, bContractBo, wContractBo);
		// nmt.setIdList(ll);
		// nmt.start();
		// tl.add(nmt);
		// }
		// while (sb.getStop() != threadCount) {
		// Thread.sleep(threadCount * 100);
		// log.debug("NodesMapping.sleep");
		// }
		// }
		//
		// if (sb.getAllWriteFlag()) {
		// for (NodesMappingThread nodesMappingThread : tl) {
		// nodeMap.putAll(nodesMappingThread.getMp());
		// }
		// return nodeMap;
		// } else {
		// throw new Exception(sb.getErrorMessage());
		// }
		return lll;
	}

	public NodesMapping(String type, List<Long> idList, BContractBO bContractBo, WContractBO wContractBo) {
		this.type = type;
		this.idList = idList;
		this.bContractBo = bContractBo;
		this.wContractBo = wContractBo;
	}

	private String type;
	private List<Long> idList;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<Long> getIdList() {
		return idList;
	}

	public void setIdList(List<Long> idList) {
		this.idList = idList;
	}
}
