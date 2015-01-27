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
package org.alfresco.test.mock;

import org.alfresco.test.AlfrescoTest;
import org.alfresco.test.TestServiceTest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.testng.Assert;
import org.testng.annotations.Test;
/**
 * Sample dummy test used by {@link TestServiceTest} test to validate
 * the {@link AlfrescoTest} annotation works correctly. The {@link TestServiceTest}
 * scans for all tests annotated with AlfrescoTest, this dummy test class
 * is then checked to verify it has been loaded.
 * 
 * @author Michael Suzuki
 *
 */
@AlfrescoTest(testlink="AONE-11")
public class DummyTest
{
    private static Log logger = LogFactory.getLog(DummyTest.class);
    @AlfrescoTest(id="4", testlink="AONE-123")
    @Test
    public void helloWorld()
    {
        logger.info("Running DummyTest hello world");
        Assert.assertTrue(true);
    }
    
    @AlfrescoTest(id="41", jira="123", testlink="AONE-1231")
    @Test()
    public void howAreYou()
    {
        logger.info("Running DummyTest how are you");
        Assert.assertTrue(true);
        
    }
    
    @AlfrescoTest(id="5", testlink="AONE-6")
    @Test(dependsOnMethods="howAreYou", ignoreMissingDependencies=true)
    public void bye()
    {
        logger.info("Running DummyTest bye");
        Assert.assertTrue(true);
    }

}