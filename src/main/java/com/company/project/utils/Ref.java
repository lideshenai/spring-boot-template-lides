/**
 * <pre>
 * Copyright (C) 2015-2016 PingYi Tech Inc.All Rights Reserved.
 *
 * Description：
 * 
 * History：
 * 
 * User				Date			Info		Reason
 * Administrator		2016年7月22日		Create		
 * </pre>
 */

package com.company.project.utils;

import java.lang.reflect.Method;

/**   
 * @Description: TODO  
 * @author lichz   
 * @date 2016年7月22日 上午11:22:28
 * 
 */

public class Ref{

    private Object object;
    private Method methodR;
    
    public Ref(Object classR, Method methodR) {
        super();
        this.object = classR;
        this.methodR = methodR;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public Method getMethodR() {
        return methodR;
    }
    public void setMethodR(Method methodR) {
        this.methodR = methodR;
    }
}
