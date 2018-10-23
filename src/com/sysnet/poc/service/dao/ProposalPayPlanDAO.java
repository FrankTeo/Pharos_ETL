package com.sysnet.poc.service.dao;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface ProposalPayPlanDAO {

	public void insertProposalPayPlan(Connection conn, List<Serializable> list) throws SQLException, Exception;
}
