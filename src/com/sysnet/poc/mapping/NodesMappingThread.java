package com.sysnet.poc.mapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;

import pharos.ctm.bo.BContractBO;
import pharos.ctm.bo.WContractBO;
import pharos.ctm.exception.CTMException;
import pharos.ctm.vo.BContractNode;

import com.sysnet.poc.control.manage.vo.StopBean;
import com.sysnet.poc.util.OdsLogger;

public class NodesMappingThread extends Thread {

	public NodesMappingThread(StopBean sb, String type, BContractBO bContractBo, WContractBO wContractBo) {
		this.sb = sb;
		this.type = type;
		this.bContractBo = bContractBo;
		this.wContractBo = wContractBo;
	}

	private BContractBO bContractBo;
	private WContractBO wContractBo;

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

	public List<Long> getIdList() {
		return idList;
	}

	public void setIdList(List<Long> idList) {
		this.idList = idList;
	}

	private List<Long> idList;

	private String type;

	private Map<Long, BContractNode> mp;

	private StopBean sb;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Map<Long, BContractNode> getMp() {
		return mp;
	}

	public void setMp(Map<Long, BContractNode> mp) {
		this.mp = mp;
	}

	public StopBean getSb() {
		return sb;
	}

	public void setSb(StopBean sb) {
		this.sb = sb;
	}

	public void run() {
		try {
			if (type.equals("1")) {
				mp = getMapPlc(idList);
			} else if (type.equals("2")) {
				mp = getMapEdr(idList);
			} else if (type.equals("3")) {
				mp = getMapPrp(idList);
			}
		} catch (CTMException e) {
			sb.setAllWriteFlag(false);
			sb.setErrorMessage(e.toString());
			e.printStackTrace();
		} finally {
			sb.addStop();
		}
	}

	private static final Log log = OdsLogger.getLog("insureLog4j", "Insure");

	private Map<Long, BContractNode> getMapPlc(List<Long> idL) throws CTMException {
		Map<Long, BContractNode> mpi = new HashMap<Long, BContractNode>();
		for (int i = 0; i < idL.size(); i++) {
			log.info("node size=" + idL.size() + ",now:" + i);
			mpi.put(idL.get(i), bContractBo.getContractNode(idL.get(i)));
			// mpi.put(idL.get(i), bContractBo.getSingleNode(idL.get(i)));
		}
		return mpi;
	}

	private Map<Long, BContractNode> getMapEdr(List<Long> idL) throws CTMException {
		Map<Long, BContractNode> mpi = new HashMap<Long, BContractNode>();
		for (int i = 0; i < idL.size(); i++) {
			log.info("node size=" + idL.size() + ",now:" + i);
			mpi.put(idL.get(i), wContractBo.getContractNode(idL.get(i)));
			// mpi.put(idL.get(i), wContractBo.getSingleNode(idL.get(i),false));
		}
		return mpi;
	}

	private Map<Long, BContractNode> getMapPrp(List<Long> idL) throws CTMException {
		Map<Long, BContractNode> mpi = new HashMap<Long, BContractNode>();
		for (int i = 0; i < idL.size(); i++) {
			log.info("node size=" + idL.size() + ",now:" + i);
			mpi.put(idL.get(i), wContractBo.getContractNode(idL.get(i), false));
			// mpi.put(idL.get(i), wContractBo.getSingleNode(idL.get(i), false));
		}
		return mpi;
	}
}
