CREATE OR REPLACE PACKAGE BODY rmnode AS
  --更新T_policy表
  PROCEDURE removenode (v_objectid IN VARCHAR2) IS

  BEGIN
  delete from T_SubjAgri where objectno = v_objectid;
  delete from T_SubjProfitLoss where objectno = v_objectid;
  delete from T_ReExpensive where objectno = v_objectid;
  delete from T_SubjShip where objectno = v_objectid;
  delete from T_SubjFreight where objectno = v_objectid;
  delete from T_PersonList where objectno = v_objectid;
  delete from T_SubjDuty where objectno = v_objectid;
  delete from T_SubjProductDuty where objectno = v_objectid;
  delete from T_PropertyList where objectno = v_objectid;
  delete from T_ProjectParty where objectno = v_objectid;
  delete from T_AcquisitionCost where objectno = v_objectid;
  delete from T_PolicyUnderWrite where objectno = v_objectid;
  delete from T_SubjEmployerDuty where objectno = v_objectid;
  delete from T_PolicyAgency where objectno = v_objectid;
  delete from T_JoInsurance where objectno = v_objectid;
  delete from T_Liability where objectno = v_objectid;
  delete from T_SubjProject where objectno = v_objectid;
  delete from T_AcquisitionCostDetail where objectno = v_objectid;
  delete from T_PolicyApplicant where objectno = v_objectid;
  delete from T_SubjTrafficDuty where objectno = v_objectid;
  delete from T_SubjAircraftDuty where objectno = v_objectid;
  delete from T_Policy where objectno = v_objectid;
  delete from T_PayPlan where objectno = v_objectid;
  delete from T_ReinsureInfo where objectno = v_objectid;
  delete from T_Node where objectno = v_objectid;
  delete from T_SubjGuarantee where objectno = v_objectid;
  delete from T_PolicyContact where objectno = v_objectid;
  delete from T_Endorse where objectno = v_objectid;
  delete from T_SubjEconomic where objectno = v_objectid;
  delete from T_PolicyBeneficiary where objectno = v_objectid;
  delete from T_SubjMarketDuty where objectno = v_objectid;
  delete from T_PolicyInsure where objectno = v_objectid;
  delete from T_SubjEnterprise where objectno = v_objectid;
  delete from T_SubjFreightForward where objectno = v_objectid;
  delete from T_CoInsurance where objectno = v_objectid;
  delete from T_SubjMedLiab where objectno = v_objectid;
  delete from T_Subject where objectno = v_objectid;
  delete from T_Contract where objectno = v_objectid;
  END;
  
  PROCEDURE removeproposal(v_objectid IN VARCHAR2) IS
  BEGIN
  delete from T_Proposal where objectno = v_objectid;
  END;
  
  PROCEDURE removequotation(v_objectid IN VARCHAR2) IS
  BEGIN
  delete from t_Quotation where objectno = v_objectid;
  END;

END rmnode;
