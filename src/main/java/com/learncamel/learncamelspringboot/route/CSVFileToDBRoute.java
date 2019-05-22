package com.learncamel.learncamelspringboot.route;

import com.learncamel.learncamelspringboot.domain.Item;
import com.learncamel.learncamelspringboot.process.BuildSQLProcessor;
import com.learncamel.learncamelspringboot.process.CSVFileToDBSuccessProcessor;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.dataformat.bindy.csv.BindyCsvDataFormat;
import org.apache.camel.spi.DataFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
public class CSVFileToDBRoute extends RouteBuilder {
    Environment environment;

    DataSource dataSource;

    BuildSQLProcessor queryProcessor;

    CSVFileToDBSuccessProcessor successProcessor;

    public CSVFileToDBRoute(Environment environment, @Qualifier("dataSource")DataSource dataSource,
                            BuildSQLProcessor queryProcessor,
                            CSVFileToDBSuccessProcessor successProcessor){
        this.environment = environment;
        this.dataSource = dataSource;
        this.queryProcessor = queryProcessor;
        this.successProcessor = successProcessor;


    }


    @Override
    public void configure() throws Exception {

        log.info("Starting the CSVFileToDBRoute Camel Route");

        DataFormat itemFormatJava = new BindyCsvDataFormat(Item.class);

        from("{{startRoute}}").
                log("Timer Invoked and the body is " + environment.getProperty("messages")).
                choice().
                when(header("env").isNotEqualTo("mock")).
                    pollEnrich("{{fromRoute}}").
                otherwise().
                    log("MOCK ENV FLOW Record is ${body}").
                end().
                to("{{toRouteFile}}").
                 unmarshal(itemFormatJava).
                     log("The unmarshalled object is ${body}").
                  split(body()).
                    log("Record is ${body}").
                    process(queryProcessor).
                    to("{{toRouteDB}}").
                    end()
                .process(successProcessor)
                .to("{{toRouteFileDBSuccess}}");


         log.info("Ending the Camel Route");
    }
}
