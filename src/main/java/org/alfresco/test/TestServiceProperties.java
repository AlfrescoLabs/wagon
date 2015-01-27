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
/**
 * Properties class that contains values used in running Alfresco tests.
 * @author Michael Suzuki
 *
 */
public class TestServiceProperties
{

    String testlinkUrl;
    String key;
    public TestServiceProperties(final String url, final String key)
    {
        this.testlinkUrl = url;
        this.key = key;
    }
    public String getTestlinkUrl()
    {
        return testlinkUrl;
    }
    public void setTestlinkUrl(String testlinkUrl)
    {
        this.testlinkUrl = testlinkUrl;
    }
    public String getKey()
    {
        return key;
    }
    public void setKey(String key)
    {
        this.key = key;
    }
    
}
