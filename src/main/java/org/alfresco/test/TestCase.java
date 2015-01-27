/*
 * Copyright (C) 2005-2015 Alfresco Software Limited.
 * This file is part of Alfresco
 * Alfresco is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * Alfresco is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License
 * along with Alfresco. If not, see <http://www.gnu.org/licenses/>.
 */
package org.alfresco.test;

import java.io.Serializable;

/**
 * Pojo that holds meta information on test case.
 * 
 * @author Michael Suzuki
 *
 */
public class TestCase implements Serializable
{
    /**
     * Generated.
     */
    private static final long serialVersionUID = 5617092392054522763L;
    private final String id;
    private String testClass;
    private String methodName;
    
    public TestCase(final String id, final String path)
    {
        this.id = id;
        this.testClass = path;
    }

    public String getTestClass()
    {
        return testClass;
    }

    public void setTestClass(String testClass)
    {
        this.testClass = testClass;
    }

    public String getId()
    {
        return id;
    }
    
    public String getClassName()
    {
        return testClass.substring((testClass.lastIndexOf(".")+1)); 
    }
    
    public String getMethodName()
    {
        return methodName;
    }
    public void setMethodName(final String methodName)
    {
        this.methodName = methodName;
    }
}
