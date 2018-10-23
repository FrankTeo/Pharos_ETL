package com.sysnet.poc.service.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.sysnet.poc.control.manage.vo.ConfigureBean;
import com.sysnet.poc.control.manage.vo.StatusBean;

public interface ClaimSchduleDAO {
	/**
	 * 设置pharos连接
	 * 
	 * @param conn
	 */
	public void setPharosConn(Connection conn);

	/**
	 * 设置ods连接
	 * 
	 * @param conn
	 */
	public void setOdsConn(Connection conn);

	/**
	 * 是否有新的增量产生
	 * 
	 * @return
	 */
	public boolean isCrateIncrement() throws SQLException, Exception;

	/**
	 * 给每条增量付予批次号
	 * 
	 * @param batId
	 */
	public void updateBatId(Long objectid, int batId) throws SQLException, Exception;

	/**
	 * 根就批次号返回产生的增量对象
	 * 
	 * @param batId
	 *            批次号
	 * @return
	 */
	public List incrementListByBatID(int batId) throws SQLException, Exception;

	/**
	 * 未处理增量
	 * 
	 * @return
	 * @throws SQLException
	 * @throws Exception
	 */
	public List<Long> incrementList(int count) throws SQLException, Exception;

	/**
	 * 回写增量表的登场区处理状态
	 */
	public void updateStatus(int objectId, int batId) throws SQLException, Exception;

	/**
	 * 查询批次未处理增量的数量
	 * 
	 * @param batId
	 * @return
	 * @throws SQLException
	 * @throws Exception
	 */
	public int incrementCount(int batId) throws SQLException, Exception;

	/**
	 * 加载当前时间段的配置信息
	 * 
	 * @return
	 * @throws SQLException
	 * @throws Exception
	 */
	public ConfigureBean initConfig() throws SQLException, Exception;

	/**
	 * 加载当前批次信息
	 * 
	 * @return
	 * @throws SQLException
	 * @throws Exception
	 */
	public StatusBean initStatus() throws SQLException, Exception;

	/**
	 * 设置当前批次的登场区处理状态
	 * 
	 * @param batId
	 *            批次号
	 * @param status
	 *            状态
	 * @throws SQLException
	 * @throws Exception
	 */
	public void updateStageStatus(int batId, String status) throws SQLException, Exception;

	/**
	 * 创建一个批次
	 * 
	 * @param configureBean
	 * @param statusBean
	 * @return
	 * @throws SQLException
	 * @throws Exception
	 */
	public void createStatus(int batId, int recordCount) throws SQLException, Exception;
}
