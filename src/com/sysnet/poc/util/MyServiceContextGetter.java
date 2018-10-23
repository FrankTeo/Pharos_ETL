package com.sysnet.poc.util;

import pharos.framework.constparameter.constant.CommonInfo;
import pharos.framework.servicectrl.PharosServiceContextGetter;
import pharos.framework.servicectrl.server.ServiceContext;
import pharos.framework.sysparam.GlobalParameter;

public class MyServiceContextGetter extends PharosServiceContextGetter {
	private static final long serialVersionUID = -1588483625354665705L;

	@Override
	public Object getServiceContext() {
		CommonInfo commonInfo = (CommonInfo) super.getServiceContext();

		if (commonInfo == null || GlobalParameter.isSysUserCommonInfo(commonInfo)) {

			commonInfo = new CommonInfo();
			commonInfo.setUserCode("fhq");
			commonInfo.setUserCompanyCode("2009051910220302537");
			commonInfo.setUserCompanyName("A保险公司总公司");
			commonInfo.setUserCompanyNodeCode("0");
			commonInfo.setUserCompanyNodeCode("2009051910220302538");
			commonInfo.setUserId("2009051910221598902");
			commonInfo.setUserName("付 海强");
			commonInfo.setCurrentCompanyCode("2009051910220302537");
			commonInfo.setCurrentCompanyName("No Name");
			commonInfo.setCurrentCompanyNodeCode("2009051910220302538");
			commonInfo.setChangeLanguage(false);
			commonInfo.setLangCode("2");
			commonInfo.setClientIP("1.1.1.1");
			commonInfo.setDactivityId("0");
			commonInfo.setDateFormat("yyyy-MM-dd");
			commonInfo.setIsTestWF(false);
			commonInfo.setLangCode4Local("1");
			commonInfo.setLocaleName("zh");
			commonInfo.setLocaleName4Local("en");
			commonInfo.setLogined(true);
			commonInfo.setLoginIp("0");
			commonInfo.setNumberFormat("#,##0.00");
			commonInfo.setOffsetWorkingHours(0);
			commonInfo.setRefreshSeconds(0);
			commonInfo.setRelationCode("2");
			commonInfo.setServerIp("0");
			commonInfo.setThirdPartyCode("940");
			commonInfo.setTimeFormat("HH:mm:ss");
		}
		ServiceContext.getInstance().setCommonInfo(commonInfo);
		return commonInfo;
	}

}
