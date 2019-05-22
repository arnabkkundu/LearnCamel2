package com.learncamel.learncamelspringboot.process;

import com.learncamel.learncamelspringboot.domain.Item;
import com.learncamel.learncamelspringboot.exception.DataValidationException;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

@Component

public class BuildSQLProcessor implements Processor {

    Logger logger = LoggerFactory.getLogger(BuildSQLProcessor.class);

    @Override
    public void process(Exchange exchange) throws Exception {

        //Access to each row
        Item item = (Item)exchange.getIn().getBody();
        logger.info("Item in Processor is: " + item);
        StringBuilder query = new StringBuilder();

        // Validate the mandatory Fields
        if(ObjectUtils.isEmpty(item.getSku())){
            throw new DataValidationException("SKU is NULL");
        }

        // CHANGE THIS TO USE JPA
        if(item.getTransactionType().equals("ADD")){
            query.append("INSERT INTO ITEMS (SKU,ITEM_DESCRIPTION,PRICE) VALUES('").
            append(item.getSku() + "','" + item.getItemDescription() + "'," + item.getPrice() + ")");
            }else if(item.getTransactionType().equals("UPDATE")){

        }
        logger.info("FINAL QUERY IS : " + query);

        exchange.getIn().setBody(query.toString());
    }
}
