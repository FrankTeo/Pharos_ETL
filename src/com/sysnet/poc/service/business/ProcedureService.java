package com.sysnet.poc.service.business;

import java.util.List;
import java.util.TimerTask;

import org.apache.log4j.Logger;

import com.sysnet.poc.util.PharosServiceContainer;

import pharos.framework.health.bo.DProcedureGroupBO;
import pharos.framework.health.vo.ProcedureDetail;
import pharos.framework.health.vo.ProcedureGroup;
import pharos.framework.health.vo.ProcedureGroupQO;

/**
 * 
 * 收费项目定时执行类
 * 
 * @author liyanpeng
 * 
 */
public class ProcedureService extends TimerTask {

	private Logger log = Logger.getLogger(ProcedureService.class);

	private DProcedureGroupBO dProcedureGroupBO = null;// 收费项目分组

	private void init() {
		this.dProcedureGroupBO = PharosServiceContainer.Instance().getDProcedureGroupBO();
	}

	@Override
	public void run() {

		init();
		importDProcedureGroupInfoAndProcedureDetailInfo();

	}

	/**
	 * 收费项目分组信息获取,明细信息获取
	 * 
	 * @return
	 */
	private boolean importDProcedureGroupInfoAndProcedureDetailInfo() {
		boolean rs = false;
		ProcedureGroupQO filter = new ProcedureGroupQO();
		List<ProcedureGroup> pList = this.dProcedureGroupBO.filterProcedureGroups(filter);
		for (ProcedureGroup procedureGroup : pList) {

			procedureGroup.getCode();
			log.info("procedureGroup.getCode()============" + procedureGroup.getCode());

			procedureGroup.getName();
			log.info("procedureGroup.getName(2)============" + procedureGroup.getName("2"));

			procedureGroup.getType();
			log.info("procedureGroup.getType()============" + procedureGroup.getType());

			procedureGroup.getCreateDate();
			log.info("procedureGroup.getCreateDate()============" + procedureGroup.getCreateDate());

			List<ProcedureDetail> dList = procedureGroup.getProcedureObjects();

			for (ProcedureDetail procedureDetail : dList) {

				log.info("procedureDetail.getId()============" + procedureDetail.getId());

				log.info("procedureDetail.getCopayment().getAmount()============" + procedureDetail.getCopayment().getAmount());

				log.info("procedureDetail.getDeductibleFixedAmount().getAmount()============" + procedureDetail.getDeductibleFixedAmount().getAmount());

				log.info("procedureDetail.getDeductiblePercentage()============" + procedureDetail.getDeductiblePercentage());

				log.info("procedureDetail.getProcedure().getId()============" + procedureDetail.getProcedure().getId());

			}

		}
		return rs;

	}

}
