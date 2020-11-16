/**
 * 
 * This class is part of the Programming the Internet of Things
 * project, and is available via the MIT License, which can be
 * found in the LICENSE file at the top level of this repository.
 * 
 * Copyright (c) 2020 by Andrew D. King
 */ 

package programmingtheiot.part03.integration.connection;

import static org.junit.Assert.*;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import programmingtheiot.common.ConfigConst;
import programmingtheiot.common.ConfigUtil;
import programmingtheiot.common.DefaultDataMessageListener;
import programmingtheiot.common.IDataMessageListener;
import programmingtheiot.common.ResourceNameEnum;
import programmingtheiot.data.DataUtil;
import programmingtheiot.data.SensorData;
import programmingtheiot.data.SystemStateData;
import programmingtheiot.gda.connection.*;

/**
 * This test case class contains very basic integration tests for
 * CoapClientConnector. It should not be considered complete,
 * but serve as a starting point for the student implementing
 * additional functionality within their Programming the IoT
 * environment.
 * 
 * NOTE: The CoAP server must be running before executing these tests.
 */
public class CoapClientPerformanceTest
{
	// static
	
	public static final int DEFAULT_TIMEOUT = 5;
	public static final boolean USE_DEFAULT_RESOURCES = true;
	
	private static final Logger _Logger =
		Logger.getLogger(CoapClientPerformanceTest.class.getName());
	
	public static final int MAX_TEST_RUNS = 100000;
	
	// member var's
	
	private CoapClientConnector coapClient = null;
	private CoapClient coapClientPrev = null;

	private IDataMessageListener dataMsgListener = null;
	
	
	// test setup methods
	
	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception
	{
	}
	
	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception
	{
	}
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception
	{
		this.coapClient = new CoapClientConnector();
		this.dataMsgListener = new DefaultDataMessageListener();
		
		this.coapClient.setDataMessageListener(this.dataMsgListener);
//		this.coapClientPrev = new CoapClient("coap://localhost:5683");
	}
	
	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception
	{
	}
	
	// test methods
	
	/**
	 * 
	 */
	//@Test
	public void testPutRequestCon()
	{
		SensorData sd = new SensorData();
		sd.setValue(22.1f);
		
		String ssdJson = DataUtil.getInstance().sensorDataToJson(sd);
		
		execTestPut(true, ssdJson);
	}
	
	/**
	 * 
	 */
	@Test
	public void testPutRequestNon()
	{
		SensorData sd = new SensorData();
		sd.setValue(22.1f);
		
		String ssdJson = DataUtil.getInstance().sensorDataToJson(sd);
		
		execTestPut(false, ssdJson);
	}
	
	// private
	
	public void execTestPut(boolean enableCON, String payload)
	{
		this.coapClient.setEndpointPath(ResourceNameEnum.CDA_SENSOR_MSG_RESOURCE);
		
		long startMillis = System.currentTimeMillis();
		
		for (int sequenceNo = 1; sequenceNo <= MAX_TEST_RUNS; sequenceNo++) {
			this.coapClient.sendPutRequest(ResourceNameEnum.CDA_SENSOR_MSG_RESOURCE, enableCON, payload, DEFAULT_TIMEOUT);
		}
		
		long endMillis = System.currentTimeMillis();
		long elapsedMillis = endMillis - startMillis;
		
		this.coapClient.clearEndpointPath();
		
		_Logger.info("PUT message - useCON " + enableCON + " [" + MAX_TEST_RUNS + "]: " + elapsedMillis + " ms");
	}
	
	public void execTestPost(boolean enableCON, String payload)
	{
		this.coapClient.setEndpointPath(ResourceNameEnum.CDA_SENSOR_MSG_RESOURCE);
		
		long startMillis = System.currentTimeMillis();
		
		for (int sequenceNo = 1; sequenceNo <= MAX_TEST_RUNS; sequenceNo++) {
			this.coapClient.sendPostRequest(ResourceNameEnum.CDA_SENSOR_MSG_RESOURCE, enableCON, payload, DEFAULT_TIMEOUT);
		}
		
		long endMillis = System.currentTimeMillis();
		long elapsedMillis = endMillis - startMillis;
		
		this.coapClient.clearEndpointPath();
		
		_Logger.info("POST message - useCON " + enableCON + " [" + MAX_TEST_RUNS + "]: " + elapsedMillis + " ms");
	}
	
}
