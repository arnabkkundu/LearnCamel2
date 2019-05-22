package com.learncamel.learncamelspringboot.route;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.test.spring.CamelSpringBootRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.Assert.*;


@ActiveProfiles("dev")
@RunWith(CamelSpringBootRunner.class)
// Clears the Context from Previous Test
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@SpringBootTest
public class CSVFileToDBRouteTest {

    @Autowired
    ProducerTemplate producerTemplate;

    @Autowired
    Environment environment;

    @Test
    public void testMoveFile() throws InterruptedException {

        String message = "type,sku#,itemdescription,price\n" +
                "ADD,100,Samsung TV,500\n" +
                "ADD,101,LG TV,500";
        String fileName = "fileTest.txt";
        //Put the file in the input Directory
        producerTemplate.sendBodyAndHeader(environment.getProperty("fromRoute"),
               message, Exchange.FILE_NAME,fileName);

        Thread.sleep(10000);
        // Check if the file is placed in the Output Directory
        File outputFile = new File("data/output/" + fileName);
        assertTrue(outputFile.exists());
    }


    @Test
    public void testMoveFile_ADD() throws Exception{

        String message = "type,sku#,itemdescription,price\n" +
                "ADD,100,Samsung TV,500\n" +
                "ADD,101,LG TV,500";
        String fileName = "fileTest.txt";
        //Put the file in the input Directory
        producerTemplate.sendBodyAndHeader(environment.getProperty("fromRoute"),
                message, Exchange.FILE_NAME,fileName);

        Thread.sleep(10000);
        // Check if the file is placed in the Output Directory
        File outputFile = new File("data/output/" + fileName);
        assertTrue(outputFile.exists());
        String successMessage="DATA UPDATED SUCCESSFULLY";
        String messageInFile =
                new String(Files.readAllBytes(Paths.get("data/output/Success.txt")));
        assertEquals(messageInFile,successMessage);

    }

    @Test
    public void testMoveFile_ADDException() throws Exception{

        String message = "type,sku#,itemdescription,price\n" +
                "ADD,,Samsung TV,500\n" +
                "ADD,101,LG TV,500";
        String fileName = "fileTest.txt";

        //Put the file in the input Directory
        producerTemplate.sendBodyAndHeader(environment.getProperty("fromRoute"),
                    message, Exchange.FILE_NAME, fileName);

        Thread.sleep(10000);
        // Check if the file is placed in the Output Directory
        File outputFile = new File("data/output/" + fileName);
        assertTrue(outputFile.exists());
        File successFile = new File("data/output/Success.txt");
        assertEquals(false, successFile.exists());

    }




}
