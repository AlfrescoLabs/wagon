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
package org.alfresco.test;

import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.alfresco.test.testlink.TestRepositoryService;
import org.apache.commons.lang.StringUtils;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import br.eti.kinoshita.testlinkjavaapi.util.TestLinkAPIException;

import com.sun.star.uno.RuntimeException;
/**
 * Service that manages and provides:
 * <ul>
 * <li>The ability to generate testng config based on test cases in a test plan.</li>
 * <ul>
 * 
 * @author Michael Suzuki
 *
 */
public class TestServiceImpl implements TestService
{
    private Map<String, TestCase> testdata = new HashMap<String, TestCase>();
    private final TestRepositoryService testLinkService;
    /**
     * Constructor.
     * @param testLinkURL URL of testlink service
     * @param devKey API devKey
     * @throws TestLinkAPIException if error
     * @throws MalformedURLException if incorrect url
     */
    public TestServiceImpl(TestRepositoryService testLinkService) 
    {
        if(testLinkService == null)
        {
            throw new IllegalArgumentException("TestLink Service is required");
        }
        this.testLinkService = testLinkService;
        prepareTestMap();

    }
    /**
     * Prepares map of all test ids and path to test.
     * Tests marked with an annotated AlfrescoTest() are collected into
     * the hash map with details of the path to find test method or class. 
     */
    private void prepareTestMap()
    {
        //Start reflection store
        Reflections reflections = new Reflections("org.alfresco",
                                                  new SubTypesScanner(false),
                                                  new TypeAnnotationsScanner(),
                                                  new MethodAnnotationsScanner());
        
        //Get all classes with alfresco test annotation
        Set<Method> allAnonMethods = reflections.getMethodsAnnotatedWith(org.alfresco.test.AlfrescoTest.class);
        Set<Class<?>> allAnonClasses = reflections.getTypesAnnotatedWith(org.alfresco.test.AlfrescoTest.class, false);
        for(Method m : allAnonMethods)
        {
            String path = m.getDeclaringClass().getName();
            AlfrescoTest a = m.getAnnotation(AlfrescoTest.class);
            String testlinkId = a.testlink();
            TestCase testcase = new TestCase(testlinkId, path);
            testcase.setMethodName(m.getName());
            testdata.put(testlinkId, testcase);
        }
        for(Class<?> c : allAnonClasses)
        {
            String path = c.getName();
            AlfrescoTest a = (AlfrescoTest) c.getAnnotation(AlfrescoTest.class);
            String testlinkId = a.testlink();
            testdata.put(testlinkId, new TestCase(testlinkId, path));
        }
    }
    
    public Map<String,TestCase> getTestData()
    {
        return testdata;
    }

    /**
     * Get all the test cases in test plan, to run in automation. 
     * @param testPlanID test plan identifier
     * @return List of test ids from test plan. 
     */
    public List<String> getTestCases(int testPlanID)
    {
        return testLinkService.getTestCases(testPlanID);
    }
    
    @Override
    public List<String> getTestCases(String testPlan)
    {
        if(StringUtils.isEmpty(testPlan))
        {
            throw new IllegalArgumentException("Testplan name is required");
        }
        return testLinkService.getTestCases(testPlan);
    }
    /**
     * Builds {@link Map} of test id and test class
     * that corresponds to the test, if test id does not 
     * match any test its omitted from the collection.
     * @param id collection of test identifiers.
     * @return {@link Map} test id and tests
     */
    public List<TestCase> matchTests(List<String> ids)
    {
        if(ids == null || ids.isEmpty())
        {
            throw new IllegalArgumentException("Test cases ids are required");
        }
        List<TestCase> tests = new ArrayList<TestCase>();
        for(String testid : ids)
        {
            //If match found pass location of test
            //else pass null location which will 
            //create a commented out test case.
            if(testdata.containsKey(testid))
            {
                tests.add(testdata.get(testid));
            }
        }
        return tests;
    }
    @Override
    public Document buildTestng(List<TestCase> testcases)
    {
        if(testcases == null || testcases.isEmpty())
        {
            throw new IllegalArgumentException("Test cases are required");
        }
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder;
        try
        {
            docBuilder = docFactory.newDocumentBuilder();
        } 
        catch (ParserConfigurationException e)
        {
            throw new RuntimeException("Unable to parse");
        }
        /**
         * Format of xml output should be:
         *  <?xml version="1.0" encoding="UTF-8"?>
         *  <!DOCTYPE suite SYSTEM "http://testng.org/testng-1.0.dtd">
         *  <suite name="Suite" parallel="none">
         *      <test name="Test">
         *          <classes>
         *              <class name="org.alfresco.utils.HttpUtilTest"/>
         *              <class name="org.alfresco.test.testlink.TestLinkServiceTest"/>
         *          </classes>
         *      </test> <!-- Test -->
         *  </suite> <!-- Suite -->
         */
        //Root element
        Document doc = docBuilder.newDocument();
        Element rootElement = doc.createElement("suite");
        
        rootElement.setAttribute("name", "Suite");
        rootElement.setAttribute("parallel", "none");
        doc.appendChild(rootElement);
        
        //Test elements <test name="Test">
        Element test = doc.createElement("test");
        test.setAttribute("name", "Test");
        rootElement.appendChild(test);
        
        /*Test casses
         *<classes>
         *    <class name="org.alfresco.utils.HttpUtilTest"/>
         *</classes>
         */
        Element testclasses = doc.createElement("classes");
        test.appendChild(testclasses);
        
        for(TestCase testcase : testcases)
        {
            Element testclass = doc.createElement("class");
            testclass.setAttribute("name", testcase.getTestClass());
            testclasses.appendChild(testclass);
           
            if(!StringUtils.isEmpty(testcase.getMethodName()))
            {
                /*<class name="test.bla.Test1">
                 * <methods>
                 *    <include name="testMethod" />
                 *</methods>
                 *</class>
                 */
                Element methods = doc.createElement("method");
                testclass.appendChild(methods);
                Element include = doc.createElement("include");
                include.setAttribute("name",testcase.getMethodName());
                methods.appendChild(include);
            }
        }
       
        return doc;
    }

}