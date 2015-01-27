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

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.testng.annotations.BeforeSuite;

/**
 * Abstract for all tests related to wagon.
 * @author Michael Suzuki
 *
 */
public class AbstractTest
{
    private Log logger = LogFactory.getLog(AbstractTest.class);
    protected static String testLinkURL;
    protected static String devKey;
    private ApplicationContext ctx;
    
    @BeforeSuite(alwaysRun = true)
    public void setupContext() throws Exception
    {
        if(logger.isTraceEnabled())
        {
            logger.trace("Starting test context");
        }
        List<String> contextXMLList = new ArrayList<String>();
        contextXMLList.add("wagon-context.xml");
        ctx = new ClassPathXmlApplicationContext(contextXMLList.toArray(new String[contextXMLList.size()]));
        TestServiceProperties properties =  (TestServiceProperties) ctx.getBean("testProperties");
        testLinkURL = properties.getTestlinkUrl();
        devKey = properties.getKey();
    }
}
