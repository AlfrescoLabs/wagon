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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.alfresco.test.testlink.TestLinkServiceImpl;
import org.alfresco.test.testlink.TestRepositoryService;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.w3c.dom.Document;

import br.eti.kinoshita.testlinkjavaapi.util.TestLinkAPIException;
/**
 * Integration test that ensures TestService is able to perform CRUD
 * operations, interact with test repository and generate the test run from
 * the AlfrescoTest annotation.
 * 
 * @author Michael Suzuki
 *
 */
public class TestServiceTest extends AbstractTest
{
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void connectNullUrl()
    {
        new TestServiceImpl(null);
    }
    @Test
    public void createTestService()
    {
        TestRepositoryService testlink = new TestLinkServiceImpl(testLinkURL, devKey);
        TestService service = new TestServiceImpl(testlink);
        Assert.assertNotNull(service);
    }
    @Test(expectedExceptions = TestLinkAPIException.class)
    public void getTestCasesNull()
    {
        TestRepositoryService testlink = new TestLinkServiceImpl(testLinkURL, devKey);
        TestService service = new TestServiceImpl(testlink);
        List<String> testcases = service.getTestCases(-1);
        Assert.assertNotNull(testcases);
    }
    @Test
    public void getTestCasesById()
    {
        TestRepositoryService testlink = new TestLinkServiceImpl(testLinkURL, devKey);
        TestService service = new TestServiceImpl(testlink);
        List<String> testcases = service.getTestCases(628054);
        Assert.assertNotNull(testcases);
    }
    @Test
    public void getTestCasesByName()
    {
        TestRepositoryService testlink = new TestLinkServiceImpl(testLinkURL, devKey);
        TestService service = new TestServiceImpl(testlink);
        List<String> testcases = service.getTestCases("Ent5.0-SanityTests-v1");
        Assert.assertNotNull(testcases);
    }
    @Test
    public void mapTest() throws TestLinkAPIException, ClassNotFoundException, IOException
    {
        TestRepositoryService testlink = new TestLinkServiceImpl(testLinkURL, devKey);
        Map<String, TestCase>tests = new TestServiceImpl(testlink).getTestData();
       
        String id = "AONE-123";
        String id2 = "AONE-11";
        String id3 = "AONE-22";
        Assert.assertTrue(tests.containsKey(id));
        Assert.assertNotNull(tests.get(id));
        Assert.assertTrue(tests.containsKey(id2));
        Assert.assertNotNull(tests.get(id));
        Assert.assertNotNull(tests.get(id));
        Assert.assertTrue(tests.containsKey(id3));
    }
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void mapInavlidNullTest()
    {
        TestRepositoryService testlink = new TestLinkServiceImpl(testLinkURL, devKey);
        TestService service = new TestServiceImpl(testlink);
        service.matchTests(null);
    }
    @Test
    public void mapInavlidTest()
    {
        TestRepositoryService testlink = new TestLinkServiceImpl(testLinkURL, devKey);
        TestService service = new TestServiceImpl(testlink);
        List<String> tests = new ArrayList<String>();
        tests.add("invalid-test-123");
        List<TestCase> result = service.matchTests(tests);
        Assert.assertNotNull(result);
        Assert.assertEquals(result.size(), 0);
    }
    @Test
    public void mapTests()
    {
        TestRepositoryService testlink = new TestLinkServiceImpl(testLinkURL, devKey);
        TestService service = new TestServiceImpl(testlink);
        List<String> tests = new ArrayList<String>();
        tests.add("AONE-11");
        tests.add("AONE-22");
        tests.add("AONE-123");
        tests.add("AONE-NOTUSED");
        
        List<TestCase> result = service.matchTests(tests);
        Assert.assertNotNull(result);
        TestCase testcase = result.get(0);
        Assert.assertTrue(testcase.getTestClass().endsWith("org.alfresco.test.mock.DummyTest"));
        Assert.assertEquals("AONE-11", testcase.getId());
        
        testcase = result.get(1);
        Assert.assertTrue(testcase.getTestClass().endsWith("org.alfresco.test.mock.DummyTest2"));
        Assert.assertEquals("AONE-22", testcase.getId());
        
        testcase = result.get(2);
        Assert.assertTrue(testcase.getTestClass().endsWith("org.alfresco.test.mock.DummyTest"));
        Assert.assertEquals("AONE-123", testcase.getId());
        Assert.assertEquals(result.size(), 3);
    }
    
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void buildTestngWithNull()
    {
        TestRepositoryService testlink = new TestLinkServiceImpl(testLinkURL, devKey);
        TestService service = new TestServiceImpl(testlink);
        service.buildTestng(null);
    }
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void buildTestngWithEmptyCollection()
    {
        TestRepositoryService testlink = new TestLinkServiceImpl(testLinkURL, devKey);
        TestService service = new TestServiceImpl(testlink);
        service.buildTestng(Collections.<TestCase> emptyList());
    }
    
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void buildTestngNull()
    {
        TestRepositoryService testlink = new TestLinkServiceImpl(testLinkURL, devKey);
        TestService service = new TestServiceImpl(testlink);
        service.buildTestng(Collections.<TestCase> emptyList());
    }
    public void buildTestng() throws TransformerException
    {
        TestRepositoryService testlink = new TestLinkServiceImpl(testLinkURL, devKey);
        TestService service = new TestServiceImpl(testlink);
        List<String> tests = new ArrayList<String>();
        tests.add("AONE-11");
        tests.add("AONE-22");
        tests.add("AONE-123");
        tests.add("AONE-NOTUSED");
        List<TestCase> plan = service.matchTests(tests);
        Document documentPlan = service.buildTestng(plan);
        Assert.assertNotNull(documentPlan);
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        DOMSource source = new DOMSource(documentPlan);
        // Output to console for testing
        StreamResult result = new StreamResult(System.out);
        Transformer transformer = transformerFactory.newTransformer();
        transformer.transform(source, result);
        String expected = 
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?><suite name=\"Suite\" parallel=\"none\">"
                + "<test name=\"DummyTest\"><classes><class name=\"org.alfresco.test.mock.DummyTest\"><methods>"
                + "<include name=\"helloWorld\"/></methods></class></classes></test>"
                + "<test name=\"DummyTest2\"><classes><class name=\"org.alfresco.test.mock.DummyTest2\"/>"
                + "</classes></test></suite>";
        Assert.assertEquals(result.toString(), expected);
    }
}
