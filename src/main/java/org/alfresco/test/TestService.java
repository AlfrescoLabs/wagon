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

import java.util.List;
import java.util.Map;

import org.w3c.dom.Document;
/**
 * Interface that manages Alfresco e2e test operations.
 * @author Michael Suzuki
 *
 */
public interface TestService
{
    /**
     * Get all the test cases in test plan, to run in automation. 
     * @param testPlanID test plan identifier
     * @return List of test ids from test plan. 
     */
    List<String> getTestCases(int testPlanID);
    
    /**
     * Get all the test cases in test plan, to run in automation. 
     * @param String testPlan name
     * @return List of test ids from test plan. 
     */
    List<String> getTestCases(String testPlan);
    
    /**
     * Matches test id with test class, once match it 
     * returns a collection of {@link TestCase} which 
     * holds the information of test id and location of
     * test.
     * @param id collection of test identifiers.
     * @return {@link Map} test id and {@link TestCase}
     */
    List<TestCase> matchTests(List<String>id);
    /**
     * Generates Testng.xml from given test cases which
     * used for running the tests.
     * @param testcases collection of test case ids
     * @return {@link Document} testng xml config
     */
    Document buildTestng(final List<TestCase> testcases);
}