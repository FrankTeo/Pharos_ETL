package com.sysnet.poc.claimmapping.vo;

import java.util.HashMap;
import java.util.Map;

import com.sysnet.poc.claimmapping.action.AbsClaimAction;
import com.sysnet.poc.claimmapping.action.DFLAction;
import com.sysnet.poc.claimmapping.action.GLOBALAction;
import com.sysnet.poc.claimmapping.action.MappingAction;
import com.sysnet.poc.claimmapping.action.ObjectAction;

public class ActionMapCatch {
	// private static Map<String, MappingAction> actionMapCatch = new HashMap<String, MappingAction>();
	// static {
	// actionMapCatch.put("DEFAULTDATAPROVIDER", new AbsClaimAction());
	// actionMapCatch.put("GLOBAL", new GLOBALAction());
	// actionMapCatch.put("DECLARATIONFORMLIST", new DFLAction());
	// }
	//
	// public static Map<String, MappingAction> initAction(String id) throws Exception {
	// Map<String, MappingAction> actionMap = new HashMap<String, MappingAction>();
	// if (!actionMapCatch.containsKey("OBJECT" + id)) {// 如果Map中有这个对象，那么就直接取出来
	// String packagePath = "mappingforclaim.p" + id + ".";
	// ObjectAction oa = new ObjectAction();
	// ActionPlus a = null;
	// try {
	// a = (ActionPlus) Class.forName(packagePath + "ActionPlusImp").newInstance();
	// } catch (ClassNotFoundException e) {
	// a = new ActionPlus();
	// }
	// oa.setAp(a);
	// actionMapCatch.put("OBJECT" + id, oa);
	// }
	// actionMap.put("OBJECT", actionMapCatch.get("OBJECT" + id));
	// actionMap.put("DEFAULTDATAPROVIDER", actionMapCatch.get("DEFAULTDATAPROVIDER"));
	// actionMap.put("GLOBAL", actionMapCatch.get("GLOBAL"));
	// actionMap.put("DECLARATIONFORMLIST", actionMapCatch.get("DECLARATIONFORMLIST"));
	// return actionMap;
	// }

	private Map<String, MappingAction> actionMapCatch = new HashMap<String, MappingAction>();
	{
		actionMapCatch.put("DEFAULTDATAPROVIDER", new AbsClaimAction());
		actionMapCatch.put("GLOBAL", new GLOBALAction());
		actionMapCatch.put("DECLARATIONFORMLIST", new DFLAction());
	}

	public Map<String, MappingAction> initAction(String id) throws Exception {
		Map<String, MappingAction> actionMap = new HashMap<String, MappingAction>();
		if (!actionMapCatch.containsKey("OBJECT" + id)) {// 如果Map中有这个对象，那么就直接取出来
			String packagePath = "mappingforclaim.p" + id + ".";
			ObjectAction oa = new ObjectAction();
			ActionPlus a = null;
			try {
				a = (ActionPlus) Class.forName(packagePath + "ActionPlusImp").newInstance();
			} catch (ClassNotFoundException e) {
				a = new ActionPlus();
			}
			oa.setAp(a);
			actionMapCatch.put("OBJECT" + id, oa);
		}
		actionMap.put("OBJECT", actionMapCatch.get("OBJECT" + id));
		actionMap.put("DEFAULTDATAPROVIDER", actionMapCatch.get("DEFAULTDATAPROVIDER"));
		actionMap.put("GLOBAL", actionMapCatch.get("GLOBAL"));
		actionMap.put("DECLARATIONFORMLIST", actionMapCatch.get("DECLARATIONFORMLIST"));
		return actionMap;
	}

}
