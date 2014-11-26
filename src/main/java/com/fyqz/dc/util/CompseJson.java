package com.fyqz.dc.util;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>description: [根据已有构造新的对象，并去掉不需要的属性,用于对象转换为json时调用 ]</p>
 * <p>editTime： 2014-3-26下午3:34:46</p>
 * <p>modifier: [修改人]</p>
 * @author: [李伟东] 
 */
public class CompseJson {

	/**
	 * <p>description: [根据已有构造新的对象，并去掉不需要的属性]</p>
	 * <p>editTime： 2014-3-26下午3:34:46</p>
	 * <p>modifier: [修改人]</p>
	 * @author: [李伟东] 
	 * @param className  对象对应的Class全路径名。
	 * @param obj 传入的对象
	 * @param excludeFields  不需要的属性名
	 * @return
	 * @throws Exception
	 */
	public static Map<String, Object> getObjMap(String className, Object obj, String[] excludeFields)
			throws Exception {

		if (className == null || className.equals("")) {
			throw new Exception("className is inValid!");
		}
		if (null == obj) {
			throw new Exception("input objce is null");
		}
		Map<String, Object> newObj = new HashMap<String, Object>();
		Class<?> class_ = Class.forName(className);
		Field[] fs = class_.getDeclaredFields();
		for (Field field : fs) {
			field.setAccessible(true);
			String fname = field.getName();
			Object val = field.get(obj);
			if (null != excludeFields) {
				int flag = 0;
				for (String fName_ : excludeFields) {
					if (fName_.equals(fname))
						flag = 1;
				}
				if (flag == 0)
					newObj.put(fname, val);
			}
		}
		return newObj;
	}
}
