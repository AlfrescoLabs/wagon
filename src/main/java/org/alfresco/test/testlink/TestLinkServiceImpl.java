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
package org.alfresco.test.testlink;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import br.eti.kinoshita.testlinkjavaapi.TestLinkAPI;
import br.eti.kinoshita.testlinkjavaapi.constants.ExecutionType;
import br.eti.kinoshita.testlinkjavaapi.constants.TestCaseDetails;
import br.eti.kinoshita.testlinkjavaapi.model.TestCase;
import br.eti.kinoshita.testlinkjavaapi.model.TestPlan;
import br.eti.kinoshita.testlinkjavaapi.model.TestProject;
import br.eti.kinoshita.testlinkjavaapi.util.TestLinkAPIException;
/**
 * Service that interacts with Alfresco test link.
 * Test Link is the repository for alfresco regression test data.
 * This class provide the list of tests that needs to be executed
 * and is also able to update records in test link using the TestLink
 * API.
 * @author Michael Suzuki
 *
 */
public class TestLinkServiceImpl implements TestRepositoryService
{
 
    private static final String DEFAULT_PROJECT = "AlfrescoOne";
    private final TestLinkAPI testlinkAPIClient;
    
    /**
     * Constructor.
     * @param testLinkURL URL of testlink service
     * @param devKey API devKey
     * @throws TestLinkAPIException if error
     * @throws MalformedURLException if incorrect url
     */
    public TestLinkServiceImpl(final String testLinkURL, final String devKey)
    {
        if(StringUtils.isEmpty(testLinkURL))
        {
            throw new IllegalArgumentException("TestLink url is required");
        }
        if(StringUtils.isEmpty(devKey))
        {
            throw new IllegalArgumentException("TestLink devKey is required");
        }
        try
        {
            testlinkAPIClient = new TestLinkAPI(new URL(testLinkURL), devKey);
        } 
        catch (TestLinkAPIException apie)
        {
            throw new RuntimeException("Unable to instantiate TestLinkServiceImpl, error with testlink", apie);
        } 
        catch (MalformedURLException urle)
        {
            throw new RuntimeException("Unable to instantiate TestLinkServiceImpl, testlink url error", urle);
        }
    }
    /* (non-Javadoc)
     * @see org.alfresco.testlink.TestService#getTestProject(java.lang.String)
     */
    public TestProject getTestProject(final String name)
    {
        if(StringUtils.isEmpty(name))
        {
            throw new IllegalArgumentException("TestLink project name is required");
        }
        return testlinkAPIClient.getTestProjectByName(name);
    }
    /* (non-Javadoc)
     * @see org.alfresco.testlink.TestService#getTestCase(java.lang.String)
     */
    public TestCase getTestCase(final String id)
    {
        if(StringUtils.isEmpty(id))
        {
            throw new IllegalArgumentException("TestLink testcase name is required");
        }
        return testlinkAPIClient.getTestCaseByExternalId(id, 1);
    }
    public List<String> getTestCases(String testPlan)
    {
        TestPlan plan = getTestPlan(testPlan, DEFAULT_PROJECT);
        return getTestCases(plan.getId());
    }
    
    /* (non-Javadoc)
     * @see org.alfresco.testlink.TestService#getTestCases(int)
     */
    public List<String> getTestCases(final int testPlanID) 
    { 
        TestCase[] testcases = testlinkAPIClient.getTestCasesForTestPlan(testPlanID, null, null, null, null,
                null, null, null, ExecutionType.AUTOMATED, true, TestCaseDetails.FULL);
        return parseTestCaseId(Arrays.asList(testcases));
    }
    /* (non-Javadoc)
     * @see org.alfresco.testlink.TestService#getTestPlan(java.lang.String, java.lang.String)
     */
    public TestPlan getTestPlan(final String testplanId, final String projectId)
    {
        if(StringUtils.isEmpty(testplanId))
        {
            throw new IllegalArgumentException("TestLink test plan name is required");
        }
        if(StringUtils.isEmpty(projectId))
        {
            throw new IllegalArgumentException("TestLink project plan name is required");
        }
        return testlinkAPIClient.getTestPlanByName(testplanId, projectId);
    }
    /**
     * Extracts the test case identifier from the test case object.
     * The externalIds param is used by Alfresco to identify the test case 
     * and not the id, hence we are using externalIds.
     *  
     * @param testcases String id
     * @return List<String> test case identifiers
     */
    public List<String> parseTestCaseId(List<TestCase> testcases)
    {
        if(testcases != null && !testcases.isEmpty())
        {
            List<String> testcaseNames = new ArrayList<String>(); 
            for (TestCase testCase : testcases) 
            { 
                testcaseNames.add(testCase.getFullExternalId()); 
            }
            return testcaseNames; 
        }
        return Collections.<String>emptyList();
    }
}
