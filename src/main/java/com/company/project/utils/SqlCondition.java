package com.company.project.utils;



import tk.mybatis.mapper.entity.Condition;
/**
 * mybatis条件类生成器.用于批量条件查询
 * @author LiDes
 *2018年5月1日
 */
public class SqlCondition {
	/**
	 * 
	 * @param zlass 对应表的实体类的Class对象
	 * @param str sql的where条件
	 * @return
	 */
	public static Condition getCondition(Class zlass,String str ) {
		Condition condition = new Condition(zlass);
	    condition.createCriteria().andCondition(str);
	    return condition;   
	}
}
