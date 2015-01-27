/*
 * Copyright (C) 2005-2014 Alfresco Software Limited.
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.alfresco.test.AbstractTest;
import org.testng.Assert;
import org.testng.annotations.Test;

import br.eti.kinoshita.testlinkjavaapi.model.TestCase;
import br.eti.kinoshita.testlinkjavaapi.model.TestPlan;
import br.eti.kinoshita.testlinkjavaapi.model.TestProject;
import br.eti.kinoshita.testlinkjavaapi.util.TestLinkAPIException;
/**
 * Integration test that ensures TestLinkService works.
 * @author Michael Suzuki
 *
 */
public class TestLinkServiceTest extends AbstractTest
{
//    private String testplan = "Ent5.0-AutomationFullRegression-v4";
    private String testplan = "Ent5.0-SanityTests-v1";
    private String testcase = "AONE-14106";
    private String project = "AlfrescoOne";
    
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void connectNullUrl() throws TestLinkAPIException, MalformedURLException
    {
        new TestLinkServiceImpl(null, devKey);
    }
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void connectNullKey() throws TestLinkAPIException, MalformedURLException
    {
        new TestLinkServiceImpl(testLinkURL, null);
    }
    @Test
    public void connect() throws TestLinkAPIException, MalformedURLException
    {
        TestRepositoryService service = new TestLinkServiceImpl(testLinkURL, devKey);
        Assert.assertNotNull(service);
    }
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void getTestProjectNull() throws TestLinkAPIException, MalformedURLException
    {
        TestRepositoryService service = new TestLinkServiceImpl(testLinkURL, devKey);
        service.getTestProject(null);
    }
    @Test
    public void getTestProject() throws TestLinkAPIException, MalformedURLException
    {
        TestRepositoryService service = new TestLinkServiceImpl(testLinkURL, devKey);
        TestProject testProject = service.getTestProject(project);
        Assert.assertNotNull(testProject);
    }
    @Test
    public void getTestCase() throws TestLinkAPIException, MalformedURLException
    {
        TestRepositoryService service = new TestLinkServiceImpl(testLinkURL, devKey);
        TestCase testCase = service.getTestCase(testcase);
        Assert.assertNotNull(testCase);
    }
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void getTestCaseNull() throws TestLinkAPIException, MalformedURLException
    {
        TestRepositoryService service = new TestLinkServiceImpl(testLinkURL, devKey);
        service.getTestCase(null);
    }
    @Test
    public void getTestPlan() throws TestLinkAPIException, MalformedURLException
    {
        TestRepositoryService service = new TestLinkServiceImpl(testLinkURL, devKey);
        TestPlan testPlan = service.getTestPlan(testplan, project);
        Assert.assertNotNull(testPlan);
    }
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void getTestPlanNull() throws TestLinkAPIException, MalformedURLException
    {
        TestRepositoryService service = new TestLinkServiceImpl(testLinkURL, devKey);
        service.getTestPlan(null, project);
    }
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void getTestPlanNullProject() throws TestLinkAPIException, MalformedURLException
    {
        TestRepositoryService service = new TestLinkServiceImpl(testLinkURL, devKey);
        service.getTestPlan(testplan, null);
    }
    @Test(expectedExceptions = TestLinkAPIException.class)
    public void getInvalidTestCases() throws TestLinkAPIException, MalformedURLException
    {
        TestRepositoryService service = new TestLinkServiceImpl(testLinkURL, devKey);
        Assert.assertNull(service.getTestCases(-1));
    }
    @Test
    public void getTestCasesById() throws TestLinkAPIException, MalformedURLException
    {
        TestLinkServiceImpl service = new TestLinkServiceImpl(testLinkURL, devKey);
        TestPlan testPlan = service.getTestPlan(testplan, project);
        List<String> testcases = service.getTestCases(testPlan.getId());
        Assert.assertNotNull(testcases);
    }
    @Test
    public void getTestCasesByName() throws TestLinkAPIException, MalformedURLException
    {
        TestLinkServiceImpl service = new TestLinkServiceImpl(testLinkURL, devKey);
        List<String> testcases = service.getTestCases(testplan);
        Assert.assertNotNull(testcases);
    }
    @Test
    public void parseEmptyTestCases() throws TestLinkAPIException, MalformedURLException
    {
        TestLinkServiceImpl service = new TestLinkServiceImpl(testLinkURL, devKey);
        List<String> list = service.parseTestCaseId(null);
        Assert.assertEquals(list, Collections.<String>emptyList());
        List<String> list2 = service.parseTestCaseId(Collections.<TestCase>emptyList());
        Assert.assertEquals(list2, Collections.<String>emptyList());
    }
    @Test
    public void parseTestCases() throws TestLinkAPIException, MalformedURLException
    {
        TestLinkServiceImpl service = new TestLinkServiceImpl(testLinkURL, devKey);
        TestCase testcase = new TestCase();
        testcase.setFullExternalId("ANO-1");
        List<TestCase> testcases = new ArrayList<TestCase>();
        testcases.add(testcase);
        List<String> list = service.parseTestCaseId(testcases);
        Assert.assertEquals(1, list.size());
        Assert.assertEquals("ANO-1",list.get(0).toString());
    }
}
