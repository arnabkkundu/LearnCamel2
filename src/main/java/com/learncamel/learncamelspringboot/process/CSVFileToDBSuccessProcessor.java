package com.learncamel.learncamelspringboot.process;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

@Component
public class CSVFileToDBSuccessProcessor implements Processor {
    @Override
    public void process(Exchange exchange) throws Exception {
        exchange.getIn().setBody("DATA UPDATED SUCCESSFULLY");
    }
}
